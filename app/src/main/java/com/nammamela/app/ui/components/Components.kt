package com.nammamela.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nammamela.app.ui.theme.*

@Composable
fun NammaMelaButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    // Stage Curtain Gradient (Deep Maroon to Darker Maroon)
    val gradient = Brush.verticalGradient(
        colors = listOf(NammaMaroon, NammaDeepMaroon)
    )
    
    val backgroundModifier = if (enabled) {
        Modifier
            .background(gradient)
            .border(1.dp, NammaGold.copy(0.3f), RoundedCornerShape(12.dp)) // Embossed Gold border
    } else {
        Modifier.background(Color.Gray.copy(0.2f))
    }
    
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .shadow(
                elevation = if (enabled) 8.dp else 0.dp,
                shape = RoundedCornerShape(12.dp),
                spotColor = Color.Black
            )
            .clip(RoundedCornerShape(12.dp))
            .then(backgroundModifier)
            .clickable(enabled = enabled, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = if (enabled) NammaGold else Color.Gray,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 1.5.sp
            )
        )
    }
}

@Composable
fun NammaMelaTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    visualTransformation: androidx.compose.ui.text.input.VisualTransformation = androidx.compose.ui.text.input.VisualTransformation.None,
    keyboardOptions: androidx.compose.foundation.text.KeyboardOptions = androidx.compose.foundation.text.KeyboardOptions.Default
) {
    Column(modifier = modifier) {
        Text(
            text = label,
            color = NammaGold,
            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(placeholder, color = NammaWarmWhite.copy(0.2f)) },
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            visualTransformation = visualTransformation,
            keyboardOptions = keyboardOptions,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = NammaGold,
                unfocusedBorderColor = NammaWarmWhite.copy(0.1f),
                focusedContainerColor = NammaSurfaceLow,
                unfocusedContainerColor = NammaSurfaceLow,
                focusedTextColor = NammaWarmWhite,
                unfocusedTextColor = NammaWarmWhite
            )
        )
    }
}

private fun Modifier.clickable(enabled: Boolean, onClick: () -> Unit): Modifier {
    return this.then(
        if (enabled) Modifier.clickable(onClick = onClick) else Modifier
    )
}
