package com.antsyferov.impl

import android.util.Log
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.features.DefaultRequest
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.features.logging.LogLevel
import io.ktor.client.features.logging.Logger
import io.ktor.client.features.logging.Logging
import io.ktor.client.features.observer.ResponseObserver
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import kotlinx.serialization.json.Json

@Module
@InstallIn(SingletonComponent::class)
object  NetworkModule {
    private val TIME_OUT = 60_000

    @Provides
    fun provideKtorClient(): HttpClient {
        return HttpClient(Android) {
            install(JsonFeature) {
                serializer = KotlinxSerializer(Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                })

                engine {
                    connectTimeout = TIME_OUT
                    socketTimeout = TIME_OUT
                }
            }

            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        Log.v("Logger Ktor =>", message)
                    }
                }
                level = LogLevel.ALL
            }

            install(ResponseObserver) {
                onResponse { response ->
                    Log.d("HTTP status:", "${response.status.value}")
                }
            }

            install(DefaultRequest) {
                header(HttpHeaders.ContentType, ContentType.Application.Json)
                parameter("api_key", "GV6vbjDqYL0YwXxm6NdAvCjJZuS0TlsAoFIwfmYj")//"96wUpzriqFPwKEOet70GLcRjoOHGXOrW0PS0RFw2")
            }

        }
    }

}