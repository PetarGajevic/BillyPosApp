package com.wind.billypos.utils

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import com.wind.billypos.R
import com.wind.billypos.data.interfaces.YesNoDialogInterface
import kotlinx.android.synthetic.main.alert_dialog.view.*

class ConfirmAlert(val context: Context, private val yesNoInterface: YesNoDialogInterface) {

    var dissmissOnOk = true
    lateinit var mBuilder: AlertDialog.Builder
    lateinit var mAlertDialog: AlertDialog

    fun showDialog(msg: String, title: String): ConfirmAlert {

        val mDialogView = LayoutInflater.from(context).inflate(R.layout.alert_dialog, null)
        mBuilder = AlertDialog.Builder(context)
                .setView(mDialogView)
                .setTitle(title)
                .setMessage(msg)

        mAlertDialog = this.mBuilder.show()

        mDialogView.cancel.setOnClickListener {
            mAlertDialog.dismiss()
            yesNoInterface.onCancel(this.mAlertDialog)

        }


        mDialogView.yes.setOnClickListener {
            if(dissmissOnOk) {
                mAlertDialog.dismiss()
            }
            yesNoInterface.onConfirm(this.mAlertDialog)
        }

        return this
    }

    fun setDismissable(dissmiss: Boolean): ConfirmAlert {
        dissmissOnOk = dissmiss

        return this
    }


}