package com.sam.healthsense.data.remote.model.response

import com.squareup.moshi.Json

data class PatientResponse(
    @Json(name = "success") val success: Boolean,
    @Json(name = "message") val message: String?,
    @Json(name = "code") val code: Int,
    @Json(name = "data") val data: PatientData?
)

data class PatientData(
    @Json(name = "id") val id: Int,
    @Json(name = "patient_number") val patientNumber: String,
    @Json(name = "first_name") val firstName: String,
    @Json(name = "last_name") val lastName: String,
    @Json(name = "date_of_birth") val dateOfBirth: String,
    @Json(name = "gender") val gender: String,
    @Json(name = "registration_date") val registrationDate: String
)

data class PatientListResponse(
    @Json(name = "success") val success: Boolean,
    @Json(name = "message") val message: String?,
    @Json(name = "code") val code: Int,
    @Json(name = "data") val data: List<PatientData>?
)