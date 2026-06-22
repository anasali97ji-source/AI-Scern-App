package com.aiscern.app.di

import android.content.Context
import androidx.room.Room
import com.aiscern.app.data.local.database.AiscernDatabase
import com.aiscern.app.data.local.database.ScanHistoryDao
import com.aiscern.app.data.remote.api.AiscernApiService
import com.aiscern.app.data.repository.DetectionRepository
import com.aiscern.app.data.repository.DetectionRepositoryImpl
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.util.concurrent.TimeUnit

class AppContainer(private val context: Context) {
    val json: Json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()
    }

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://aiscern.com/")
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
    }

    val apiService: AiscernApiService by lazy {
        retrofit.create(AiscernApiService::class.java)
    }

    val database: AiscernDatabase by lazy {
        Room.databaseBuilder(
            context,
            AiscernDatabase::class.java,
            "aiscern_database"
        )
            .fallbackToDestructiveMigration(dropAllTables = true)
            .build()
    }

    val scanHistoryDao: ScanHistoryDao by lazy {
        database.scanHistoryDao()
    }

    val detectionRepository: DetectionRepository by lazy {
        DetectionRepositoryImpl(apiService, scanHistoryDao, json)
    }
}
