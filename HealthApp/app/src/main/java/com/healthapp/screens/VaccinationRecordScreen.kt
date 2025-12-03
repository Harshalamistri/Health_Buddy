@file:OptIn(ExperimentalMaterial3Api::class)

package com.healthapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Bloodtype
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Vaccines
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Calendar
import java.util.Locale

data class Vaccination(
    val id: String,
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

private fun today(): String = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
private fun prettyDate(date: String): String =
    runCatching {
        val fmt = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val out = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
        out.format(fmt.parse(date) ?: Date())
    }.getOrDefault(date)

    @Composable
    fun VaccinationRecordScreen(
        navController: NavController,
        vaccinations: List<Vaccination>,
        onVaccinationAdd: (Vaccination) -> Unit,
        onVaccinationDelete: (String) -> Unit
    ) {
    var vaccineName by remember { mutableStateOf("") }
    var doseNumber by remember { mutableStateOf("") }
    var dateAdministered by remember { mutableStateOf("") }
    var nextDueDate by remember { mutableStateOf("") }
    var administeredBy by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var batchNumber by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }
    var deleteId by remember { mutableStateOf<String?>(null) }

    fun addRecord() {
        if (vaccineName.isBlank() || doseNumber.isBlank() || administeredBy.isBlank() || location.isBlank()) return
        val administeredDate = if (dateAdministered.isNotBlank()) dateAdministered else today()
        val newVaccination = Vaccination(
            id = System.currentTimeMillis().toString(),
            vaccineName = vaccineName.trim(),
            doseNumber = doseNumber.trim(),
            dateAdministered = administeredDate,
            nextDueDate = nextDueDate.trim(),
            administeredBy = administeredBy.trim(),
            location = location.trim(),
            batchNumber = batchNumber.trim(),
            notes = notes.trim(),
            createdAt = today()
        )
        onVaccinationAdd(newVaccination)
        vaccineName = ""
        doseNumber = ""
        dateAdministered = ""
        nextDueDate = ""
        administeredBy = ""
        location = ""
        batchNumber = ""
        notes = ""
    }

    val recentVaccinations = vaccinations.takeLast(2).reversed()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Vaccination Record", fontWeight = FontWeight.Bold)
                        Text("Track your immunization history", fontSize = 12.sp, color = Color(0xFF7A8297))
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate("home") }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, Color(0xFF1FB569)),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .background(Color.Transparent),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(Icons.Default.Vaccines, contentDescription = null, tint = Color(0xFF0EB05A))
                        Text("Add Vaccination Record", fontWeight = FontWeight.SemiBold, color = Color(0xFF1C1D2D))
                    }

                    FieldLabel("Vaccine Name")
                    FormTextField(
                        value = vaccineName,
                        onValueChange = { vaccineName = it },
                        placeholder = "e.g., COVID-19, Hepatitis B, MMR",
                        leadingIcon = { Icon(Icons.Default.Bloodtype, contentDescription = null, tint = Color(0xFF8FA3B9)) }
                    )

                    FieldLabel("Dose Number")
                    FormTextField(
                        value = doseNumber,
                        onValueChange = { doseNumber = it },
                        placeholder = "e.g., 1st Dose, 2nd Dose, Booster"
                    )

                    FieldLabel("Date Administered")
                    DateField(
                        value = dateAdministered,
                        onValueChange = { dateAdministered = it },
                        placeholder = "dd-mm-yyyy"
                    )

                    FieldLabel("Next Dose Due Date (Optional)")
                    DateField(
                        value = nextDueDate,
                        onValueChange = { nextDueDate = it },
                        placeholder = "dd-mm-yyyy"
                    )

                    FieldLabel("Administered By")
                    FormTextField(
                        value = administeredBy,
                        onValueChange = { administeredBy = it },
                        placeholder = "e.g., Dr. Smith, City Hospital",
                        leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = Color(0xFF8FA3B9)) }
                    )

                    FieldLabel("Location")
                    FormTextField(
                        value = location,
                        onValueChange = { location = it },
                        placeholder = "e.g., City Health Center",
                        leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = null, tint = Color(0xFF8FA3B9)) }
                    )

                    FieldLabel("Batch/Lot Number (Optional)")
                    FormTextField(
                        value = batchNumber,
                        onValueChange = { batchNumber = it },
                        placeholder = "e.g., ABC12345"
                    )

                    FieldLabel("Additional Notes (Optional)")
                    FormTextField(
                        value = notes,
                        onValueChange = { notes = it },
                        placeholder = "Any side effects, reactions, or additional information...",
                        singleLine = false,
                        maxLines = 4
                    )
                }
            }

            Button(
                onClick = { addRecord() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF0EB05A),
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Icon(Icons.Default.Bloodtype, contentDescription = null, modifier = Modifier.padding(end = 8.dp))
                Text("Save Vaccination Record", fontWeight = FontWeight.SemiBold)
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Vaccination History (${vaccinations.size})",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF1C1D2D)
                    )
                    if (vaccinations.isNotEmpty()) {
                        Badge(
                            containerColor = Color(0xFFE8F1FF),
                            contentColor = Color(0xFF2D4E8F),
                            modifier = Modifier.padding(top = 4.dp)
                        ) {
                            Text("${vaccinations.size} total")
                        }
                    }
                }
                if (vaccinations.size > 2) {
                    TextButton(onClick = { navController.navigate("vaccination-history") }) {
                        Text("View all")
                    }
                }
            }

            if (recentVaccinations.isEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF6F8FF)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(24.dp)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .background(Color(0xFFE9EDF7), RoundedCornerShape(32.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Bloodtype, contentDescription = null, tint = Color(0xFF9AA5B1))
                        }
                        Text("No vaccination records yet", color = Color(0xFF4A5568))
                        Text("Add your first vaccination record above", color = Color(0xFF7B8498))
                    }
                }
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    recentVaccinations.forEach { vaccination ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            shape = RoundedCornerShape(12.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Box(
                                            modifier = Modifier
                                                .size(44.dp)
                                                .background(Color(0xFFE9F5FF), RoundedCornerShape(12.dp)),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(Icons.Default.Bloodtype, contentDescription = null, tint = Color(0xFF2D8DFE))
                                        }
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Column {
                                            Text("Administered by ${vaccination.administeredBy}", fontWeight = FontWeight.Medium, color = Color(0xFF1C1D2D))
                                            Text("At ${vaccination.location}", color = Color(0xFF5A6375))
                                        }
                                    }
                                    IconButton(
                                        onClick = { deleteId = vaccination.id },
                                        colors = IconButtonDefaults.iconButtonColors(
                                            containerColor = Color(0xFFFFE4E6),
                                            contentColor = Color(0xFFB91C1C)
                                        )
                                    ) {
                                        Icon(Icons.Default.Delete, contentDescription = "Delete", modifier = Modifier.size(16.dp))
                                    }
                                }
                                Text("Administered: ${prettyDate(vaccination.dateAdministered)}", fontSize = 12.sp, color = Color(0xFF5A6375))
                                if (vaccination.batchNumber.isNotEmpty()) {
                                    Text("Batch: ${vaccination.batchNumber}", fontSize = 12.sp, color = Color(0xFF5A6375))
                                }
                                if (vaccination.notes.isNotEmpty()) {
                                    Text("Notes: ${vaccination.notes}", fontSize = 12.sp, color = Color(0xFF5A6375))
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (deleteId != null) {
        AlertDialog(
            onDismissRequest = { deleteId = null },
            title = { Text("Delete Vaccination Record?") },
            text = { Text("Are you sure you want to delete this vaccination record?") },
            confirmButton = {
                Button(
                    onClick = {
                        onVaccinationDelete(deleteId!!)
                        deleteId = null
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error,
                        contentColor = MaterialTheme.colorScheme.onError
                    )
                ) { Text("Delete") }
            },
            dismissButton = {
                OutlinedButton(onClick = { deleteId = null }) { Text("Cancel") }
            }
        )
    }
}

