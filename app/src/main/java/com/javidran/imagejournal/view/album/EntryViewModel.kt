package com.javidran.imagejournal.view.album

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.javidran.imagejournal.model.Album
import com.javidran.imagejournal.model.DataSource
import com.javidran.imagejournal.model.Entry
import java.util.*
import kotlin.random.Random

class EntryViewModel(val dataSource: DataSource, val album: Album) : ViewModel() {

    val entriesLiveData = dataSource.getEntriesFromAlbum(album.title)

    fun insertEntry(imagePath: String) {
        val entry = Entry(
            Random.nextLong(),
            getNextNumber(),
            imagePath,
            Date(),
            album.title
        )

        dataSource.addImageToAlbum(entry)
    }

    fun getNextNumber(): Int {
        return dataSource.getNextNumberForAlbum(album.title)
    }
}

class EntryViewModelFactory(private val context: Context, private val album: Album) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EntryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return EntryViewModel(
                dataSource = DataSource.getDataSource(context),
                album
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}