package com.nammamela.app.ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.nammamela.app.domain.model.Actor
import com.nammamela.app.domain.model.Play
import com.nammamela.app.ui.components.NammaMelaButton
import com.nammamela.app.ui.theme.*
import com.nammamela.app.viewmodel.PlayDetailViewModel

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun PlayDetailScreen(
    viewModel: PlayDetailViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
    onBookClick: () -> Unit,
    onReviewClick: () -> Unit
) {
    val play by viewModel.play.collectAsState()
    val cast by viewModel.cast.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    val scrollState = rememberScrollState()

    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize().background(NammaDarkBrown), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = NammaGold)
        }
    } else if (play == null) {
        Box(modifier = Modifier.fillMaxSize().background(NammaDarkBrown), contentAlignment = Alignment.Center) {
            Text("Drama details not found.", color = NammaWarmWhite)
        }
    } else {
        val playData = play!!
        Scaffold(
            containerColor = NammaDarkBrown,
            bottomBar = {
                Surface(
                    color = NammaSurfaceLow,
                    modifier = Modifier.fillMaxWidth(),
                    tonalElevation = 16.dp,
                    border = BorderStroke(1.dp, Color.White.copy(0.05f))
                ) {
                    Row(
                        modifier = Modifier.padding(24.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        OutlinedButton(
                            onClick = onReviewClick,
                            modifier = Modifier.weight(1f).height(56.dp),
                            shape = RoundedCornerShape(16.dp),
                            border = BorderStroke(1.dp, NammaGold.copy(0.3f))
                        ) {
                            Text("Reviews", color = NammaGold)
                        }
                        NammaMelaButton(
                            text = "Reserve Seat 🎟",
                            onClick = onBookClick,
                            modifier = Modifier.weight(2f)
                        )
                    }
                }
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(scrollState)
            ) {
                // Hero Section with Poster (Glide)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(450.dp)
                ) {
                    if (playData.posterUrl != null) {
                        GlideImage(
                            model = playData.posterUrl,
                            contentDescription = playData.title,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Box(modifier = Modifier.fillMaxSize().background(NammaMaroon))
                    }
                    
                    // Gradient Overlay
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(Color.Transparent, NammaDarkBrown),
                                    startY = 600f
                                )
                            )
                    )

                    // Back and Share Buttons
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Surface(
                            onClick = onNavigateBack,
                            color = Color.Black.copy(0.3f),
                            shape = CircleShape,
                            modifier = Modifier.size(44.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.ArrowBack, null, tint = Color.White)
                            }
                        }
                        Surface(
                            onClick = {},
                            color = Color.Black.copy(0.3f),
                            shape = CircleShape,
                            modifier = Modifier.size(44.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.Share, null, tint = Color.White)
                            }
                        }
                    }
                }

                // Info Section
                Column(modifier = Modifier.padding(horizontal = 24.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            color = NammaMaroon.copy(0.2f),
                            shape = RoundedCornerShape(4.dp),
                            border = BorderStroke(1.dp, NammaMaroon.copy(0.5f))
                        ) {
                            Text(
                                text = playData.genre.uppercase(),
                                color = NammaGold,
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Icon(Icons.Default.Star, null, tint = NammaGold, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "${playData.rating}", color = Color.White, style = MaterialTheme.typography.labelLarge)
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = playData.title,
                        color = NammaGold,
                        style = MaterialTheme.typography.displaySmall.copy(fontWeight = FontWeight.ExtraBold, fontSize = 34.sp)
                    )
                    Text(
                        text = "${playData.duration} | Digital Poster HD",
                        color = NammaWarmWhite.copy(0.5f),
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        text = "SYNOPSIS",
                        color = NammaGold,
                        style = MaterialTheme.typography.labelSmall.copy(letterSpacing = 2.sp, fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = playData.description,
                        color = NammaWarmWhite.copy(0.7f),
                        style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 24.sp)
                    )

                    Spacer(modifier = Modifier.height(32.dp))
                    Text(
                        text = "TONIGHT'S TROUPE",
                        color = NammaGold,
                        style = MaterialTheme.typography.labelSmall.copy(letterSpacing = 2.sp, fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    if (cast.isEmpty()) {
                        Text("Cast list arriving soon...", color = NammaWarmWhite.copy(0.3f), style = MaterialTheme.typography.bodySmall)
                    } else {
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                            items(cast) { actor ->
                                CastAvatar(actor = actor)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(60.dp))
                }
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun CastAvatar(actor: Actor) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(72.dp)
                .clip(CircleShape)
                .background(NammaSurfaceLow)
                .border(1.dp, NammaGold.copy(0.2f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            GlideImage(
                model = actor.imageUrl,
                contentDescription = actor.name,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = actor.name, color = Color.White, style = MaterialTheme.typography.labelMedium)
        Text(text = actor.category, color = NammaGold.copy(0.5f), style = MaterialTheme.typography.labelSmall)
    }
}
