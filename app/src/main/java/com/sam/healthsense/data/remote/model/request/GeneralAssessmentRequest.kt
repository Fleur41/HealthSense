package com.sam.healthsense.data.remote.model.request

import com.squareup.moshi.Json

data class GeneralAssessmentRequest(
    @Json(name = "patient_id") val patientId: String,
    @Json(name = "visit_date") val visitDate: String,
    @Json(name = "general_health") val generalHealth: String,
    @Json(name = "on_diet") val onDiet: String,
    @Json(name = "comments") val comments: String,
    @Json(name = "vital_id") val vitalId: String
)