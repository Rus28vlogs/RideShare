package com.rideshare.app.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

val mText = 14
val ltext = 16
val headerText = 90

val spadding = 5.dp
val padding = 10.dp
val lpadding = 20.dp
val xlpadding = 30.dp

val mShape = 10.dp
val defShape = 20.dp

val mIconSize = 20.dp
val iconSize = 30.dp

//TextField
val textFieldHeight = 60.dp
val textFieldWidth = 280.dp

//Button
val buttonSize = 60.dp
val smallButtonWidth = 150.dp
val buttonHeight = 40.dp

//EditText
val borderSize = 2.dp

//Header
val headerHeight = 60.dp

val Int.nonScaledSp
    @Composable
    get() = (this / LocalDensity.current.fontScale).sp

