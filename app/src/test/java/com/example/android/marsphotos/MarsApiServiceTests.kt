package com.example.android.marsphotos

import com.example.android.marsphotos.network.MarsApiService
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class MarsApiServiceTest : BaseTest() {

    private lateinit var service: MarsApiService

    @Before
    fun setup() {
        val url = mockWebServer.url("/")
        service = Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(
                MoshiConverterFactory.create(
                    Moshi.Builder()
                        .add(KotlinJsonAdapterFactory())
                        .build()
                ))
            .build()
            .create(MarsApiService::class.java)
    }

    @Test
    fun api_service() {
        enqueue("mars_photos.json")
        runBlocking {
            val apiResponse = service.getPhotos()

            Assert.assertNotNull(apiResponse)
            Assert.assertTrue("The list was empty", apiResponse.isNotEmpty())
            Assert.assertEquals("The id's did not match", "424905", apiResponse[0].id)
        }
    }
}