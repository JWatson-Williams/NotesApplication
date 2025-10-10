package com.example.notesapp.notes_feature.ui.views.util

import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.notesapp.NotesApplication.Companion.provider

object Constants {
    val NOTE_ITEM_PADDING = 10.dp
    val LAZY_COLUMN_PADDING = 10.dp
    const val LOREN_IPSUM =
        "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed sagittis felis placerat augue pretium laoreet. Sed ac risus non ex tincidunt feugiat sit amet quis tortor. Phasellus ac dui rutrum, ultricies odio quis, pharetra urna. Donec vitae tristique massa. Sed porta placerat diam malesuada ullamcorper. Etiam faucibus sodales arcu, id dictum ante molestie eget. Donec interdum tincidunt sapien. Suspendisse aliquam quam vulputate, accumsan lacus sit amet, ultricies orci. Sed vel tellus non tortor accumsan tristique. Donec eget placerat magna. Nullam sagittis enim dui, at luctus neque scelerisque quis. Ut aliquam lacus nulla, vitae egestas nunc dictum quis. Sed dapibus lacinia facilisis. Morbi tempor, magna at ultricies dignissim, est diam dignissim neque, quis tincidunt neque tellus vitae massa. In risus felis, dictum non tellus at, tristique mollis erat. Maecenas eu odio sollicitudin mauris eleifend fermentum ut at enim. "

    val FONT_NAME = GoogleFont("Inter")
    val HEADER_FONT_SIZE = 35.sp
    val HEADER_FONT_FAMILY = FontFamily(
        Font(
            googleFont = FONT_NAME,
            fontProvider = provider,
            weight = FontWeight.Bold
        )
    )
    val HEADER_TEXT_STYLE = TextStyle(
        fontSize = HEADER_FONT_SIZE,
        fontFamily = HEADER_FONT_FAMILY
    )

    @Composable
    fun headerColors() = TextFieldDefaults.colors(
        focusedContainerColor = Color.Transparent,
        unfocusedContainerColor = Color.Transparent,
        focusedIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent
    )

    val BODY_FONT_SIZE = 16.sp
    val BODY_FONT_FAMILY = FontFamily(
        Font(
            googleFont = FONT_NAME,
            fontProvider = provider,
            weight = FontWeight.SemiBold
        )
    )
    val BODY_TEXT_STYLE = TextStyle(
        fontSize = BODY_FONT_SIZE,
        fontFamily = BODY_FONT_FAMILY
    )

    @Composable
    fun bodyTextColors() = TextFieldDefaults.colors(
        focusedContainerColor = Color.Transparent,
        unfocusedContainerColor = Color.Transparent,
        focusedIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent
    )

    val headerPlaceHolder: @Composable () -> Unit = {
        Text(
            text = "Header",
            fontFamily = HEADER_FONT_FAMILY,
            fontSize = HEADER_FONT_SIZE,
            color = Color.LightGray
        )
    }
    val bodyPlaceHolder: @Composable () -> Unit = {
        Text(
            text = "Body",
            fontFamily = BODY_FONT_FAMILY,
            fontSize = BODY_FONT_SIZE,
            color = Color.LightGray
        )
    }

    @Composable
    fun iconButtonColors() = IconButtonDefaults.iconButtonColors(
        containerColor = Color.Black,
        contentColor = Color.White
    )
}