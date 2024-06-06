package com.rideshare.app.ui.screen.registration

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.rideshare.app.R
import com.rideshare.app.data.storage.models.Users
import com.rideshare.app.ui.elements.DefText
import com.rideshare.app.ui.elements.EditText
import com.rideshare.app.ui.navigation.Screen
import com.rideshare.app.ui.theme.Black
import com.rideshare.app.ui.theme.Orange
import com.rideshare.app.ui.theme.White
import com.rideshare.app.ui.theme.headerText
import com.rideshare.app.ui.theme.lpadding
import com.rideshare.app.ui.theme.nonScaledSp
import com.rideshare.app.utils.Const
import com.rideshare.app.utils.ToastHelper
import com.rideshare.app.ui.elements.DefButton
import com.rideshare.app.ui.screen.main.MainViewModel


@Composable
fun RegView(
    navController: NavController = NavController(LocalContext.current),
    mainViewModel: MainViewModel = viewModel()
) {
    val context = LocalContext.current

    UI(
        onClickLogin = {
            mainViewModel.reg(context, it) { result ->
                if (result) {
                    val user = MainViewModel.user.value
                    if (user.type == Const.DRIVER) navController.navigate(Screen.Main.name)
                    else navController.navigate(Screen.Drivers.name)
                }
                else ToastHelper().show(context, context.getString(R.string.toastLoginError))
            }
        },
        back = {
            navController.navigate(Screen.Login.name)
        }
    )
}

@Composable
fun UI(
    onClickLogin: (Users) -> Unit,
    back: () -> Unit
) {
    var role by remember { mutableIntStateOf(3) }

    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 30.dp)
    ){
        Logo()
        when (role) {
            1 -> {
                AuthPass(onClick = onClickLogin, back = { role = 0 })
            }
            2 -> {
                AuthDrive(onClick = onClickLogin, back = { role = 0 })
            }
            else -> Switcher {
                role = it
            }
        }

    }
}

