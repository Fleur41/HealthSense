package com.sam.healthsense.authentication

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

interface AuthRepository {
    fun  signUp(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    )
    fun  signIn(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    )

    fun signOut()
}


class AuthRepositoryImpl @Inject constructor(
    private  val auth: FirebaseAuth,
    private  val  firestore: FirebaseFirestore
): AuthRepository{
    override fun signUp(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener{authResult ->
                authResult.user?.let {
                    GlobalScope.launch {
                        saveToFirestore(it)
                        Log.d("TAG", "authResult: $authResult")
                        onSuccess()
                    }
                }
            }
            .addOnFailureListener{ exception ->
                Log.d("TAG", "exception: $exception")
                // Handle failure
                onFailure(exception)
            }
    }

    override fun signIn(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener { authResult ->
                Log.d("TAG", "authResult: $authResult")
                onSuccess()
            }
            .addOnFailureListener { exception ->
                Log.d("TAG", "exception: $exception")
                onFailure(exception)
            }

    }

    suspend fun saveToFirestore(user: FirebaseUser){
        val usersCollection = firestore.collection("users")
        
        val userReference = usersCollection.document(user.uid)
            .get()
            .await()
        
        if(userReference.exists())  { 
            Log.d("TAG", "User: ${user.uid} already exists")
            return 
        }
        usersCollection.document(user.uid)
            .set(user)
            .await()

    }

    override fun signOut() {
        auth.signOut()
    }
}