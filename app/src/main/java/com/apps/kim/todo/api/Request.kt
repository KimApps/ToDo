package com.apps.kim.todo.api

import io.reactivex.Observable
import retrofit2.http.GET

/**
Created by KIM on 25.09.2019
 **/

interface Request {
    @GET("qod.json")
    fun getQuote(): Observable<Quote>
}