@Composable
fun Switcher(onChange:(Int) -> Unit) {
    Column (
        Modifier
            .fillMaxWidth()
            .padding(top = 120.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        DefText(R.string.choiceRole, size = 30)
        Row(
            Modifier
                .padding(top = 30.dp)
                .fillMaxWidth(0.8f),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Box(
                Modifier
                    .size(120.dp)
                    .border(2.dp, Orange, RoundedCornerShape(20.dp))
                    .clickable {
                        onChange(1)
                    }
            ) {
                Icon(
                    painterResource(id = R.drawable.passanger),
                    Const.ICON_DESCRIPTION,
                    tint = Black,
                    modifier = Modifier
                        .fillMaxHeight(0.7f)
                        .align(Alignment.Center)
                )
                DefText(
                    R.string.passenger,
                    size = 16,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 5.dp)
                )
            }
            Box(
                Modifier
                    .size(120.dp)
                    .border(2.dp, Orange, RoundedCornerShape(20.dp))
                    .clickable {
                        onChange(2)
                    }
            ) {
                Icon(
                    painterResource(id = R.drawable.driver),
                    Const.ICON_DESCRIPTION,
                    tint = Black,
                    modifier = Modifier
                        .fillMaxHeight(0.7f)
                        .align(Alignment.Center)
                )
                DefText(
                    R.string.driver,
                    size = 16,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 5.dp)
                )
            }
        }
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
fun AuthPass(
    onClick: (Users) -> Unit,
    back: () -> Unit
) {
    var fio by remember { mutableStateOf("") }
    var number by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    var login by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column (
        Modifier
            .padding(top = 60.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        DefText(R.string.passenger, size = 30)

        EditText(fio, stringResource(id = R.string.fio), modifier = Modifier.padding(top = 10.dp)) { fio = it }

        EditText(
            number,
            stringResource(id = R.string.number),
            Modifier.padding(top = lpadding),
            number = true,
        ) { number = it }

        EditText(
            date,
            stringResource(id = R.string.date),
            Modifier.padding(top = lpadding),
            number = true,
            dateFilter = true
        ) { date = it }

        EditText(
            login,
            stringResource(id = R.string.mail),
            Modifier.padding(top = lpadding),
        ) { login = it }

        EditText(
            password,
            stringResource(id = R.string.pass),
            Modifier.padding(top = lpadding),
            password = true
        ) { password = it }
        
        DefButton(
            text = stringResource(id = R.string.registration),
            onClick = { onClick(Users(
                fio = fio,
                login = login,
                password = password,
                number = number,
                type = Const.PASS
            )
            ) },
            modifier = Modifier
                .padding(top = lpadding)
                .width(190.dp)
        )

        DefButton(
            text = stringResource(id = R.string.back),
            onClick = { back() },
            modifier = Modifier
                .padding(top = lpadding)
                .width(190.dp)
        )
    }
}

@Composable
fun AuthDrive(
    onClick: (Users) -> Unit,
    back: () -> Unit
) {
    var openMapType by remember { mutableStateOf(0) }
    var openMap by remember { mutableStateOf(false) }
    var fio by remember { mutableStateOf("") }
    var number by remember { mutableStateOf("") }
    var carMark by remember { mutableStateOf("") }
    var carNumber by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    var login by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var locationLong by remember { mutableDoubleStateOf(0.0) }
    var locationLat by remember { mutableDoubleStateOf(0.0) }
    var finishLocation by remember { mutableStateOf("") }
    var finishLocationLong by remember { mutableDoubleStateOf(0.0) }
    var finishLocationLat by remember { mutableDoubleStateOf(0.0) }

    Box{
        if (openMap) {
            Map(onClick = { add, long, lat ->
                if (openMapType == 1) {
                    location = add
                    locationLong = long
                    locationLat = lat
                }
                else {
                    finishLocation = add
                    finishLocationLong = long
                    finishLocationLat = lat
                }
                openMap = false
            })
        } else {
            Column (
                Modifier
                    .padding(top = 10.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                DefText(R.string.driver, size = 30)

                EditText(fio, stringResource(id = R.string.fio), modifier = Modifier.padding(top = 10.dp)) { fio = it }

                EditText(
                    number,
                    stringResource(id = R.string.number),
                    Modifier.padding(top = lpadding),
                    number = true,
                ) { number = it }

                EditText(
                    carMark,
                    stringResource(id = R.string.carModel),
                    Modifier.padding(top = lpadding),
                ) { carMark = it }

                EditText(
                    carNumber,
                    stringResource(id = R.string.carNumber),
                    Modifier.padding(top = lpadding),
                ) { carNumber = it }

                EditText(
                    location,
                    stringResource(id = R.string.startLocation),
                    Modifier.padding(top = lpadding),
                    enabled = false,
                    onClick = {
                        openMapType = 1
                        openMap = true
                    }
                ) { location = it }

                EditText(
                    finishLocation,
                    stringResource(id = R.string.finishLocation),
                    Modifier.padding(top = lpadding),
                    enabled = false,
                    onClick = {
                        openMapType = 2
                        openMap = true
                    }
                ) { finishLocation = it }

                EditText(
                    date,
                    stringResource(id = R.string.date),
                    Modifier.padding(top = lpadding),
                    number = true,
                    dateFilter = true
                ) { date = it }

                EditText(
                    login,
                    stringResource(id = R.string.mail),
                    Modifier.padding(top = lpadding),
                ) { login = it }

                EditText(
                    password,
                    stringResource(id = R.string.pass),
                    Modifier.padding(top = lpadding),
                    password = true
                ) { password = it }

                DefButton(
                    text = stringResource(id = R.string.registration),
                    onClick = { onClick(
                        Users(
                            login = login,
                            password = password,
                            type = Const.DRIVER,
                            fio = fio,
                            date = date,
                            number = number,
                            carMark = carMark,
                            carNumber = carNumber,
                            startAddress = location,
                            startLongitude = locationLong,
                            startLatitude = locationLat,
                            finishAddress = finishLocation,
                            finishLongitude = finishLocationLong,
                            finishLatitude = finishLocationLat
                        )
                    ) },
                    modifier = Modifier
                        .padding(top = lpadding)
                        .width(190.dp)
                )

                DefButton(
                    text = stringResource(id = R.string.back),
                    onClick = { back() },
                    modifier = Modifier
                        .padding(top = lpadding)
                        .width(190.dp)
                )

                Spacer(modifier = Modifier.padding(bottom = 100.dp))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Column (modifier = Modifier
        .fillMaxSize()
        .background(White)) {
        UI(
            onClickLogin = { },
            back = {}
        )
    }
}