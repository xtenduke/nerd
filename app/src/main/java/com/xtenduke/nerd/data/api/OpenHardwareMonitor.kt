package com.xtenduke.nerd.data.api

import com.xtenduke.nerd.data.model.OhmRawResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

object OpenHardwareMonitorApi {
    private val retrofit = Retrofit.Builder()
        .baseUrl("http://192.168.2.173:8085")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val openHardwareMonitorService: OpenHardwareMonitorService = retrofit.create(
        OpenHardwareMonitorService::class.java
    )
}

interface OpenHardwareMonitorService {
    @GET("/data.json")
    suspend fun getData(): OhmRawResponse
}