package com.rideshare.app.ui.screen.load

import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.rideshare.app.R
import com.rideshare.app.ui.elements.BackPressHandler
import com.rideshare.app.ui.navigation.Screen
import com.rideshare.app.ui.screen.main.MainViewModel
import com.rideshare.app.ui.theme.Orange
import com.rideshare.app.ui.theme.headerText
import com.rideshare.app.ui.theme.nonScaledSp
import com.rideshare.app.utils.Const
import kotlinx.coroutines.delay

@Composable
fun LoadView(
    navController: NavController,
    mainViewModel: MainViewModel = viewModel()
) {
    val context = LocalContext.current

    var hasPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        )
    }
    val launcherPermission = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = {granted ->
            hasPermission = granted
        }
    )
    LaunchedEffect(Unit) {
        if (!hasPermission) {
            launcherPermission.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    Countdown(1000L) {
        mainViewModel.isAuth(context) {
            if (it) {
                val user = MainViewModel.user.value
                if (user.type == Const.DRIVER || user.isSit) navController.navigate(Screen.Main.name)
                else navController.navigate(Screen.Drivers.name)
            }
            else navController.navigate(Screen.Login.name)
        }
    }

    UI()

    BackPressHandler{}
}

@Composable
private fun UI() {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(50.dp)
            .background(White)
    ) {
        Text(
            stringResource(id = R.string.rideshare),
            fontSize = headerText.nonScaledSp,
            fontWeight = FontWeight.Bold,
            lineHeight = 70.nonScaledSp,
            color = Color.Black,
            style = MaterialTheme.typography.bodyLarge,
        )
        Icon(
            painterResource(id = R.drawable.rideshare),
            Const.ICON_DESCRIPTION,
            tint = Orange,
            modifier = Modifier.size(90.dp)
        )
    }
}

@Composable
fun Countdown(targetTime: Long, endEvent: () -> Unit) {
    var remainingTime by remember(targetTime) {
        mutableLongStateOf(targetTime - System.currentTimeMillis())
    }

    LaunchedEffect(remainingTime) {
        delay(targetTime)
        remainingTime = targetTime - System.currentTimeMillis()
        endEvent()
    }
}


@Preview(showBackground = true)
@Composable
private fun Preview() {
    UI()
}