package com.nammamela.app.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.nammamela.app.domain.model.Actor
import com.nammamela.app.ui.theme.*
import com.nammamela.app.viewmodel.CastFilter
import com.nammamela.app.viewmodel.CastViewModel

@Composable
fun CastScreen(viewModel: CastViewModel = hiltViewModel()) {
    val filteredActors by viewModel.filteredActors.collectAsState()
    val filter by viewModel.filter.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(NammaDarkBrown)
    ) {
        // Header Banner
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(NammaMaroon.copy(alpha = 0.8f), NammaDarkBrown)
                    )
                )
                .padding(24.dp)
        ) {
            Column(modifier = Modifier.align(Alignment.BottomStart)) {
                Surface(
                    color = NammaMaroon,
                    shape = RoundedCornerShape(50),
                    modifier = Modifier.padding(bottom = 12.dp)
                ) {
                    Text(
                        text = "FEATURED CAST",
                        color = Color.White,
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                    )
                }
                Text(
                    text = "Meet the Stars of\nthe Night",
                    color = NammaGold,
                    style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold, lineHeight = 38.sp)
                )
                Text(
                    text = "Discover the talented cast behind Namma-Mela's biggest performance yet.",
                    color = Color.White.copy(alpha = 0.6f),
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Filter Chips
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            CastFilterChip("All Cast", filter == CastFilter.ALL) { viewModel.setFilter(CastFilter.ALL) }
            CastFilterChip("Heroes", filter == CastFilter.HEROES) { viewModel.setFilter(CastFilter.HEROES) }
            CastFilterChip("Comedians", filter == CastFilter.COMEDIANS) { viewModel.setFilter(CastFilter.COMEDIANS) }
        }

        // Cast Grid
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(start = 24.dp, end = 24.dp, bottom = 32.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.weight(1f)
        ) {
            items(filteredActors) { actor ->
                ActorCard(actor = actor)
            }
        }
    }
}

@Composable
fun CastFilterChip(label: String, isSelected: Boolean, onClick: () -> Unit) {
    val bg = if (isSelected) NammaGold else NammaSurface
    val textColor = if (isSelected) NammaDarkBrown else Color.White.copy(0.7f)
    val border = if (isSelected) 0.dp else 1.dp
    
    Surface(
        onClick = onClick,
        color = bg,
        shape = RoundedCornerShape(50),
        border = if (border > 0.dp) BorderStroke(1.dp, Color.White.copy(0.1f)) else null
    ) {
        Text(
            text = label, 
            color = textColor, 
            fontWeight = FontWeight.Bold, 
            fontSize = 13.sp,
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp)
        )
    }
}

@Composable
fun ActorCard(actor: Actor) {
    val badgeColor = when (actor.category.uppercase()) {
        "HERO" -> NammaMaroon
        "HEROINE" -> Color(0xFFAD1457)
        "COMEDIAN" -> Color(0xFF2E7D32)
        "VILLAIN" -> Color(0xFF1A237E)
        else -> NammaMaroon
    }

    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(NammaSurface)
            .border(1.dp, Color.White.copy(0.05f), RoundedCornerShape(20.dp))
    ) {
        Box {
            // Actor image area
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .background(Color(0xFF2C1F1F))
            ) {
                // Spotlight overlay
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.radialGradient(
                                colors = listOf(Color.White.copy(0.05f), Color.Transparent),
                                radius = 300f
                            )
                        )
                )
            }

            // Role badge
            Surface(
                color = badgeColor,
                shape = RoundedCornerShape(bottomEnd = 12.dp),
                modifier = Modifier.align(Alignment.TopStart)
            ) {
                Text(
                    text = actor.category.uppercase(),
                    color = Color.White,
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, fontSize = 9.sp),
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                )
            }
            
            // Rating badge
            Surface(
                color = Color.Black.copy(0.6f),
                shape = RoundedCornerShape(50),
                modifier = Modifier.align(Alignment.BottomEnd).padding(8.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)) {
                    Icon(Icons.Default.Star, null, tint = NammaGold, modifier = Modifier.size(10.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("4.8", color = Color.White, style = MaterialTheme.typography.labelSmall)
                }
            }
        }

        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = actor.name,
                color = Color.White,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = actor.role.uppercase(),
                color = NammaGold,
                style = MaterialTheme.typography.labelSmall.copy(letterSpacing = 1.sp, fontWeight = FontWeight.Medium)
            )
        }
    }
}
