package com.healthapp.screens

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Environment
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Badge
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.navigation.NavController
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class UploadedFile(
    val id: String,
    val name: String,
    val type: String, // "image" or "document"
    val uploadDate: String,
    val size: String,
    val url: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UploadRecordScreen(
    navController: NavController,
    onFileUpload: (UploadedFile) -> Unit,
    uploadedFiles: List<UploadedFile>,
    onDeleteFile: (String) -> Unit
) {
    var showUpgradeDialog by remember { mutableStateOf(false) }
    var deleteFileId by remember { mutableStateOf<String?>(null) }
    var customName by remember { mutableStateOf("") }
    val maxFiles = 30
    val scrollState = rememberScrollState()
    val context = LocalContext.current

    fun formatFileSize(bytes: Long): String {
        return when {
            bytes < 1024 -> "$bytes B"
            bytes < 1024 * 1024 -> "${String.format("%.1f", bytes / 1024.0)} KB"
            else -> "${String.format("%.1f", bytes / (1024.0 * 1024.0))} MB"
        }
    }

    fun formatDate(dateString: String): String {
        val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        val date = format.parse(dateString)
        val output = SimpleDateFormat("MMM dd, yyyy 'at' HH:mm", Locale.getDefault())
        return output.format(date ?: Date())
    }

    fun checkFileLimit(count: Int = 1): Boolean {
        if (uploadedFiles.size + count > maxFiles) {
            showUpgradeDialog = true
            return false
        }
        return true
    }

    fun resolveDisplayName(uri: Uri): String? {
        val projection = arrayOf(android.provider.OpenableColumns.DISPLAY_NAME)
        val cursor: Cursor? = context.contentResolver.query(uri, projection, null, null, null)
        cursor?.use {
            val nameIndex = it.getColumnIndex(android.provider.OpenableColumns.DISPLAY_NAME)
            if (it.moveToFirst() && nameIndex >= 0) return it.getString(nameIndex)
        }
        return null
    }

    fun resolveSize(uri: Uri): Long? {
        val projection = arrayOf(android.provider.OpenableColumns.SIZE)
        val cursor: Cursor? = context.contentResolver.query(uri, projection, null, null, null)
        cursor?.use {
            val sizeIndex = it.getColumnIndex(android.provider.OpenableColumns.SIZE)
            if (it.moveToFirst() && sizeIndex >= 0) return it.getLong(sizeIndex)
        }
        return null
    }

    fun addFileFromUri(
        uri: Uri,
        explicitName: String? = null,
        explicitType: String? = null,
        explicitSize: Long? = null
    ) {
        if (!checkFileLimit()) return
        val guessedName = explicitName ?: resolveDisplayName(uri)
        val pickedName = customName.ifBlank { guessedName ?: "Document" }
        val mimeType = explicitType ?: context.contentResolver.getType(uri) ?: "application/octet-stream"
        val sizeBytes = explicitSize ?: resolveSize(uri) ?: 0L
        val newFile = UploadedFile(
            id = System.currentTimeMillis().toString() + (0..1000).random().toString(),
            name = pickedName,
            type = mimeType,
            url = uri.toString(),
            size = formatFileSize(sizeBytes),
            uploadDate = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault()).format(Date())
        )
        onFileUpload(newFile)
        customName = ""
    }

    val filePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { addFileFromUri(it) }
    }

    var pendingPhotoUri by remember { mutableStateOf<Uri?>(null) }
    var pendingPhotoFile by remember { mutableStateOf<File?>(null) }

    val takePictureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        val capturedUri = pendingPhotoUri
        val capturedFile = pendingPhotoFile
        if (success && capturedUri != null) {
            val defaultName = capturedFile?.nameWithoutExtension ?: "Captured Photo"
            addFileFromUri(
                uri = capturedUri,
                explicitName = defaultName,
                explicitType = "image/jpeg",
                explicitSize = capturedFile?.length()
            )
        } else {
            capturedFile?.delete()
        }
        pendingPhotoUri = null
        pendingPhotoFile = null
    }

    fun createImageFile(): File? {
        return runCatching {
            val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            File.createTempFile("photo_${timeStamp}_", ".jpg", storageDir)
        }.getOrNull()
    }

    fun launchCamera() {
        if (!checkFileLimit()) return
        val photoFile = createImageFile()
        val photoUri = photoFile?.let {
            FileProvider.getUriForFile(
                context,
                "${context.packageName}.provider",
                it
            )
        }
        if (photoFile != null && photoUri != null) {
            pendingPhotoFile = photoFile
            pendingPhotoUri = photoUri
            takePictureLauncher.launch(photoUri)
        }
    }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            launchCamera()
        }
    }

    fun launchCameraWithPermission() {
        when {
            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED -> launchCamera()
            else -> cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    fun getFileIcon(type: String): ImageVector = when {
        type.startsWith("image") -> Icons.Default.Image
        else -> Icons.Default.Description
    }

    fun openFile(uriString: String) {
        if (uriString.isBlank()) return
        runCatching {
            val uri = Uri.parse(uriString)
            val intent = Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(uri, context.contentResolver.getType(uri) ?: "*/*")
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            context.startActivity(intent)
        }
    }

    fun confirmDelete(id: String) {
        deleteFileId = id
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Upload Medical Records", fontWeight = FontWeight.Bold)
                        Text("Add photos and files securely", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
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
                .verticalScroll(scrollState)
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = customName,
                onValueChange = { customName = it },
                label = { Text("Document Name (optional)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { filePicker.launch("*/*") },
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F1FF)),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(52.dp)
                                .background(Color(0xFFD8E7FF), RoundedCornerShape(12.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Upload, contentDescription = null, tint = Color(0xFF4374E0))
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Upload Files", fontWeight = FontWeight.SemiBold)
                        Text("Pick from device", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }

                Card(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { launchCameraWithPermission() },
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFEFF3FF)),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(52.dp)
                                .background(Color(0xFFDDE7FF), RoundedCornerShape(12.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.CameraAlt, contentDescription = null, tint = Color(0xFF5D6EF7))
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Take Photo", fontWeight = FontWeight.SemiBold)
                        Text("Use camera", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFEFF3FF)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .background(Color(0xFFDBE7FF), RoundedCornerShape(12.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Description, contentDescription = null, tint = Color(0xFF4F6FFF))
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text("Supported Files", fontWeight = FontWeight.SemiBold, color = Color(0xFF1D2C3A))
                        Text(
                            text = "You can upload images (JPG, PNG), PDFs, and documents (DOC, DOCX). Maximum file size: 10MB per file.",
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F8F2)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .background(Color(0xFF9AE6C6), RoundedCornerShape(12.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF1B8A5A))
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Free Plan: ${uploadedFiles.size}/$maxFiles Files Used",
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF1D2C3A)
                            )
                            Badge(containerColor = Color(0xFFE0F7EC), contentColor = Color(0xFF1B8A5A)) {
                                Text("${(maxFiles - uploadedFiles.size).coerceAtLeast(0)} left")
                            }
                        }
                        LinearProgressIndicator(
                            progress = (uploadedFiles.size.toFloat() / maxFiles).coerceIn(0f, 1f),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            color = Color(0xFF1B8A5A)
                        )
                        Text(
                            text = "Upload more medical records to keep all your health data organized.",
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Uploaded Files (${uploadedFiles.size})",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
                if (uploadedFiles.isNotEmpty()) {
                    Badge(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    ) {
                        Text("${uploadedFiles.size} file${if (uploadedFiles.size != 1) "s" else ""}")
                    }
                }
            }

            if (uploadedFiles.isEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF7F8FA)),
                    shape = RoundedCornerShape(16.dp)
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
                                .background(Color(0xFFEAEFF5), RoundedCornerShape(12.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Upload, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Text(
                            text = "No files uploaded yet",
                            modifier = Modifier.padding(top = 8.dp),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "Upload files or take a photo to get started",
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    uploadedFiles.forEach { file ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { openFile(file.url) },
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                if (file.type.startsWith("image")) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(180.dp)
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(Color(0xFFF1F3F5)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(Icons.Default.Image, contentDescription = null, modifier = Modifier.size(64.dp))
                                    }
                                } else {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(120.dp)
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(Color(0xFFF6F7FB)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(getFileIcon(file.type), contentDescription = null, modifier = Modifier.size(48.dp))
                                    }
                                }

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 12.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(file.name, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onSurface)
                                        Text(file.size, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    }
                                    Badge(
                                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                                    ) { Text("Uploaded") }
                                }

                                Text(
                                    text = formatDate(file.uploadDate),
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontSize = 12.sp,
                                    modifier = Modifier.padding(vertical = 8.dp)
                                )

                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    Button(
                                        onClick = { openFile(file.url) },
                                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.outline),
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Icon(Icons.Default.Download, contentDescription = null, modifier = Modifier.size(14.dp))
                                        Text(text = "Open", fontSize = 12.sp, modifier = Modifier.padding(start = 4.dp))
                                    }
                                    Button(
                                        onClick = { confirmDelete(file.id) },
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = MaterialTheme.colorScheme.errorContainer,
                                            contentColor = MaterialTheme.colorScheme.onErrorContainer
                                        ),
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Icon(Icons.Default.Delete, contentDescription = null, modifier = Modifier.size(14.dp))
                                        Text(text = "Delete", fontSize = 12.sp, modifier = Modifier.padding(start = 4.dp))
                                    }
                                }
                            }
                        }
                    }
                }
            }

            if (uploadedFiles.isNotEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFEFF8FF))
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Pro Tip",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF1D2C3A)
                            )
                            Text(
                                text = "Make sure your medical records are clear and readable. You can add multiple files before saving.",
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }

    if (deleteFileId != null) {
        AlertDialog(
            onDismissRequest = { deleteFileId = null },
            title = { Text("Delete Record") },
            text = { Text("Are you sure you want to delete this record? This action cannot be undone.") },
            confirmButton = {
                Button(
                    onClick = {
                        deleteFileId?.let { id ->
                            onDeleteFile(id)
                            deleteFileId = null
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error,
                        contentColor = MaterialTheme.colorScheme.onError
                    )
                ) { Text("Delete") }
            },
            dismissButton = {
                OutlinedButton(onClick = { deleteFileId = null }) { Text("Cancel") }
            }
        )
    }

    if (showUpgradeDialog) {
        AlertDialog(
            onDismissRequest = { showUpgradeDialog = false },
            title = { Text("Upgrade Required") },
            text = { Text("You have reached the file limit for the free plan. Upgrade to upload more files.") },
            confirmButton = {
                Button(
                    onClick = { showUpgradeDialog = false },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) { Text("Upgrade") }
            },
            dismissButton = {
                OutlinedButton(onClick = { showUpgradeDialog = false }) { Text("Cancel") }
            }
        )
    }
}
