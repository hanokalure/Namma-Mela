package com.nammamela.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.nammamela.app.domain.model.Actor
import com.nammamela.app.ui.theme.*

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ActorDetailsDialog(actor: Actor, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = NammaSurfaceLow,
        title = {
            Text(actor.name, color = NammaGold, style = MaterialTheme.typography.headlineSmall)
        },
        text = {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(NammaDarkBrown)
                        .border(1.dp, NammaGold.copy(0.3f), RoundedCornerShape(16.dp))
                ) {
                    GlideImage(
                        model = actor.imageUrl,
                        contentDescription = actor.name,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = actor.category.uppercase(),
                    color = NammaGold,
                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = "Role: ${actor.role}",
                    color = NammaWarmWhite.copy(0.6f),
                    style = MaterialTheme.typography.bodySmall
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "A dedicated performer in the Namma-Mela troupe, bringing years of stage experience to tonight's play. Known for versatility and powerful screen presence.",
                    color = NammaWarmWhite.copy(0.7f),
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("CLOSE", color = NammaGold)
            }
        }
    )
}
