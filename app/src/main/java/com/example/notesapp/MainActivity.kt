package com.example.notesapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.lifecycleScope
import com.example.notesapp.NotesApplication.Companion.appModule
import com.example.notesapp.notes_feature.data.room_db.NoteEntity
import com.example.notesapp.notes_feature.ui.navigation.NavigationGraph
import com.example.notesapp.notes_feature.ui.views.util.Constants.LOREN_IPSUM
import com.example.notesapp.ui.theme.NotesAppTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.Instant

//@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val notesRepo = appModule.notesRepository

        val note1 = NoteEntity(
            1,
            "Hello",
            LOREN_IPSUM,
            Instant.now().toEpochMilli().toString(),
            Instant.now().toEpochMilli().toString(),
            isSynced = false
        )
        val note2 = NoteEntity(
            2,
            "Goodbye",
            LOREN_IPSUM,
            Instant.now().toEpochMilli().toString(),
            Instant.now().toEpochMilli().toString(),
            isSynced = false
        )
        val note3 = NoteEntity(
            3,
            "I'm here",
            LOREN_IPSUM,
            Instant.now().toEpochMilli().toString(),
            Instant.now().toEpochMilli().toString(),
            isSynced = false
        )

        lifecycleScope.launch(Dispatchers.Default) {
            notesRepo.synchronizeNotes("")
        }

        enableEdgeToEdge()
        setContent {
            NotesAppTheme {
                NavigationGraph(notesRepo)
            }
        }
    }
}