package com.nammamela.app.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.LibraryAdd
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
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
import java.io.File

@OptIn(ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class)
@Composable
fun ManageCastScreen(
    playId: Int,
    viewModel: ManageCastViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {
    var showSheet by remember { mutableStateOf(false) }
    var showSavedCastSheet by remember { mutableStateOf(false) }
    var editingActor by remember { mutableStateOf<Actor?>(null) }
    val sheetState = rememberModalBottomSheetState()
    val savedCastSheetState = rememberModalBottomSheetState()

    var actorName by remember { mutableStateOf("") }
    var actorRole by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("Lead Actor") }

    val categories = listOf("Lead Actor", "Comedian", "Singer", "Supporting")
    val cast by viewModel.cast.collectAsState()
    val pickedPhotoUri by viewModel.pickedPhotoUri.collectAsState()
    val plays by viewModel.plays.collectAsState()
    val selectedPlayId by viewModel.selectedPlayId.collectAsState()
    val selectedPlayTitle = plays.find { it.id == selectedPlayId }?.title
    val canEditCast = playId > 0 || selectedPlayId != null

    val photoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri -> viewModel.onPhotoPicked(uri) }

    fun openAddSheet() {
        showSavedCastSheet = false
        editingActor = null
        actorName = ""
        actorRole = ""
        selectedCategory = "Lead Actor"
        viewModel.clearPickedPhoto()
        showSheet = true
    }

    fun openEditSheet(actor: Actor) {
        editingActor = actor
        actorName = actor.name
        actorRole = actor.role
        selectedCategory = actor.category
        viewModel.clearPickedPhoto()
        showSheet = true
    }

    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(Unit) {
        viewModel.errorEvent.collect { message ->
            snackbarHostState.showSnackbar(message = message)
        }
    }

    Scaffold(
        containerColor = NammaDarkBrown,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("MANAGE CAST", color = NammaGold, style = MaterialTheme.typography.labelLarge.copy(letterSpacing = 2.sp))
                        selectedPlayTitle?.takeIf { it.isNotBlank() }?.let { t ->
                            Text(
                                t,
                                color = NammaWarmWhite.copy(0.55f),
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, null, tint = NammaGold)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.Transparent)
            )
        },
        floatingActionButton = {
            if (canEditCast) {
                FloatingActionButton(
                    onClick = { openAddSheet() },
                    containerColor = NammaGold,
                    contentColor = NammaDarkBrown
                ) {
                    Icon(Icons.Default.Add, null)
                }
            }
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {
            if (showSavedCastSheet) {
                ModalBottomSheet(
                    onDismissRequest = { showSavedCastSheet = false },
                    sheetState = savedCastSheetState,
                    containerColor = NammaSurfaceLow,
                    contentColor = NammaWarmWhite
                ) {
                    val pool = viewModel.savedCastPoolForPicker()
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .padding(bottom = 32.dp)
                    ) {
                        Text(
                            "Saved cast",
                            color = NammaWarmWhite,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            "Pick someone already added on another play. A copy is added to this play.",
                            color = NammaWarmWhite.copy(0.55f),
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(top = 4.dp, bottom = 12.dp)
                        )
                        if (pool.isEmpty()) {
                            Text(
                                "No other cast entries yet. Add performers on another play first.",
                                color = NammaWarmWhite.copy(0.45f),
                                style = MaterialTheme.typography.bodySmall
                            )
                        } else {
                            LazyColumn(
                                verticalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier.heightIn(max = 420.dp)
                            ) {
                                items(pool, key = { it.id }) { a ->
                                    val fromPlayTitle = plays.find { it.id == a.playId }?.title ?: "Play #${a.playId}"
                                    Surface(
                                        onClick = {
                                            viewModel.cloneActorFromSaved(a)
                                            showSavedCastSheet = false
                                        },
                                        color = NammaSurfaceHigh,
                                        shape = RoundedCornerShape(12.dp),
                                        border = androidx.compose.foundation.BorderStroke(1.dp, NammaWarmWhite.copy(0.08f)),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(12.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Box(
                                                modifier = Modifier.size(44.dp).clip(CircleShape).background(NammaSurfaceLow),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                val path = a.imageUrl
                                                if (!path.isNullOrBlank()) {
                                                    val model: Any = if (path.startsWith("/")) File(path) else path
                                                    GlideImage(
                                                        model = model,
                                                        contentDescription = null,
                                                        modifier = Modifier.fillMaxSize(),
                                                        contentScale = ContentScale.Crop
                                                    )
                                                } else {
                                                    Icon(Icons.Default.Person, null, tint = NammaGold.copy(0.5f), modifier = Modifier.size(24.dp))
                                                }
                                            }
                                            Spacer(Modifier.width(12.dp))
                                            Column(Modifier.weight(1f)) {
                                                Text(
                                                    a.name,
                                                    color = NammaWarmWhite,
                                                    fontWeight = FontWeight.SemiBold,
                                                    maxLines = 2,
                                                    overflow = TextOverflow.Ellipsis
                                                )
                                                Text(
                                                    "${a.role} · $fromPlayTitle",
                                                    color = NammaWarmWhite.copy(0.5f),
                                                    style = MaterialTheme.typography.bodySmall,
                                                    maxLines = 2,
                                                    overflow = TextOverflow.Ellipsis
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

            if (showSheet) {
                ModalBottomSheet(
                    onDismissRequest = {
                        showSheet = false
                        editingActor = null
                    },
                    sheetState = sheetState,
                    containerColor = NammaSurfaceLow,
                    contentColor = NammaWarmWhite
                ) {
                    CastMemberForm(
                        isEdit = editingActor != null,
                        name = actorName,
                        onNameChange = { actorName = it },
                        role = actorRole,
                        onRoleChange = { actorRole = it },
                        category = selectedCategory,
                        categories = categories,
                        onCategoryChange = { selectedCategory = it },
                        pickedPhotoUri = pickedPhotoUri,
                        existingPhotoPath = editingActor?.imageUrl,
                        onPickImage = {
                            photoPicker.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        },
                        onSave = {
                            if (actorName.isBlank()) return@CastMemberForm
                            if (editingActor == null) {
                                viewModel.addActor(actorName, actorRole, selectedCategory)
                            } else {
                                viewModel.updateActor(
                                    editingActor!!,
                                    actorName,
                                    actorRole,
                                    selectedCategory,
                                    pickedPhotoUri
                                )
                            }
                            showSheet = false
                            editingActor = null
                        },
                        onCancel = {
                            showSheet = false
                            editingActor = null
                            viewModel.clearPickedPhoto()
                        }
                    )
                }
            }

            if (playId == 0) {
                Text(
                    "ASSIGN TO PLAY",
                    color = NammaGold.copy(0.4f),
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.padding(start = 24.dp, end = 24.dp, top = 8.dp),
                    letterSpacing = 2.sp
                )
                if (plays.isEmpty()) {
                    Text(
                        "No plays yet. Create a play first, then add cast.",
                        color = NammaWarmWhite.copy(0.45f),
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
                    )
                } else {
                    Column(
                        modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                            .fillMaxWidth()
                    ) {
                        plays.forEach { p ->
                            val selected = selectedPlayId == p.id
                            Surface(
                                onClick = { viewModel.selectPlay(p.id) },
                                color = if (selected) NammaGold.copy(0.12f) else NammaSurfaceLow,
                                shape = RoundedCornerShape(12.dp),
                                border = androidx.compose.foundation.BorderStroke(
                                    1.dp,
                                    if (selected) NammaGold.copy(0.4f) else NammaWarmWhite.copy(0.06f)
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                            ) {
                                Row(
                                    Modifier.padding(16.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    RadioButton(
                                        selected = selected,
                                        onClick = { viewModel.selectPlay(p.id) },
                                        colors = RadioButtonDefaults.colors(
                                            selectedColor = NammaGold,
                                            unselectedColor = NammaWarmWhite.copy(0.4f)
                                        )
                                    )
                                    Spacer(Modifier.width(8.dp))
                                    Column {
                                        Text(p.title, color = NammaWarmWhite, fontWeight = FontWeight.SemiBold)
                                        if (p.isActive) {
                                            Text("Tonight’s show", color = NammaGold.copy(0.7f), style = MaterialTheme.typography.labelSmall)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                if (!canEditCast) {
                    Text(
                        "Select a play above to add or edit cast.",
                        color = NammaWarmWhite.copy(0.4f),
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
                    )
                }
                Divider(color = NammaWarmWhite.copy(0.08f), modifier = Modifier.padding(vertical = 8.dp))
            }

            Text(
                "CAST",
                color = NammaGold.copy(0.4f),
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.padding(horizontal = 24.dp),
                letterSpacing = 2.sp
            )

            if (canEditCast) {
                OutlinedButton(
                    onClick = {
                        showSheet = false
                        showSavedCastSheet = true
                    },
                    modifier = Modifier
                        .padding(horizontal = 24.dp, vertical = 10.dp)
                        .fillMaxWidth(),
                    border = androidx.compose.foundation.BorderStroke(1.dp, NammaGold.copy(0.35f)),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = NammaGold)
                ) {
                    Icon(Icons.Outlined.LibraryAdd, contentDescription = null, modifier = Modifier.size(20.dp))
                    Spacer(Modifier.width(10.dp))
                    Text(
                        "Add from saved cast",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            if (!canEditCast) {
                Box(modifier = Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Text("Choose a play to see cast.", color = NammaWarmWhite.copy(0.35f))
                }
            } else if (cast.isEmpty()) {
                Box(modifier = Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Text("No cast members added yet.", color = NammaWarmWhite.copy(0.3f))
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(start = 24.dp, end = 24.dp, bottom = 100.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    items(cast, key = { it.id }) { actor ->
                        AdminActorCard(
                            actor = actor,
                            onEdit = { openEditSheet(actor) },
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
private fun CastMemberForm(
    isEdit: Boolean,
    name: String,
    onNameChange: (String) -> Unit,
    role: String,
    onRoleChange: (String) -> Unit,
    category: String,
    categories: List<String>,
    onCategoryChange: (String) -> Unit,
    pickedPhotoUri: Uri?,
    existingPhotoPath: String?,
    onPickImage: () -> Unit,
    onSave: () -> Unit,
    onCancel: () -> Unit
) {
    Column(modifier = Modifier.padding(24.dp).padding(bottom = 32.dp)) {
        Text(
            if (isEdit) "Edit performer" else "Add new performer",
            color = NammaWarmWhite,
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
        )
        Spacer(modifier = Modifier.height(24.dp))

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
            when {
                pickedPhotoUri != null -> {
                    GlideImage(
                        model = pickedPhotoUri,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
                !existingPhotoPath.isNullOrBlank() -> {
                    val model: Any = if (existingPhotoPath.startsWith("/")) File(existingPhotoPath) else existingPhotoPath
                    GlideImage(
                        model = model,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
                else -> Icon(Icons.Default.CameraAlt, null, tint = NammaGold)
            }
        }
        Text(
            "Tap to upload profile photo",
            color = NammaWarmWhite.copy(0.4f),
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.align(Alignment.CenterHorizontally).padding(top = 8.dp)
        )

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
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(categories, key = { it }) { cat ->
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
                text = if (isEdit) "Save changes" else "Add to cast",
                onClick = onSave,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun AdminActorCard(actor: Actor, onEdit: () -> Unit, onDelete: () -> Unit) {
    Surface(
        color = NammaSurfaceLow,
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth(),
        border = androidx.compose.foundation.BorderStroke(1.dp, NammaWarmWhite.copy(0.03f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier.size(48.dp).clip(CircleShape).background(NammaSurfaceHigh),
                    contentAlignment = Alignment.Center
                ) {
                    val path = actor.imageUrl
                    if (!path.isNullOrBlank()) {
                        val model: Any = if (path.startsWith("/")) File(path) else path
                        GlideImage(
                            model = model,
                            contentDescription = actor.name,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Icon(Icons.Default.Person, null, tint = NammaGold.copy(0.5f))
                    }
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .widthIn(min = 0.dp)
                ) {
                    Text(
                        actor.name,
                        color = NammaWarmWhite,
                        fontWeight = FontWeight.Bold,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.titleSmall
                    )
                    Spacer(Modifier.height(2.dp))
                    Text(
                        actor.role,
                        color = NammaWarmWhite.copy(0.55f),
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    color = NammaGold.copy(0.1f),
                    shape = RoundedCornerShape(4.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, NammaGold.copy(0.3f)),
                    modifier = Modifier.weight(1f, fill = false).padding(end = 8.dp)
                ) {
                    Text(
                        actor.category.uppercase(),
                        color = NammaGold,
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Row {
                    IconButton(onClick = onEdit) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit", tint = NammaGold)
                    }
                    IconButton(onClick = onDelete) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete", tint = NammaError.copy(0.7f))
                    }
                }
            }
        }
    }
}
