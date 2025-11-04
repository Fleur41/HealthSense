package com.sam.healthsense.data.local.dao

import androidx.room.*
import com.sam.healthsense.data.local.entity.PatientVitalsEntity
import kotlinx.datetime.LocalDate

@Dao
interface PatientVitalsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVitals(vitals: PatientVitalsEntity)

    @Query("SELECT * FROM patient_vitals WHERE patientId = :patientId AND visitDate = :visitDate")
    suspend fun getVitalsByPatientAndDate(patientId: String, visitDate: LocalDate): PatientVitalsEntity?

    @Query("SELECT * FROM patient_vitals WHERE patientId = :patientId ORDER BY visitDate DESC LIMIT 1")
    suspend fun getLatestVitals(patientId: String): PatientVitalsEntity?

    @Query("SELECT * FROM patient_vitals WHERE patientId = :patientId ORDER BY visitDate DESC")
    suspend fun getVitalsHistory(patientId: String): List<PatientVitalsEntity>

    @Query("SELECT COUNT(*) FROM patient_vitals WHERE patientId = :patientId AND visitDate = :visitDate")
    suspend fun checkVitalsExistForDate(patientId: String, visitDate: LocalDate): Boolean
}