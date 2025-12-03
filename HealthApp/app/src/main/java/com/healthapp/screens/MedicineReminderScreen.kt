package com.healthapp.screens

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.EmojiObjects
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.NotificationImportant
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

data class Reminder(
    val id: String,
    val medicineName: String,
    val dosage: String,
    val times: List<String>,
    val phoneNumber: String,
    val sendSms: Boolean,
    val active: Boolean,
    val durationType: String, // "everyday", "week", "custom"
    val startDate: String,
    val endDate: String,
    val createdAt: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedicineReminderScreen(
    navController: NavController,
    reminders: List<Reminder>,
    onAddReminder: (Reminder) -> Unit,
    onDeleteReminder: (String) -> Unit,
    onToggleReminder: (String) -> Unit
) {
    var showForm by remember { mutableStateOf(false) }
    var medicineName by remember { mutableStateOf("") }
    var dosage by remember { mutableStateOf("") }
    var timePrimary by remember { mutableStateOf("") }
    var timeSecondary by remember { mutableStateOf("") }
    var includeSecondary by remember { mutableStateOf(false) }
    var phoneNumber by remember { mutableStateOf("") }
    var sendSms by remember { mutableStateOf(false) }
    var durationType by remember { mutableStateOf("everyday") }
    var startDate by remember { mutableStateOf(SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())) }
    var endDate by remember { mutableStateOf("") }
    val context = LocalContext.current

    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { }
    )

    fun requestNotificationPermissionIfNeeded() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val granted = ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
            if (!granted) {
                notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    val dateFormatter = remember { SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()) }
    val prettyFormatter = remember { SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()) }

    fun parseDateSafe(dateString: String): Date? = runCatching { dateFormatter.parse(dateString) }.getOrNull()

    fun formatDate(dateString: String): String {
        if (dateString.isEmpty()) return ""
        val date = parseDateSafe(dateString) ?: return ""
        return prettyFormatter.format(date)
    }

    fun getDurationText(reminder: Reminder): String = when (reminder.durationType) {
        "everyday" -> "Everyday (Ongoing)"
        "week" -> "For One Week (until ${formatDate(reminder.endDate)})"
        else -> "${formatDate(reminder.startDate)} - ${formatDate(reminder.endDate)}"
    }

    fun submitReminder() {
        try {
            if (medicineName.isEmpty() || timePrimary.isEmpty()) return

            val safeStart = parseDateSafe(startDate) ?: Date()
            val calculatedEndDate = when (durationType) {
                "week" -> {
                    val cal = Calendar.getInstance()
                    cal.time = safeStart
                    cal.add(Calendar.DAY_OF_MONTH, 7)
                    dateFormatter.format(cal.time)
                }
                "everyday" -> ""
                else -> if (endDate.isNotBlank()) endDate else dateFormatter.format(safeStart)
            }

            val timesList = buildList {
                add(timePrimary)
                if (includeSecondary && timeSecondary.isNotEmpty()) add(timeSecondary)
            }.ifEmpty { listOf(timePrimary) }

            val newReminder = Reminder(
                id = System.currentTimeMillis().toString(),
                medicineName = medicineName,
                dosage = if (dosage.isEmpty()) "As prescribed" else dosage,
                times = timesList,
                phoneNumber = phoneNumber,
                sendSms = sendSms,
                active = true,
                durationType = durationType,
                startDate = dateFormatter.format(safeStart),
                endDate = calculatedEndDate,
                createdAt = dateFormatter.format(Date())
            )

            onAddReminder(newReminder)
            requestNotificationPermissionIfNeeded()
            medicineName = ""
            dosage = ""
            timePrimary = ""
            timeSecondary = ""
            includeSecondary = false
            phoneNumber = ""
            sendSms = false
            durationType = "everyday"
            startDate = dateFormatter.format(Date())
            endDate = ""
            showForm = false
        } catch (e: Exception) {
            // Swallow to prevent crash; in production log/report this error.
        }
    }

    // Show reminders until their end date (or indefinitely if no end)
    val today = Date()
    fun isActiveForToday(reminder: Reminder): Boolean {
        val start = parseDateSafe(reminder.startDate) ?: today
        val end = if (reminder.endDate.isBlank()) null else parseDateSafe(reminder.endDate)
        val started = !today.before(start)
        val notEnded = end == null || !today.after(end)
        return started && notEnded
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Medicine\nReminders", fontWeight = FontWeight.Bold, lineHeight = 24.sp)
                        Text("Never miss your medication", fontSize = 12.sp, color = Color(0xFF7A8297))
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate("home") }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    GradientActionButton(
                        text = if (showForm) "Close" else "Add New",
                        onClick = { showForm = !showForm }
                    )
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .background(Color(0xFFF6F8FF)),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (showForm) {
                item {
                    ReminderFormCard(
                        medicineName = medicineName,
                        dosage = dosage,
                        timePrimary = timePrimary,
                        timeSecondary = timeSecondary,
                        includeSecondary = includeSecondary,
                        phoneNumber = phoneNumber,
                        sendSms = sendSms,
                        durationType = durationType,
                        startDate = startDate,
                        endDate = endDate,
                        onMedicineChange = { medicineName = it },
                        onDosageChange = { dosage = it },
                        onTimePrimaryChange = { timePrimary = it },
                        onTimeSecondaryChange = { timeSecondary = it },
                        onIncludeSecondaryChange = { includeSecondary = it },
                        onPhoneChange = { phoneNumber = it },
                        onSendSmsChange = { sendSms = it },
                        onDurationChange = { durationType = it },
                        onStartDateChange = { startDate = it },
                        onEndDateChange = { endDate = it },
                        onSubmit = { submitReminder() },
                        onCancel = { showForm = false }
                    )
                }
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFEEF4FF))
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .background(Color(0xFFDCE7FF), MaterialTheme.shapes.small)
                                .padding(12.dp)
                        ) {
                            Icon(Icons.Default.Message, contentDescription = null, tint = Color(0xFF5D7BFF))
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text(
                                text = "SMS Notifications",
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF1C1D2D)
                            )
                            Text(
                                text = "You'll receive text message reminders at the scheduled times. Make sure to provide a valid phone number.",
                                color = Color(0xFF5A6375)
                            )
                        }
                    }
                }
            }

            item {
                Text(
                    text = "Your Reminders (${reminders.size})",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            val activeReminders = reminders.filter { isActiveForToday(it) }

            if (activeReminders.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F4FF))
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(24.dp)
                                .fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(64.dp)
                                    .background(MaterialTheme.colorScheme.surface, MaterialTheme.shapes.small)
                                    .padding(16.dp)
                            ) {
                                Icon(Icons.Default.EmojiObjects, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                            Text(
                                text = "No reminders set yet",
                                modifier = Modifier.padding(top = 8.dp),
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "Click \"Add New\" to create your first medicine reminder",
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            } else {
                items(activeReminders) { reminder ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = if (!reminder.active) MaterialTheme.colorScheme.surface.copy(alpha = 0.6f) else MaterialTheme.colorScheme.surface
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .background(
                                        if (reminder.active) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant,
                                        MaterialTheme.shapes.small
                                    )
                                    .padding(12.dp)
                            ) {
                                Icon(
                                    Icons.Default.EmojiObjects,
                                    contentDescription = null,
                                    tint = if (reminder.active) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            Spacer(modifier = Modifier.width(16.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(
                                            text = reminder.medicineName,
                                            fontWeight = FontWeight.Medium,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                        Text(
                                            text = reminder.dosage,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }

                                    if (reminder.active) {
                                        BadgedBox(badge = {
                                            Badge(
                                                containerColor = MaterialTheme.colorScheme.primaryContainer,
                                                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                                            ) { Text("Active") }
                                        }) {}
                                    }
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Default.AccessTime, contentDescription = null, modifier = Modifier.size(14.dp))
                                        Text(
                                            text = reminder.times.joinToString(", "),
                                            fontSize = 12.sp,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                    if (reminder.phoneNumber.isNotEmpty() && reminder.sendSms) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(Icons.Default.Message, contentDescription = null, modifier = Modifier.size(14.dp))
                                            Text(
                                                text = "SMS to ${reminder.phoneNumber}",
                                                fontSize = 12.sp,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }
                                    }
                                }

                                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 4.dp)) {
                                    Icon(Icons.Default.CalendarToday, contentDescription = null, modifier = Modifier.size(14.dp))
                                    Text(
                                        text = getDurationText(reminder),
                                        fontSize = 12.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    Button(
                                        onClick = { onToggleReminder(reminder.id) },
                                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.outline),
                                        modifier = Modifier.height(32.dp)
                                    ) {
                                        Icon(Icons.Default.NotificationImportant, contentDescription = null, modifier = Modifier.size(14.dp))
                                        Text(
                                            text = if (reminder.active) "Disable" else "Enable",
                                            fontSize = 12.sp,
                                            modifier = Modifier.padding(start = 4.dp)
                                        )
                                    }

                                    Button(
                                        onClick = { onDeleteReminder(reminder.id) },
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = MaterialTheme.colorScheme.errorContainer,
                                            contentColor = MaterialTheme.colorScheme.onErrorContainer
                                        ),
                                        modifier = Modifier.height(32.dp)
                                    ) {
                                        Icon(Icons.Default.Delete, contentDescription = null, modifier = Modifier.size(14.dp))
                                        Text(
                                            text = "Delete",
                                            fontSize = 12.sp,
                                            modifier = Modifier.padding(start = 4.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun GradientActionButton(text: String, onClick: () -> Unit) {
    val gradient = Brush.horizontalGradient(listOf(Color(0xFF5C7BFF), Color(0xFF9B4CFF)))
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
        shape = MaterialTheme.shapes.small,
        modifier = Modifier
            .height(36.dp)
            .background(gradient, MaterialTheme.shapes.small)
    ) {
        Icon(Icons.Default.Add, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
        Spacer(modifier = Modifier.width(6.dp))
        Text(text, color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
    }
}

@Composable
private fun ReminderFormCard(
    medicineName: String,
    dosage: String,
    timePrimary: String,
    timeSecondary: String,
    includeSecondary: Boolean,
    phoneNumber: String,
    sendSms: Boolean,
    durationType: String,
    startDate: String,
    endDate: String,
    onMedicineChange: (String) -> Unit,
    onDosageChange: (String) -> Unit,
    onTimePrimaryChange: (String) -> Unit,
    onTimeSecondaryChange: (String) -> Unit,
    onIncludeSecondaryChange: (Boolean) -> Unit,
    onPhoneChange: (String) -> Unit,
    onSendSmsChange: (Boolean) -> Unit,
    onDurationChange: (String) -> Unit,
    onStartDateChange: (String) -> Unit,
    onEndDateChange: (String) -> Unit,
    onSubmit: () -> Unit,
    onCancel: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color(0xFFE5E8F0), MaterialTheme.shapes.medium),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Link, contentDescription = null, tint = Color(0xFF4F6BFF), modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Add Medicine Reminder", fontWeight = FontWeight.SemiBold, color = Color(0xFF1C1D2D))
            }

            FormTextField(
                value = medicineName,
                onValueChange = onMedicineChange,
                label = "Medicine Name *",
                placeholder = "e.g., Aspirin"
            )

            FormTextField(
                value = dosage,
                onValueChange = onDosageChange,
                label = "Dosage",
                placeholder = "e.g., 1 tablet, 500mg"
            )

            TimePickerField(
                label = "Time *",
                value = timePrimary,
                onValueChange = onTimePrimaryChange
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(top = 4.dp)
            ) {
                RadioButton(
                    selected = includeSecondary,
                    onClick = { onIncludeSecondaryChange(!includeSecondary) },
                    colors = RadioButtonDefaults.colors(selectedColor = Color(0xFF5C7BFF))
                )
                Text("Add second time (Night)", color = Color(0xFF1C1D2D))
            }
            if (includeSecondary) {
                TimePickerField(
                    label = "Night Time",
                    value = timeSecondary,
                    onValueChange = onTimeSecondaryChange
                )
            }

            FormTextField(
                value = phoneNumber,
                onValueChange = onPhoneChange,
                label = "Phone Number (for SMS) *",
                placeholder = "+1 234 567 8900",
                leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null, tint = Color(0xFFB0B5C5)) }
            )
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                RadioButton(
                    selected = sendSms,
                    onClick = { onSendSmsChange(!sendSms) },
                    colors = RadioButtonDefaults.colors(selectedColor = Color(0xFF5C7BFF))
                )
                Text("Send SMS reminder", color = Color(0xFF1C1D2D))
            }

            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text("Duration Type *", fontWeight = FontWeight.Medium, color = Color(0xFF1C1D2D))
                DurationOption(
                    selected = durationType == "everyday",
                    label = "Everyday (Ongoing)",
                    onSelect = { onDurationChange("everyday") },
                    bold = true
                )
                DurationOption(
                    selected = durationType == "week",
                    label = "For One Week",
                    onSelect = { onDurationChange("week") }
                )
                DurationOption(
                    selected = durationType == "custom",
                    label = "Custom Time Period",
                    onSelect = { onDurationChange("custom") }
                )
            }

            if (durationType == "custom") {
                FormTextField(
                    value = startDate,
                    onValueChange = onStartDateChange,
                    label = "Start Date",
                    placeholder = "YYYY-MM-DD",
                    leadingIcon = { Icon(Icons.Default.CalendarToday, contentDescription = null, tint = Color(0xFFB0B5C5)) }
                )
                FormTextField(
                    value = endDate,
                    onValueChange = onEndDateChange,
                    label = "End Date",
                    placeholder = "YYYY-MM-DD",
                    leadingIcon = { Icon(Icons.Default.CalendarToday, contentDescription = null, tint = Color(0xFFB0B5C5)) }
                )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                Button(
                    onClick = onSubmit,
                    modifier = Modifier
                        .weight(1f)
                        .height(42.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF5C7BFF),
                        contentColor = Color.White
                    )
                ) {
                    Text("Set Reminder", fontWeight = FontWeight.SemiBold)
                }
                OutlinedButton(
                    onClick = onCancel,
                    modifier = Modifier
                        .weight(1f)
                        .height(42.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF4A5568)),
                    border = ButtonDefaults.outlinedButtonBorder
                ) {
                    Text("Cancel")
                }
            }
        }
    }
}

