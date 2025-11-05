package com.sam.healthsense.data.repository

import com.sam.healthsense.Utils.toDomain
import com.sam.healthsense.Utils.toEntity
import com.sam.healthsense.data.local.dao.PatientVitalsDao
import com.sam.healthsense.data.local.dao.GeneralAssessmentDao
import com.sam.healthsense.data.local.dao.OverweightAssessmentDao
import com.sam.healthsense.data.remote.api.VisitApi
import com.sam.healthsense.domain.model.PatientVitals
import com.sam.healthsense.domain.model.GeneralAssessment
import com.sam.healthsense.domain.model.OverweightAssessment
import com.sam.healthsense.Utils.Result
import kotlinx.datetime.LocalDate
import javax.inject.Inject

interface IVisitRepository {
    suspend fun savePatientVitals(vitals: PatientVitals): Result<Unit>
    suspend fun getPatientVitals(patientId: String, visitDate: LocalDate): Result<PatientVitals?>
    suspend fun getLatestVitals(patientId: String): Result<PatientVitals?>
    suspend fun saveGeneralAssessment(assessment: GeneralAssessment): Result<Unit>
    suspend fun saveOverweightAssessment(assessment: OverweightAssessment): Result<Unit>
}

class VisitRepository @Inject constructor(
    private val patientVitalsDao: PatientVitalsDao,
    private val generalAssessmentDao: GeneralAssessmentDao,
    private val overweightAssessmentDao: OverweightAssessmentDao,
) : IVisitRepository {

    override suspend fun savePatientVitals(vitals: PatientVitals): Result<Unit> {
        return try {
            // Save locally first
            patientVitalsDao.insertPatientVitals(vitals.toEntity())

            // Sync to backend with Firebase token
            // val token = getFirebaseToken()
            // val response = visitApi.saveVitals(token, vitals.toRequest())

            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e.message ?: "Failed to save vitals")
        }
    }

    override suspend fun getPatientVitals(patientId: String, visitDate: LocalDate): Result<PatientVitals?> {
        return try {
            val vitals = patientVitalsDao.getVitalsByPatientAndDate(patientId, visitDate)?.toDomain()
            Result.Success(vitals)
        } catch (e: Exception) {
            Result.Error(e.message ?: "Database error")
        }
    }

    override suspend fun getLatestVitals(patientId: String): Result<PatientVitals?> {
        return try {
            val vitals = patientVitalsDao.getLatestVitals(patientId)?.toDomain()
            Result.Success(vitals)
        } catch (e: Exception) {
            Result.Error(e.message ?: "Database error")
        }
    }

    override suspend fun saveGeneralAssessment(assessment: GeneralAssessment): Result<Unit> {
        return try {
            // Save locally first
            generalAssessmentDao.insertAssessment(assessment.toEntity())

            // Sync to backend with Firebase token
            // val token = getFirebaseToken()
            // val response = visitApi.saveGeneralAssessment(token, assessment.toRequest())

            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e.message ?: "Failed to save assessment")
        }
    }

    override suspend fun saveOverweightAssessment(assessment: OverweightAssessment): Result<Unit> {
        return try {
            // Save locally first
            overweightAssessmentDao.insertAssessment(assessment.toEntity())

            // Sync to backend with Firebase token
            // val token = getFirebaseToken()
            // val response = visitApi.saveOverweightAssessment(token, assessment.toRequest())

            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e.message ?: "Failed to save assessment")
        }
    }
}