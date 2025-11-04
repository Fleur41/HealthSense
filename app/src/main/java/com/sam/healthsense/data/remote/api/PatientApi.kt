package com.sam.healthsense.data.remote.api

import com.sam.healthsense.data.remote.model.request.PatientRegistrationRequest
import com.sam.healthsense.data.remote.model.response.PatientListResponse
import com.sam.healthsense.data.remote.model.response.PatientResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface PatientApi {

    @POST("patients/register")
    suspend fun registerPatient(
        @Header("Authorization") token: String,
        @Body request: PatientRegistrationRequest
    ): PatientResponse

    @GET("patients/list")
    suspend fun getPatientsList(
        @Header("Authorization") token: String
    ): PatientListResponse

    @GET("patients/show/{id}")
    suspend fun getPatientById(
        @Header("Authorization") token: String,
        @Path("id") patientId: String
    ): PatientResponse
}