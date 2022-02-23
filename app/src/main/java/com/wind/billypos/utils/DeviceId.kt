package com.wind.billypos.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.provider.Settings
import android.telephony.TelephonyManager

class DeviceId(inContext: Context?) {

    private var context: Context? = inContext


    @SuppressLint("HardwareIds")
    fun myDeviceId(): String? {
        val deviceUniqueIdentifier: String?
        deviceUniqueIdentifier = try {
            val telephonyManager =
                context!!.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            if (telephonyManager != null && PermissionClass(context as Activity?).checkPhoneStatePermission() && telephonyManager.deviceId != null) {
                telephonyManager.deviceId
            } else {
                Settings.Secure.getString(context!!.contentResolver, Settings.Secure.ANDROID_ID)
            }
        } catch (s: SecurityException) {
            Settings.Secure.getString(context!!.contentResolver, Settings.Secure.ANDROID_ID)
        }
        return deviceUniqueIdentifier
    }

}