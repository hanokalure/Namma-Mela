package com.nammamela.app.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nammamela.app.ui.theme.*
import kotlinx.coroutines.delay
import kotlin.math.min

@Composable
fun SplashScreen(onAnimationFinished: () -> Unit) {
    var isCurtainOpen by remember { mutableStateOf(false) }
    
    val curtainOffset by animateFloatAsState(
        targetValue = if (isCurtainOpen) 1f else 0f,
        animationSpec = tween(durationMillis = 2000, easing = FastOutSlowInEasing),
        label = "CurtainAnimation"
    )

    LaunchedEffect(Unit) {
        delay(800)
        isCurtainOpen = true
        delay(2200)
        onAnimationFinished()
    }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(NammaDarkBrown)
    ) {
        // App Name behind curtains (Highly Responsive Scaling)
        // Adjusting calculation to be more conservative for responsiveness
        val titleFontSize = min(maxWidth.value / 8.5f, 42f).sp 
        val subtitleFontSize = min(maxWidth.value / 28f, 14f).sp
        val letterSpacing = (maxWidth.value / 45).sp

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .align(Alignment.Center) // Center the text behind curtains
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
        ) {
            Text(
                text = "NAMMA-MELA", 
                color = NammaGold, 
                style = MaterialTheme.typography.displayMedium.copy(
                    fontWeight = FontWeight.ExtraBold, 
                    letterSpacing = letterSpacing,
                    fontSize = titleFontSize
                ),
                textAlign = TextAlign.Center,
                maxLines = 1, // Ensure single line
                softWrap = false // Prevent wrapping
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "NATIONAL PRIDE THEATER", 
                color = NammaWarmWhite.copy(0.6f), 
                style = MaterialTheme.typography.labelLarge.copy(
                    letterSpacing = (letterSpacing.value / 2).sp,
                    fontSize = subtitleFontSize
                ),
                textAlign = TextAlign.Center,
                maxLines = 1
            )
        }

        // --- CURTAIN LAYERS (Meeting in the exact center) ---
        val halfWidthPx = (constraints.maxWidth / 2)
        
        // Left Curtain (Starts from 0 and slides LEFT)
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(0.51f) // Slight overlap for gapless center
                .align(Alignment.TopStart)
                .offset { IntOffset((-halfWidthPx * curtainOffset).toInt(), 0) }
                .background(NammaMaroon)
                .border(1.dp, NammaGold.copy(0.1f))
        )
        
        // Right Curtain (Starts from center and slides RIGHT)
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(0.51f)
                .align(Alignment.TopEnd)
                .offset { IntOffset((halfWidthPx * curtainOffset).toInt(), 0) }
                .background(NammaMaroon)
                .border(1.dp, NammaGold.copy(0.1f))
        )
    }
}
