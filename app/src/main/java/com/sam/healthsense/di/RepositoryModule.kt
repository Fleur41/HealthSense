package com.sam.healthsense.di

import com.sam.healthsense.authentication.AuthRepository
import com.sam.healthsense.authentication.AuthRepositoryImpl
import com.sam.healthsense.data.repository.IPatientRepository
import com.sam.healthsense.data.repository.IVisitRepository
import com.sam.healthsense.data.repository.PatientRepository
import com.sam.healthsense.data.repository.VisitRepository
import com.sam.healthsense.datastore.DatastoreRepository
import com.sam.healthsense.datastore.DatastoreRepositoryImpl
//import com.sam.healthsense.home.HomeRepository
//import com.sam.healthsense.home.HomeRepositoryImpl
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
    abstract fun bindDatastoreRepositoryImpl(impl: DatastoreRepositoryImpl): DatastoreRepository

    @Binds
    @Singleton
    abstract fun bindPatientRepository(impl: PatientRepository): IPatientRepository

    @Binds
    @Singleton
    abstract fun bindVisitRepository(impl: VisitRepository): IVisitRepository
}