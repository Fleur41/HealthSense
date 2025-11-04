package com.sam.healthsense.data.remote.model.request

import com.squareup.moshi.Json

data class VitalsRequest(
    @Json(name = "patient_id") val patientId: String,
    @Json(name = "visit_date") val visitDate: String,
    @Json(name = "height") val height: Double,
    @Json(name = "weight") val weight: Double,
    @Json(name = "bmi") val bmi: Double
)