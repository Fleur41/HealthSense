package com.sam.healthsense.data.repository

import com.sam.healthsense.Utils.toDomain
import com.sam.healthsense.Utils.toEntity
import com.sam.healthsense.data.local.dao.PatientDao
import com.sam.healthsense.data.local.dao.PatientVitalsDao
import com.sam.healthsense.data.remote.api.PatientApi
import com.sam.healthsense.domain.model.Patient
import com.sam.healthsense.domain.model.BMIStatus
import com.sam.healthsense.Utils.Result
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.datetime.LocalDate
import javax.inject.Inject

interface IPatientRepository {
    suspend fun registerPatient(patient: Patient): Result<Unit>
    suspend fun getPatientById(patientId: String): Result<Patient>
    suspend fun checkPatientNumberExists(patientNumber: String): Boolean
    suspend fun getAllPatients(): Result<List<Patient>>
    suspend fun getPatientsWithLatestStatus(): Result<List<PatientWithStatus>>
}

data class PatientWithStatus(
    val patient: Patient,
    val latestBmiStatus: BMIStatus?,
    val lastVisitDate: LocalDate?
)

class PatientRepository @Inject constructor(
    private val patientDao: PatientDao,
    private val patientVitalsDao: PatientVitalsDao,
    private val patientApi: PatientApi
) : IPatientRepository {

    override suspend fun registerPatient(patient: Patient): Result<Unit> {
        return try {
            // Save locally first
            patientDao.insertPatient(patient.toEntity())

            // TODO: Get Firebase token and sync to backend
            // val token = getFirebaseToken()
            // val response = patientApi.registerPatient(token, patient.toRequest())

            // For now, just return success - we'll implement sync later
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e.message ?: "Failed to register patient")
        }
    }

    override suspend fun getPatientById(patientId: String): Result<Patient> {
        return try {
            val patientEntity = patientDao.getPatientById(patientId)
            if (patientEntity != null) {
                Result.Success(patientEntity.toDomain())
            } else {
                Result.Error("Patient not found")
            }
        } catch (e: Exception) {
            Result.Error(e.message ?: "Database error")
        }
    }

    override suspend fun checkPatientNumberExists(patientNumber: String): Boolean {
        return patientDao.checkPatientNumberExists(patientNumber)
    }

    override suspend fun getAllPatients(): Result<List<Patient>> {
        return try {
            val patients = patientDao.getAllPatients().map { it.toDomain() }
            Result.Success(patients)
        } catch (e: Exception) {
            Result.Error(e.message ?: "Failed to load patients")
        }
    }
    override suspend fun getPatientsWithLatestStatus(): Result<List<PatientWithStatus>> {
        return try {
            val patients = patientDao.getAllPatients().map { it.toDomain() }

            val patientsWithStatus = coroutineScope {
                patients.map { patient ->
                    async {
                        val latestVitals = patientVitalsDao.getLatestVitals(patient.id)
                        val bmiStatus = latestVitals?.let { vitals ->
                            when {
                                vitals.bmi < 18.5 -> BMIStatus.UNDERWEIGHT
                                vitals.bmi < 25 -> BMIStatus.NORMAL
                                else -> BMIStatus.OVERWEIGHT
                            }
                        }
                        PatientWithStatus(
                            patient = patient,
                            latestBmiStatus = bmiStatus,
                            lastVisitDate = latestVitals?.visitDate
                        )
                    }
                }.map { it.await() }
            }

            Result.Success(patientsWithStatus)
        } catch (e: Exception) {
            Result.Error(e.message ?: "Failed to load patient list")
        }
    }

//    override suspend fun getPatientsWithLatestStatus(): Result<List<PatientWithStatus>> {
//        return try {
//            val patients = patientDao.getAllPatients().map { it.toDomain() }
//
//            val patientsWithStatus = coroutineScope {
//                patients.map { patient ->
//                    async {
//                        val latestVitals = patientVitalsDao.getLatestVitals(patient.id)
//                        PatientWithStatus(
//                            patient = patient,
//                            latestBmiStatus = latestVitals?.bmiStatus,
//                            lastVisitDate = latestVitals?.visitDate
//                        )
//                    }
//                }.map { it.await() }
//            }
//
//            Result.Success(patientsWithStatus)
//        } catch (e: Exception) {
//            Result.Error(e.message ?: "Failed to load patient list")
//        }
//    }
}