package com.rideshare.app.ui.elements

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.rideshare.app.ui.theme.Black
import com.rideshare.app.ui.theme.Gray
import com.rideshare.app.ui.theme.White
import com.rideshare.app.ui.theme.mShape
import com.rideshare.app.ui.theme.textFieldHeight
import com.rideshare.app.ui.theme.textFieldWidth
import com.rideshare.app.utils.DateTransformation

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditText (
    text: String,
    hint: String,
    modifier: Modifier = Modifier,
    password: Boolean = false,
    number: Boolean = false,
    enabled: Boolean = true,
    dateFilter: Boolean = false,
    onClick: () -> Unit = {},
    onChange: (String) -> Unit,

) {
    TextField(
        value = text,
        label = { Text(hint, color = Gray) },
        onValueChange = onChange,
        visualTransformation = if (password) PasswordVisualTransformation() else if (dateFilter) DateTransformation() else VisualTransformation.None,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            containerColor = White,
            focusedTextColor = Black,
            unfocusedTextColor = Black,
            disabledTextColor = Black
        ),
        singleLine = true,
        maxLines = 1,
        enabled = enabled,
        keyboardOptions = KeyboardOptions(keyboardType = if (number) KeyboardType.Number else KeyboardType.Ascii),
        modifier = modifier
            .shadow(10.dp, RoundedCornerShape(20.dp))
            .height(textFieldHeight)
            .width(textFieldWidth)
            .clip(RoundedCornerShape(mShape))
            .clickable { onClick() }
    )
}