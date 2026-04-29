package com.nammamela.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.nammamela.app.domain.model.Play
import com.nammamela.app.ui.theme.*
import com.nammamela.app.viewmodel.SearchViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    viewModel: SearchViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
    onNavigateToPlayDetail: (Int) -> Unit
) {
    val searchQuery by viewModel.searchQuery.collectAsState()
    val searchResults by viewModel.searchResults.collectAsState()
    val isSearching by viewModel.isSearching.collectAsState()

    Scaffold(
        containerColor = NammaDarkBrown,
        topBar = {
            TopAppBar(
                title = {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 16.dp)
                            .height(44.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(NammaSurfaceLow)
                            .border(1.dp, NammaWarmWhite.copy(0.05f), RoundedCornerShape(12.dp)),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(horizontal = 12.dp)) {
                            Icon(Icons.Default.Search, null, tint = NammaGold.copy(0.4f), modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            BasicTextField(
                                value = searchQuery,
                                onValueChange = viewModel::onQueryChanged,
                                modifier = Modifier.fillMaxWidth(),
                                textStyle = MaterialTheme.typography.bodyMedium.copy(color = NammaWarmWhite),
                                singleLine = true
                            )
                            if (searchQuery.isEmpty()) {
                                Text("Search plays...", color = NammaWarmWhite.copy(0.2f), style = MaterialTheme.typography.bodyMedium)
                            }
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, null, tint = NammaGold)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize()) {
            if (isSearching) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth(), color = NammaGold, trackColor = Color.Transparent)
            }

            if (searchQuery.isEmpty()) {
                EmptySearchContent()
            } else if (searchResults.isEmpty() && !isSearching) {
                NoResultsContent(searchQuery)
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(searchResults) { play ->
                        SearchResultItem(play = play, onClick = { onNavigateToPlayDetail(play.id) })
                    }
                }
            }
        }
    }
}

@Composable
fun SearchResultItem(play: Play, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        color = NammaSurfaceLow,
        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, NammaWarmWhite.copy(0.05f))
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(NammaMaroon),
                contentAlignment = Alignment.Center
            ) {
                Text(play.title.first().toString(), color = NammaGold, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(play.title, color = NammaWarmWhite, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                Text(play.genre, color = NammaGold.copy(0.6f), style = MaterialTheme.typography.labelSmall)
                Text(play.duration, color = NammaWarmWhite.copy(0.3f), style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

@Composable
fun EmptySearchContent() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(Icons.Default.Search, null, tint = NammaGold.copy(0.1f), modifier = Modifier.size(80.dp))
        Spacer(modifier = Modifier.height(16.dp))
        Text("Search for your favorite plays", color = NammaWarmWhite.copy(0.2f))
    }
}

@Composable
fun NoResultsContent(query: String) {
    Column(
        modifier = Modifier.fillMaxSize().padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("No results for \"$query\"", color = NammaGold, style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))
        Text("Try searching for something else", color = NammaWarmWhite.copy(0.4f), textAlign = androidx.compose.ui.text.style.TextAlign.Center)
    }
}
