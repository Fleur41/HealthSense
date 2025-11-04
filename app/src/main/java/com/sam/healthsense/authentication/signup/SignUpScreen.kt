
package com.sam.healthsense.authentication.signup

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(
    onBack: () -> Unit,
    authViewModel: AuthViewModel
) {
    val context = LocalContext.current
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") } // State for the confirm password field
    val authState by authViewModel.authState.collectAsState()

    LaunchedEffect(authState) {
        if (authState is AuthState.Success) {
            // Navigate to the home screen or perform other actions
//            onSignUpSuccess()
        }
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Sign Up") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back" // Important for accessibility
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CompanyInfo(
                modifier = Modifier.weight(1f) // Takes available top space
            )

            EmailAndPasswordContent(
                modifier = Modifier
                    .weight(1f) // Takes available middle space
                    .padding(horizontal = 16.dp), // Padding for the content
                email = email,
                password = password,
                onEmailChange = { email = it },
                onPasswordChange = { password = it },
                onEmailClear = { email = "" },
                onPasswordClear = { password = "" },
                onActionButtonClick = {
                    if (email.isBlank() || password.isBlank()){
                        Toast.makeText(context, "Please enter email/password", Toast.LENGTH_SHORT).show()
                        return@EmailAndPasswordContent
                    }
                    authViewModel.signUp(email.trim(), password.trim())
                },
                actionButtonContent = {
                    if(authState is AuthState.Loading){
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = Color.White
                        )
                    } else {
                        Text(text = "Sign Up")
                    }
                },
                enableActionButton = authState !is AuthState.Loading,
                // --- Explicitly enable and provide values for Confirm Password ---
                showConfirmPasswordField = true,
                confirmPasswordValue = confirmPassword,
                onConfirmPasswordChange = { confirmPassword = it },
                onConfirmPasswordClear = { confirmPassword = "" }
            )


            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(16.dp) // Takes available bottom space (acts as a spacer)
            ){
                if (authState is AuthState.Error) {
                    Text(
                        text = (authState as AuthState.Error).message,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}
