package com.aiscern.app.data.remote.api

import com.aiscern.app.data.remote.dto.CreditResponse
import com.aiscern.app.data.remote.dto.DetectionResponse
import com.aiscern.app.data.remote.dto.FeedbackRequest
import com.aiscern.app.data.remote.dto.HistoryResponse
import com.aiscern.app.data.remote.dto.TextDetectRequest
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface AiscernApiService {

    @POST("api/v1/detect/text")
    suspend fun detectText(
        @Header("X-API-Key") apiKey: String?,
        @Body request: TextDetectRequest
    ): Response<DetectionResponse>

    @Multipart
    @POST("api/v1/detect/image")
    suspend fun detectImage(
        @Header("X-API-Key") apiKey: String?,
        @Part image: MultipartBody.Part,
        @Part("deep_scan") deepScan: RequestBody? = null
    ): Response<DetectionResponse>

    @Multipart
    @POST("api/v1/detect/audio")
    suspend fun detectAudio(
        @Header("X-API-Key") apiKey: String?,
        @Part audio: MultipartBody.Part
    ): Response<DetectionResponse>

    @Multipart
    @POST("api/v1/detect/video")
    suspend fun detectVideo(
        @Header("X-API-Key") apiKey: String?,
        @Part video: MultipartBody.Part,
        @Part("frame_interval") frameInterval: RequestBody? = null
    ): Response<DetectionResponse>

    @GET("api/v1/history")
    suspend fun getHistory(
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 20
    ): Response<HistoryResponse>

    @GET("api/v1/credits")
    suspend fun getCredits(): Response<CreditResponse>

    @POST("api/v1/feedback")
    suspend fun submitFeedback(
        @Body feedback: FeedbackRequest
    ): Response<Unit>
}
