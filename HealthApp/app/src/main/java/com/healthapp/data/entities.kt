package com.healthapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "vaccinations")
data class VaccinationEntity(
    @PrimaryKey val id: String,
    val vaccineName: String,
    val doseNumber: String,
    val dateAdministered: String,
    val nextDueDate: String,
    val administeredBy: String,
    val location: String,
    val batchNumber: String,
    val notes: String,
    val createdAt: String
)

@Entity(tableName = "uploaded_files")
data class UploadedFileEntity(
    @PrimaryKey val id: String,
    val name: String,
    val type: String,
    val uploadDate: String,
    val size: String,
    val url: String
)

@Entity(tableName = "reminders")
data class ReminderEntity(
    @PrimaryKey val id: String,
    val medicineName: String,
    val dosage: String,
    val timesCsv: String,
    val phoneNumber: String,
    val sendSms: Boolean,
    val active: Boolean,
    val durationType: String,
    val startDate: String,
    val endDate: String,
    val createdAt: String
)
