package com.sam.healthsense.data.remote.interceptor

import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor() : Interceptor {

    private var authToken: String? = null

    fun setAuthToken(token: String) {
        authToken = "Bearer $token"
    }

    fun clearAuthToken() {
        authToken = null
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        val requestBuilder = originalRequest.newBuilder()

        // Add auth token if available
        authToken?.let { token ->
            requestBuilder.addHeader("Authorization", token)
        }

        // Add common headers
        requestBuilder.addHeader("Accept", "application/json")
        requestBuilder.addHeader("Content-Type", "application/json")

        return chain.proceed(requestBuilder.build())
    }
}