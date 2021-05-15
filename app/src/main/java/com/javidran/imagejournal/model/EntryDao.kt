package com.javidran.imagejournal.model

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface EntryDao {

    @Query("SELECT * FROM Entry")
    fun getAll(): List<Entry>

    @Insert
    fun insertAll(vararg entries: Entry)

    @Delete
    fun delete(entry: Entry)

}