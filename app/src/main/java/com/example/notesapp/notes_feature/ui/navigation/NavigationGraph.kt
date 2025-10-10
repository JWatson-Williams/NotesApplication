package com.example.notesapp.notes_feature.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.notesapp.notes_feature.data.repository.NotesRepository
import com.example.notesapp.notes_feature.ui.viewmodel.AddEditNoteViewModel
import com.example.notesapp.notes_feature.ui.viewmodel.NotesOverviewViewModel
import com.example.notesapp.notes_feature.ui.viewmodel.viewModelFactory
import com.example.notesapp.notes_feature.ui.views.Note
import com.example.notesapp.notes_feature.ui.views.NotesOverview
import com.example.notesapp.notes_feature.ui.views.util.Constants
import timber.log.Timber

@Composable
fun NavigationGraph(notesRepository: NotesRepository) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = NavigationRoutes.MainScreen
    ) {
        composable<NavigationRoutes.MainScreen> {
            val notesOverviewVM: NotesOverviewViewModel = viewModel (
                factory = viewModelFactory {
                    NotesOverviewViewModel(notesRepository)
                }
            )
            val notesOverviewState = notesOverviewVM.overviewState.collectAsState()
            val noteList = notesOverviewState.value.notesList

            NotesOverview(
                notes = noteList,
                loadNoteIntoVM = { noteId ->

                    navController.navigate(
                        route = NavigationRoutes.NoteEditScreen(
                            noteId
                        )
                    )
                },
                createNewNote = {
                    navController.navigate(
                        route = NavigationRoutes.NoteEditScreen(
                            null
                        )
                    )
                }
            )
        }
        composable<NavigationRoutes.NoteEditScreen> { backstackEntry ->
            val backstackArgs: NavigationRoutes.NoteEditScreen = backstackEntry.toRoute()
            val addEditNoteVM: AddEditNoteViewModel = viewModel(
                factory = viewModelFactory {
                    AddEditNoteViewModel(notesRepository)
                }
            )
            val addEditNoteState = addEditNoteVM.noteState.collectAsState()
            val noteId = backstackArgs.noteId

            LaunchedEffect(noteId) {
                Timber.v("loading noteId: $noteId")
                addEditNoteVM.createOrLoadNote(noteId)
            }

            Note(
                headerState = addEditNoteVM.noteHeaderState,
                bodyState = addEditNoteVM.noteBodyState,
                headerPlaceholder = Constants.headerPlaceHolder,
                bodyPlaceholder = Constants.bodyPlaceHolder,
                dateCreated = addEditNoteState.value.dateCreated,
                dateModified = addEditNoteState.value.dateModified,
                displayDateModified = addEditNoteState.value.dateModifiedDisplayed,
                onBackPress = {
                    addEditNoteVM.deleteOrUpdateNote()
                    navController.popBackStack()
                },
                onTextChanged = {
                    addEditNoteVM.updateDateModified()
                },
                changeDateShown = {
                    addEditNoteVM.changeDateShown()
                }
            )
        }
    }
}