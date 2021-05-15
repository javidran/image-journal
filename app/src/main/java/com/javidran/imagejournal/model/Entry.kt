package com.javidran.imagejournal.model

import android.location.Location
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Entry(
    @PrimaryKey val imageId: Long,
    val number: Int,
    val imagePath:String,
    val imageDate: Date,
    val location: Location,
    val albumTitle: Album)