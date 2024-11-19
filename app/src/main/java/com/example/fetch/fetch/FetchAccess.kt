package com.example.fetch.fetch

import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Retrofit

//gives remote access to FetchApi;
//use as singleton as we don't need > 1 instance
class FetchAccess private constructor() {
    companion object {
        const val FETCH_BASE_URL = "https://fetch-hiring.s3.amazonaws.com"

        @Volatile
        private var instance: FetchAccess? = null

        fun getInstance(): FetchAccess {
            if (null == instance) { //unsynchronized check
                synchronized(this) { // synchronized to avoid concurrent use
                    if (null == instance) { //synchronized check
                        instance = FetchAccess()
                    }
                }
            }
            return instance!!
        }
    }

    private val retrofit: Retrofit = Retrofit.Builder().baseUrl(FETCH_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create()).build()

    val fetchApi: FetchApi = retrofit.create(FetchApi::class.java)

}