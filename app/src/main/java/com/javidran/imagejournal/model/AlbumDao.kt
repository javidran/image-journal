package com.javidran.imagejournal.model

import androidx.room.*

@Dao
interface AlbumDao {

    @Query("SELECT * FROM Album")
    fun getAll(): List<Album>

    @Query("SELECT * FROM Album WHERE title LIKE :title")
    fun getAnAlbum(title: String): Album

    @Transaction
    @Query("SELECT * FROM Album")
    fun getAlbumWithEntries(): List<AlbumWithEntries>

    @Insert
    fun insertAll(vararg albums: Album)

    @Delete
    fun delete(album: Album)


}