package com.javidran.imagejournal.model

import android.util.Log

data class Album(val id: Int, val title: String, val emoji: String) {

    init {

    }

//    constructor(val id: Int, val title: String, val emoji: String, val entry: ArrayList<Entry>) {
//        Log.d(Album::class.simpleName, "Album con entry inicializado")
//    }

}