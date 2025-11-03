package com.sam.healthsense.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.sam.healthsense.R
import com.sam.healthsense.Settings.SettingsViewmodel


@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun HomeScreen(
    settingsViewmodel: SettingsViewmodel = hiltViewModel(),
    homeViewModel: HomeViewModel = hiltViewModel(),
    onDetailClick: () -> Unit
) {
    val notificationPermission = rememberPermissionState(android.Manifest.permission.POST_NOTIFICATIONS)

    LaunchedEffect(Unit) {
        notificationPermission.launchPermissionRequest()
    }
    var nullableProperty: Int? = null
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = " Home")},
                actions = {
                    IconButton(
                        onClick = {
                        settingsViewmodel.signOut()
                        }){
                        Icon(
                            painter = painterResource(R.drawable.ic_logout),
                            contentDescription = null
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Home")
            Button(onClick = { onDetailClick() }) {
                Text(text = "Go to Detail Screen")
            }

            Button(
                onClick = {
                    nullableProperty?.inc()
                }
            ) {
                Text(text = "Crash the app")
            }
        }
    }
}
