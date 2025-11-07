package com.example.notesapp.notes_feature.di

import android.content.Context
import com.example.notesapp.notes_feature.data.repository.NotesRepository
import com.example.notesapp.notes_feature.data.room_db.NotesDao
import com.example.notesapp.notes_feature.data.room_db.NotesDatabase
import com.example.notesapp.notes_feature.domain.NotesRepositoryImpl
import com.google.firebase.Firebase
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database

interface AppModule {
    val notesDatabase: NotesDatabase
    val notesDao: NotesDao
    val notesRepository: NotesRepository
    var firebaseDatabase: DatabaseReference
    var firebaseNotesList: DatabaseReference
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

    override var firebaseDatabase: DatabaseReference = Firebase.database.reference
    override var firebaseNotesList: DatabaseReference = firebaseDatabase.child("notesList")

    override val notesRepository: NotesRepository by lazy {
        NotesRepositoryImpl(notesDao, firebaseNotesList)
    }
}