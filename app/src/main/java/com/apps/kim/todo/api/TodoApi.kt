package com.apps.kim.todo.api

import io.reactivex.Observable

/**
Created by KIM on 25.09.2019
 **/

class TodoApi(private val mRequest: Request) {

    //GET
    fun getQuote(): Observable<Quote> = mRequest.getQuote()
}