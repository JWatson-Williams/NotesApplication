package com.example.notesapp.notes_feature.di

import android.content.Context
import com.example.notesapp.notes_feature.data.repository.NotesRepository
import com.example.notesapp.notes_feature.data.room_db.NotesDao
import com.example.notesapp.notes_feature.data.room_db.NotesDatabase
import com.example.notesapp.notes_feature.domain.NotesRepositoryImpl

interface AppModule {
    val notesDatabase: NotesDatabase
    val notesDao: NotesDao
    val notesRepository: NotesRepository
}

class AppModuleImpl (
    private val appContext: Context
) : AppModule {

    override val notesDatabase: NotesDatabase by lazy {
        NotesDatabase.getDatabase(appContext)
    }

    override val notesDao: NotesDao by lazy {
        notesDatabase.NotesDao()
    }

    override val notesRepository: NotesRepository by lazy {
        NotesRepositoryImpl(notesDao)
    }
}