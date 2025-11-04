package com.sam.healthsense.data.local.dao

import androidx.room.*
import com.sam.healthsense.data.local.entity.OverweightAssessmentEntity
import kotlinx.datetime.LocalDate

@Dao
interface OverweightAssessmentDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAssessment(assessment: OverweightAssessmentEntity)

    @Query("SELECT * FROM overweight_assessments WHERE patientId = :patientId AND visitDate = :visitDate")
    suspend fun getAssessmentByPatientAndDate(patientId: String, visitDate: LocalDate): OverweightAssessmentEntity?

    @Query("SELECT * FROM overweight_assessments WHERE patientId = :patientId ORDER BY visitDate DESC")
    suspend fun getAssessmentHistory(patientId: String): List<OverweightAssessmentEntity>

    @Query("SELECT COUNT(*) FROM overweight_assessments WHERE patientId = :patientId AND visitDate = :visitDate")
    suspend fun checkAssessmentExistsForDate(patientId: String, visitDate: LocalDate): Boolean
}