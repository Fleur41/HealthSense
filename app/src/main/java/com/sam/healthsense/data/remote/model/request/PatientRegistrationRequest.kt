package com.sam.healthsense.data.remote.model.request

import com.squareup.moshi.Json

data class PatientRegistrationRequest(
    @Json(name = "patient_number") val patientNumber: String,
    @Json(name = "first_name") val firstName: String,
    @Json(name = "last_name") val lastName: String,
    @Json(name = "date_of_birth") val dateOfBirth: String,
    @Json(name = "gender") val gender: String,
    @Json(name = "registration_date") val registrationDate: String
)