package com.rideshare.app.ui.screen.login

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.rideshare.app.R
import com.rideshare.app.ui.navigation.Screen
import com.rideshare.app.ui.theme.White
import com.rideshare.app.ui.theme.headerText
import com.rideshare.app.ui.theme.lpadding
import com.rideshare.app.ui.theme.xlpadding
import com.rideshare.app.ui.elements.DefButton
import com.rideshare.app.ui.elements.EditText
import com.rideshare.app.ui.screen.main.MainViewModel
import com.rideshare.app.ui.theme.nonScaledSp
import com.rideshare.app.utils.Const
import com.rideshare.app.utils.ToastHelper

@Composable
fun LoginView(
    navController: NavController = NavController(LocalContext.current),
    mainViewModel: MainViewModel = viewModel()
) {
    val context = LocalContext.current

    UI(
        onClickLogin = { mail, pass ->
            mainViewModel.login(context, mail, pass) {
                if (it) {
                    val user = MainViewModel.user.value
                    if (user.type == Const.DRIVER || user.isSit) navController.navigate(Screen.Main.name)
                    else navController.navigate(Screen.Drivers.name)
                }
                else ToastHelper().show(context, context.getString(R.string.toastNotFoundUser))
            }
        },
        regOnClick = {
            navController.navigate(Screen.Registration.name)
        }
    )
}

@Composable
fun UI(
    onClickLogin: (String, String) -> Unit,
    regOnClick: () -> Unit
) {

    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround,
        modifier = Modifier
            .fillMaxSize()
            .padding(xlpadding)
    ){
        Logo()
        Auth(onClickLogin, regOnClick = regOnClick)
    }
}

@Composable
fun Logo() {
    Column {
        Text(
            stringResource(id = R.string.rideshare),
            fontSize = headerText.nonScaledSp,
            fontWeight = FontWeight.Bold,
            lineHeight = 70.nonScaledSp,
            color = Color.Black,
            style = MaterialTheme.typography.bodyLarge,
        )
    }
}

@Composable
fun Auth(
    onClick: (String, String) -> Unit,
    regOnClick: () -> Unit
) {
    var mail by remember { mutableStateOf("") }
    var pass by remember { mutableStateOf("") }

    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        EditText(mail, stringResource(id = R.string.mail)) { mail = it }

        EditText(
            pass,
            stringResource(id = R.string.pass),
            Modifier.padding(top = lpadding),
            true,
        ) { pass = it }

        DefButton(
            text = stringResource(id = R.string.enter),
            onClick = { onClick(mail, pass) },
            modifier = Modifier
                .padding(top = lpadding)
                .width(190.dp)
        )

        DefButton(
            text = stringResource(id = R.string.registration),
            onClick = regOnClick,
            modifier = Modifier
                .padding(top = lpadding)
                .width(190.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Column (modifier = Modifier
        .fillMaxSize()
        .background(White)) {
        UI(
            onClickLogin = { _, _ ->

            },
            regOnClick = {}
        )
    }
}