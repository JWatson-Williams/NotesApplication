package com.example.notesapp.notes_feature.domain

import com.example.notesapp.notes_feature.data.network.NetworkNoteModel
import com.example.notesapp.notes_feature.data.network.asEntity
import com.example.notesapp.notes_feature.data.repository.NotesRepository
import com.example.notesapp.notes_feature.data.room_db.NoteEntity
import com.example.notesapp.notes_feature.data.room_db.NotesDao
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.getValue
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import timber.log.Timber


class NotesRepositoryImpl (
    private val dao: NotesDao,
    private val firebaseNotesList: DatabaseReference
) : NotesRepository {
    override suspend fun addOrUpdateNote(note: NoteEntity): Long {
        var rowId: Long
        if(note.noteId == 0) {
            rowId = dao.insertNote(note)
        } else {
            rowId = dao.upsertNote(note)
        }
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
        Timber.d("Synchronizing notes")

        val modifiedNotesQuery = firebaseNotesList.orderByChild("date_modified").startAt(dateLastModified)
        //modifiedNotesQuery
            firebaseNotesList
            .get()
            .addOnSuccessListener { modifiedNotes ->
                val noteUpdates = modifiedNotes.getValue<HashMap<String, NetworkNoteModel>>()

                //TODO: SEE IF I CAN CHANGE THIS BACK TO LAUNCH
                runBlocking {
                    Timber.d("NOTE UPDATES: %s", noteUpdates)

                    if (!noteUpdates.isNullOrEmpty())
                    {
                        Timber.d("List is not empty")
                        if(noteUpdates.size > 1) {
                            noteUpdates.forEach { unconvertedNote ->
                                if (unconvertedNote != null && unconvertedNote.value.noteId != 0) {
                                    val note = unconvertedNote.value.asEntity()

                                    //Try to insert the note into the room database (assumes this is a new note)
                                    val rowId = dao.insertNote(note)

                                    //If insert didn't work, note already exists on device. Only update the note if the updates are newer than what's on the device
                                    if (rowId == (-1).toLong()) {
                                        dao.updateNoteIfNewer(
                                            noteId = note.noteId,
                                            noteHeader = note.noteHeader,
                                            noteBody = note.noteBody,
                                            dateModified = note.dateModified,
                                            isSynced = note.isSynced
                                        )
                                    }
                                }
                            }
                        }
                    }

                    pushUnsyncedNotesToFirebase()
                }
            }
            .addOnFailureListener { errorBody ->
                Timber.e("Couldn't get notes from database: ${errorBody.message}")
            }
            .await()

        Unit
    }

    override suspend fun pushUnsyncedNotesToFirebase() {
        Timber.d("Pushing unsynced notes to database")
        val unsyncedNotes = dao.getUnsyncedNotes()
        unsyncedNotes.forEach { note ->
            Timber.d("%s, IS SYNCED: %s", note.noteId ,note.isSynced)
            setFirebaseNoteValue(createSyncedNote(note))
        }
    }

    private suspend fun setFirebaseNoteValue(note: NoteEntity) = coroutineScope {
        firebaseNotesList.child(note.noteId.toString())
            .setValue(note)
            .addOnSuccessListener {
                Timber.d("NoteId %s successfully updated on firebase", note.noteId)
                if (note.noteId != 0) {
                    //TODO: SEE IF I CAN CHANGE THIS BACK TO LAUNCH
                    runBlocking {
                        dao.updateNote(
                            createSyncedNote(note)
                        )
                        Timber.d("NoteId %s: updated: %s", note.noteId, note.isSynced)
                    }
                }
            }
            .addOnFailureListener {
                Timber.d("NoteId %s could not updated on firebase", note.noteId)
            }
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