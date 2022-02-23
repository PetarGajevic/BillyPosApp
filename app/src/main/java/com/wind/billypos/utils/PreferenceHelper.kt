package com.wind.billypos.utils

import com.wind.billypos.view.model.Configuration
import com.wind.billypos.view.model.Synchronization
import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object PreferenceHelper {

    val USER_ID = "USER_ID"
    val COMPANY_ID = "COMPANY_ID"
    val USER_EMAIL = "USER_EMAIL"
    val USER_PASSWORD = "USER_PASSWORD"
    val USER_DEVICEID = "USER_DEVICEID"
    val USER_TOKEN = "USER_TOKEN"
    val COMPANY_NUIS = "COMPANY_NUIS"
    val LAST_COMPANY_NUIS = "LAST_COMPANY_NUIS"
    val CONFIGURATION_DETAILS = "CONFIGURATION_DETAILS"
    val SYNC_DETAILS = "SYNC_DETAILS"

    val CUSTOM_PREF_NAME = "USER_DATA"
    val CUSTOM_PREF_LANG = "USER_LANG"
    val CUSTOM_PREF_PRINTER_NAME = "PRINTER_NAME"
    val CUSTOM_PREF_USB_PRINTER_NAME = "USB_PRINTER_NAME"
    val CUSTOM_PREF_TYPE_OF_PRINTER = "TYPE_OF_PRINTER" //usb, bluetooth
    val CUSTOM_PREF_PRINTER_ADDRESS = "PRINTER_ADDRESS"
    val CUSTOM_PREF_CERT_FILENAME = "PREF_CERT_FILENAME"
    val CUSTOM_PREF_CERT_PIN = "PREF_CERT_PIN"
    val CUSTOM_PREF_AUTO_PRINT_INVOICE = "PREF_AUTO_PRINT_INVOICE"
    val CUSTOM_PREF_PRINT_WITH_USB = "PREF_PRINT_WITH_USB"

    fun defaultPreference(context: Context): SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    fun customPreference(context: Context, name: String): SharedPreferences = context.getSharedPreferences(name, Context.MODE_PRIVATE)
    fun customPreference(context: Context): SharedPreferences = context.getSharedPreferences(CUSTOM_PREF_NAME, Context.MODE_PRIVATE)
    fun customPreference(activity: FragmentActivity): SharedPreferences = (activity as AppCompatActivity).applicationContext.getSharedPreferences(CUSTOM_PREF_NAME, Context.MODE_PRIVATE)
    fun customPreference(activity: AppCompatActivity): SharedPreferences = activity.applicationContext.getSharedPreferences(CUSTOM_PREF_NAME, Context.MODE_PRIVATE)

    inline fun SharedPreferences.editMe(operation: (SharedPreferences.Editor) -> Unit) {
        val editMe = edit()
        operation(editMe)
        editMe.apply()
    }


    var SharedPreferences.userId
        get() = getLong(USER_ID, 0)
        set(value) {
            editMe {
                it.putLong(USER_ID, value)
            }
        }
    var SharedPreferences.companyId
        get() = getLong(COMPANY_ID, 0)
        set(value) {
            editMe {
                it.putLong(COMPANY_ID, value)
            }
        }

    var SharedPreferences.password
        get() = getString(USER_PASSWORD, "")
        set(value) {
            editMe {
                it.putString(USER_PASSWORD, value)
            }
        }

    var SharedPreferences.deviceId
        get() = getString(USER_DEVICEID, "")
        set(value) {
            editMe {
                it.putString(USER_DEVICEID, value)
            }
        }

    var SharedPreferences.token
        get() = getString(USER_TOKEN, "")
        set(value) {
            editMe {
                it.putString(USER_TOKEN, value)
            }
        }

    var SharedPreferences.company_nuis
        get() = getString(COMPANY_NUIS, "")
        set(value) {
            editMe {
                it.putString(COMPANY_NUIS, value)
            }
        }

    var SharedPreferences.last_company_nuis
        get() = getString(LAST_COMPANY_NUIS, "")
        set(value) {
            editMe {
                it.putString(LAST_COMPANY_NUIS, value)
            }
        }
    var SharedPreferences.lang
        get() = getString(CUSTOM_PREF_LANG, "en")
        set(value) {
            editMe {
                it.putString(CUSTOM_PREF_LANG, value)
            }
        }

    var SharedPreferences.pairedPrinterName
        get() = getString(CUSTOM_PREF_PRINTER_NAME, "")
        set(value) {
            editMe {
                it.putString(CUSTOM_PREF_PRINTER_NAME, value)
            }
        }

    var SharedPreferences.pairedUSBPrinterName
        get() = getString(CUSTOM_PREF_USB_PRINTER_NAME, "")
        set(value) {
            editMe {
                it.putString(CUSTOM_PREF_USB_PRINTER_NAME, value)
            }
        }

    var SharedPreferences.typeOfPrinter
        get() = getString(CUSTOM_PREF_TYPE_OF_PRINTER, "")
        set(value) {
            editMe {
                it.putString(CUSTOM_PREF_TYPE_OF_PRINTER, value)
            }
        }

    var SharedPreferences.printAutoInvoice
        get() = getBoolean(CUSTOM_PREF_AUTO_PRINT_INVOICE, true)
        set(value) {
            editMe {
                it.putBoolean(CUSTOM_PREF_AUTO_PRINT_INVOICE, value)
            }
        }

    var SharedPreferences.printWithUSB
        get() = getBoolean(CUSTOM_PREF_PRINT_WITH_USB, true)
        set(value) {
            editMe {
                it.putBoolean(CUSTOM_PREF_PRINT_WITH_USB, value)
            }
        }
    var SharedPreferences.pairedPrinterAddress
        get() = getString(CUSTOM_PREF_PRINTER_ADDRESS, "")
        set(value) {
            editMe {
                it.putString(CUSTOM_PREF_PRINTER_ADDRESS, value)
            }
        }

    var SharedPreferences.certFile
        get() = getString(CUSTOM_PREF_CERT_FILENAME, "")
        set(value) {
            editMe {
                it.putString(CUSTOM_PREF_CERT_FILENAME, value)
            }
        }

    var SharedPreferences.certPIN
        get() = getString(CUSTOM_PREF_CERT_PIN, "")
        set(value) {
            editMe {
                it.putString(CUSTOM_PREF_CERT_PIN, value)
            }
        }

    var SharedPreferences.configurations: Configuration
        get() = run {
            var configs = getString(CONFIGURATION_DETAILS, "{}")
            val gson = Gson()
            val type = object : TypeToken<Configuration>() {}.type
            return gson.fromJson(configs, type)
        }
        set(obj) {
            val gson = Gson()
            val json = gson.toJson(obj)
            editMe {
                it.putString(CONFIGURATION_DETAILS, json)
            }
        }
    var SharedPreferences.sync: Synchronization
        get() = run {
            var configs = getString(SYNC_DETAILS, "{}")
            val gson = Gson()
            val type = object : TypeToken<Synchronization>() {}.type
            return gson.fromJson(configs, type)
        }
        set(obj) {
            val gson = Gson()
            val json = gson.toJson(obj)
            editMe {
                it.putString(SYNC_DETAILS, json)
            }
        }

    var SharedPreferences.clearValues: () -> Unit
        @SuppressLint("ApplySharedPref")
        get() = {
            edit().remove(USER_ID)
                .remove(USER_DEVICEID)
                .remove(USER_EMAIL)
                .remove(USER_TOKEN)
                .remove(CONFIGURATION_DETAILS)
                .remove(CUSTOM_PREF_CERT_FILENAME)
                .remove(CUSTOM_PREF_CERT_PIN)
                .remove(CUSTOM_PREF_LANG)
                .remove(CUSTOM_PREF_PRINTER_ADDRESS)
                .remove(CUSTOM_PREF_PRINTER_NAME)
                .remove(CUSTOM_PREF_AUTO_PRINT_INVOICE)
                .remove(SYNC_DETAILS)
                .clear()
                .commit()
        }
        set(value) {
            editMe {
                it.remove(USER_ID)
                it.remove(USER_DEVICEID)
                it.remove(USER_EMAIL)
                it.remove(USER_TOKEN)
                it.remove(CONFIGURATION_DETAILS)
                it.remove(CUSTOM_PREF_CERT_FILENAME)
                it.remove(CUSTOM_PREF_CERT_PIN)
                it.remove(CUSTOM_PREF_LANG)
                it.remove(CUSTOM_PREF_PRINTER_ADDRESS)
                it.remove(CUSTOM_PREF_PRINTER_NAME)
                it.remove(CUSTOM_PREF_AUTO_PRINT_INVOICE)
                it.remove(SYNC_DETAILS)
                it.clear()
                it.commit()
            }
        }

    var SharedPreferences.clearValues2: () -> Unit
        @SuppressLint("ApplySharedPref")
        get() = {
            edit().remove(USER_ID)
                .remove(USER_DEVICEID)
                .remove(USER_EMAIL)
                .remove(USER_TOKEN)
                .remove(CUSTOM_PREF_CERT_PIN)
                .commit()
        }
        set(value) {
            editMe {
                it.remove(USER_ID)
                it.remove(USER_DEVICEID)
                it.remove(USER_EMAIL)
                it.remove(USER_TOKEN)
                it.remove(CUSTOM_PREF_CERT_PIN)
                it.remove(CONFIGURATION_DETAILS)
                it.commit()
            }
        }

}