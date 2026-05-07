package com.nammamela.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.nammamela.app.ui.theme.NammaGold
import com.nammamela.app.ui.theme.NammaSurfaceLow
import java.io.File

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun UserAvatar(
    imageUrl: String?,
    displayName: String?,
    modifier: Modifier = Modifier,
    size: Dp = 40.dp,
    borderWidth: Dp = 1.dp
) {
    val initials = remember(displayName) {
        val parts = displayName?.trim()?.split("\\s+".toRegex())?.filter { it.isNotBlank() }.orEmpty()
        when {
            parts.size >= 2 -> "${parts[0].first()}${parts[1].first()}".uppercase()
            parts.size == 1 && parts[0].isNotEmpty() -> parts[0].take(2).uppercase()
            else -> "?"
        }
    }

    val borderMod = if (borderWidth > 0.dp) {
        Modifier.border(borderWidth, NammaGold.copy(0.3f), CircleShape)
    } else {
        Modifier
    }
    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(NammaSurfaceLow)
            .then(borderMod),
        contentAlignment = Alignment.Center
    ) {
        val path = imageUrl?.trim().orEmpty()
        if (path.isNotEmpty()) {
            val model: Any = when {
                path.startsWith("content:") || path.startsWith("file:") || path.startsWith("android.resource:") -> path
                path.startsWith("/") -> File(path)
                else -> File(path)
            }
            GlideImage(
                model = model,
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        } else {
            Text(
                text = initials,
                color = NammaGold,
                fontWeight = FontWeight.Bold,
                fontSize = (size.value / 2.2f).sp,
                maxLines = 1
            )
        }
    }
}

@Composable
fun UserAvatarIconSlot(
    imageUrl: String?,
    displayName: String?,
    modifier: Modifier = Modifier,
    size: Dp = 24.dp
) {
    UserAvatar(
        imageUrl = imageUrl,
        displayName = displayName,
        modifier = modifier,
        size = size
    )
}
