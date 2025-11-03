package com.sam.healthsense.datastore


import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface DatastoreRepository {
    val authenticated: Flow<Boolean>
    suspend fun saveIsAuthenticated(authenticated: Boolean)
}
class DatastoreRepositoryImpl @Inject constructor(
    private val datastoreManager: DatastoreManager
) : DatastoreRepository {
    override val authenticated: Flow<Boolean> = datastoreManager.authenticated

    override suspend fun saveIsAuthenticated(authenticated: Boolean) {
        datastoreManager.saveIsAuthenticated(authenticated)
    }
}