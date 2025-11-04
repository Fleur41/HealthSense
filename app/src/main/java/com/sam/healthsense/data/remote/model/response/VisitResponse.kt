package com.sam.healthsense.data.remote.model.response

import com.squareup.moshi.Json

data class VisitResponse(
    @Json(name = "success") val success: Boolean,
    @Json(name = "message") val message: String?,
    @Json(name = "code") val code: Int,
    @Json(name = "data") val data: List<VisitData>?
)

data class VisitData(
    @Json(name = "name") val name: String,
    @Json(name = "age") val age: Int,
    @Json(name = "bmi") val bmi: String,
    @Json(name = "status") val status: String
)