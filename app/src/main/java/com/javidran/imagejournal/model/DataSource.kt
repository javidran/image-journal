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

import android.content.Context
import android.content.res.Resources
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/* Handles operations on flowersLiveData and holds details about it. */
class DataSource(val context: Context) {
    private val albumsLiveData = MutableLiveData(getAlbum())
    private val entriesListLiveData = getAllEntries()

    fun getAlbum() : List<Album> {
        return AppDatabase.getInstance(context).albumDao().getAll()
    }

    /* Adds flower to liveData and posts value. */
    fun addAlbum(album: Album) {
        val currentList = albumsLiveData.value
        if (currentList == null) {
            albumsLiveData.postValue(listOf(album))
            AppDatabase.getInstance(context).albumDao().insertAll(album)
        } else {
            val updatedList = currentList.toMutableList()
            updatedList.add(0, album)
            albumsLiveData.postValue(updatedList)
            AppDatabase.getInstance(context).albumDao().insertAll(album)
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
            AppDatabase.getInstance(context).albumDao().delete(album)
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

    fun addImageToAlbum(entry: Entry) {


    }

    fun getNextNumberForAlbum(albumTitle: String): Int {
        return getEntriesFromAlbum(albumTitle).value!!.size + 1
    }

    fun getEntriesFromAlbum(albumTitle: String) : LiveData<List<Entry>> {
        return entriesListLiveData[albumTitle]!!
    }

    fun getAllEntries() : HashMap<String, MutableLiveData<List<Entry>>> {
        val map = HashMap<String, MutableLiveData<List<Entry>>>()
        val list = AppDatabase.getInstance(context).albumDao().getAlbumWithEntries()
        list.forEach {
            map.set(it.album.title, MutableLiveData(it.entries))
        }
        return map
    }

//    /* Returns a random flower asset for flowers that are added. */
//    fun getRandomFlowerImageAsset(): Int? {
//        val randomNumber = (initialFlowerList.indices).random()
//        return initialFlowerList[randomNumber].image
//    }

    companion object {
        private var INSTANCE: DataSource? = null

        fun getDataSource(context: Context): DataSource {
            return synchronized(DataSource::class) {
                val newInstance = INSTANCE ?: DataSource(context)
                INSTANCE = newInstance
                newInstance
            }
        }
    }
}