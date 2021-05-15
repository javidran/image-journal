/*
 * Copyright (C) 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.javidran.imagejournal.model

import android.content.res.Resources
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

/* Handles operations on flowersLiveData and holds details about it. */
class DataSource(resources: Resources) {
    private val albumsLiveData = MutableLiveData<List<Album>>()

    /* Adds flower to liveData and posts value. */
    fun addAlbum(album: Album) {
        val currentList = albumsLiveData.value
        if (currentList == null) {
            albumsLiveData.postValue(listOf(album))
        } else {
            val updatedList = currentList.toMutableList()
            updatedList.add(0, album)
            albumsLiveData.postValue(updatedList)
        }
        Log.d("DataSource", "Album saved!")
    }

    /* Removes flower from liveData and posts value. */
    fun removeAlbum(album: Album) {
        val currentList = albumsLiveData.value
        if (currentList != null) {
            val updatedList = currentList.toMutableList()
            updatedList.remove(album)
            albumsLiveData.postValue(updatedList)
        }
    }

    /* Returns flower given an ID. */
    fun getAlbumForTitle(title: String): Album? {
        albumsLiveData.value?.let { albums ->
            return albums.firstOrNull{ it.title == title}
        }
        return null
    }

    fun getAlbumList(): LiveData<List<Album>> {
        return albumsLiveData
    }

//    /* Returns a random flower asset for flowers that are added. */
//    fun getRandomFlowerImageAsset(): Int? {
//        val randomNumber = (initialFlowerList.indices).random()
//        return initialFlowerList[randomNumber].image
//    }

    companion object {
        private var INSTANCE: DataSource? = null

        fun getDataSource(resources: Resources): DataSource {
            return synchronized(DataSource::class) {
                val newInstance = INSTANCE ?: DataSource(resources)
                INSTANCE = newInstance
                newInstance
            }
        }
    }
}