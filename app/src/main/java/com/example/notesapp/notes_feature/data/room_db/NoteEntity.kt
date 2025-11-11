package com.example.notesapp.notes_feature.data.room_db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.notesapp.notes_feature.data.model.NoteModel
import com.example.notesapp.notes_feature.data.network.NetworkNoteModel


@Entity("notes")
data class NoteEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo("note_id") val noteId: Int = 0,
    @ColumnInfo("note_header") val noteHeader: String? = "",
    @ColumnInfo("note_body") val noteBody: String? = "",
    @ColumnInfo("date_created") val dateCreated: String = "",
    @ColumnInfo("date_modified") val dateModified: String = "",
    @ColumnInfo("is_synced") val isSynced: Boolean = false
)

//fun NoteEntity.asNetworkModel() = NetworkNoteModel (
//    noteId = noteId,
//    noteHeader = noteHeader,
//    noteBody = noteBody,
//    dateCreated = dateCreated.toDouble(),
//    dateModified = dateModified.toDouble(),
//    isSynced = isSynced
//)
