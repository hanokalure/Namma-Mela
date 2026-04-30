package com.nammamela.app.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.nammamela.app.ui.components.NammaMelaButton
import com.nammamela.app.ui.theme.*
import com.nammamela.app.viewmodel.AdminViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class)
@Composable
fun AdminPlayManagementScreen(
    viewModel: AdminViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var genre by remember { mutableStateOf("") }
    var duration by remember { mutableStateOf("") }
    var showTime by remember { mutableStateOf("10:00 PM") }
    var price by remember { mutableStateOf("150.00") }

    val selectedImageUri by viewModel.selectedImageUri.collectAsState()
    val isSaveSuccess by viewModel.isSaveSuccess.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    // Gallery Picker Launcher
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        viewModel.onImageSelected(uri)
    }

    LaunchedEffect(Unit) {
        viewModel.errorEvent.collect { message ->
            snackbarHostState.showSnackbar(message = message)
        }
    }

    LaunchedEffect(isSaveSuccess) {
        if (isSaveSuccess) {
            viewModel.resetSuccessState()
            onNavigateBack()
        }
    }

    Scaffold(
        containerColor = NammaDarkBrown,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("UPLOAD TONIGHT'S PLAY", color = NammaGold, style = MaterialTheme.typography.labelLarge.copy(letterSpacing = 2.sp)) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, null, tint = NammaGold)
                    }
                },
                actions = {
                    IconButton(onClick = { 
                        viewModel.saveTonightPlay(title, description, genre, duration, showTime, price)
                    }) {
                        Icon(Icons.Default.Save, null, tint = NammaGold)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.Transparent)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Image Upload Placeholder / Preview
            Surface(
                onClick = { launcher.launch("image/*") },
                color = NammaSurfaceLow,
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier.fillMaxWidth().height(260.dp),
                border = BorderStroke(1.dp, NammaWarmWhite.copy(0.1f))
            ) {
                if (selectedImageUri != null) {
                    GlideImage(
                        model = selectedImageUri,
                        contentDescription = "Poster Preview",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(Icons.Default.AddPhotoAlternate, null, tint = NammaGold, modifier = Modifier.size(48.dp))
                        Spacer(modifier = Modifier.height(12.dp))
                        Text("Pick Poster from Gallery", color = NammaWarmWhite.copy(0.6f))
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text("PLAY DETAILS", color = NammaGold, style = MaterialTheme.typography.labelSmall, letterSpacing = 2.sp)
            Spacer(modifier = Modifier.height(16.dp))

            AdminField("Show Title", title, { title = it }, "e.g. Sati-Savitri")
            Spacer(modifier = Modifier.height(20.dp))
            AdminField("Description", description, { description = it }, "The epic tale of...", isMultiline = true)
            Spacer(modifier = Modifier.height(20.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Box(modifier = Modifier.weight(1f)) {
                    AdminField("Genre", genre, { genre = it }, "Drama")
                }
                Box(modifier = Modifier.weight(1f)) {
                    AdminField("Duration", duration, { duration = it }, "3h 15m")
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            AdminField("Show Timing", showTime, { showTime = it }, "e.g. 10:00 PM")
            
            Spacer(modifier = Modifier.height(32.dp))
            Text("BOX OFFICE SETTINGS", color = NammaGold, style = MaterialTheme.typography.labelSmall, letterSpacing = 2.sp)
            Spacer(modifier = Modifier.height(16.dp))
            
            AdminField("Base Ticket Price (₹)", price, { price = it }, "150.00")
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Surface(
                color = NammaSurfaceLow,
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, Color.White.copy(0.05f))
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Info, null, tint = NammaGold, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        "Theater layout is fixed to 66 Seats (A-F).",
                        style = MaterialTheme.typography.bodySmall,
                        color = NammaWarmWhite.copy(0.6f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(48.dp))
            
            NammaMelaButton(
                text = "OPEN BOOKINGS  🎟",
                onClick = { 
                    viewModel.saveTonightPlay(title, description, genre, duration, showTime, price)
                }
            )
            
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    isMultiline: Boolean = false
) {
    Column {
        Text(label, color = NammaWarmWhite.copy(0.5f), style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold))
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(placeholder, color = NammaWarmWhite.copy(0.2f)) },
            modifier = Modifier.fillMaxWidth().then(if (isMultiline) Modifier.height(120.dp) else Modifier),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = NammaGold,
                unfocusedBorderColor = NammaWarmWhite.copy(0.1f),
                focusedContainerColor = NammaSurfaceLow,
                unfocusedContainerColor = NammaSurfaceLow,
                focusedTextColor = NammaWarmWhite,
                unfocusedTextColor = NammaWarmWhite
            ),
            shape = RoundedCornerShape(12.dp),
            singleLine = !isMultiline
        )
    }
}
