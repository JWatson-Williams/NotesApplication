package com.example.notesapp

import android.app.Application
import androidx.compose.ui.text.googlefonts.GoogleFont
import com.example.notesapp.notes_feature.di.AppModule
import com.example.notesapp.notes_feature.di.AppModuleImpl
import timber.log.Timber

class NotesApplication : Application() {

    companion object {
        lateinit var appModule: AppModule
        val provider = GoogleFont.Provider(
            providerAuthority = "com.google.android.gms.fonts",
            providerPackage = "com.google.android.gms",
            certificates = R.array.com_google_android_gms_fonts_certs
        )
    }

    override fun onCreate() {
        super.onCreate()
        appModule = AppModuleImpl(this)
        Timber.plant(Timber.DebugTree())
    }
}