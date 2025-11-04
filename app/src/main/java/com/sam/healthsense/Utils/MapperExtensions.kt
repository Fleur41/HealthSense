package com.sam.healthsense.Utils

import com.sam.healthsense.data.local.entity.*
import com.sam.healthsense.data.remote.model.request.GeneralAssessmentRequest
import com.sam.healthsense.data.remote.model.request.OverweightAssessmentRequest
import com.sam.healthsense.data.remote.model.request.PatientRegistrationRequest
import com.sam.healthsense.data.remote.model.request.VitalsRequest
import com.sam.healthsense.domain.model.*
import java.util.UUID


// Patient Mappers
fun Patient.toEntity(): PatientEntity {
    return PatientEntity(
        id = this.id,
        patientNumber = this.patientNumber,
        registrationDate = this.registrationDate,
        firstName = this.firstName,
        lastName = this.lastName,
        dateOfBirth = this.dateOfBirth,
        gender = this.gender
    )
}

fun PatientEntity.toDomain(): Patient {
    return Patient(
        id = this.id,
        patientNumber = this.patientNumber,
        registrationDate = this.registrationDate,
        firstName = this.firstName,
        lastName = this.lastName,
        dateOfBirth = this.dateOfBirth,
        gender = this.gender
    )
}

// PatientVitals Mappers
fun PatientVitals.toEntity(): PatientVitalsEntity {
    return PatientVitalsEntity(
        id = this.id,
        patientId = this.patientId,
        visitDate = this.visitDate,
        height = this.height,
        weight = this.weight,
        bmi = this.bmi
    )
}

fun PatientVitalsEntity.toDomain(): PatientVitals {
    return PatientVitals(
        id = this.id,
        patientId = this.patientId,
        visitDate = this.visitDate,
        height = this.height,
        weight = this.weight,
        bmi = this.bmi
    )
}

// GeneralAssessment Mappers
fun GeneralAssessment.toEntity(): GeneralAssessmentEntity {
    return GeneralAssessmentEntity(
        id = this.id,
        patientId = this.patientId,
        visitDate = this.visitDate,
        generalHealth = this.generalHealth.name,
        hasBeenOnDiet = this.hasBeenOnDiet,
        comments = this.comments
    )
}

fun GeneralAssessmentEntity.toDomain(): GeneralAssessment {
    return GeneralAssessment(
        id = this.id,
        patientId = this.patientId,
        visitDate = this.visitDate,
        generalHealth = GeneralHealth.valueOf(this.generalHealth),
        hasBeenOnDiet = this.hasBeenOnDiet,
        comments = this.comments
    )
}

// OverweightAssessment Mappers
fun OverweightAssessment.toEntity(): OverweightAssessmentEntity {
    return OverweightAssessmentEntity(
        id = this.id,
        patientId = this.patientId,
        visitDate = this.visitDate,
        generalHealth = this.generalHealth.name,
        isTakingDrugs = this.isTakingDrugs,
        comments = this.comments
    )
}

fun OverweightAssessmentEntity.toDomain(): OverweightAssessment {
    return OverweightAssessment(
        id = this.id,
        patientId = this.patientId,
        visitDate = this.visitDate,
        generalHealth = GeneralHealth.valueOf(this.generalHealth),
        isTakingDrugs = this.isTakingDrugs,
        comments = this.comments
    )
}

// API Request Mappers
fun Patient.toRequest(): PatientRegistrationRequest {
    return PatientRegistrationRequest(
        patientNumber = this.patientNumber,
        firstName = this.firstName,
        lastName = this.lastName,
        dateOfBirth = this.dateOfBirth.toString(),
        gender = this.gender,
        registrationDate = this.registrationDate.toString()
    )
}

fun PatientVitals.toRequest(): VitalsRequest {
    return VitalsRequest(
        patientId = this.patientId,
        visitDate = this.visitDate.toString(),
        height = this.height,
        weight = this.weight,
        bmi = this.bmi
    )
}

fun GeneralAssessment.toRequest(): GeneralAssessmentRequest {
    return GeneralAssessmentRequest(
        patientId = this.patientId,
        visitDate = this.visitDate.toString(),
        generalHealth = this.generalHealth.name,
        onDiet = if (this.hasBeenOnDiet) "Yes" else "No",
        comments = this.comments,
        vitalId = "" // We'll need to get this from vitals response
    )
}

fun OverweightAssessment.toRequest(): OverweightAssessmentRequest {
    return OverweightAssessmentRequest(
        patientId = this.patientId,
        visitDate = this.visitDate.toString(),
        generalHealth = this.generalHealth.name,
        onDrugs = if (this.isTakingDrugs) "Yes" else "No",
        comments = this.comments,
        vitalId = "" // We'll need to get this from vitals response
    )
}
