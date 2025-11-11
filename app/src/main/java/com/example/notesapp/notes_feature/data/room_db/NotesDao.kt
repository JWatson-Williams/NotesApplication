package com.example.notesapp.notes_feature.data.room_db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.IGNORE
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface NotesDao {
    @Query("SELECT * FROM notes ORDER BY date_modified DESC")
    fun getALlNotes(): Flow<List<NoteEntity>>

    @Query("SELECT * FROM notes WHERE note_id = :noteId")
    suspend fun getNoteById(noteId: Int): NoteEntity

    @Upsert
    suspend fun upsertNote(note: NoteEntity): Long

    @Update(onConflict = REPLACE)
    suspend fun updateNote(note: NoteEntity)

    @Delete
    suspend fun deleteNote(note: NoteEntity): Int

    @Insert(onConflict = IGNORE)
    suspend fun insertNote(note: NoteEntity): Long

    @Query(
        "UPDATE notes " +
            "SET note_header = :noteHeader, note_body = :noteBody, date_modified = :dateModified, is_synced = :isSynced " +
            "WHERE note_id = :noteId AND :dateModified > date_modified"
    )
    suspend fun updateNoteIfNewer(noteId: Int?, noteHeader: String?, noteBody: String?, dateModified: String?, isSynced: Boolean): Int

    @Query("SELECT * FROM notes WHERE is_synced = 0")
    suspend fun getUnsyncedNotes(): List<NoteEntity>
}