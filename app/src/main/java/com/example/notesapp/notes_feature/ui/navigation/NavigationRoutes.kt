package com.example.notesapp.notes_feature.ui.navigation

import kotlinx.serialization.Serializable

sealed class NavigationRoutes {
    @Serializable
    object MainScreen : NavigationRoutes()
    @Serializable
    data class NoteEditScreen(
        val noteId: Int?
    ): NavigationRoutes()
}