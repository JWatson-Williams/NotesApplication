package com.example.notesapp.notes_feature.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notesapp.notes_feature.data.repository.NotesRepository
import com.example.notesapp.notes_feature.data.room_db.NoteEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber

data class NoteOverviewViewUiState(
    val notesList: List<NoteEntity> = emptyList(),
    val isRefreshing: Boolean = false
)

class NotesOverviewViewModel (
    val notesRepository: NotesRepository
) : ViewModel() {
    private var _overviewState = MutableStateFlow(NoteOverviewViewUiState())
    val overviewState = _overviewState.asStateFlow()

    init {
        viewModelScope.launch {
            Timber.d("Getting notes")
            launch {
                notesRepository.getALlNotes().collect { notes ->
                    _overviewState.update { it ->
                        it.copy(
                            notesList = notes
                        )
                    }
                }
            }
        }
    }

    fun synchronizeNotes(){
        viewModelScope.launch {
            try {
                updateIsRefreshing(true)
                val dateModified = _overviewState.value.notesList.elementAtOrNull(0)?.dateModified
                notesRepository.synchronizeNotes(dateModified.toString())
            } catch (error: Exception) {
                Timber.e(error, "Couldn't synchronize notes")
            } finally {
                updateIsRefreshing(false)
            }
        }
    }

    fun deleteNote(note: NoteEntity) {
        viewModelScope.launch {
            notesRepository.deleteNote(note)
        }
    }

    private fun updateIsRefreshing(isRefreshing: Boolean) {
        _overviewState.update {
            it.copy(
                isRefreshing = isRefreshing
            )
        }
    }
}