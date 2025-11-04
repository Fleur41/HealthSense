package com.sam.healthsense.data.remote.model.response

import com.squareup.moshi.Json

data class ApiResponse<T>(
    @Json(name = "success") val success: Boolean,
    @Json(name = "message") val message: String?,
    @Json(name = "code") val code: Int,
    @Json(name = "data") val data: T?
)

data class MessageResponse(
    @Json(name = "slug") val slug: Int,
    @Json(name = "message") val message: String
)