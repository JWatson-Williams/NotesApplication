package com.example.notesapp.notes_feature.ui.navigation

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.Composable
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

sealed class NavigationRoutes {
    @Serializable
    object MainScreen : NavigationRoutes()
    @Serializable
    data class NoteEditScreen(
        val noteId: Int?
    ): NavigationRoutes()
}