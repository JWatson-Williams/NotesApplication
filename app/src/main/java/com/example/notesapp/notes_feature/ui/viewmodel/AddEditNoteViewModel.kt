package com.example.notesapp.notes_feature.ui.viewmodel

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notesapp.notes_feature.data.room_db.NoteEntity
import com.example.notesapp.notes_feature.data.repository.NotesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.Instant

data class NoteUiState(
    val noteId: Int = 0,
    val dateCreated: String = Instant.now().toEpochMilli().toString(),
    val dateModified: String = Instant.now().toEpochMilli().toString(),
    val dateModifiedDisplayed: Boolean = true,
    val pushNoteUpdates: Boolean = false
)

class AddEditNoteViewModel (
    private val notesRepo: NotesRepository
) : ViewModel() {
    private val _noteState = MutableStateFlow(NoteUiState())
    val noteState = _noteState.asStateFlow()
    val noteHeaderState = TextFieldState()
    val noteBodyState = TextFieldState()

    fun createOrLoadNote(noteId: Int?) {
        if (noteId == null) {
            createNote()
        } else {
            loadNoteById(noteId)
        }
    }


    fun loadNoteById(noteId: Int) {
        viewModelScope.launch {
            try {
                val currentNote = notesRepo.getNoteById(noteId)

                currentNote.noteHeader?.let {
                    noteHeaderState.setTextAndPlaceCursorAtEnd(it)
                }
                currentNote.noteBody?.let {
                    noteBodyState.setTextAndPlaceCursorAtEnd( it )
                }
                _noteState.update { it ->
                    it.copy(
                        noteId = currentNote.noteId,
                        dateCreated = currentNote.dateCreated,
                        dateModified = currentNote.dateModified
                    )
                }

            } catch (e: Exception) {
                Timber.e(e, "Can't load note")
            }
        }
    }

    fun createNote() {
        noteHeaderState.setTextAndPlaceCursorAtEnd("")
        noteBodyState.setTextAndPlaceCursorAtEnd("")
        _noteState.update { it ->
            it.copy(
                dateCreated = Instant.now().toEpochMilli().toString(),
                dateModified = Instant.now().toEpochMilli().toString()
            )
        }
    }

    fun deleteOrUpdateNote(){
        if(noteHeaderState.text.isEmpty() && noteBodyState.text.isEmpty()) {
            deleteNote()
        } else {
            if (_noteState.value.pushNoteUpdates) {
                pushNoteUpdatesToDatabase()
            }
        }
    }

    fun pushNoteUpdatesToDatabase() {
        val noteValue =  _noteState.value

        viewModelScope.launch {
            try {
                notesRepo.addOrUpdateNote(
                    NoteEntity(
                        noteId = noteValue.noteId,
                        noteHeader = noteHeaderState.text.toString(),
                        noteBody = noteBodyState.text.toString(),
                        dateCreated = noteValue.dateCreated,
                        dateModified = noteValue.dateModified
                    )
                )
            } catch (e: Exception) {
                Timber.e(e, "Couldn't load note")
            }
        }
    }

    fun deleteNote() {
        val noteValue =  _noteState.value

        viewModelScope.launch {
            try {
                notesRepo.deleteNote(
                    NoteEntity(
                        noteId = noteValue.noteId,
                        noteHeader = noteHeaderState.text.toString(),
                        noteBody = noteBodyState.text.toString(),
                        dateCreated = noteValue.dateCreated,
                        dateModified = noteValue.dateModified
                    )
                )
            } catch (e: Exception) {
                Timber.e(e, "Couldn't delete note")
            }
        }
    }

    fun updateDateModified() {
        _noteState.update { it ->
            it.copy(
                dateModified = Instant.now().toEpochMilli().toString(),
                pushNoteUpdates = true
            )
        }
    }

    fun changeDateShown() {
        val isModifiedDateDisplayed = _noteState.value.dateModifiedDisplayed
        _noteState.update { it ->
            it.copy(
                dateModifiedDisplayed = !isModifiedDateDisplayed
            )
        }
    }
}