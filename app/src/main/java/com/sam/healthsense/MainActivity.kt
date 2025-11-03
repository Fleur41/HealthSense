package com.sam.healthsense

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.google.android.gms.tasks.OnCompleteListener
import com.sam.healthsense.Settings.SettingsViewmodel
import com.sam.healthsense.Utils.Constants
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val settingsViewmodel: SettingsViewmodel by viewModels<SettingsViewmodel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val startDestination by settingsViewmodel.startDestination.collectAsState()
            FirebaseApp(startDestination = startDestination)
        }

    }
}




