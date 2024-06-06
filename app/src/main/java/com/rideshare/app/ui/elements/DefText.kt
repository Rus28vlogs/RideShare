package com.rideshare.app.ui.elements

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import com.rideshare.app.ui.theme.Black
import com.rideshare.app.ui.theme.mText
import com.rideshare.app.ui.theme.nonScaledSp

@Composable
fun DefText(
    textId: Int,
    size: Int = mText,
    color: Color = Black,
    weight: FontWeight = FontWeight.Normal,
    style: FontStyle = FontStyle.Normal,
    modifier: Modifier = Modifier
) {
    Text(
        text = stringResource(id = textId),
        fontSize = size.nonScaledSp,
        color = color,
        fontStyle = style,
        fontWeight = weight,
        modifier = modifier
    )
}

@Composable
fun DefText(
    textId: String,
    size: Int = mText,
    color: Color = Black,
    weight: FontWeight = FontWeight.Normal,
    style: FontStyle = FontStyle.Normal,
    textDecoration: TextDecoration = TextDecoration.None,
    modifier: Modifier = Modifier,
    singleLine: Boolean = false,
) {
    Text(
        text = textId,
        fontSize = size.nonScaledSp,
        color = color,
        fontStyle = style,
        fontWeight = weight,
        modifier = modifier,
        maxLines = if (singleLine) 1 else 50,
        textDecoration = textDecoration,
    )
}