package com.example.notesapp.notes_feature.ui.views.util

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

object HelperMethods {
    fun formatEpochMilliForLocalTime(time: Long): String {
        val instant = Instant.ofEpochMilli(time)
        val localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
        val formattedDateTime = DateTimeFormatter.ofPattern("MM'/'dd'/'yyyy 'at' h':'mma").format(localDateTime)
        return formattedDateTime
    }
}