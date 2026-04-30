package com.nammamela.app.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.nammamela.app.ui.components.NammaMelaButton
import com.nammamela.app.ui.components.AdminInputField
import com.nammamela.app.ui.theme.*
import com.nammamela.app.viewmodel.AuthViewModel

import androidx.compose.ui.platform.LocalContext
import com.nammamela.app.util.findActivity

@Composable
fun AdminLoginScreen(
    onLoginClick: () -> Unit,
    onSignUpClick: () -> Unit,
    onBackToFanLogin: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel(LocalContext.current.findActivity()!!)
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    
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
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(60.dp))
                
                Icon(
                    Icons.Default.ManageAccounts, 
                    contentDescription = null, 
                    tint = NammaGold, 
                    modifier = Modifier.size(80.dp)
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    "MANAGER LOGIN", 
                    color = NammaGold, 
                    style = MaterialTheme.typography.displaySmall.copy(fontWeight = FontWeight.ExtraBold, letterSpacing = 4.sp)
                )
                Text(
                    "Access your drama company dashboard", 
                    color = NammaWarmWhite.copy(0.5f), 
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(48.dp))

                Surface(
                    color = NammaSurfaceLow,
                    shape = RoundedCornerShape(32.dp),
                    modifier = Modifier.fillMaxWidth(),
                    border = androidx.compose.foundation.BorderStroke(1.dp, NammaGold.copy(0.2f))
                ) {
                    Column(modifier = Modifier.padding(32.dp)) {
                        AdminInputField("Manager Email", email, { email = it }, Icons.Default.Email)
                        Spacer(modifier = Modifier.height(20.dp))
                        AdminInputField("Password", password, { password = it }, Icons.Default.Lock, isPassword = true)
                        
                        Spacer(modifier = Modifier.height(40.dp))
                        
                        NammaMelaButton(
                            text = "LOGIN TO DASHBOARD 🎪",
                            onClick = { viewModel.adminLogin(email, password) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))
                
                TextButton(onClick = onSignUpClick) {
                    Text("New Company? Register as Manager", color = NammaGold)
                }
                
                TextButton(onClick = onBackToFanLogin) {
                    Text("Not a Manager? Go to Fan Login", color = NammaWarmWhite.copy(0.4f))
                }
                
                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }
}
