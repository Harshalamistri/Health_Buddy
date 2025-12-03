package com.healthapp.viewmodel

import android.app.Application
import android.content.Context
import android.database.sqlite.SQLiteException
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.healthapp.data.AppDatabaseProvider
import com.healthapp.data.ReminderEntity
import com.healthapp.data.UploadedFileEntity
import com.healthapp.data.VaccinationEntity
import com.healthapp.reminders.ReminderAlarmScheduler
import com.healthapp.screens.Reminder
import com.healthapp.screens.UploadedFile
import com.healthapp.screens.Vaccination
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

data class UserProfile(
    val name: String = "",
    val email: String = "",
    val phone: String = "",
    val gender: String = "",
    val bloodGroup: String = "",
    val illness: String = "",
    val emergencyContact: String = "",
    val address: String = ""
)

class HealthAppViewModel(application: Application) : AndroidViewModel(application) {
    private var db = AppDatabaseProvider.getInstance(application)
    private var vaccinationDao = db.vaccinationDao()
    private var uploadDao = db.uploadedFileDao()
    private var reminderDao = db.reminderDao()
    private val prefs = application.getSharedPreferences("health_prefs", Context.MODE_PRIVATE)
    private val reminderScheduler = ReminderAlarmScheduler(application.applicationContext)

    private val _uploadedFiles = MutableStateFlow<List<UploadedFile>>(emptyList())
    val uploadedFiles: StateFlow<List<UploadedFile>> = _uploadedFiles
    
    private val _vaccinations = MutableStateFlow<List<Vaccination>>(emptyList())
    val vaccinations: StateFlow<List<Vaccination>> = _vaccinations

    private val _reminders = MutableStateFlow<List<Reminder>>(emptyList())
    val reminders: StateFlow<List<Reminder>> = _reminders

    private val _userProfile = MutableStateFlow(loadUserProfile())
    val userProfile: StateFlow<UserProfile> = _userProfile

    private val _isAuthenticated = MutableStateFlow(prefs.getBoolean("is_authenticated", false))
    val isAuthenticated: StateFlow<Boolean> = _isAuthenticated

    private var uploadsJob: Job? = null
    private var vaccinationsJob: Job? = null
    private var remindersJob: Job? = null

    init {
        startCollectors()
    }

    private fun startCollectors() {
        uploadsJob?.cancel()
        vaccinationsJob?.cancel()
        remindersJob?.cancel()

        uploadsJob = viewModelScope.launch(Dispatchers.IO) {
            try {
                uploadDao.getAll().collectLatest { list ->
                    _uploadedFiles.value = list.map { it.toModel() }
                }
            } catch (_: Exception) {
            }
        }

        vaccinationsJob = viewModelScope.launch(Dispatchers.IO) {
            try {
                vaccinationDao.getAll().collectLatest { list ->
                    _vaccinations.value = list.map { it.toModel() }
                }
            } catch (_: Exception) {
            }
        }

        remindersJob = viewModelScope.launch(Dispatchers.IO) {
            tryCollectReminders()
        }
    }

    private suspend fun tryCollectReminders(hasRetried: Boolean = false) {
        try {
            reminderDao.getAll().collectLatest { list ->
                _reminders.value = list.map { it.toModel() }
            }
        } catch (e: SQLiteException) {
            if (!hasRetried) {
                rebuildDatabase()
                tryCollectReminders(true)
            }
        } catch (_: Exception) {
            // Ignore to prevent crash; logging can be added if needed.
        }
    }

    private fun rebuildDatabase() {
        AppDatabaseProvider.reset(getApplication())
        db = AppDatabaseProvider.getInstance(getApplication())
        vaccinationDao = db.vaccinationDao()
        uploadDao = db.uploadedFileDao()
        reminderDao = db.reminderDao()
        startCollectors()
    }

    fun addFile(file: UploadedFile) {
        viewModelScope.launch(Dispatchers.IO) {
            uploadDao.insert(file.toEntity())
        }
    }

