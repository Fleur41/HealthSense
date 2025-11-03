
package com.sam.healthsense.authentication.signup // Or your chosen components package

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.sam.healthsense.authentication.signin.CustomTextField // Make sure this import is correct
import com.sam.healthsense.components.VerticalSpacer // Make sure this import is correct

@Composable
fun CompanyInfo(modifier: Modifier = Modifier) {
    Box (
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        Text(
            text = "HealthSense", // Or your company name
            style = MaterialTheme.typography.headlineLarge
        )
    }
}

@Composable
fun EmailAndPasswordContent(
    modifier: Modifier = Modifier,
    email: String,
    password: String,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onEmailClear: () -> Unit,
    onPasswordClear: () -> Unit,
    onActionButtonClick: () -> Unit,
    actionButtonContent: @Composable () -> Unit,
    enableActionButton: Boolean = true,
    // --- Parameters for optional Confirm Password field ---
    showConfirmPasswordField: Boolean = false,       // Default to false
    confirmPasswordValue: String = "",               // Value for the confirm password field
    onConfirmPasswordChange: (String) -> Unit = {},  // Handler for confirm password change
    onConfirmPasswordClear: () -> Unit = {}          // Handler for clearing confirm password
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CustomTextField(
            modifier = Modifier.fillMaxWidth(),
            value = email,
            onValueChange = onEmailChange,
            placeholderText = "Enter your Email",
            onClear = onEmailClear
        )

        VerticalSpacer(8)

        CustomTextField(
            modifier = Modifier.fillMaxWidth(),
            value = password,
            onValueChange = onPasswordChange,
            placeholderText = "Enter your Password",
            isPasswordField = true, // Assuming CustomTextField handles this
            onClear = onPasswordClear
        )

        // CONDITIONAL DISPLAY BASED ON THE FLAG
        if (showConfirmPasswordField) {
            VerticalSpacer(8)
            CustomTextField(
                modifier = Modifier.fillMaxWidth(),
                value = confirmPasswordValue,
                onValueChange = onConfirmPasswordChange,
                placeholderText = "Confirm your Password",
                isPasswordField = true, // Assuming CustomTextField handles this
                onClear = onConfirmPasswordClear
            )
        }

        VerticalSpacer(16)

        Button(
            onClick = onActionButtonClick,
            modifier = Modifier.fillMaxWidth(),
            enabled = enableActionButton
        ) {
            actionButtonContent()
//            Text(text = actionButtonText)
        }
    }
}