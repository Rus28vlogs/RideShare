package com.rideshare.app.ui.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.rideshare.app.ui.screen.load.LoadView
import com.rideshare.app.ui.screen.login.LoginView
import com.rideshare.app.ui.screen.drivers.DriversView
import com.rideshare.app.ui.screen.main.MainView
import com.rideshare.app.ui.screen.registration.RegView

enum class Screen {
    Login,
    Registration,
    Load,
    Main,
    Drivers
}

@Composable
fun Navigation() {
    val navController = rememberNavController()

    Column (
        modifier = Modifier.background(White)
    ) {
        NavHost(modifier = Modifier.fillMaxSize(), navController = navController, startDestination = Screen.Load.name) {
            composable( route = Screen.Load.name) { EnterAnimation { LoadView(navController) } }
            composable( route = Screen.Login.name) { EnterAnimation { LoginView(navController) } }
            composable( route = Screen.Registration.name) { EnterAnimation { RegView(navController) } }
            composable( route = Screen.Main.name) { EnterAnimation { MainView(navController) } }
            composable( route = Screen.Drivers.name) { EnterAnimation { DriversView(navController) } }
        }
    }
}

@Composable
fun EnterAnimation(content: @Composable () -> Unit) {
    AnimatedVisibility(
        visibleState = MutableTransitionState(
            initialState = false
        ).apply { targetState = true },
        modifier = Modifier,
        enter = slideInVertically(
            initialOffsetY = { -40 }
        ) + expandVertically(
            expandFrom = Alignment.Top
        ) + fadeIn(initialAlpha = 0.3f),
        exit = slideOutVertically() + shrinkVertically() + fadeOut(),
    ) {
        content()
    }
}