package com.rideshare.app.ui.screen.registration

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.rideshare.app.R
import com.rideshare.app.ui.elements.DefText
import com.rideshare.app.ui.elements.EditText
import com.rideshare.app.ui.elements.DefButton
import com.rideshare.app.ui.theme.Orange
import java.util.Locale

@Composable
fun Map(
    onClick: (String, Double, Double) -> Unit,
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

    val cameraPositionState = rememberCameraPositionState {}
    var address by remember { mutableStateOf("") }
    var mark by remember { mutableStateOf(LatLng(0.0, 0.0)) }
    if (mark.latitude != 0.0) {
        address = getCompleteAddressString(LocalContext.current, mark.latitude, mark.longitude)
    }
    Box (
        modifier = Modifier.fillMaxSize(),
    ){
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            properties = MapProperties(isMyLocationEnabled = true),
            onMapClick = {
                mark = it
            }
        ) {
            if (mark.latitude != 0.0) {
                Marker(
                    state = MarkerState(position = mark),
                    title = "mark",
                    snippet = "mark"
                )
            }
        }
        DefText(
            textId = R.string.clickOnCard,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 20.dp, bottom = 120.dp),
        )
        EditText(
            address,
            stringResource(id = R.string.startLocation),
            Modifier
                .fillMaxWidth(0.7f)
                .align(Alignment.TopCenter)
                .padding(top = 5.dp),
            enabled = false
        ) { address = it }

        DefButton(
            text = stringResource(id = R.string.choice),
            onClick = { onClick(address, mark.longitude, mark.latitude) },
            colors = ButtonDefaults.buttonColors(
                containerColor = if (address.isEmpty()) Color.LightGray else Orange,
                contentColor = Color.DarkGray,
            ),
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 20.dp, bottom = 30.dp)
                .width(190.dp)
        )
    }
}

private fun getCompleteAddressString(context: Context, LATITUDE: Double, LONGITUDE: Double): String {
    var strAdd = ""
    val geocoder = Geocoder(context, Locale.getDefault())
    try {
        val addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1)
        if (addresses != null) {
            val returnedAddress = addresses[0]
            val strReturnedAddress = StringBuilder("")
            for (i in 0..returnedAddress.maxAddressLineIndex) {
                strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n")
            }
            strAdd = strReturnedAddress.toString()
            Log.w("My Current loction address", strReturnedAddress.toString())
        } else {
            Log.w("My Current loction address", "No Address returned!")
        }
    } catch (e: Exception) {
        e.printStackTrace()
        Log.w("My Current loction address", "Canont get Address!")
    }
    return strAdd
}