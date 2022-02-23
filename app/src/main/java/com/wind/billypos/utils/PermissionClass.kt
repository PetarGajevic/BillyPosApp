package com.wind.billypos.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class PermissionClass(context: Activity?) {

    val MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 100
    val MY_PERMISSIONS_REQUEST_READ_PHONE_STATE = 110
    val MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 120
    val MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 125
    val MY_PERMISSIONS_REQUEST_IS_NET_ENABLED = 140
    private var mActivity: Activity? = context

    fun PermissionClass(context: Activity?) {
        mActivity = context
    }

    fun PermissionClass(context: Context?) {
        mActivity = context as Activity?
    }

    fun checkPhoneStatePermission(): Boolean {
        val permissionCheckPhone = ContextCompat.checkSelfPermission(
            mActivity!!.applicationContext,
            Manifest.permission.READ_PHONE_STATE
        )
        if (permissionCheckPhone != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    mActivity!!,
                    Manifest.permission.READ_PHONE_STATE
                )
            ) {
                ActivityCompat.requestPermissions(
                    mActivity!!, arrayOf(Manifest.permission.READ_PHONE_STATE),
                    MY_PERMISSIONS_REQUEST_READ_PHONE_STATE
                )
            } else {
                ActivityCompat.requestPermissions(
                    mActivity!!, arrayOf(Manifest.permission.READ_PHONE_STATE),
                    MY_PERMISSIONS_REQUEST_READ_PHONE_STATE
                )
            }
        }
        return permissionCheckPhone == PackageManager.PERMISSION_GRANTED
    }

    fun checkStoragePermission(): Boolean {
        val permissionCheckPhone = ContextCompat.checkSelfPermission(
            mActivity!!.applicationContext,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        if (permissionCheckPhone != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    mActivity!!,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            ) {
                ActivityCompat.requestPermissions(
                    mActivity!!, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE
                )
                Toast.makeText(
                    mActivity!!.applicationContext, Utils.CONSTANTS.ALERTS.STORAGE_PERMISSION_NEEDED,
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                ActivityCompat.requestPermissions(
                    mActivity!!, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE
                )
            }
        }
        return permissionCheckPhone == PackageManager.PERMISSION_GRANTED
    }

    fun checkLocationPermission(): Boolean {
        val permissionCheckPhone = ContextCompat.checkSelfPermission(
            mActivity!!.applicationContext,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        val permissionCheckCoarse = ContextCompat.checkSelfPermission(
            mActivity!!.applicationContext,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        if (permissionCheckPhone != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    mActivity!!,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {
                ActivityCompat.requestPermissions(
                    mActivity!!, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION
                )
                Toast.makeText(
                    mActivity!!.applicationContext, Utils.CONSTANTS.ALERTS.PERMISSION_LOCATION_NEEDED,
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                ActivityCompat.requestPermissions(
                    mActivity!!, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION
                )
            }
        }
        if (permissionCheckCoarse != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    mActivity!!,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            ) {
                ActivityCompat.requestPermissions(
                    mActivity!!, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                    MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION
                )
                Toast.makeText(
                    mActivity!!.applicationContext, Utils.CONSTANTS.ALERTS.PERMISSION_LOCATION_NEEDED,
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                ActivityCompat.requestPermissions(
                    mActivity!!, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                    MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION
                )
            }
        }
        return permissionCheckPhone == PackageManager.PERMISSION_GRANTED && permissionCheckCoarse == PackageManager.PERMISSION_GRANTED
    }

}