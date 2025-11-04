package com.sam.healthsense.data.local.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import android.content.Context
import com.sam.healthsense.data.local.dao.*
import com.sam.healthsense.data.local.entity.*

@Database(
    entities = [
        PatientEntity::class,
        PatientVitalsEntity::class,
        GeneralAssessmentEntity::class,
        OverweightAssessmentEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class HealthSenseDatabase : RoomDatabase() {

    abstract fun patientDao(): PatientDao
    abstract fun patientVitalsDao(): PatientVitalsDao
    abstract fun generalAssessmentDao(): GeneralAssessmentDao
    abstract fun overweightAssessmentDao(): OverweightAssessmentDao

    companion object {
        @Volatile
        private var INSTANCE: HealthSenseDatabase? = null

        fun getInstance(context: Context): HealthSenseDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    HealthSenseDatabase::class.java,
                    "healthsense_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
