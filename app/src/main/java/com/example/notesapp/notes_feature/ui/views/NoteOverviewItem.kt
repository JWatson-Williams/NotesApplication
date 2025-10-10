package com.example.notesapp.notes_feature.ui.views

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.notesapp.notes_feature.data.room_db.NoteEntity
import com.example.notesapp.notes_feature.ui.views.util.Constants.NOTE_ITEM_PADDING
import com.example.notesapp.notes_feature.ui.views.util.HelperMethods.formatEpochMilliForLocalTime

@Composable
fun NoteOverviewItem(
    noteEntity: NoteEntity,
    loadNoteIntoVM: (Int) -> Unit
    ) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .height(IntrinsicSize.Min)
        .border(
            width = 2.dp,
            color = Color.Black,
            shape = RoundedCornerShape(5.dp))
        .padding(NOTE_ITEM_PADDING)
        .clickable(
            onClick = {
                noteEntity.noteId?.let {
                    loadNoteIntoVM(it)
                }
            }
        )
    ) {
        //Header
        noteEntity.noteHeader?.let {
            Text(
                it,
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
            )
        }

        //Date
        Text(
            text = formatEpochMilliForLocalTime(noteEntity.dateModified),
            fontSize = 12.sp,
            modifier = Modifier.padding(bottom = 5.dp)
        )

        //Body
        noteEntity.noteBody?.let {
            Text(
                it,
                maxLines = 10,
                overflow = TextOverflow.Ellipsis
            )
        }


        /*
          Row(){
              Column(modifier = Modifier.padding(end = 10.dp)) {
                  Text(
                      noteEntity.noteHeader,
                      style = MaterialTheme.typography.headlineLarge,
                      fontWeight = FontWeight.Bold,
                  )
                  Text(
                      formatInstantForLocalTime(noteEntity.dateModified),
                      fontSize = 12.sp
                  )
              }

              noteEntity.noteBody?.let {
                  Text(it)
              }
          }*/
    }
}