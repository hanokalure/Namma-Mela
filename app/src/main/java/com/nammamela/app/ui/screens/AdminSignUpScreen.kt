package com.nammamela.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.nammamela.app.ui.components.NammaMelaButton
import com.nammamela.app.ui.components.AdminInputField
import com.nammamela.app.ui.theme.*
import com.nammamela.app.viewmodel.AuthViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminSignUpScreen(
    onSignUpClick: () -> Unit,
    onLoginClick: () -> Unit,
    onClose: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    var managerName by remember { mutableStateOf("") }
    var companyName by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var secretKey by remember { mutableStateOf("") }
    
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.errorEvent.collect { message ->
            snackbarHostState.showSnackbar(message)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.loginSuccess.collect { success ->
            if (success) onSignUpClick()
        }
    }

    val scope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = NammaDarkBrown,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("COMPANY REGISTRATION", color = NammaGold, style = MaterialTheme.typography.labelLarge.copy(letterSpacing = 2.sp)) },
                navigationIcon = {
                    IconButton(onClick = onClose) {
                        Icon(Icons.Default.ArrowBack, null, tint = NammaGold)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.Transparent)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                Icons.Default.TheaterComedy, 
                contentDescription = null, 
                tint = NammaGold, 
                modifier = Modifier.size(64.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                "Register Your Drama Company", 
                color = NammaWarmWhite, 
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
            )
            Text(
                "Digitalizing the rural theater experience", 
                color = NammaWarmWhite.copy(0.5f), 
                style = MaterialTheme.typography.bodySmall
            )

            Spacer(modifier = Modifier.height(32.dp))

            AdminInputField("Manager Name", managerName, { managerName = it }, Icons.Default.Person)
            Spacer(modifier = Modifier.height(16.dp))
            AdminInputField("Drama Company Name", companyName, { companyName = it }, Icons.Default.Business)
            Spacer(modifier = Modifier.height(16.dp))
            AdminInputField("Theater Location", location, { location = it }, Icons.Default.LocationOn)
            Spacer(modifier = Modifier.height(16.dp))
            AdminInputField("Email Address", email, { email = it }, Icons.Default.Email)
            Spacer(modifier = Modifier.height(16.dp))
            AdminInputField("Password", password, { password = it }, Icons.Default.Lock, isPassword = true)
            Spacer(modifier = Modifier.height(16.dp))
            AdminInputField("Secret Manager Key", secretKey, { secretKey = it }, Icons.Default.VpnKey)

            Spacer(modifier = Modifier.height(40.dp))

            NammaMelaButton(
                text = "REGISTER COMPANY 🎟",
                onClick = {
                    if (secretKey != "MELA2026") {
                        scope.launch { snackbarHostState.showSnackbar("Invalid Secret Manager Key!") }
                    } else {
                        viewModel.signUp(
                            name = managerName,
                            email = email,
                            pass = password,
                            role = com.nammamela.app.domain.model.UserRole.ADMIN,
                            companyName = companyName,
                            location = location,
                            autoLogin = false
                        )
                    }
                }
            )

            Spacer(modifier = Modifier.height(24.dp))
            TextButton(onClick = onLoginClick) {
                Text("Already a Manager? Login Here", color = NammaGold)
            }
            
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}
