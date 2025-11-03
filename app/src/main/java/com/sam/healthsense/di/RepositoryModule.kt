package com.sam.healthsense.di

import com.sam.healthsense.authentication.AuthRepository
import com.sam.healthsense.authentication.AuthRepositoryImpl
import com.sam.healthsense.datastore.DatastoreRepository
import com.sam.healthsense.datastore.DatastoreRepositoryImpl
import com.sam.healthsense.home.HomeRepository
import com.sam.healthsense.home.HomeRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository

    @Binds
    @Singleton
    abstract fun bindHomeRepository(impl: HomeRepositoryImpl): HomeRepository //HomeRepository

    @Binds
    @Singleton
    abstract fun bindDatastoreRepositoryImpl(impl: DatastoreRepositoryImpl): DatastoreRepository
}