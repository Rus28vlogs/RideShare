package com.rideshare.app.ui.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.rideshare.app.R
import com.rideshare.app.ui.theme.headerHeight
import com.rideshare.app.ui.theme.ltext
import com.rideshare.app.ui.theme.mIconSize
import com.rideshare.app.ui.theme.nonScaledSp
import com.rideshare.app.ui.theme.padding
import com.rideshare.app.utils.Const

@Composable
fun Header(
    backClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .height(headerHeight)
            .background(Color.LightGray)
            .padding(padding)
    ) {
        Row (
            verticalAlignment = Alignment.CenterVertically,
        ){
            Icon (
                painterResource(id = R.drawable.back),
                Const.ICON_DESCRIPTION,
                tint = Color.Black,
                modifier = Modifier
                    .size(mIconSize)
                    .clickable { backClick() }

            )
            Text(
                stringResource(id = R.string.back),
                color = Color.Black,
                fontSize = ltext.nonScaledSp,
                modifier = Modifier
                    .padding(padding)
                    .clickable { backClick() }
            )
        }
    }
}