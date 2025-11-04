package com.sam.healthsense.data.local.dao

import androidx.room.*
import com.sam.healthsense.data.local.entity.GeneralAssessmentEntity
import kotlinx.datetime.LocalDate

@Dao
interface GeneralAssessmentDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAssessment(assessment: GeneralAssessmentEntity)

    @Query("SELECT * FROM general_assessments WHERE patientId = :patientId AND visitDate = :visitDate")
    suspend fun getAssessmentByPatientAndDate(patientId: String, visitDate: LocalDate): GeneralAssessmentEntity?

    @Query("SELECT * FROM general_assessments WHERE patientId = :patientId ORDER BY visitDate DESC")
    suspend fun getAssessmentHistory(patientId: String): List<GeneralAssessmentEntity>

    @Query("SELECT COUNT(*) FROM general_assessments WHERE patientId = :patientId AND visitDate = :visitDate")
    suspend fun checkAssessmentExistsForDate(patientId: String, visitDate: LocalDate): Boolean
}