package com.example.notesapp.notes_feature.data.room_db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.notesapp.notes_feature.data.model.NoteModel


@Entity("notes")
data class NoteEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo("note_id") val noteId: Int = 0,
    @ColumnInfo("note_header") val noteHeader: String?,
    @ColumnInfo("note_body") val noteBody: String?,
    @ColumnInfo("date_created") val dateCreated: Long,
    @ColumnInfo("date_modified") val dateModified: Long,
    @ColumnInfo("is_synced") val isSynced: Boolean = false
) {
    fun NoteEntity.asExternalModel() = NoteModel (
        noteId = noteId,
        noteHeader = noteHeader,
        noteBody = noteBody,
        dateCreated = dateCreated,
        dateModified = dateModified
    )
}
