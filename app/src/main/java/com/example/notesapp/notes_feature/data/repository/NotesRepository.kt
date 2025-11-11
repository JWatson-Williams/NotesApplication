package com.example.notesapp.notes_feature.data.repository

import com.example.notesapp.notes_feature.data.room_db.NoteEntity
import kotlinx.coroutines.flow.Flow

interface NotesRepository {

    suspend fun addOrUpdateNote(note: NoteEntity): Long

    fun getALlNotes(): Flow<List<NoteEntity>>

    suspend fun getNoteById(noteId: Int): NoteEntity

    suspend fun deleteNote(note: NoteEntity): Int

    suspend fun synchronizeNotes(dateLastModified: String)

    suspend fun pushUnsyncedNotesToFirebase()
}