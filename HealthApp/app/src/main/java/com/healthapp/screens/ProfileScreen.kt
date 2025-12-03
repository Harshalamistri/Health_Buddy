@file:OptIn(ExperimentalMaterial3Api::class)

package com.healthapp.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Bloodtype
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Male
import androidx.compose.material.icons.filled.ModeEditOutline
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.healthapp.viewmodel.HealthAppViewModel
import com.healthapp.viewmodel.UserProfile

@Composable
fun ProfileScreen(navController: NavController, viewModel: HealthAppViewModel, onLogout: () -> Unit) {
    val profile by viewModel.userProfile.collectAsState()
    var editing by remember { mutableStateOf(false) }

    var name by remember { mutableStateOf(profile.name) }
    var email by remember { mutableStateOf(profile.email) }
    var phone by remember { mutableStateOf(profile.phone) }
    var gender by remember { mutableStateOf(profile.gender) }
    var bloodGroup by remember { mutableStateOf(profile.bloodGroup) }
    var illness by remember { mutableStateOf(profile.illness) }
    var emergencyContact by remember { mutableStateOf(profile.emergencyContact) }
    var address by remember { mutableStateOf(profile.address) }

    LaunchedEffect(profile) {
        if (!editing) {
            name = profile.name
            email = profile.email
            phone = profile.phone
            gender = profile.gender
            bloodGroup = profile.bloodGroup
            illness = profile.illness
            emergencyContact = profile.emergencyContact
            address = profile.address
        }
    }

    val background = Color(0xFFF6F7FB)
    val primary = Color(0xFF0A66FF)
    val textPrimary = Color(0xFF1C1D2D)

    Scaffold(
        containerColor = background,
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White),
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Profile", fontWeight = FontWeight.SemiBold, color = textPrimary)
                        Spacer(modifier = Modifier.width(8.dp))
                        PremiumChip()
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate("home") }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = textPrimary)
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
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ProfileHeaderCard(
                name = if (name.isNotBlank()) name else "",
                email = if (email.isNotBlank()) email else "",
                background = background
            )

            InfoCard(title = "Contact Information") {
                EditableField("Full Name", name, { name = it }, Icons.Default.Person, editing)
                EditableField("Gender", gender, { gender = it }, Icons.Default.Male, editing)
                EditableField("Blood Group", bloodGroup, { bloodGroup = it }, Icons.Default.Bloodtype, editing)
                EditableField("Email Address", email, { email = it }, Icons.Default.Email, editing)
                EditableField("Phone Number", phone, { phone = it }, Icons.Default.Phone, editing, showDivider = false)
            }

            InfoCard(title = "Medical Information", leadingIcon = Icons.Default.FavoriteBorder, leadingTint = Color(0xFFFF4D67)) {
                EditableField("Permanent Illness / Chronic Conditions", illness, { illness = it }, null, editing)
                EditableField("Emergency Contact", emergencyContact, { emergencyContact = it }, Icons.Default.Phone, editing)
                EditableField("Address", address, { address = it }, null, editing, showDivider = false)
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (editing) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = {
                            editing = false
                            name = profile.name
                            email = profile.email
                            phone = profile.phone
                            gender = profile.gender
                            bloodGroup = profile.bloodGroup
                            illness = profile.illness
                            emergencyContact = profile.emergencyContact
                            address = profile.address
                        },
                        modifier = Modifier.weight(1f),
                        border = BorderStroke(1.dp, primary),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Cancel", fontWeight = FontWeight.SemiBold, color = primary)
                    }
                    Button(
                        onClick = {
                            viewModel.updateUserProfile(
                                UserProfile(
                                    name = name.trim(),
                                    email = email.trim(),
                                    phone = phone.trim(),
                                    gender = gender.trim(),
                                    bloodGroup = bloodGroup.trim(),
                                    illness = illness.trim(),
                                    emergencyContact = emergencyContact.trim(),
                                    address = address.trim()
                                )
                            )
                            editing = false
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = primary)
                    ) {
                        Text("Save", fontWeight = FontWeight.SemiBold, color = Color.White)
                    }
                }
            } else {
                Button(
                    onClick = { editing = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = primary)
                ) {
                    Icon(Icons.Default.ModeEditOutline, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Edit Profile", fontWeight = FontWeight.SemiBold)
                }
            }

            OutlinedButton(
                onClick = onLogout,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFE53935)),
                border = BorderStroke(1.dp, Color(0xFFE53935))
            ) {
                Icon(Icons.Default.Logout, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Log Out", fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@Composable
private fun PremiumChip() {
    Box(
        modifier = Modifier
            .background(Color(0xFF7B61FF), RoundedCornerShape(12.dp))
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Text("Premium", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun ProfileHeaderCard(name: String, email: String, background: Color) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(background),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Person, contentDescription = null, tint = Color(0xFF7B8794), modifier = Modifier.size(36.dp))
            }
            Text(name, fontWeight = FontWeight.SemiBold, fontSize = 18.sp, color = Color(0xFF1C1D2D))
            Text(email, color = Color(0xFF6B7280), fontSize = 14.sp)
        }
    }
}

@Composable
private fun InfoCard(
    title: String,
    leadingIcon: androidx.compose.ui.graphics.vector.ImageVector? = null,
    leadingTint: Color = Color(0xFF0A66FF),
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (leadingIcon != null) {
                    Icon(leadingIcon, contentDescription = null, tint = leadingTint, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                }
                Text(title, fontWeight = FontWeight.SemiBold, color = Color(0xFF1C1D2D))
            }
            content()
        }
    }
}

@Composable
private fun EditableField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    leadingIcon: androidx.compose.ui.graphics.vector.ImageVector?,
    editable: Boolean,
    showDivider: Boolean = true
) {
    Column {
        Text(label, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF4B5563))
        Spacer(modifier = Modifier.height(6.dp))
        OutlinedTextField(
            value = value,
            onValueChange = { if (editable) onValueChange(it) },
            readOnly = !editable,
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = leadingIcon?.let { icon ->
                { Icon(icon, contentDescription = null, tint = Color(0xFF8FA3B9), modifier = Modifier.size(18.dp)) }
            },
            singleLine = true,
            shape = RoundedCornerShape(10.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFFDADCE5),
                unfocusedBorderColor = Color(0xFFDADCE5),
                disabledBorderColor = Color(0xFFDADCE5),
                focusedTextColor = Color(0xFF1C1D2D),
                unfocusedTextColor = Color(0xFF1C1D2D),
                cursorColor = Color(0xFF0A66FF),
                disabledTextColor = Color(0xFF1C1D2D)
            )
        )
        if (showDivider) {
            Divider(color = Color(0xFFE5E7EB), modifier = Modifier.padding(top = 12.dp))
        }
    }
}
