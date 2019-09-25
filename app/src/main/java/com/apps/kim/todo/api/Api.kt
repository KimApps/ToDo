package com.apps.kim.todo.api

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
Created by KIM on 25.09.2019
 **/

class Api {
    val reminderApi: TodoApi
    private var request: Request

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://quotes.rest/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
        request = retrofit.create(Request::class.java)
        reminderApi = TodoApi(request)
    }

    companion object {
        val instance = Api()
    }
}