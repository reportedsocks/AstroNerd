package com.antsyferov.impl.network

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import javax.inject.Inject


class AsteroidsApi @Inject constructor(
    private val client: HttpClient
) {

    companion object {
        private const val BASE_URL = "https://api.nasa.gov/neo/rest"
        private const val ASTEROIDS = "/v1/feed"
    }

    suspend fun getAsteroids(
        startDate: String,
        endDate: String
    ): AsteroidsResponse =
        client.get("$BASE_URL$ASTEROIDS?start_date=$startDate&end_date=$endDate")
}