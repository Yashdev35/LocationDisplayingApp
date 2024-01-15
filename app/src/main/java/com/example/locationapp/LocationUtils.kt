package com.example.locationapp

import android.content.Context
import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.os.Looper

import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY
import com.google.android.gms.maps.model.LatLng
import java.util.Locale

class LocationUtils(val context : Context){


    private val _fusedLocationClient : FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)


@SuppressLint("SuspiciousIndentation")
@Suppress("MissingPermission")
    fun requestLocationUpdates(viewModel: LocationViewModel){
        //here we directly created and object rather than
        // importing class and then creating an object of that class
        val locationCallback = object : LocationCallback(){
            override fun onLocationResult(locationResult: LocationResult) {
               super.onLocationResult(locationResult)
                locationResult.lastLocation?.let {
                    //it is the location, the result we get from google play services
                    val location = LocationData(it.latitude,it.longitude)
                    viewModel.updateLocation(location)
                }
            }
        }


            val locationRequest = LocationRequest.Builder(PRIORITY_HIGH_ACCURACY,1000).build()

            _fusedLocationClient.requestLocationUpdates(locationRequest,locationCallback, Looper.getMainLooper())


    }

    fun hasLocationPermission(context: Context) : Boolean{
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED
                &&
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_COARSE_LOCATION)== PackageManager.PERMISSION_GRANTED
    }
    fun reverseGeocodeLocation(location: LocationData): String {
        val geoCoder = Geocoder(context, Locale.ENGLISH)
        val coordinates = LatLng(location.latitude, location.longitude)
        val addresses: MutableList<Address>? =
            geoCoder.getFromLocation(coordinates.latitude, coordinates.longitude, 1)
        //getfromlocation is deprecated but we are using it for simplicity, new is complex
        return if (addresses?.isNotEmpty() == true) {
            addresses[0].getAddressLine(0)
        } else {
            "Unknown Location"
        }
    }
}
/*
the above is a class which will tell us if we have the location permission or not
we initially used an if else statement to check if we have the permission or not then replaced it with direct
return statement, in the return statement we are using the ContextCompat class to check if we have the permission
if we have the permission then the int returned by the checkSelfPermission method will be equal to the
package manager permission granted constant which is equal to 0, if we don't have the permission then then they wont be
equal and the return statement will return false
 */