package com.rideshare.app.ui.screen.drivers

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.rideshare.app.R
import com.rideshare.app.data.storage.models.Pass
import com.rideshare.app.data.storage.models.Users
import com.rideshare.app.ui.elements.BackPressHandler
import com.rideshare.app.ui.elements.DefButton
import com.rideshare.app.ui.elements.DefText
import com.rideshare.app.ui.navigation.Screen
import com.rideshare.app.ui.theme.White
import com.rideshare.app.ui.screen.main.MainViewModel
import com.rideshare.app.ui.theme.Orange
import com.rideshare.app.ui.theme.Red
import com.rideshare.app.ui.theme.borderSize
import com.rideshare.app.ui.theme.lpadding
import com.rideshare.app.ui.theme.ltext
import com.rideshare.app.ui.theme.mText
import com.rideshare.app.utils.Const
import com.rideshare.app.utils.ToastHelper
import com.rideshare.app.utils.dateFilter

@Composable
fun DriversView(
    navController: NavController = NavController(LocalContext.current),
    mainViewModel: MainViewModel = viewModel()
) {
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        mainViewModel.getAllUsers(context)
        mainViewModel.getAllPass(context)
    }

    UI(
        mainViewModel,
        sitClick = {
            navController.navigate(Screen.Main.name)
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
    mainViewModel: MainViewModel,
    sitClick: () -> Unit,
    logoutClick: () -> Unit,
) {
    val context = LocalContext.current
    val passList = MainViewModel.passList.collectAsState()
    val userList = MainViewModel.userList.collectAsState()

    val finalList = userList.value.filter { it.type == Const.DRIVER && !it.isStart}

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
        ) {
        DefText(textId = R.string.actualDrivers, size = 20, modifier = Modifier
            .padding(top = 20.dp)
            .align(Alignment.TopCenter))
        LazyColumn (
            Modifier
                .padding(top = 60.dp, start = 20.dp, end = 20.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ){
            items(finalList.size){
                val item = finalList[it]
                val filterPassList = passList.value.filter { it.driverId == item.id }

                Column (
                    Modifier
                        .fillMaxWidth()
                        .background(White, RoundedCornerShape(20.dp))
                        .padding(10.dp),
                ){
                    Column (
                        Modifier.height(80.dp)
                    ){
                        Row {
                            DefText(textId = R.string.startLocation, size = mText, color = Orange)
                            val transDate = dateFilter(AnnotatedString.Builder(item.date).toAnnotatedString()).text
                            DefText(textId = transDate.text, size = mText, color = Color.DarkGray, modifier = Modifier.padding(start = 20.dp))
                        }
                        DefText(textId = item.startAddress, size = 12)
                    }
                    Column (
                        Modifier.height(80.dp)
                    ){
                        DefText(textId = R.string.finishLocation, size = mText, color = Orange)
                        DefText(textId = item.finishAddress, size = 12)
                    }
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(top = 5.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            DefText(textId = "ФИО: ${item.fio}")
                            DefText(textId = "Марка: ${item.carMark}")
                            DefText(textId = "Номер: ${item.carNumber}", size = ltext)
                        }
                        Column (
                            horizontalAlignment = Alignment.CenterHorizontally
                        ){
                            DefText(textId = "Мест: ${filterPassList.size} / 3")

                            DefButton(
                                text = stringResource(id = R.string.start),
                                onClick = {
                                    if (filterPassList.size < 3) {
                                        mainViewModel.insertPass(context, Pass(
                                            driverId = item.id,
                                            passId = MainViewModel.user.value.id
                                        ))
                                        mainViewModel.getAllPass(context)
                                        mainViewModel.getAllUsers(context)
                                        sitClick()
                                    }
                                    else ToastHelper().show(context, "Все места заняты")
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (filterPassList.size < 3) Orange else Color.Gray,
                                    contentColor = Color.DarkGray,
                                ),
                                modifier = Modifier
                                    .width(120.dp)
                            )
                        }

                    }
                }
            }
        }
        DefButton(
            text = stringResource(id = R.string.logout),
            modifier = Modifier
                .padding(bottom = lpadding)
                .fillMaxWidth(0.8f)
                .align(Alignment.BottomCenter)
                .border(borderSize, Red, RoundedCornerShape(40.dp)),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = Red,
            ),
            onClick = logoutClick,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun NavigatorPreview() {
    UI(
        hiltViewModel(),
        sitClick = {},
        logoutClick = {}
    )
}