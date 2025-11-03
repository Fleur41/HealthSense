package com.sam.healthsense

import androidx.compose.runtime.Composable
import com.sam.healthsense.navigation.AppNavigation
import com.sam.healthsense.navigation.NavigationDestination
import com.sam.healthsense.ui.theme.FirebaseAuthenticationTheme

@Composable
fun FirebaseApp(
    startDestination: NavigationDestination
) {
    FirebaseAuthenticationTheme {
        AppNavigation(startDestination = startDestination)

    }
}