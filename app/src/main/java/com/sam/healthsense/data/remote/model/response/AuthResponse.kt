package com.sam.healthsense.data.remote.model.response

import com.squareup.moshi.Json

data class AuthResponse(
    @Json(name = "success") val success: Boolean,
    @Json(name = "message") val message: String?,
    @Json(name = "code") val code: Int,
    @Json(name = "data") val data: AuthData?
)

data class AuthData(
    @Json(name = "token") val token: String,
    @Json(name = "user") val user: UserData?
)

data class UserData(
    @Json(name = "id") val id: Int,
    @Json(name = "name") val name: String,
    @Json(name = "email") val email: String
)