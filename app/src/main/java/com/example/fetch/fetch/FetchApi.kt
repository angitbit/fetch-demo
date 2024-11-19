package com.example.fetch.fetch

import retrofit2.http.GET

//REST endpoint/call interface to FetchApi
interface FetchApi {
    @GET("/hiring.json")
    suspend fun getFetchItems(): List<FetchItem>
}