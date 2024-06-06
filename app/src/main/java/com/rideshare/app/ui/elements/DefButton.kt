package com.rideshare.app.ui.elements

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.rideshare.app.ui.theme.buttonHeight
import com.rideshare.app.ui.theme.smallButtonWidth

@Composable
fun DefButton(
    text: String,
    modifier: Modifier = Modifier,
    colors: ButtonColors = ButtonDefaults.buttonColors(
        containerColor = Color.LightGray,
        contentColor = Color.DarkGray,
    ),
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        colors = colors,
        modifier = modifier
            .width(smallButtonWidth)
            .height(buttonHeight)
    ) {
        Text(text)
    }
}