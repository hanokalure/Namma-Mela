package com.nammamela.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nammamela.app.ui.components.NammaMelaButton
import com.nammamela.app.ui.theme.*

@Composable
fun SignUpScreen(
    onSignUpClick: () -> Unit,
    onLoginClick: () -> Unit,
    onClose: () -> Unit
) {
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(NammaDarkBrown)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Text("🎪", fontSize = 24.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("NAMMA-MELA", color = NammaGold, fontWeight = FontWeight.Black, style = MaterialTheme.typography.titleLarge, letterSpacing = 2.sp)
                }
                IconButton(onClick = onClose) {
                    Icon(Icons.Default.Close, null, tint = NammaWarmWhite.copy(0.7f))
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                "Join the\nSpectacle",
                color = NammaWarmWhite,
                style = MaterialTheme.typography.displayLarge.copy(fontSize = 40.sp, fontWeight = FontWeight.Bold),
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(40.dp))

            Surface(
                color = NammaSurfaceLow,
                shape = RoundedCornerShape(32.dp),
                modifier = Modifier.fillMaxWidth(),
                border = androidx.compose.foundation.BorderStroke(1.dp, NammaWarmWhite.copy(0.05f))
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    SignUpFieldRow(
                        label = "Full Name",
                        value = fullName,
                        onValueChange = { fullName = it },
                        placeholder = "E.g. Basavaraj Patil"
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    SignUpFieldRow(
                        label = "Email Address",
                        value = email,
                        onValueChange = { email = it },
                        placeholder = "name@example.com",
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    SignUpFieldRow(
                        label = "Password",
                        value = password,
                        onValueChange = { password = it },
                        placeholder = "••••••••",
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    NammaMelaButton(text = "CREATE ACCOUNT", onClick = onSignUpClick)

                    Spacer(modifier = Modifier.height(20.dp))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                        Text("Already have an account? ", color = NammaWarmWhite.copy(0.5f))
                        Text(
                            "Login",
                            color = NammaGold,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.clickable { onLoginClick() }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
fun SignUpFieldRow(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default
) {
    Text(label, color = NammaGold, style = MaterialTheme.typography.labelLarge)
    Spacer(modifier = Modifier.height(8.dp))
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(placeholder, color = NammaWarmWhite.copy(0.35f)) },
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        modifier = Modifier.fillMaxWidth(),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = NammaGold,
            unfocusedBorderColor = NammaWarmWhite.copy(0.1f),
            focusedTextColor = NammaWarmWhite,
            unfocusedTextColor = NammaWarmWhite,
            focusedLabelColor = NammaGold
        ),
        shape = RoundedCornerShape(8.dp)
    )
}
