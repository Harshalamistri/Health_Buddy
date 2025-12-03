@file:OptIn(ExperimentalMaterial3Api::class)

package com.healthapp.screens

import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.Bloodtype
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.healthapp.components.BottomNavigationBar
import com.healthapp.viewmodel.HealthAppViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.abs
import kotlin.math.ceil

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController, viewModel: HealthAppViewModel) {
    val uploadedFiles by viewModel.uploadedFiles.collectAsState()
    val vaccinations by viewModel.vaccinations.collectAsState()
    val profile by viewModel.userProfile.collectAsState()
    val displayName = profile.name.ifBlank { "User" }

    val recentVaccinations = remember(vaccinations) {
        vaccinations.takeLast(2).reversed()
    }
    val recentUploads = remember(uploadedFiles) {
        uploadedFiles.takeLast(2).reversed()
    }

    val recentVaccinationItems = recentVaccinations.map { vaccination ->
        VaccinationItem(
            vaccination.id,
            vaccination.vaccineName,
            vaccination.doseNumber,
            vaccination.dateAdministered,
            vaccination.createdAt
        )
    }

    val recentUploadFileItems = recentUploads.map { file ->
        UploadFileItem(
            file.id,
            file.name,
            file.type,
            file.uploadDate,
            file.size,
            file.url
        )
    }

    // Pastel tints to match the reference UI
    val quickActionBg = listOf(
        Color(0xFFE8F1FF), // light blue
        Color(0xFFF5EAFF), // light purple
        Color(0xFFE7FBF1), // light green
        Color(0xFFE8F7F5)  // light teal
    )
    val quickActionIcon = listOf(
        Color(0xFF3D7EF8),
        Color(0xFFBD4CF0),
        Color(0xFF24C277),
        Color(0xFF36C1A3)
    )

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            BottomNavigationBar(
                items = listOf("Home", "Appointment", "History"),
                selectedItem = 0,
                onItemClick = { index ->
                    when (index) {
                        0 -> {}
                        1 -> navController.navigate("doctors-appointment")
                        2 -> navController.navigate("history")
                    }
                }
            )
        }
    ) { padding ->
        val scrollState = rememberScrollState()
        Column(
            modifier = Modifier
                .padding(padding)
                .verticalScroll(scrollState)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            HomeHeader(name = displayName)

            Text("Quick Actions", fontWeight = FontWeight.SemiBold, fontSize = 16.sp, color = Color(0xFF2A3846))
            val actions = listOf(
                QuickActionData("Set Reminder", Icons.Default.Alarm, quickActionBg[0], quickActionIcon[0]) { navController.navigate("medicine-reminder") },
                QuickActionData("Upload Record", Icons.Default.UploadFile, quickActionBg[1], quickActionIcon[1]) { navController.navigate("upload-record") },
                QuickActionData("Vaccination", Icons.Default.Bloodtype, quickActionBg[2], quickActionIcon[2]) { navController.navigate("vaccination-record") },
                QuickActionData("Update Profile", Icons.Default.Person, quickActionBg[3], quickActionIcon[3]) { navController.navigate("profile") }
            )
            actions.chunked(2).forEach { pair ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    pair.forEach { item ->
                        QuickActionItem(
                            icon = item.icon,
                            label = item.label,
                            containerColor = item.bg,
                            iconTint = item.tint,
                            onClick = item.onClick,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    if (pair.size == 1) Spacer(modifier = Modifier.weight(1f))
                }
            }

            Text("Recommendation", fontWeight = FontWeight.SemiBold, fontSize = 16.sp, color = Color(0xFF2A3846))
            RecommendationCard()

            BenefitsCard()

            Text("Recent Activity", fontWeight = FontWeight.SemiBold, fontSize = 16.sp, color = Color(0xFF2A3846))
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                recentVaccinationItems.forEach { vaccination ->
                    ActivityCard(
                        icon = Icons.Default.Bloodtype,
                        title = "Vaccination",
                        subtitle = "${vaccination.dateAdministered} - ${vaccination.vaccineName}",
                        color = Color(0xFFF0F2F5)
                    )
                }
                recentUploadFileItems.forEach { file ->
                    val icon = Icons.Default.Description
                    ActivityCard(
                        icon = icon,
                        title = "New Record Added",
                        subtitle = "${formatDate(file.uploadDate)} - ${file.name}",
                        color = Color(0xFFF0F2F5)
                    )
                }
                if (recentVaccinationItems.isEmpty() && recentUploadFileItems.isEmpty()) {
                    EmptyRecentCard()
                }
            }

            DailyHealthTip()
        }
    }
}


// -------------------- Helper Components --------------------
data class QuickActionData(
    val label: String,
    val icon: ImageVector,
    val bg: Color,
    val tint: Color,
    val onClick: () -> Unit
)

