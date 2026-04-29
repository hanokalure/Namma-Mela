package com.nammamela.app.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import com.nammamela.app.domain.model.Actor
import com.nammamela.app.ui.components.NammaMelaButton
import com.nammamela.app.ui.theme.*
import com.nammamela.app.viewmodel.ManageCastViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class)
@Composable
fun ManageCastScreen(
    playId: Int,
    viewModel: ManageCastViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {
    var showSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()
    
    var actorName by remember { mutableStateOf("") }
    var actorRole by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("Lead Actor") }
    
    val categories = listOf("Lead Actor", "Comedian", "Singer", "Supporting")
    val cast by viewModel.cast.collectAsState()
    val selectedImageUri by viewModel.selectedImageUri.collectAsState()

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        viewModel.onImageSelected(uri)
    }

    Scaffold(
        containerColor = NammaDarkBrown,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("MANAGE CAST", color = NammaGold, style = MaterialTheme.typography.labelLarge.copy(letterSpacing = 2.sp)) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, null, tint = NammaGold)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.Transparent)
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showSheet = true },
                containerColor = NammaGold,
                contentColor = NammaDarkBrown
            ) {
                Icon(Icons.Default.Add, null)
            }
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {
            if (showSheet) {
                ModalBottomSheet(
                    onDismissRequest = { showSheet = false },
                    sheetState = sheetState,
                    containerColor = NammaSurfaceLow,
                    contentColor = NammaWarmWhite
                ) {
                    AddActorForm(
                        name = actorName,
                        onNameChange = { actorName = it },
                        role = actorRole,
                        onRoleChange = { actorRole = it },
                        category = selectedCategory,
                        categories = categories,
                        onCategoryChange = { selectedCategory = it },
                        selectedImageUri = selectedImageUri,
                        onPickImage = { launcher.launch("image/*") },
                        onSave = { 
                            if (actorName.isNotEmpty()) {
                                viewModel.addActor(actorName, actorRole, selectedCategory)
                                showSheet = false
                                actorName = ""
                                actorRole = ""
                            }
                        },
                        onCancel = { showSheet = false }
                    )
                }
            }

            Text(
                "TONIGHT'S TROUPE", 
                color = NammaGold.copy(0.4f), 
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.padding(24.dp),
                letterSpacing = 2.sp
            )

            if (cast.isEmpty()) {
                Box(modifier = Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Text("No cast members added yet.", color = NammaWarmWhite.copy(0.3f))
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(start = 24.dp, end = 24.dp, bottom = 100.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    items(cast) { actor ->
                        AdminActorCard(
                            actor = actor,
                            onDelete = { viewModel.deleteActor(actor) }
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class)
@Composable
fun AddActorForm(
    name: String,
    onNameChange: (String) -> Unit,
    role: String,
    onRoleChange: (String) -> Unit,
    category: String,
    categories: List<String>,
    onCategoryChange: (String) -> Unit,
    selectedImageUri: Uri?,
    onPickImage: () -> Unit,
    onSave: () -> Unit,
    onCancel: () -> Unit
) {
    Column(modifier = Modifier.padding(24.dp).padding(bottom = 32.dp)) {
        Text("Add New Performer", color = NammaWarmWhite, style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold))
        Spacer(modifier = Modifier.height(24.dp))

        // Actor Image Picker
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(NammaSurfaceHigh)
                .border(2.dp, NammaGold.copy(0.3f), CircleShape)
                .clickable { onPickImage() }
                .align(Alignment.CenterHorizontally),
            contentAlignment = Alignment.Center
        ) {
            if (selectedImageUri != null) {
                GlideImage(model = selectedImageUri, contentDescription = null, modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
            } else {
                Icon(Icons.Default.CameraAlt, null, tint = NammaGold)
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        OutlinedTextField(
            value = name,
            onValueChange = onNameChange,
            label = { Text("Actor Name") },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = NammaGold, 
                unfocusedBorderColor = NammaWarmWhite.copy(0.2f), 
                focusedTextColor = NammaWarmWhite, 
                unfocusedTextColor = NammaWarmWhite
            )
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = role,
            onValueChange = onRoleChange,
            label = { Text("Stage Role") },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = NammaGold, 
                unfocusedBorderColor = NammaWarmWhite.copy(0.2f), 
                focusedTextColor = NammaWarmWhite, 
                unfocusedTextColor = NammaWarmWhite
            )
        )
        Spacer(modifier = Modifier.height(24.dp))
        
        Text("Category", color = NammaGold, style = MaterialTheme.typography.labelSmall)
        Spacer(modifier = Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
            categories.forEach { cat ->
                FilterChip(
                    selected = category == cat,
                    onClick = { onCategoryChange(cat) },
                    label = { Text(cat, fontSize = 10.sp) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = NammaGold, 
                        selectedLabelColor = NammaDarkBrown,
                        labelColor = NammaWarmWhite.copy(0.6f)
                    )
                )
            }
        }
        
        Spacer(modifier = Modifier.height(40.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            OutlinedButton(
                onClick = onCancel, 
                modifier = Modifier.weight(1f).height(56.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, NammaWarmWhite.copy(0.1f))
            ) { 
                Text("Cancel", color = NammaWarmWhite) 
            }
            NammaMelaButton(
                text = "Add to Cast", 
                onClick = onSave, 
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun AdminActorCard(actor: Actor, onDelete: () -> Unit) {
    Surface(
        color = NammaSurfaceLow,
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth(),
        border = androidx.compose.foundation.BorderStroke(1.dp, NammaWarmWhite.copy(0.03f))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.size(56.dp).clip(CircleShape).background(NammaSurfaceHigh),
                contentAlignment = Alignment.Center
            ) {
                GlideImage(model = actor.imageUrl, contentDescription = null, modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(actor.name, color = NammaWarmWhite, fontWeight = FontWeight.Bold)
                Text(actor.role, color = NammaWarmWhite.copy(0.5f), style = MaterialTheme.typography.bodySmall)
            }
            Surface(
                color = NammaGold.copy(0.1f),
                shape = RoundedCornerShape(4.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, NammaGold.copy(0.3f))
            ) {
                Text(
                    actor.category.uppercase(), 
                    color = NammaGold, 
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            IconButton(onClick = onDelete) { Icon(Icons.Default.Delete, null, tint = NammaError.copy(0.7f)) }
        }
    }
}
