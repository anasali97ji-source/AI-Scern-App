package com.example.data

import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Header
import com.example.BuildConfig

data class LoginRequest(val email: String, val password: String)
data class LoginResponse(val token: String, val user: UserProfile)

data class UserProfile(
    val id: String,
    val name: String,
    val email: String,
    val planBadge: String = "Free",
    val scansRemaining: Int = 45,
    val totalScans: Int = 50
)

data class ScanRequest(val content: String, val modality: String)
data class ScanResponse(
    val id: String,
    val score: Float,
    val breakdown: Map<String, Float>
)

interface AiscernApi {
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse
    
    @POST("scan")
    suspend fun scanContent(
        @Header("Authorization") token: String,
        @Body request: ScanRequest
    ): ScanResponse
    
    @GET("user/profile")
    suspend fun getProfile(@Header("Authorization") token: String): UserProfile
}

object NetworkClient {
    private val BASE_URL = BuildConfig.AISCERN_API_URL

    val api: AiscernApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(AiscernApi::class.java)
    }
}
