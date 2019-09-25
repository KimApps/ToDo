package com.apps.kim.todo.photopicker

/**
Created by KIM on 25.09.2019
 **/

interface IDoBackGround {
    fun onCompleted()
    fun onDoBackGround(z: Boolean)
}

interface IHandler {
    fun doWork()
}

interface OnAlbum {
    fun OnItemAlbumClick(i: Int)
}

interface OnListAlbum {
    fun OnItemListAlbumClick(item: ImageModel)
}