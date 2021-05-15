package com.javidran.imagejournal.model

import android.util.Log
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Album(@PrimaryKey val title: String, val emoji: String)