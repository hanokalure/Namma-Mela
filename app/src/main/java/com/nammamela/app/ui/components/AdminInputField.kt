package com.nammamela.app.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.nammamela.app.ui.theme.*

@Composable
fun AdminInputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    icon: ImageVector,
    isPassword: Boolean = false
) {
    var passwordVisible by remember { mutableStateOf(false) }
    
    Column {
        Text(label, color = NammaWarmWhite.copy(0.4f), style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold))
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = { Icon(icon, null, tint = NammaGold.copy(0.4f)) },
            trailingIcon = if (isPassword) {
                {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            if (passwordVisible) androidx.compose.material.icons.Icons.Default.VisibilityOff else androidx.compose.material.icons.Icons.Default.Visibility,
                            null,
                            tint = NammaGold.copy(0.4f)
                        )
                    }
                }
            } else null,
            visualTransformation = if (isPassword && !passwordVisible) PasswordVisualTransformation() else VisualTransformation.None,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = NammaGold,
                unfocusedBorderColor = NammaWarmWhite.copy(0.1f),
                focusedContainerColor = NammaSurfaceLow,
                unfocusedContainerColor = NammaSurfaceLow,
                focusedTextColor = NammaWarmWhite,
                unfocusedTextColor = NammaWarmWhite
            ),
            shape = RoundedCornerShape(12.dp),
            singleLine = true
        )
    }
}
