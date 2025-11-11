package com.example.notesapp.notes_feature.data.network

import com.example.notesapp.notes_feature.data.room_db.NoteEntity
import kotlinx.serialization.Serializable


@Serializable
data class NetworkNoteModel(
   val noteId: Int = 0,
   val noteHeader: String? = "",
   val noteBody: String? = "",
   val dateCreated: String = "",
   val dateModified: String = "",
   val isSynced: Boolean = false
)

fun NetworkNoteModel.asEntity() = NoteEntity (
    noteId = noteId,
    noteHeader = noteHeader,
    noteBody = noteBody,
    dateCreated = dateCreated,
    dateModified = dateModified,
    isSynced = isSynced
)