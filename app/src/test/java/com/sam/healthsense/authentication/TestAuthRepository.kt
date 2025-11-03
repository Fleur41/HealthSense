package com.sam.healthsense.authentication

class TestAuthRepository: AuthRepository {
    var signUpError: Boolean = false
    override fun signUp(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        println("Signing up with email: $email and password: $password")

        if (signUpError) {
            onFailure(RuntimeException("Sign up failed"))
            return
        }
        onSuccess()
    }

    var signInError: Boolean = false
    override fun signIn(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        println("Signing in with email: $email and password: $password")
        if (signInError) {
            onFailure(RuntimeException("Sign in failed"))
            return
        }
        onSuccess()
    }

    override fun signOut() {
        println("Signing out")
    }
}