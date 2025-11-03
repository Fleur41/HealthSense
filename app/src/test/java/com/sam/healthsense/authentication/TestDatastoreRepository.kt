package com.sam.healthsense.authentication

import com.sam.healthsense.datastore.DatastoreRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

class TestDatastoreRepository: DatastoreRepository {
    override val authenticated: Flow<Boolean>
        get() = emptyFlow()

    override suspend fun saveIsAuthenticated(authenticated: Boolean) {
        println("Saving isAuthenticated: $authenticated")
    }
}