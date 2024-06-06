package com.rideshare.app.ui.screen.main

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import com.rideshare.app.R
import com.rideshare.app.data.storage.models.Users
import com.rideshare.app.ui.elements.BackPressHandler
import com.rideshare.app.ui.navigation.Screen
import com.rideshare.app.ui.theme.Red
import com.rideshare.app.ui.theme.White
import com.rideshare.app.ui.theme.borderSize
import com.rideshare.app.ui.theme.lpadding
import com.rideshare.app.ui.theme.ltext
import com.rideshare.app.utils.Const
import com.rideshare.app.ui.elements.DefButton
import com.rideshare.app.ui.elements.DefText
import com.rideshare.app.ui.theme.Orange
import com.rideshare.app.ui.theme.mText
import com.rideshare.app.utils.ToastHelper
import com.rideshare.app.utils.dateFilter

@Composable
fun MainView(
    navController: NavController = NavController(LocalContext.current),
    mainViewModel: MainViewModel = viewModel()
) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        mainViewModel.getAllPass(context)
        mainViewModel.getAllUsers(context)
    }

    UI(
        startClick = {
            mainViewModel.start(context)
        },
        finishClick = {
            mainViewModel.logout()
            navController.navigate(Screen.Login.name)
            ToastHelper().show(context, "Поездка завершена!")
        },
        helpClick = {
            mainViewModel.help(context)
        },
        cancelClick = {
            mainViewModel.cancelClick(context)
            navController.navigate(Screen.Drivers.name)
        },
        logoutClick = {
            mainViewModel.logout()
            navController.navigate(Screen.Login.name)
        }
    )
    BackPressHandler {}
}

