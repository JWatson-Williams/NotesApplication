package com.example.notesapp.notes_feature.data.room_db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface NotesDao {
    @Query("SELECT * FROM notes ORDER BY date_modified DESC")
    fun getALlNotes(): Flow<List<NoteEntity>>

    @Query("SELECT * FROM notes WHERE note_id LIKE :noteId")
    suspend fun getNoteById(noteId: Int): NoteEntity

    @Upsert
    suspend fun upsertNote(note: NoteEntity): Long

    @Delete
    suspend fun deleteNote(note: NoteEntity): Int
}