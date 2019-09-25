package com.apps.kim.todo.api

/**
Created by KIM on 25.09.2019
 **/

data class Quote(
    val contents: Contents,
    val success: Success
)

data class Success(
    val total: Int
)

data class Contents(
    val copyright: String,
    val quotes: List<Quotes>
)

data class Quotes(
    val author: String,
    val background: String,
    val category: String,
    val date: String,
    val id: String,
    val length: String,
    val permalink: String,
    val quote: String,
    val tags: List<String>,
    val title: String
)