@Composable
private fun TimePickerField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit
) {
    val context = LocalContext.current
    val interactionSource = remember { MutableInteractionSource() }

    fun showPicker(current: String) {
        val initialHour = current.substringBefore(":").toIntOrNull() ?: 8
        val initialMinute = current.substringAfter(":", "0").toIntOrNull() ?: 0
        android.app.TimePickerDialog(
            context,
            { _, hourOfDay, minute ->
                val hh = hourOfDay.toString().padStart(2, '0')
                val mm = minute.toString().padStart(2, '0')
                onValueChange("$hh:$mm")
            },
            initialHour,
            initialMinute,
            true
        ).show()
    }

    OutlinedTextField(
        value = value,
        onValueChange = { },
        label = { Text(label) },
        placeholder = { Text("--:--", color = Color(0xFF9AA2B1)) },
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) { showPicker(value) },
        readOnly = true,
        leadingIcon = { Icon(Icons.Default.AccessTime, contentDescription = null, tint = Color(0xFFB0B5C5)) },
        trailingIcon = {
            IconButton(
                onClick = { showPicker(value) },
                colors = IconButtonDefaults.iconButtonColors(contentColor = Color(0xFF5C7BFF))
            ) {
                Icon(Icons.Default.AccessTime, contentDescription = "Select time")
            }
        },
        singleLine = true,
        shape = MaterialTheme.shapes.small,
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = Color(0xFFE3E7F0),
            focusedBorderColor = Color(0xFF5C7BFF),
            cursorColor = Color(0xFF5C7BFF)
        )
    )
}

@Composable
private fun FormTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    leadingIcon: @Composable (() -> Unit)? = null
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        placeholder = { Text(placeholder, color = Color(0xFF9AA2B1)) },
        modifier = Modifier.fillMaxWidth(),
        leadingIcon = leadingIcon,
        singleLine = true,
        shape = MaterialTheme.shapes.small,
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = Color(0xFFE3E7F0),
            focusedBorderColor = Color(0xFF5C7BFF),
            cursorColor = Color(0xFF5C7BFF)
        )
    )
}

@Composable
private fun DurationOption(selected: Boolean, label: String, bold: Boolean = false, onSelect: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clickable { onSelect() }
            .padding(vertical = 2.dp)
    ) {
        RadioButton(
            selected = selected,
            onClick = onSelect,
            colors = RadioButtonDefaults.colors(
                selectedColor = Color(0xFF5C7BFF),
                unselectedColor = Color(0xFFC3C7D4)
            )
        )
        Text(
            label,
            color = Color(0xFF1C1D2D),
            fontWeight = if (bold && selected) FontWeight.SemiBold else FontWeight.Normal
        )
    }
}
