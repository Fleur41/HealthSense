package com.sam.healthsense.data.local.dao

import androidx.room.*
import com.sam.healthsense.data.local.entity.PatientEntity
import com.sam.healthsense.data.local.entity.PatientVitalsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PatientDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPatient(patient: PatientEntity)

    @Query("SELECT * FROM patients WHERE id = :patientId")
    suspend fun getPatientById(patientId: String): PatientEntity?

    @Query("SELECT * FROM patients WHERE patientNumber = :patientNumber")
    suspend fun getPatientByNumber(patientNumber: String): PatientEntity?

    @Query("SELECT COUNT(*) FROM patients WHERE patientNumber = :patientNumber")
    suspend fun checkPatientNumberExists(patientNumber: String): Boolean

    @Query("SELECT * FROM patients ORDER BY firstName, lastName")
    suspend fun getAllPatients(): List<PatientEntity>

    @Query("""
        SELECT p.*, 
               (SELECT pv.* FROM patient_vitals pv 
                WHERE pv.patientId = p.id 
                ORDER BY pv.visitDate DESC LIMIT 1) as latestVitals
        FROM patients p
        ORDER BY p.firstName, p.lastName
    """)
    suspend fun getPatientsWithLatestStatus(): List<PatientWithVitals>

    data class PatientWithVitals(
        @Embedded val patient: PatientEntity,
        @Embedded(prefix = "latestVitals_") val latestVitals: PatientVitalsEntity?
    )
}