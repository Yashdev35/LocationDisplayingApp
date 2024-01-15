package com.example.locationapp

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class LocationViewModel : ViewModel(){
    private val _locationData = mutableStateOf<LocationData?>(null)
    val location: State<LocationData?> = _locationData

    fun updateLocation(newLocation : LocationData){
        _locationData.value = newLocation
    }
}