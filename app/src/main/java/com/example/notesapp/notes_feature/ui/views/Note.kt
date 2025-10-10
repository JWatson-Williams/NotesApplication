package com.example.notesapp.notes_feature.ui.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.notesapp.R
import com.example.notesapp.notes_feature.ui.views.util.Constants.bodyTextColors
import com.example.notesapp.notes_feature.ui.views.util.Constants.BODY_TEXT_STYLE
import com.example.notesapp.notes_feature.ui.views.util.Constants.headerColors
import com.example.notesapp.notes_feature.ui.views.util.Constants.HEADER_TEXT_STYLE
import com.example.notesapp.notes_feature.ui.views.util.Constants.iconButtonColors
import com.example.notesapp.notes_feature.ui.views.util.HelperMethods.formatEpochMilliForLocalTime
import kotlinx.coroutines.flow.drop

@Composable
fun Note(
    headerState: TextFieldState,
    bodyState: TextFieldState,
    headerPlaceholder: @Composable () -> Unit,
    bodyPlaceholder: @Composable () -> Unit,
    dateCreated: Long,
    dateModified: Long,
    displayDateModified: Boolean,
    onBackPress: () -> Unit,
    onTextChanged: () -> Unit,
    changeDateShown: () -> Unit
) {
    LaunchedEffect(Unit) {
        snapshotFlow { headerState.text.toString() to bodyState.text.toString() }
            .drop(2) // Skip initial value
            .collect {
                onTextChanged()
            }
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .windowInsetsPadding(WindowInsets.displayCutout)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())

    ) {
        //Time
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp),
            contentAlignment = Alignment.Center,
        ) {
            FilledIconButton(
                onClick = onBackPress,
                modifier = Modifier.align(Alignment.TopStart),
                colors = iconButtonColors()
            ) {
                Icon(
                    painter = painterResource(R.drawable.rounded_arrow_back_24),
                    contentDescription = "Back Button"
                )
            }
            DateDisplay(
                displayDateModified = displayDateModified,
                dateModified = dateModified,
                dateCreated = dateCreated,
                onClick =  changeDateShown
            )
        }

        //Header
        TextField(
            modifier = Modifier.fillMaxWidth(),
            textStyle = HEADER_TEXT_STYLE,
            state = headerState,
            placeholder = headerPlaceholder,
            colors = headerColors(),
            contentPadding = PaddingValues.Zero
        )

        //Body
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            textStyle = BODY_TEXT_STYLE,
            state = bodyState,
            placeholder = bodyPlaceholder,
            colors = bodyTextColors(),
            contentPadding = PaddingValues.Zero
        )
    }
}

@Composable
fun DateDisplay(displayDateModified: Boolean, dateModified: Long, dateCreated: Long, onClick: () -> Unit) {
    Text(
        text =
            if (displayDateModified) {
                "Modified: ${formatEpochMilliForLocalTime(dateModified)}"
            } else {
                "Created: ${formatEpochMilliForLocalTime(dateCreated)}"
            },
            modifier = Modifier.clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick = onClick
            )
    )
}