@Composable
fun HomeHeader(name: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(54.dp)
                .clip(CircleShape)
                .background(
                    Brush.linearGradient(
                        colors = listOf(Color(0xFF7B8CFF), Color(0xFF9BAAFF))
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
                Icon(Icons.Default.Person, contentDescription = null, tint = Color.White, modifier = Modifier.size(28.dp))
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text("Welcome, $name", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = Color(0xFF1D2C3A))
                Text("How are you feeling today?", fontSize = 14.sp, color = Color(0xFF6F7A87))
            }
        }
    }
}

@Composable
fun QuickActionItem(
    icon: ImageVector,
    label: String,
    containerColor: Color,
    iconTint: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = modifier
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick() },
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FBFF)),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
        ) {
            Box(
                modifier = Modifier
                    .padding(vertical = 22.dp)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(containerColor),
                    contentAlignment = Alignment.Center
                ) {
                Icon(
                    icon,
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(22.dp)
                )
            }
        }
        }
        Text(
            label,
            textAlign = TextAlign.Center,
            fontSize = 14.sp,
            color = Color(0xFF2A3846)
        )
    }
}

@Composable
fun BenefitItem(text: String) {
    Row(
        verticalAlignment = Alignment.Top,
        modifier = Modifier.padding(vertical = 4.dp)
    ) {
        Icon(
            Icons.Default.Check,
            contentDescription = null,
            tint = Color(0xFF1CB36E),
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text,
            color = Color(0xFF3E4B59)
        )
    }
}

@Composable
fun BenefitsCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE6FFF1)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            BenefitItem(text = "Regular exercise improves cardiovascular health")
            BenefitItem(text = "Balanced diet boosts immune system")
            BenefitItem(text = "Adequate sleep enhances mental clarity")
        }
    }
}

@Composable
fun RecommendationCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF7E5)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFFFE8B8)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Favorite, contentDescription = null, tint = Color(0xFFD89500))
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text("For illness this days", fontWeight = FontWeight.SemiBold, color = Color(0xFF3A3A3A))
                    AssistChip(
                        onClick = { },
                        label = { Text("Health Alert") },
                        colors = AssistChipDefaults.assistChipColors(
                            containerColor = Color(0xFFFFE8B8),
                            labelColor = Color(0xFF9B6A00)
                        ),
                        modifier = Modifier.padding(top = 6.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                "Based on current health trends, make sure to get adequate rest and maintain good hygiene practices.",
                color = Color(0xFF4A5B6A)
            )
        }
    }
}

@Composable
fun ActivityCard(
    icon: ImageVector,
    title: String,
    subtitle: String,
    color: Color
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
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
                    .clip(RoundedCornerShape(12.dp))
                    .background(color)
                    .padding(12.dp)
            ) {
                Icon(
                    icon,
                    contentDescription = null,
                    tint = Color(0xFF4A5B6A)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    title,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF2A3846)
                )
                Text(
                    subtitle,
                    color = Color(0xFF6F7A87)
                )
            }
        }
    }
}

@Composable
fun EmptyRecentCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(32.dp))
                    .background(Color(0xFFF0F2F5))
                    .padding(16.dp)
            ) {
                Icon(
                    Icons.Default.History,
                    contentDescription = null,
                    tint = Color(0xFF9AA5B1)
                )
            }
            Text(
                "No Recent Activity",
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF2A3846)
            )
            Text(
                "Your recent vaccinations and uploaded records will appear here",
                textAlign = TextAlign.Center,
                color = Color(0xFF6F7A87)
            )
        }
    }
}

@Composable
fun DailyHealthTip() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFE6EFFF)
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFD8E4FF))
                    .padding(12.dp)
            ) {
                Icon(
                    Icons.Default.Lightbulb,
                    contentDescription = null,
                    tint = Color(0xFF4D6FFF)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text("Daily Health Tip", fontWeight = FontWeight.SemiBold, color = Color(0xFF2A3846))
                Text(
                    "Stay hydrated! Drink at least 8 glasses of water today.",
                    color = Color(0xFF4A5B6A)
                )
            }
        }
    }
}

fun formatDate(dateString: String): String {
    return try {
        val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(dateString)
        val now = Date()
        val diffTime = abs(now.time - date?.time!!)
        val diffDays = ceil(diffTime.toDouble() / (1000 * 60 * 60 * 24)).toInt()

        when {
            diffDays == 0 -> "Today"
            diffDays == 1 -> "Yesterday"
            diffDays < 7 -> "$diffDays days ago"
            else -> SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(date)
        }
    } catch (e: Exception) {
        dateString
    }
}

data class VaccinationItem(
    val id: String,
    val vaccineName: String,
    val doseNumber: String,
    val dateAdministered: String,
    val createdAt: String
)

data class UploadFileItem(
    val id: String,
    val name: String,
    val type: String,
    val uploadDate: String,
    val size: String,
    val url: String
)
