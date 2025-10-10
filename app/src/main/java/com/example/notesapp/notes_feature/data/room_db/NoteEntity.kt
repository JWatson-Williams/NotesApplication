package com.example.notesapp.notes_feature.data.room_db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity("notes")
data class NoteEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo("note_id") val noteId: Int? = null,
    @ColumnInfo("note_header") val noteHeader: String?,
    @ColumnInfo("note_body") val noteBody: String?,
    @ColumnInfo("date_created") val dateCreated: Long,
    @ColumnInfo("date_modified") val dateModified: Long
)
