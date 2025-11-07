package com.example.notesapp.notes_feature.data.model

data class NoteModel(
    val noteId: Int? = null,
    val noteHeader: String?,
    val noteBody: String?,
    val dateCreated: Long,
    val dateModified: Long
)

