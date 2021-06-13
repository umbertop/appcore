package com.umbertop.appcore.utility

import android.Manifest
import android.app.Activity
import android.content.Context
import android.location.LocationManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import pub.devrel.easypermissions.EasyPermissions

sealed class PermissionUtil(private val context: Context) {
    fun isLocationEnabled(): Boolean {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE)
                as LocationManager

        val gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        val networkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

        if (!gpsEnabled && !networkEnabled) {
            return false
        }

        return true
    }

    fun hasLocationPermission(): Boolean =
        EasyPermissions.hasPermissions(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )

    fun hasBackgroundLocationPermission(): Boolean =
        // Prior to Android Q background location permission is not necessary
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            EasyPermissions.hasPermissions(
                context,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            )
        } else true

    fun requestLocationPermission(host: Activity, rationale: String){
        if(hasLocationPermission()){
            return
        }

        EasyPermissions.requestPermissions(
            host,
            rationale,
            REQUEST_CODE_LOCATION_PERMISSION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    }

    fun requestLocationPermission(host: Fragment, rationale: String){
        if(hasLocationPermission()){
            return
        }

        EasyPermissions.requestPermissions(
            host,
            rationale,
            REQUEST_CODE_LOCATION_PERMISSION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    fun requestBackgroundLocationPermission(host: Activity, rationale: String){
        if(hasBackgroundLocationPermission()){
            return
        }

        EasyPermissions.requestPermissions(
            host,
            rationale,
            REQUEST_CODE_BACKGROUND_LOCATION_PERMISSION,
            Manifest.permission.ACCESS_BACKGROUND_LOCATION
        )
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    fun requestBackgroundLocationPermission(host: Fragment, rationale: String){
        if(hasBackgroundLocationPermission()){
            return
        }

        EasyPermissions.requestPermissions(
            host,
            rationale,
            REQUEST_CODE_BACKGROUND_LOCATION_PERMISSION,
            Manifest.permission.ACCESS_BACKGROUND_LOCATION
        )
    }

    companion object{
        const val REQUEST_CODE_LOCATION_PERMISSION = 0
        const val REQUEST_CODE_BACKGROUND_LOCATION_PERMISSION = 1
    }
}