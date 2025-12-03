package com.healthapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.healthapp.screens.*
import com.healthapp.viewmodel.HealthAppViewModel

@Composable
fun HealthAppNavigation(viewModel: HealthAppViewModel = viewModel()) {
    val navController = rememberNavController()
    val uploadedFiles by viewModel.uploadedFiles.collectAsState()
    val vaccinations by viewModel.vaccinations.collectAsState()
    val reminders by viewModel.reminders.collectAsState()
    NavHost(
        navController = navController,
        startDestination = "splash"
    ) {
        composable("splash") {
            SplashScreen(navController = navController, viewModel = viewModel)
        }
        composable("login") {
            AuthScreen(navController = navController, viewModel = viewModel)
        }
        composable("signup") {
            SignUpScreen(navController = navController, viewModel = viewModel)
        }
        composable("profile-details") {
            AdditionalInfoScreen(navController = navController, viewModel = viewModel)
        }
        composable("home") {
            HomeScreen(navController = navController, viewModel = viewModel)
        }
        composable("medicine-reminder") {
            MedicineReminderScreen(
                navController = navController,
                reminders = reminders,
                onAddReminder = { reminder -> viewModel.addReminder(reminder) },
                onDeleteReminder = { id -> viewModel.deleteReminder(id) },
                onToggleReminder = { id -> viewModel.toggleReminder(id) }
            )
        }
        composable("upload-record") {
            UploadRecordScreen(
                navController = navController,
                onFileUpload = { file ->
                    viewModel.addFile(file)
                },
                uploadedFiles = uploadedFiles,
                onDeleteFile = { id ->
                    viewModel.deleteFile(id)
                }
            )
        }
        composable("profile") {
            ProfileScreen(navController = navController, viewModel = viewModel) {
                viewModel.setAuthenticated(false)
                navController.navigate("login") {
                    popUpTo("home") { inclusive = true }
                }
            }
        }
        composable("history") {
            HistoryScreen(
                navController = navController,
                uploadedFiles = uploadedFiles,
                onDeleteFile = { id ->
                    viewModel.deleteFile(id)
                }
            )
        }
        composable("doctors-appointment") {
            DoctorsAppointmentScreen(navController = navController)
        }
        composable("vaccination-record") {
            VaccinationRecordScreen(
                navController = navController,
                vaccinations = vaccinations,
                onVaccinationAdd = { vaccination ->
                    viewModel.addVaccination(vaccination)
                },
                onVaccinationDelete = { id ->
                    viewModel.deleteVaccination(id)
                }
            )
        }
        composable("vaccination-history") {
            VaccinationHistoryScreen(
                navController = navController,
                vaccinations = vaccinations,
                onVaccinationDelete = { id -> viewModel.deleteVaccination(id) }
            )
        }
        composable("symptoms") {
            SymptomsScreen(navController = navController)
        }
        composable("records") {
            RecordsScreen(navController = navController)
        }
    }
}
