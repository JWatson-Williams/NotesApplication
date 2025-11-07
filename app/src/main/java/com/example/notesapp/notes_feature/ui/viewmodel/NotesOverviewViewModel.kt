package com.example.notesapp.notes_feature.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notesapp.notes_feature.data.repository.NotesRepository
import com.example.notesapp.notes_feature.data.room_db.NoteEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class NoteOverviewViewUiState(
    val notesList: List<NoteEntity> = emptyList()
)

class NotesOverviewViewModel (
    val notesRepository: NotesRepository
) : ViewModel() {
    private var _overviewState = MutableStateFlow(NoteOverviewViewUiState())
    val overviewState = _overviewState.asStateFlow()

    init {
        viewModelScope.launch {
            notesRepository.getALlNotes().collect { notes ->
                _overviewState.update { it ->
                    it.copy(
                        notesList = notes
                    )
                }
            }
            notesRepository.synchronizeNotes(_overviewState.value.notesList.elementAt(0).dateModified.toString())
        }
    }

    fun deleteNote(note: NoteEntity) {
        viewModelScope.launch {
            notesRepository.deleteNote(note)
        }
    }
}