@Composable
    fun VaccinationHistoryScreen(
        navController: NavController,
        vaccinations: List<Vaccination>,
        onVaccinationDelete: (String) -> Unit
    ) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Vaccination History", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        if (vaccinations.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(Icons.Default.Bloodtype, contentDescription = null, tint = Color(0xFF9AA5B1), modifier = Modifier.size(64.dp))
                Text("No vaccination records yet", fontWeight = FontWeight.SemiBold, color = Color(0xFF2A3846), modifier = Modifier.padding(top = 8.dp))
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(vaccinations) { vaccination ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(vaccination.administeredBy, fontWeight = FontWeight.SemiBold, color = Color(0xFF1C1D2D))
                                    Text(vaccination.location, color = Color(0xFF5A6375))
                                }
                                IconButton(
                                    onClick = { onVaccinationDelete(vaccination.id) },
                                    colors = IconButtonDefaults.iconButtonColors(
                                        containerColor = Color(0xFFFFE4E6),
                                        contentColor = Color(0xFFB91C1C)
                                    )
                                ) {
                                    Icon(Icons.Default.Delete, contentDescription = "Delete", modifier = Modifier.size(16.dp))
                                }
                            }
                            Text("Administered: ${prettyDate(vaccination.dateAdministered)}", fontSize = 12.sp, color = Color(0xFF5A6375))
                            if (vaccination.batchNumber.isNotEmpty()) {
                                Text("Batch: ${vaccination.batchNumber}", fontSize = 12.sp, color = Color(0xFF5A6375))
                            }
                            if (vaccination.notes.isNotEmpty()) {
                                Text("Notes: ${vaccination.notes}", fontSize = 12.sp, color = Color(0xFF5A6375))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun FieldLabel(text: String) {
    Text(text, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Color(0xFF1C1D2D))
}

@Composable
private fun FormTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    leadingIcon: (@Composable () -> Unit)? = null,
    singleLine: Boolean = true,
    maxLines: Int = 1
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(placeholder, color = Color(0xFF9AA5B1)) },
        leadingIcon = leadingIcon,
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF2F4F8), RoundedCornerShape(8.dp)),
        singleLine = singleLine,
        maxLines = maxLines,
        shape = RoundedCornerShape(8.dp),
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = Color(0xFFD6DBE5),
            focusedBorderColor = Color(0xFF0EB05A),
            cursorColor = Color(0xFF0EB05A)
        )
    )
}

