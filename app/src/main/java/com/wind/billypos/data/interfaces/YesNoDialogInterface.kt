package com.wind.billypos.data.interfaces

import android.app.AlertDialog

interface YesNoDialogInterface{

    fun onConfirm(dialog: AlertDialog)

    fun onCancel(dialog: AlertDialog)

}