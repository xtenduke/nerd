package com.xtenduke.nerd.data.api

import com.xtenduke.nerd.data.model.OhmRawResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

class OpenHardwareMonitorApi(private val host: String) {
    private val retrofit = Retrofit.Builder()
        .baseUrl(host)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    public var openHardwareMonitorService: OpenHardwareMonitorService = retrofit.create(
        OpenHardwareMonitorService::class.java
    )
}

interface OpenHardwareMonitorService {
    @GET("/data.json")
    suspend fun getData(): OhmRawResponse
}