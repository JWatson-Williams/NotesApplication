package com.example.notesapp.notes_feature.domain

import com.example.notesapp.notes_feature.data.repository.NotesRepository
import com.example.notesapp.notes_feature.data.room_db.NoteEntity
import com.example.notesapp.notes_feature.data.room_db.NotesDao
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.getValue
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import timber.log.Timber


class NotesRepositoryImpl (
    private val dao: NotesDao,
    private val firebaseNotesList: DatabaseReference
) : NotesRepository {
    override suspend fun addOrUpdateNote(note: NoteEntity): Long {
        val rowId = dao.upsertNote(note)
        if (rowId == (-1).toLong()) {
            Timber.d("Note Id \"%s\" successfully updated in room", note.noteId)
        } else {
            Timber.d("Note Id \"%s\" successfully inserted in room", note.noteId)
        }

        val firebaseNote = createSyncedNote(note)
        setFirebaseNoteValue(firebaseNote)

        return rowId
    }

    override fun getALlNotes(): Flow<List<NoteEntity>> {
        return dao.getALlNotes()
    }

    override suspend fun getNoteById(noteId: Int): NoteEntity {
        return dao.getNoteById(noteId)
    }

    override suspend fun deleteNote(note: NoteEntity): Int {
        val numberOfNotesDeleted = dao.deleteNote(note)
        if (numberOfNotesDeleted != 0)
        {
            Timber.d("Note Id \"%s\" successfully deleted from room", note.noteId)

            firebaseNotesList.child(note.noteId.toString()).removeValue()
                .addOnSuccessListener {
                    Timber.d("NoteId %s successfully deleted from firebase", note.noteId)
                }
                .addOnFailureListener {
                    Timber.d("NoteId %s could not deleted from firebase", note.noteId)
                }
        }
        return numberOfNotesDeleted
    }

    override suspend fun synchronizeNotes(dateLastModified: String) = coroutineScope {
        val modifiedNotesQuery = firebaseNotesList.orderByChild("date_modified").startAt(dateLastModified)

        //Get the notes from firebase that were modified after the most recent note in the local database.
        modifiedNotesQuery
            .get()
            .addOnSuccessListener { modifiedNotes ->
                val noteUpdates = modifiedNotes.getValue<List<NoteEntity>>()

                if (!noteUpdates.isNullOrEmpty())
                {
                    if(noteUpdates.size > 1) {
                        noteUpdates.forEach { note ->
                            launch {
                                //Try to insert the note into the room database (assumes this is a new note)
                                val rowId = dao.insertNote(note)

                                //If insert didn't work, note already exists
                                if (rowId == (-1).toLong()) {
                                    dao.updateNoteIfNewer(
                                        noteId = note.noteId,
                                        noteHeader = note.noteHeader,
                                        noteBody = note.noteBody,
                                        dateModified = note.dateModified,
                                        isSynced = note.isSynced
                                    )
                                }

                                //If update didn't work, the changes on the device are newer than the ones in the database. Push those changes
                                val unsyncedNotes = dao.getUnsyncedNotes()
                                unsyncedNotes.forEach { note ->
                                    setFirebaseNoteValue(note)
                                }
                            }
                        }
                    }
                }
            }
            .addOnFailureListener { errorBody ->
                Timber.e("Couldn't get notes from database: ${errorBody.message}")
            }
            .await()

        Unit
    }

    private suspend fun setFirebaseNoteValue(note: NoteEntity) = coroutineScope {
        firebaseNotesList.child(note.noteId.toString())
            .setValue(note)
            .addOnSuccessListener {
                Timber.d("NoteId %s successfully updated on firebase", note.noteId)
                launch {
                    dao.upsertNote(
                        createSyncedNote(note)
                    )
                }
            }
            .addOnFailureListener {
                Timber.d("NoteId %s could not updated on firebase", note.noteId)
            }

        Unit
    }

    private fun createSyncedNote(note: NoteEntity): NoteEntity {
        return NoteEntity(
            noteId = note.noteId,
            noteHeader = note.noteHeader,
            noteBody = note.noteBody,
            dateCreated = note.dateCreated,
            dateModified = note.dateModified,
            isSynced = true
        )
    }

    //TODO: CREATE DELETE LISTENER SO WHEN A NOTE GETS DELETED ON THE DATABASE, IT REFLECTS IN ROOM
}