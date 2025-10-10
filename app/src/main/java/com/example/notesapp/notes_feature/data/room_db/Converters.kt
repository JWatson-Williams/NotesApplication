package com.example.notesapp.notes_feature.data.room_db

import androidx.room.TypeConverter
import java.time.Instant

class Converters {
    @TypeConverter
    fun fromTimestamp(timestamp: String?): Instant? {
        return timestamp?.let { Instant.parse(it) }
    }

    @TypeConverter
    fun instantToTimestamp(instant: Instant?): String? {
        return instant?.toString()
    }
}