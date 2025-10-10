package com.example.notesapp.notes_feature.domain

import com.example.notesapp.notes_feature.data.repository.NotesRepository
import com.example.notesapp.notes_feature.data.room_db.NoteEntity
import com.example.notesapp.notes_feature.data.room_db.NotesDao
import kotlinx.coroutines.flow.Flow


class NotesRepositoryImpl (
    private val dao: NotesDao
) : NotesRepository {
    override suspend fun addOrUpdateNote(note: NoteEntity): Long {
        return dao.upsertNote(note)
    }

    override fun getALlNotes(): Flow<List<NoteEntity>> {
        return dao.getALlNotes()
    }

    override suspend fun getNoteById(noteId: Int): NoteEntity {
        return dao.getNoteById(noteId)
    }

    override suspend fun deleteNote(note: NoteEntity): Int {
        return dao.deleteNote(note)
    }
}