    fun deleteFile(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            uploadDao.delete(id)
        }
    }

    fun addVaccination(vaccination: Vaccination) {
        viewModelScope.launch(Dispatchers.IO) {
            vaccinationDao.insert(vaccination.toEntity())
        }
    }

    fun deleteVaccination(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            vaccinationDao.delete(id)
        }
    }

    fun addReminder(reminder: Reminder) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                reminderDao.insert(reminder.toEntity())
                reminderScheduler.scheduleReminder(reminder)
            } catch (e: SQLiteException) {
                rebuildDatabase()
                runCatching { reminderDao.insert(reminder.toEntity()) }
                reminderScheduler.scheduleReminder(reminder)
            }
        }
    }

    fun deleteReminder(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val existing = reminderDao.getAll()
                    .firstOrNull()?.firstOrNull { it.id == id }?.toModel()
                reminderDao.delete(id)
                existing?.let { reminderScheduler.cancelReminder(it) }
            } catch (e: SQLiteException) {
                rebuildDatabase()
                val existing = reminderDao.getAll()
                    .firstOrNull()?.firstOrNull { it.id == id }?.toModel()
                runCatching { reminderDao.delete(id) }
                existing?.let { reminderScheduler.cancelReminder(it) }
            }
        }
    }

    fun toggleReminder(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val current = reminderDao.getAll()
                    .firstOrNull()?.firstOrNull { it.id == id } ?: return@launch
                val updated = current.copy(active = !current.active)
                reminderDao.update(updated)
                val model = updated.toModel()
                if (model.active) {
                    reminderScheduler.scheduleReminder(model)
                } else {
                    reminderScheduler.cancelReminder(model)
                }
            } catch (e: SQLiteException) {
                rebuildDatabase()
                runCatching {
                    val current = reminderDao.getAll()
                        .firstOrNull()?.firstOrNull { it.id == id } ?: return@runCatching
                    val updated = current.copy(active = !current.active)
                    reminderDao.update(updated)
                    val model = updated.toModel()
                    if (model.active) {
                        reminderScheduler.scheduleReminder(model)
                    } else {
                        reminderScheduler.cancelReminder(model)
                    }
                }
            }
        }
    }

    fun updateUserProfile(profile: UserProfile) {
        _userProfile.value = profile
        prefs.edit()
            .putString("user_name", profile.name)
            .putString("user_email", profile.email)
            .putString("user_phone", profile.phone)
            .putString("user_gender", profile.gender)
            .putString("user_blood_group", profile.bloodGroup)
            .putString("user_illness", profile.illness)
            .putString("user_emergency", profile.emergencyContact)
            .putString("user_address", profile.address)
            .apply()
    }

    fun setAuthenticated(authenticated: Boolean) {
        _isAuthenticated.value = authenticated
        prefs.edit().putBoolean("is_authenticated", authenticated).apply()
    }

    private fun loadUserProfile(): UserProfile {
        return UserProfile(
            name = prefs.getString("user_name", "") ?: "",
            email = prefs.getString("user_email", "") ?: "",
            phone = prefs.getString("user_phone", "") ?: "",
            gender = prefs.getString("user_gender", "") ?: "",
            bloodGroup = prefs.getString("user_blood_group", "") ?: "",
            illness = prefs.getString("user_illness", "") ?: "",
            emergencyContact = prefs.getString("user_emergency", "") ?: "",
            address = prefs.getString("user_address", "") ?: ""
        )
    }
}

private fun UploadedFile.toEntity() = UploadedFileEntity(
    id = id,
    name = name,
    type = type,
    uploadDate = uploadDate,
    size = size,
    url = url
)

private fun UploadedFileEntity.toModel() = UploadedFile(
    id = id,
    name = name,
    type = type,
    uploadDate = uploadDate,
    size = size,
    url = url
)

private fun Vaccination.toEntity() = VaccinationEntity(
    id = id,
    vaccineName = vaccineName,
    doseNumber = doseNumber,
    dateAdministered = dateAdministered,
    nextDueDate = nextDueDate,
    administeredBy = administeredBy,
    location = location,
    batchNumber = batchNumber,
    notes = notes,
    createdAt = createdAt
)

private fun VaccinationEntity.toModel() = Vaccination(
    id = id,
    vaccineName = vaccineName,
    doseNumber = doseNumber,
    dateAdministered = dateAdministered,
    nextDueDate = nextDueDate,
    administeredBy = administeredBy,
    location = location,
    batchNumber = batchNumber,
    notes = notes,
    createdAt = createdAt
)

private fun Reminder.toEntity() = ReminderEntity(
    id = id,
    medicineName = medicineName,
    dosage = dosage,
    timesCsv = times.joinToString(","),
    phoneNumber = phoneNumber,
    sendSms = sendSms,
    active = active,
    durationType = durationType,
    startDate = startDate,
    endDate = endDate,
    createdAt = createdAt
)

private fun ReminderEntity.toModel() = Reminder(
    id = id,
    medicineName = medicineName,
    dosage = dosage,
    times = timesCsv.split(",").filter { it.isNotBlank() },
    phoneNumber = phoneNumber,
    sendSms = sendSms,
    active = active,
    durationType = durationType,
    startDate = startDate,
    endDate = endDate,
    createdAt = createdAt
)
