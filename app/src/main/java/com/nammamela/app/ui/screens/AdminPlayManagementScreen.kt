package com.nammamela.app.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.nammamela.app.R
import com.nammamela.app.domain.model.Play
import com.nammamela.app.ui.components.NammaMelaButton
import com.nammamela.app.ui.theme.*
import com.nammamela.app.viewmodel.AdminViewModel
import java.io.File

@OptIn(ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class)
@Composable
fun AdminPlayManagementScreen(
    viewModel: AdminViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
    onNavigateToManageCast: (Int) -> Unit
) {
    var showForm by remember { mutableStateOf(false) }
    var editingPlayId by remember { mutableStateOf<Int?>(null) }

    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var genre by remember { mutableStateOf("") }
    var duration by remember { mutableStateOf("") }
    var showTimeSlots by remember { mutableStateOf(listOf<String>()) }
    var newTimeInput by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("150.00") }
    var setAsTonight by remember { mutableStateOf(true) }
    var existingPosterUrl by remember { mutableStateOf<String?>(null) }

    fun showTimesJoined(): String = showTimeSlots.joinToString(", ")

    fun resetFormForNew() {
        editingPlayId = null
        title = ""
        description = ""
        genre = ""
        duration = ""
        showTimeSlots = emptyList()
        newTimeInput = ""
        price = "150.00"
        setAsTonight = true
        existingPosterUrl = null
        viewModel.clearPosterSelection()
    }

    fun openEdit(play: Play) {
        editingPlayId = play.id
        title = play.title
        description = play.description
        genre = play.genre
        duration = play.duration
        showTimeSlots = play.showTime.split(",").map { it.trim() }.filter { it.isNotEmpty() }
        price = String.format("%.2f", play.ticketPrice).trimEnd('0').trimEnd('.').ifEmpty { play.ticketPrice.toString() }
        setAsTonight = play.isActive
        existingPosterUrl = play.posterUrl
        viewModel.clearPosterSelection()
        showForm = true
    }

    val selectedImageUri by viewModel.selectedImageUri.collectAsState()
    val allPlays by viewModel.allPlays.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    val posterLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        viewModel.onImageSelected(uri)
    }

    var playPendingDelete by remember { mutableStateOf<Play?>(null) }

    LaunchedEffect(Unit) {
        viewModel.errorEvent.collect { message ->
            snackbarHostState.showSnackbar(message = message)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.commitOutcome.collect { outcome ->
            showForm = false
            resetFormForNew()
            if (outcome.openCastScreen) {
                onNavigateToManageCast(outcome.playId)
            }
        }
    }

    if (playPendingDelete != null) {
        AlertDialog(
            onDismissRequest = { playPendingDelete = null },
            title = { Text("Delete play?", color = NammaGold) },
            text = {
                Text(
                    "“${playPendingDelete!!.title}” and its seats, cast links, and bookings for this play will be removed.",
                    color = NammaWarmWhite.copy(0.8f)
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        playPendingDelete?.let { viewModel.deletePlay(it.id) }
                        playPendingDelete = null
                    }
                ) {
                    Text("Delete", color = NammaError)
                }
            },
            dismissButton = {
                TextButton(onClick = { playPendingDelete = null }) {
                    Text("Cancel", color = NammaGold)
                }
            },
            containerColor = NammaSurfaceHigh
        )
    }

    Scaffold(
        containerColor = NammaDarkBrown,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        when {
                            showForm && editingPlayId == null -> "NEW PLAY"
                            showForm -> "EDIT PLAY"
                            else -> "MANAGE PLAYS"
                        },
                        color = NammaGold,
                        style = MaterialTheme.typography.labelLarge.copy(letterSpacing = 2.sp)
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            if (showForm) {
                                showForm = false
                                resetFormForNew()
                            } else {
                                onNavigateBack()
                            }
                        }
                    ) {
                        Icon(Icons.Default.ArrowBack, null, tint = NammaGold)
                    }
                },
                actions = {
                    if (showForm) {
                        IconButton(onClick = {
                            viewModel.commitPlay(
                                editingPlayId = editingPlayId,
                                title = title,
                                description = description,
                                genre = genre,
                                duration = duration,
                                showTime = showTimesJoined(),
                                price = price,
                                setAsTonight = setAsTonight,
                                openCastScreenAfter = false
                            )
                        }) {
                            Icon(Icons.Default.Save, contentDescription = "Save", tint = NammaGold)
                        }
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.Transparent)
            )
        },
        floatingActionButton = {
            if (!showForm) {
                ExtendedFloatingActionButton(
                    onClick = {
                        resetFormForNew()
                        showForm = true
                    },
                    containerColor = NammaGold,
                    contentColor = NammaDarkBrown
                ) {
                    Icon(Icons.Default.Add, null)
                    Spacer(Modifier.width(8.dp))
                    Text("New play")
                }
            }
        }
    ) { padding ->
        if (!showForm) {
            if (allPlays.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize().padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("No plays yet.", color = NammaWarmWhite.copy(0.6f))
                        Spacer(Modifier.height(16.dp))
                        TextButton(onClick = {
                            resetFormForNew()
                            showForm = true
                        }) {
                            Text("Create your first play", color = NammaGold)
                        }
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(padding),
                    contentPadding = PaddingValues(24.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(allPlays, key = { it.id }) { play ->
                        PlayListRow(
                            play = play,
                            onEdit = { openEdit(play) },
                            onDelete = { playPendingDelete = play },
                            onSetTonight = { viewModel.setPlayAsTonightsShow(play.id) },
                            onManageCast = { onNavigateToManageCast(play.id) }
                        )
                    }
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 24.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Surface(
                    onClick = { posterLauncher.launch("image/*") },
                    color = NammaSurfaceLow,
                    shape = RoundedCornerShape(24.dp),
                    modifier = Modifier.fillMaxWidth().height(260.dp),
                    border = BorderStroke(1.dp, NammaWarmWhite.copy(0.1f))
                ) {
                    when {
                        selectedImageUri != null -> {
                            GlideImage(
                                model = selectedImageUri,
                                contentDescription = "Poster Preview",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }
                        !existingPosterUrl.isNullOrBlank() -> {
                            val model: Any = when {
                                existingPosterUrl!!.startsWith("content:") || existingPosterUrl!!.startsWith("file:") ->
                                    Uri.parse(existingPosterUrl!!)
                                existingPosterUrl!!.startsWith("/") -> File(existingPosterUrl!!)
                                else -> existingPosterUrl!!
                            }
                            GlideImage(
                                model = model,
                                contentDescription = "Poster",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }
                        else -> {
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
                Text("SHOW TIMES", color = NammaWarmWhite.copy(0.5f), style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold))
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = newTimeInput,
                        onValueChange = { newTimeInput = it },
                        label = { Text("Add a time", color = NammaWarmWhite.copy(0.5f)) },
                        placeholder = { Text(stringResource(R.string.show_times_hint), color = NammaWarmWhite.copy(0.2f)) },
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = NammaGold,
                            unfocusedBorderColor = NammaWarmWhite.copy(0.1f),
                            focusedContainerColor = NammaSurfaceLow,
                            unfocusedContainerColor = NammaSurfaceLow,
                            focusedTextColor = NammaWarmWhite,
                            unfocusedTextColor = NammaWarmWhite
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )
                    Button(
                        onClick = {
                            val t = newTimeInput.trim()
                            if (t.isNotEmpty() && !showTimeSlots.contains(t)) {
                                showTimeSlots = showTimeSlots + t
                                newTimeInput = ""
                            }
                        }
                    ) {
                        Text("Add")
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
                if (showTimeSlots.isEmpty()) {
                    Text(
                        "No show times yet. Add one or more (for example 6:30 PM, 9:00 PM).",
                        color = NammaWarmWhite.copy(0.4f),
                        style = MaterialTheme.typography.bodySmall
                    )
                } else {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(showTimeSlots, key = { it }) { slot ->
                            InputChip(
                                selected = true,
                                onClick = { showTimeSlots = showTimeSlots.filter { it != slot } },
                                label = { Text(slot, maxLines = 1) },
                                trailingIcon = {
                                    Icon(Icons.Default.Close, contentDescription = "Remove", tint = NammaGold, modifier = Modifier.size(18.dp))
                                },
                                colors = InputChipDefaults.inputChipColors(
                                    selectedContainerColor = NammaSurfaceLow,
                                    selectedLabelColor = NammaWarmWhite
                                )
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Tonight’s show", color = NammaWarmWhite, style = MaterialTheme.typography.bodyMedium)
                        Text(
                            "Only one play can be active for booking.",
                            color = NammaWarmWhite.copy(0.45f),
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                    Switch(
                        checked = setAsTonight,
                        onCheckedChange = { setAsTonight = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = NammaGold,
                            checkedTrackColor = NammaMaroon
                        )
                    )
                }

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

                Spacer(modifier = Modifier.height(24.dp))

                NammaMelaButton(
                    text = "Save play",
                    onClick = {
                        viewModel.commitPlay(
                            editingPlayId = editingPlayId,
                            title = title,
                            description = description,
                            genre = genre,
                            duration = duration,
                            showTime = showTimesJoined(),
                            price = price,
                            setAsTonight = setAsTonight,
                            openCastScreenAfter = false
                        )
                    }
                )
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedButton(
                    onClick = {
                        viewModel.commitPlay(
                            editingPlayId = editingPlayId,
                            title = title,
                            description = description,
                            genre = genre,
                            duration = duration,
                            showTime = showTimesJoined(),
                            price = price,
                            setAsTonight = setAsTonight,
                            openCastScreenAfter = true
                        )
                    },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    border = BorderStroke(1.dp, NammaGold.copy(0.5f)),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = NammaGold)
                ) {
                    Icon(Icons.Default.Groups, null, modifier = Modifier.size(20.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("Save & assign cast")
                }

                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }
}

@Composable
private fun PlayListRow(
    play: Play,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onSetTonight: () -> Unit,
    onManageCast: () -> Unit
) {
    Surface(
        color = NammaSurfaceLow,
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth(),
        border = BorderStroke(1.dp, NammaWarmWhite.copy(0.06f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(play.title, color = NammaWarmWhite, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Text(
                        play.showTime.ifBlank { "No times" },
                        color = NammaWarmWhite.copy(0.45f),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                if (play.isActive) {
                    Surface(color = NammaGold.copy(0.15f), shape = RoundedCornerShape(4.dp)) {
                        Text(
                            "TONIGHT",
                            color = NammaGold,
                            style = MaterialTheme.typography.labelSmall,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }
            Spacer(Modifier.height(12.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(onClick = onEdit) {
                    Icon(Icons.Default.Edit, "Edit", tint = NammaGold)
                }
                IconButton(onClick = onSetTonight) {
                    Icon(Icons.Default.Star, "Set as tonight", tint = NammaGold)
                }
                IconButton(onClick = onManageCast) {
                    Icon(Icons.Default.Groups, "Cast", tint = NammaGold)
                }
                Spacer(Modifier.weight(1f))
                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, "Delete", tint = NammaError.copy(0.85f))
                }
            }
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
