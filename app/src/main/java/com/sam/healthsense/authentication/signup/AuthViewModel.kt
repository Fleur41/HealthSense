package com.sam.healthsense.authentication.signup

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sam.healthsense.authentication.AuthRepository
import com.sam.healthsense.datastore.DatastoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private  val datastoreRepository: DatastoreRepository
): ViewModel() {
    private val _authState = MutableStateFlow<AuthState>(AuthState.Initial)
    val authState: StateFlow<AuthState> get() = _authState.asStateFlow()

    init {
        Log.d("TAG", "Created an instance of ${this::class.simpleName}")

    }
    fun  signUp(email: String, password: String){
        viewModelScope.launch(Dispatchers.IO){
            _authState.value = AuthState.Loading
            delay(3000)
            authRepository.signUp(
                email = email,
                password = password,
                onSuccess = {
                    saveIsAuthenticated(true)
                    _authState.value = AuthState.Success
                },
                onFailure = { exception ->
                    _authState.value = AuthState.Error(exception.message ?: "Unknown error")
                }
            )
        }
    }
    fun  signIn(email: String , password: String){
        viewModelScope.launch(Dispatchers.IO){
            _authState.value = AuthState.Loading
            delay(3000)
            authRepository.signIn(
                email = email,
                password = password,
                onSuccess = {
                    saveIsAuthenticated(true)
                    _authState.value = AuthState.Success
                },
                onFailure = { exception ->
                    _authState.value = AuthState.Error(exception.message ?: "Unknown error")
                }
            )
        }
    }
//     "sam1@gmail.com", "samy10@gmail.com", "sammirngahu123@gmail.com", "tintin@gmail.com"
//    pwd: samqwerty, "samy10" "sammirngahu123" "tintin1234"

    fun saveIsAuthenticated(authenticated: Boolean){
        viewModelScope.launch {
            datastoreRepository.saveIsAuthenticated(authenticated)
        }
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("TAG", "Clearing an instance of ${this::class.simpleName}")
    }
}

sealed interface AuthState{
    data object Initial: AuthState
    data object Loading: AuthState
    data object Success: AuthState
    data class Error(val message: String): AuthState

}

//suspend fun main() {
//    val scope = CoroutineScope(Dispatchers.Default)
//    scope.launch {
//        delay(4000)
//        println("Hello sir")
//    }.join()
//    println("This will be printed")
//}