@Composable
private fun UI(
    startClick: () -> Unit,
    finishClick: () -> Unit = {},
    cancelClick: () -> Unit = {},
    helpClick: () -> Unit = {},
    logoutClick: () -> Unit,
) {
    var start by remember { mutableStateOf(false) }
    val user = MainViewModel.user.collectAsState()
    val userList = MainViewModel.userList.collectAsState()
    val passList = MainViewModel.passList.collectAsState()

    val userInfo: Users? =
        if (user.value.type == Const.DRIVER) user.value
        else {
            val pass = passList.value.firstOrNull{ it.passId == user.value.id }

            if (pass != null) {
                userList.value.firstOrNull{it.id == pass.driverId}
            } else Users()
        }

    val pos = LatLng(
        userInfo?.startLatitude ?: user.value.startLatitude,
        userInfo?.startLongitude ?: user.value.startLongitude
    )
    val finishPos = LatLng(
        userInfo?.finishLatitude ?: user.value.finishLatitude,
        userInfo?.finishLongitude ?: user.value.finishLongitude
    )

    val routeCoordinates = listOf(
        pos,
        finishPos
    )

    val cameraPositionState = rememberCameraPositionState {
        if (pos.latitude != 0.0) {
            position = CameraPosition.fromLatLngZoom(pos, 10f)
        }
    }

    Box (
        modifier = Modifier
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.LightGray,
                        White
                    )
                )
            )
            .fillMaxSize()
    ){
        GoogleMap(
            modifier = Modifier
                .fillMaxWidth()
                .height(230.dp),
            cameraPositionState = cameraPositionState,
            properties = MapProperties(isMyLocationEnabled = true),
        ) {
            Marker(
                state = MarkerState(position = pos),
                title = "start",
            )
            Marker(
                state = MarkerState(position = finishPos),
                title = "finish",
            )
            Polyline(
                points = routeCoordinates,
                clickable = true,
                color = Color.Blue,
                width = 5f,
            )
        }
        Column (
            Modifier
                .shadow(5.dp, RoundedCornerShape(30.dp))
                .background(White, RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp))
                .fillMaxHeight(0.73f)
                .align(Alignment.BottomCenter)
                .padding(lpadding)
                .verticalScroll(rememberScrollState())
        ){
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column (
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    Icon(
                        painter = painterResource(id = R.drawable.rideshare),
                        contentDescription = Const.ICON_DESCRIPTION,
                        tint = Orange,
                        modifier = Modifier.size(60.dp)
                    )
                }
                Column (
                    Modifier.padding(start = 20.dp)
                ){
                    DefText(textId = userInfo?.carMark ?: user.value.carMark)
                    DefText(textId = userInfo?.carNumber ?: user.value.carNumber, size = ltext)
                }
            }
            Column(
                Modifier.padding(top = 5.dp)
            ) {
                Row {
                    DefText(textId = R.string.startLocation, size = ltext, color = Orange)
                    val transDate = dateFilter(AnnotatedString.Builder(userInfo?.date ?: user.value.date).toAnnotatedString()).text
                    DefText(textId = transDate.text, size = mText, color = Color.DarkGray, modifier = Modifier.padding(start = 20.dp))
                }
                DefText(textId = userInfo?.startAddress ?: user.value.startAddress)
            }
            Column {
                DefText(textId = R.string.finishLocation, size = ltext, color = Orange)
                DefText(textId = userInfo?.finishAddress ?: user.value.finishAddress)
            }
            if (user.value.type == Const.DRIVER) {
                val filterPassList = passList.value.filter { it.driverId == user.value.id }

                Row (
                    Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ){
                    DefText(textId = R.string.youPass, size = ltext, color = Orange)
                    DefText(textId = "Мест: ${filterPassList.size} / 3")
                }

                filterPassList.forEach {
                    val filterUser = userList.value.firstOrNull { elem -> elem.id == it.passId }
                    if (filterUser != null) {
                        Column (
                            Modifier
                                .padding(top = 5.dp)
                                .border(1.dp, Orange, RoundedCornerShape(20.dp))
                                .padding(10.dp)
                        ){
                            Row (
                                Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ){
                                DefText(textId = filterUser.fio)
                                DefText(textId = filterUser.number)
                            }
                            DefText(textId = userInfo?.finishAddress ?: user.value.finishAddress)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(100.dp))
            } else {
                val filterPassList = passList.value.filter { it.driverId == userInfo?.id }

                Row (
                    Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ){
                    DefText(textId = R.string.youDriver, size = ltext, color = Orange)
                    DefText(textId = "Мест: ${filterPassList.size} / 3")
                }

                DefText(textId = "ФИО: ${userInfo?.fio}", modifier = Modifier.padding(top = 10.dp))
                DefText(textId = "Номер: ${userInfo?.number}", modifier = Modifier.padding(top = 10.dp))
                DefText(textId = "Статус: ${ if (userInfo?.isStart == true) "Поехал" else "Ожидает" }", modifier = Modifier.padding(top = 10.dp))
            }


            Spacer(modifier = Modifier.weight(1f))
        }

        if (user.value.type == Const.DRIVER) {
            if (user.value.isStart || start) {
                DefButton(
                    text = stringResource(id = R.string.finish),
                    onClick = {
                        finishClick()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Orange,
                        contentColor = Color.DarkGray,
                    ),
                    modifier = Modifier
                        .padding(bottom = 80.dp)
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth(0.8f)
                )
            } else {
                DefButton(
                    text = stringResource(id = R.string.start),
                    onClick = {
                        startClick()
                        start = true
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Orange,
                        contentColor = Color.DarkGray,
                    ),
                    modifier = Modifier
                        .padding(bottom = 80.dp)
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth(0.8f)
                )
            }
        } else {
            DefButton(
                text = stringResource(id = R.string.cancel),
                onClick = {
                    cancelClick()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Orange,
                    contentColor = Color.DarkGray,
                ),
                modifier = Modifier
                    .padding(bottom = 80.dp)
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth(0.8f)
            )
        }

        Row (
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(bottom = lpadding),
            horizontalArrangement = Arrangement.SpaceAround
        ){
            DefButton(
                text = stringResource(id = R.string.help),
                onClick = {
                    helpClick()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Red,
                    contentColor = White,
                ),
                modifier = Modifier
                    .width(120.dp)
            )
            DefButton(
                text = stringResource(id = R.string.logout),
                modifier = Modifier
                    .width(120.dp)
                    .border(borderSize, Red, RoundedCornerShape(40.dp)),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = Red,
                ),
                onClick = logoutClick,
            )
        }
    }

}

@Preview(showBackground = true)
@Composable
private fun NavigatorPreview() {
    UI(
        startClick = {},
        logoutClick = {}
    )
}