@Composable
private fun DateField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String
) {
    val formatter = remember { SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()) }
    val context = androidx.compose.ui.platform.LocalContext.current
    val cal = remember(value) {
        Calendar.getInstance().apply {
            runCatching { formatter.parse(value) }.getOrNull()?.let { time = it }
        }
    }

    OutlinedTextField(
        value = value,
        onValueChange = { onValueChange(it) },
        placeholder = { Text(placeholder, color = Color(0xFF9AA5B1)) },
        modifier = Modifier
            .fillMaxWidth(),
        singleLine = true,
        leadingIcon = { Icon(Icons.Default.CalendarToday, contentDescription = null, tint = Color(0xFF8FA3B9)) },
        trailingIcon = {
            IconButton(onClick = {
                android.app.DatePickerDialog(
                    context,
                    { _, year, month, day ->
                        val mm = (month + 1).toString().padStart(2, '0')
                        val dd = day.toString().padStart(2, '0')
                        onValueChange("$year-$mm-$dd")
                    },
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)
                ).show()
            }) {
                Icon(Icons.Default.CalendarToday, contentDescription = "Pick date", tint = Color(0xFF8FA3B9))
            }
        },
        shape = RoundedCornerShape(8.dp),
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = Color(0xFFD6DBE5),
            focusedBorderColor = Color(0xFF0EB05A),
            cursorColor = Color(0xFF0EB05A)
        )
    )
}
