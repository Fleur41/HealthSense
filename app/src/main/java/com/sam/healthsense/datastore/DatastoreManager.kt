package com.sam.healthsense.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "my_preference")


class DatastoreManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    // Companion object for preference keys is a common pattern
    companion object {
        val AUTHENTICATED_KEY = booleanPreferencesKey(name = "authenticated")
    }

    val authenticated: Flow<Boolean> = context.dataStore.data.map { preference ->
        preference[AUTHENTICATED_KEY] ?: false
    }

    suspend fun saveIsAuthenticated(authenticated: Boolean) {
        context.dataStore.edit { preference ->
            preference[AUTHENTICATED_KEY] = authenticated
        }
    }
}


//val Context.dataStore: DataStore<androidx.datastore.preferences.core.Preferences> by preferencesDataStore(name = "my_preference")

//@Singleton
//class DatastoreManager @Inject constructor(
//    @ApplicationContext private val context: Context
//) {
//    private val authenticatedKey = booleanPreferencesKey(name = "authenticated")
//
//    val authenticated: Flow<Boolean> = context.dataStore.data.map { preference ->
//        preference[authenticatedKey] ?: false
//    }
//
//    suspend fun saveIsAuthenticated(authenticated: Boolean) {
//        context.dataStore.edit { preference ->
//            preference[authenticatedKey] = authenticated
//        }
//    }
//}

// This extension property defines your DataStore instance