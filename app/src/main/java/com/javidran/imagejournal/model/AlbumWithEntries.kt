package com.javidran.imagejournal.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Relation

data class AlbumWithEntries (
    @Embedded val album: Album,
    @Relation(
        parentColumn = "title",
        entityColumn = "albumTitle"
    )
    val entries: List<Entry>
)