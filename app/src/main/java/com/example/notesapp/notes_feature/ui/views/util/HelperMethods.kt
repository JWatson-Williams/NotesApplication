package com.example.notesapp.notes_feature.ui.views.util

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

object HelperMethods {
    fun formatEpochMilliForLocalTime(time: String): String {
        val timeToLong = time.toLong()
        val instant = Instant.ofEpochMilli(timeToLong)
        val localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
        val formattedDateTime = DateTimeFormatter.ofPattern("MM'/'dd'/'yyyy 'at' h':'mma").format(localDateTime)
        return formattedDateTime
    }
}