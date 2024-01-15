package com.example.locationapp

import android.content.Context
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import android.Manifest
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat

@Composable
fun LocationDisplay(
    locationUtils: LocationUtils,
    viewModel: LocationViewModel,
    context : Context
){
    val location = viewModel.location.value

    val address = location?.let { locationUtils.reverseGeocodeLocation(location) }

    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { permissions ->
            if (permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true
                && permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
            ) {
                //get the location because we have the permission
                locationUtils.requestLocationUpdates(viewModel)
            }else {
                //asking for permission, this is relevent to not just location but any permission
                val retionaleRequired = ActivityCompat.shouldShowRequestPermissionRationale(
                    context as MainActivity,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) || ActivityCompat.shouldShowRequestPermissionRationale(
                    context as MainActivity,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
                if(retionaleRequired){
                    Toast.makeText(context,"Permission is required to get the location",Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(context,"Permission is required to get the location, go to settings to enable it",
                        Toast.LENGTH_SHORT).show()
                }
            }

        })
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        if(location != null){
            Text(text = "Latitude : ${location.latitude}")
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = "Longitude : ${location.longitude} ")
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = "Address : $address")
        }else{
            Text(text = "Location not available")
        }
        Spacer(modifier = Modifier.height(10.dp))
        Button(
            onClick = {
                if(locationUtils.hasLocationPermission(context)){
                    //get the location because we have the permission
                      locationUtils.requestLocationUpdates(viewModel)

                }else{
                    //asking the user for permission,by launching the permission launcher
                    requestPermissionLauncher.launch(
                        arrayOf(
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        )
                    )
                }
            }
        ){
            Text(text = "Get Location")
        }
    }
}
//this is actually location permission display screen i should rename the file name
