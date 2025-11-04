package com.sam.healthsense.Settings

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sam.healthsense.authentication.AuthRepository
import com.sam.healthsense.datastore.DatastoreRepository
import com.sam.healthsense.navigation.NavigationDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel // This is correct
class SettingsViewmodel @Inject constructor(
    private val authRepository: AuthRepository,
    private val datastoreRepository: DatastoreRepository
) : ViewModel() {
    private val _startDestination =
        MutableStateFlow<NavigationDestination>(NavigationDestination.Splash)
    val startDestination: StateFlow<NavigationDestination> get() = _startDestination

    private val isAuthenticated: StateFlow<Boolean> = datastoreRepository.authenticated
        .catch { e ->
            // Handle errors from the datastoreRepository.authenticated Flow
            Log.e("SettingsViewModel", "Error collecting authentication state from DataStore", e)
            emit(false)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = false
        )

    init {
        viewModelScope.launch {
            delay(2000)
            // Observe the already transformed isAuthenticated StateFlow
            isAuthenticated.collect { authenticated ->
                _startDestination.value =
                    if (authenticated) NavigationDestination.Home else NavigationDestination.SignIn
                Log.d("SettingsViewModel", "Start destination updated: ${_startDestination.value}")
            }
        }
    }


    fun saveIsAuthenticated(authenticated: Boolean) {
        viewModelScope.launch {
            try {
                datastoreRepository.saveIsAuthenticated(authenticated)
            } catch (e: Exception) {
                Log.e("SettingsViewModel", "Error saving authentication state", e)
                // Optionally, notify UI of the error
            }
        }
    }

    fun signOut(){
        viewModelScope.launch {
            authRepository.signOut()
            datastoreRepository.saveIsAuthenticated(false)
        }
    }
}


