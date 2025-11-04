package com.sam.healthsense.data.remote.api

import com.sam.healthsense.data.remote.model.request.GeneralAssessmentRequest
import com.sam.healthsense.data.remote.model.request.OverweightAssessmentRequest
import com.sam.healthsense.data.remote.model.request.VitalsRequest
import com.sam.healthsense.data.remote.model.response.ApiResponse
import com.sam.healthsense.data.remote.model.response.VisitResponse
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface VisitApi {

    @POST("vitals/add")
    suspend fun saveVitals(
        @Header("Authorization") token: String,
        @Body request: VitalsRequest
    ): ApiResponse<Any>

    @POST("visits/add")
    suspend fun saveGeneralAssessment(
        @Header("Authorization") token: String,
        @Body request: GeneralAssessmentRequest
    ): ApiResponse<Any>

    @POST("visits/add")
    suspend fun saveOverweightAssessment(
        @Header("Authorization") token: String,
        @Body request: OverweightAssessmentRequest
    ): ApiResponse<Any>

    @POST("visits/view")
    suspend fun getVisitsByDate(
        @Header("Authorization") token: String,
        @Body request: VisitDateRequest
    ): VisitResponse
}

data class VisitDateRequest(
    val visit_date: String
)