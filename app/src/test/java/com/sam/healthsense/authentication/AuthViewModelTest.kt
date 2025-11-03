package com.sam.healthsense.authentication

import app.cash.turbine.test
import com.sam.healthsense.authentication.signup.AuthState
import com.sam.healthsense.authentication.signup.AuthViewModel
import com.sam.healthsense.datastore.DatastoreRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class AuthViewModelTest {
    private lateinit var viewModel: AuthViewModel
    private lateinit var authRepository : TestAuthRepository
    private  lateinit var datastoreRepository : DatastoreRepository

    @Before
    fun setup(){
        authRepository = TestAuthRepository()
        datastoreRepository = TestDatastoreRepository()
        viewModel = AuthViewModel(authRepository, datastoreRepository)
    }

    @Test
    fun testSignUpSuccess(){
        runTest{
            viewModel.authState.test {
                viewModel.signUp("sam1@gmail.com", "samqwerty")

                skipItems(1) //Skip AuthState.Initial item
                val item = awaitItem()
                println("item: $item")
                Assert.assertTrue(item is AuthState.Loading)

                val nextItem = awaitItem()
                Assert.assertTrue(nextItem is AuthState.Success)
            }
        }
    }

    @Test
    fun testSignUpFailure(){
        authRepository.signUpError = true
        runTest{
            viewModel.authState.test {
                viewModel.signUp("sam1@gmail.com", "samqwerty")

                skipItems(1) //Skip AuthState.Initial item
                val item = awaitItem()
                println("item: $item")
                Assert.assertTrue(item is AuthState.Loading)

                val nextItem = awaitItem()
                Assert.assertTrue(nextItem is AuthState.Error)
            }
        }
    }

    @Test
    fun testSignInSuccess(){
        runTest{
            viewModel.authState.test {
                viewModel.signUp("sam1@gmail.com", "samqwerty")

                skipItems(1) //Skip AuthState.Initial item
                val item = awaitItem()
                println("item: $item")
                Assert.assertTrue(item is AuthState.Loading)

                val nextItem = awaitItem()
                Assert.assertTrue(nextItem is AuthState.Success)
            }
        }
    }

    @Test
    fun testSignInFailure(){
        authRepository.signUpError = true
        runTest{
            viewModel.authState.test {
                viewModel.signUp("sam1@gmail.com", "samqwerty")

                skipItems(1) //Skip AuthState.Initial item
                val item = awaitItem()
                println("item: $item")
                Assert.assertTrue(item is AuthState.Loading)

                val nextItem = awaitItem()
                Assert.assertTrue(nextItem is AuthState.Error)
            }
        }
    }
}