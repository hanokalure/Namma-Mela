package com.nammamela.app.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.nammamela.app.ui.components.NammaMelaButton
import com.nammamela.app.ui.theme.*
import com.nammamela.app.viewmodel.AuthViewModel

@Composable
fun LoginScreen(
    onLoginClick: () -> Unit,
    onSignUpClick: () -> Unit,
    onAdminLoginClick: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.errorEvent.collect { message ->
            snackbarHostState.showSnackbar(message)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.loginSuccess.collect { success ->
            if (success) onLoginClick()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = NammaDarkBrown
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Theatrical Gradient Overlay
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(NammaDeepMaroon.copy(0.3f), NammaDarkBrown)
                        )
                    )
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(80.dp))

                // Theatrical Logo Area
                Surface(
                    modifier = Modifier.size(100.dp),
                    color = NammaGold.copy(0.1f),
                    shape = RoundedCornerShape(24.dp),
                    border = BorderStroke(1.dp, NammaGold.copy(0.3f))
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text("🎪", fontSize = 48.sp)
                    }
                }
                
                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "NAMMA-MELA",
                    color = NammaGold,
                    style = MaterialTheme.typography.displayLarge.copy(fontSize = 38.sp, fontWeight = FontWeight.ExtraBold, letterSpacing = 4.sp)
                )
                Text(
                    text = "NATIONAL PRIDE THEATER",
                    color = NammaGold.copy(0.5f),
                    style = MaterialTheme.typography.labelMedium.copy(letterSpacing = 2.sp)
                )

                Spacer(modifier = Modifier.height(48.dp))

                // Login Card
                Surface(
                    color = NammaSurfaceLow,
                    shape = RoundedCornerShape(32.dp),
                    modifier = Modifier.fillMaxWidth(),
                    border = BorderStroke(1.dp, NammaWarmWhite.copy(0.05f))
                ) {
                    Column(modifier = Modifier.padding(32.dp)) {
                        Text("The Stage Awaits", color = NammaWarmWhite, style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold))
                        Text("Enter your credentials to continue", color = NammaWarmWhite.copy(0.5f), style = MaterialTheme.typography.bodyMedium)

                        Spacer(modifier = Modifier.height(32.dp))

                        OutlinedTextField(
                            value = email,
                            onValueChange = { email = it },
                            label = { Text("Email") },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = NammaGold,
                                unfocusedBorderColor = NammaWarmWhite.copy(0.1f),
                                focusedTextColor = NammaWarmWhite,
                                unfocusedTextColor = NammaWarmWhite,
                                focusedLabelColor = NammaGold
                            )
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        OutlinedTextField(
                            value = password,
                            onValueChange = { password = it },
                            label = { Text("Password") },
                            modifier = Modifier.fillMaxWidth(),
                            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = NammaGold,
                                unfocusedBorderColor = NammaWarmWhite.copy(0.1f),
                                focusedTextColor = NammaWarmWhite,
                                unfocusedTextColor = NammaWarmWhite,
                                focusedLabelColor = NammaGold
                            ),
                            trailingIcon = {
                                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                    Icon(
                                        if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                        null,
                                        tint = NammaGold.copy(0.4f)
                                    )
                                }
                            }
                        )

                        Spacer(modifier = Modifier.height(32.dp))

                        NammaMelaButton(
                            text = "SIGN IN", 
                            onClick = { viewModel.login(email, password) }
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text("New to the troupe? ", color = NammaWarmWhite.copy(0.5f))
                            Text(
                                "Join Now",
                                color = NammaGold,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.clickable { onSignUpClick() }
                            )
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(32.dp))

                // Manager Entry Point
                Surface(
                    onClick = onAdminLoginClick,
                    color = Color.Transparent,
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.dp, NammaGold.copy(0.2f)),
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(Icons.Default.ManageAccounts, null, tint = NammaGold, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(12.dp))
                        Text("Theater Manager Login", color = NammaGold, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(60.dp))
            }
        }
    }
}
