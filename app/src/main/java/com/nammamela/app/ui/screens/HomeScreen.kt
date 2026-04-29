package com.nammamela.app.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
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
import com.nammamela.app.domain.model.Play
import com.nammamela.app.ui.components.NammaMelaButton
import com.nammamela.app.ui.theme.*
import com.nammamela.app.viewmodel.HomeViewModel

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onNavigateToSeatBooking: (Int) -> Unit,
    onNavigateToPlayDetail: (Int) -> Unit,
    onNavigateToSearch: () -> Unit,
    onNavigateToNotifications: () -> Unit
) {
    val plays by viewModel.plays.collectAsState()
    val scrollState = rememberScrollState()

    Scaffold(
        containerColor = NammaDarkBrown
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(scrollState)
                .windowInsetsPadding(WindowInsets.statusBars)
        ) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Namma-Mela", color = NammaGold, style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold, letterSpacing = 2.sp))
                    Text("The Stage Awaits You", color = NammaWarmWhite.copy(0.5f), style = MaterialTheme.typography.bodySmall)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onNavigateToNotifications) {
                        Icon(Icons.Default.Notifications, null, tint = NammaGold)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(NammaSurfaceLow)
                            .border(1.dp, NammaGold.copy(0.3f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("B", color = NammaGold, fontWeight = FontWeight.Bold)
                    }
                }
            }

            // Search Bar
            Surface(
                onClick = onNavigateToSearch,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                color = NammaSurfaceLow,
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, NammaWarmWhite.copy(0.05f))
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Search, null, tint = NammaGold.copy(0.4f))
                    Spacer(modifier = Modifier.width(12.dp))
                    Text("Search drama, music or comedy...", color = NammaWarmWhite.copy(0.3f), style = MaterialTheme.typography.bodyMedium)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Featured Play Slider
            if (plays.isNotEmpty()) {
                BannerSlider(
                    plays = plays,
                    onBannerClick = onNavigateToPlayDetail,
                    onBookClick = onNavigateToSeatBooking
                )
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Categories
            LazyRow(
                contentPadding = PaddingValues(horizontal = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(listOf("Drama", "Music", "Comedy", "Folk", "Classical")) { category ->
                    CategoryChip(text = category, isSelected = category == "Drama")
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Fan Favorites
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "FAN FAVORITES",
                    color = NammaWarmWhite,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, letterSpacing = 2.sp)
                )
                Text("View All", color = NammaGold, style = MaterialTheme.typography.labelSmall)
            }
            Spacer(modifier = Modifier.height(20.dp))
            LazyRow(
                contentPadding = PaddingValues(horizontal = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                items(listOf("Arjun", "Meera", "Vikram", "Kiran", "Basu")) { name ->
                    FanFavoriteAvatar(name = name)
                }
            }
            
            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BannerSlider(
    plays: List<Play>,
    onBannerClick: (Int) -> Unit,
    onBookClick: (Int) -> Unit
) {
    // Correct API for newer Compose: pageCount is in rememberPagerState
    val pagerState = rememberPagerState(initialPage = 0) { plays.size }
    
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 24.dp),
            pageSpacing = 16.dp
        ) { page ->
            val play = plays[page]
            FeaturedPlayBanner(
                play = play,
                onBannerClick = { onBannerClick(play.id) },
                onBookClick = { onBookClick(play.id) }
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Row(
            modifier = Modifier.height(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            repeat(plays.size) { iteration ->
                val color = if (pagerState.currentPage == iteration) NammaGold else NammaWarmWhite.copy(0.2f)
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(color)
                )
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun FeaturedPlayBanner(play: Play, onBannerClick: () -> Unit, onBookClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(440.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(NammaSurfaceLow)
            .border(1.dp, NammaWarmWhite.copy(0.05f), RoundedCornerShape(24.dp))
            .clickable(onClick = onBannerClick)
    ) {
        // GLIDE IMAGE FOR POSTER
        if (play.posterUrl != null) {
            GlideImage(
                model = play.posterUrl,
                contentDescription = play.title,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        } else {
            // Fallback colorful background if no poster
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.linearGradient(
                            colors = listOf(NammaMaroon, NammaDarkBrown)
                        )
                    )
            )
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Transparent, NammaDarkBrown.copy(0.95f)),
                        startY = 600f
                    )
                )
        )
        
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(24.dp)
        ) {
            Surface(
                color = NammaMaroon,
                shape = RoundedCornerShape(50),
                modifier = Modifier.padding(bottom = 12.dp)
            ) {
                Text(
                    text = "TOP RATED",
                    color = Color.White,
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                )
            }
            Text(
                text = play.title,
                color = NammaGold,
                style = MaterialTheme.typography.displayLarge.copy(fontSize = 32.sp, fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = play.duration, color = NammaWarmWhite.copy(0.6f), style = MaterialTheme.typography.bodySmall)
                Spacer(modifier = Modifier.width(16.dp))
                Text(text = "★ ${play.rating}", color = NammaGold, style = MaterialTheme.typography.labelLarge)
                Spacer(modifier = Modifier.width(16.dp))
                Text(text = play.genre, color = NammaWarmWhite.copy(0.6f), style = MaterialTheme.typography.bodySmall)
            }
            Spacer(modifier = Modifier.height(24.dp))
            NammaMelaButton(
                text = "BOOK SEAT 🎟",
                onClick = onBookClick
            )
        }
    }
}

@Composable
fun CategoryChip(text: String, isSelected: Boolean) {
    val backgroundColor = if (isSelected) NammaGold else NammaSurfaceLow
    val textColor = if (isSelected) NammaDarkBrown else NammaWarmWhite.copy(alpha = 0.3f)
    val border = if (isSelected) 1.dp to NammaGold else 1.dp to NammaWarmWhite.copy(0.05f)
    
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(backgroundColor)
            .border(border.first, border.second, RoundedCornerShape(12.dp))
            .clickable { }
            .padding(horizontal = 24.dp, vertical = 14.dp)
    ) {
        Text(text = text, color = textColor, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium)
    }
}

@Composable
fun FanFavoriteAvatar(name: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(70.dp)
                .clip(CircleShape)
                .background(NammaSurfaceLow)
                .border(2.dp, NammaGold.copy(0.3f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(Color.Gray)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = name, color = NammaWarmWhite.copy(0.6f), style = MaterialTheme.typography.labelLarge)
    }
}
