package com.example.notesapp.notes_feature.ui.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.notesapp.notes_feature.data.room_db.NoteEntity
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.example.notesapp.R
import com.example.notesapp.notes_feature.ui.views.util.Constants.LAZY_COLUMN_PADDING

@Composable
fun NotesOverview(
    notes: List<NoteEntity>,
    loadNoteIntoVM: (Int) -> Unit,
    createNewNote: () -> Unit,
    deleteNote: (NoteEntity) -> Unit,
    isRefreshing: Boolean,
    onRefresh: () -> Unit
) {
    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = onRefresh
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(LAZY_COLUMN_PADDING)
                .windowInsetsPadding(WindowInsets.displayCutout),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            items(notes) { notes ->
                NoteOverviewItem(
                    noteId = notes.noteId,
                    noteHeader = notes.noteHeader,
                    noteBody = notes.noteBody,
                    dateModified = notes.dateModified,
                    loadNoteIntoVM = loadNoteIntoVM,
                    deleteNote = {
                        deleteNote(notes)
                    }
                )
            }
        }

        FloatingActionButton(
            onClick = createNewNote,
            shape = CircleShape,
            containerColor = Color.Black,
            contentColor = Color.White,
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.BottomCenter)
                .size(75.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.rounded_add_24),
                contentDescription = "Plus Button",
                modifier = Modifier.size(50.dp)
            )
        }
    }
}