package com.nammamela.app.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.EmojiEmotions
import androidx.compose.material.icons.outlined.Image
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
import com.nammamela.app.domain.model.Comment
import com.nammamela.app.ui.theme.*
import com.nammamela.app.viewmodel.FanWallViewModel
import java.util.concurrent.TimeUnit

@Composable
fun FanWallScreen(viewModel: FanWallViewModel = hiltViewModel()) {
    val comments by viewModel.comments.collectAsState()
    val inputText by viewModel.inputText.collectAsState()
    val onlineCount by viewModel.onlineCount.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.errorEvent.collect { message ->
            snackbarHostState.showSnackbar(message = message)
        }
    }

    Scaffold(
        containerColor = NammaDarkBrown,
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
        // Header Area with Heritage flair
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(NammaDeepMaroon, NammaDarkBrown)
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(24.dp)
            ) {
                Text("Fan Wall", color = NammaGold, style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold))
                Text(
                    "CONNECT WITH THE COMMUNITY",
                    color = NammaGold.copy(alpha = 0.5f),
                    style = MaterialTheme.typography.labelSmall.copy(letterSpacing = 2.sp)
                )
            }
            Surface(
                color = Color.Black.copy(alpha = 0.4f),
                shape = RoundedCornerShape(50),
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(20.dp)
            ) {
                Row(modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp), verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(Color.Green))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("$onlineCount Online", color = Color.White, style = MaterialTheme.typography.labelSmall)
                }
            }
        }

        // Post Input Box (Heritage Card)
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 8.dp),
            colors = CardDefaults.cardColors(containerColor = NammaSurfaceLow),
            shape = RoundedCornerShape(20.dp),
            border = BorderStroke(1.dp, NammaWarmWhite.copy(0.05f))
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(verticalAlignment = Alignment.Top) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(NammaMaroon)
                            .border(1.dp, NammaGold.copy(0.3f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("B", color = NammaGold, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Box(modifier = Modifier.weight(1f).padding(top = 8.dp)) {
                        if (inputText.isEmpty()) {
                            Text("Share your excitement...", color = NammaWarmWhite.copy(0.3f), style = MaterialTheme.typography.bodyMedium)
                        }
                        BasicTextField(
                            value = inputText,
                            onValueChange = viewModel::onInputChanged,
                            modifier = Modifier.fillMaxWidth(),
                            textStyle = MaterialTheme.typography.bodyMedium.copy(color = NammaWarmWhite)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Outlined.Image, null, tint = NammaWarmWhite.copy(0.4f), modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(20.dp))
                        Icon(Icons.Outlined.EmojiEmotions, null, tint = NammaWarmWhite.copy(0.4f), modifier = Modifier.size(20.dp))
                    }
                    Button(
                        onClick = viewModel::postComment,
                        colors = ButtonDefaults.buttonColors(containerColor = NammaMaroon),
                        shape = RoundedCornerShape(12.dp),
                        contentPadding = PaddingValues(horizontal = 24.dp)
                    ) {
                        Text("POST", color = NammaGold, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                    }
                }
            }
        }

        // Feed
        LazyColumn(
            contentPadding = PaddingValues(horizontal = 24.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.weight(1f)
        ) {
            items(comments, key = { it.id }) { comment ->
                CommentCard(
                    comment = comment,
                    onLike = { viewModel.likeComment(comment) },
                    onFire = { viewModel.fireComment(comment) }
                )
            }
        }
    }
}
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommentCard(
    comment: Comment,
    onLike: () -> Unit,
    onFire: () -> Unit
) {
    val timeAgo = remember(comment.timestamp) {
        val diff = System.currentTimeMillis() - comment.timestamp
        when {
            diff < TimeUnit.MINUTES.toMillis(1) -> "Just now"
            diff < TimeUnit.HOURS.toMillis(1) -> "${TimeUnit.MILLISECONDS.toMinutes(diff)}m ago"
            else -> "${TimeUnit.MILLISECONDS.toHours(diff)}h ago"
        }
    }

    Card(
        colors = CardDefaults.cardColors(containerColor = NammaSurfaceLow),
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier.fillMaxWidth(),
        border = BorderStroke(1.dp, NammaWarmWhite.copy(0.03f))
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(NammaSurfaceHigh),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        comment.username.first().toString(),
                        color = NammaGold,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        comment.userHandle,
                        color = NammaGold,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleSmall
                    )
                    Text(timeAgo, color = NammaWarmWhite.copy(0.4f), style = MaterialTheme.typography.labelSmall)
                }
                Icon(Icons.Default.MoreVert, null, tint = NammaWarmWhite.copy(0.3f))
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text(
                comment.content, 
                color = NammaWarmWhite.copy(0.9f), 
                style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 22.sp)
            )

            Spacer(modifier = Modifier.height(20.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                ReactionButton(emoji = "👏", count = comment.likes, onClick = onLike)
                Spacer(modifier = Modifier.width(12.dp))
                ReactionButton(emoji = "🔥", count = comment.fires, onClick = onFire, highlight = true)
                Spacer(modifier = Modifier.weight(1f))
                Text("💬 ${comment.replies}", color = NammaWarmWhite.copy(0.4f), style = MaterialTheme.typography.labelMedium)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReactionButton(emoji: String, count: Int, onClick: () -> Unit, highlight: Boolean = false) {
    val bg = if (highlight) NammaMaroon else NammaSurfaceHigh
    val border = if (highlight) NammaGold.copy(0.3f) else NammaWarmWhite.copy(0.05f)
    
    Surface(
        color = bg,
        shape = RoundedCornerShape(50),
        onClick = onClick,
        border = BorderStroke(1.dp, border)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(emoji, fontSize = 14.sp)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                "$count", 
                color = NammaWarmWhite, 
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
            )
        }
    }
}
