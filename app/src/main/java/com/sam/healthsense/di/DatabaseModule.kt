package com.sam.healthsense.di

import android.content.Context
import com.sam.healthsense.data.local.dao.*
import com.sam.healthsense.data.local.database.HealthSenseDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): HealthSenseDatabase {
        return HealthSenseDatabase.getInstance(context)
    }

    @Provides
    @Singleton
    fun providePatientDao(database: HealthSenseDatabase): PatientDao {
        return database.patientDao()
    }

    @Provides
    @Singleton
    fun providePatientVitalsDao(database: HealthSenseDatabase): PatientVitalsDao {
        return database.patientVitalsDao()
    }

    @Provides
    @Singleton
    fun provideGeneralAssessmentDao(database: HealthSenseDatabase): GeneralAssessmentDao {
        return database.generalAssessmentDao()
    }

    @Provides
    @Singleton
    fun provideOverweightAssessmentDao(database: HealthSenseDatabase): OverweightAssessmentDao {
        return database.overweightAssessmentDao()
    }
}