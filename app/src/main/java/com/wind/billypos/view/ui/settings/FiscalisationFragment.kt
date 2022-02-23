package com.wind.billypos.view.ui.settings

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.EditTextPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.wind.billypos.R
import com.wind.billypos.utils.PreferenceHelper
import com.wind.billypos.utils.PreferenceHelper.certFile
import com.wind.billypos.utils.PreferenceHelper.certPIN
import com.wind.billypos.utils.PreferenceHelper.configurations
import com.wind.billypos.view.ui.settings.viewmodel.FiscalisationViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber


class FiscalisationFragment: PreferenceFragmentCompat() {

    private val fiscalisationViewModel: FiscalisationViewModel by viewModel()
    private val requestPermission = registerForActivityResult(ActivityResultContracts.RequestPermission()){ granted ->
        Timber.e(granted.toString())
    }
    private lateinit var syncPref: SharedPreferences
    private var fiscalisationFilePreference: Preference? = null
    private var fiscalisationPINPreference: EditTextPreference? = null
    private var fiscalisationDevicePreference: Preference? = null

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.fiscalisation_preferences, rootKey)

        syncPref = PreferenceHelper.customPreference((activity as AppCompatActivity))
        fiscalisationFilePreference = this.findPreference<Preference>("fiscalisation_certificate_key")!!
        fiscalisationPINPreference = this.findPreference<EditTextPreference>("fiscalisation_certificate_pin_key")!!
        fiscalisationDevicePreference = this.findPreference<Preference>("fiscalisation_device_register_key")!!


        fiscalisationFilePreference!!.setOnPreferenceClickListener {
            openFileManager()
            true
        }

        fiscalisationPINPreference?.setOnPreferenceChangeListener { preference, newValue ->

            true
        }
    }


//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == PICKFILE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
//            if (data != null && data.data != null) {
//                val fileChosen = data.dataString
//                try {
//                    syncPref.certFile = fileChosen
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//                        data.data?.let { requireActivity().contentResolver.takePersistableUriPermission(it, Intent.FLAG_GRANT_READ_URI_PERMISSION) }
//                    }
//
//                    if (syncPref.certPIN != "") {
////                      //  var util = FiscalizationCertificationUtil(activity, syncPref.certFile, syncPref.certPIN)
////                        if (!util.isCertReadable) {
////                            Toast.makeText(activity, R.string.cert_not_supported_format, Toast.LENGTH_SHORT).show()
////                            syncPref.certFile = ""
////                        }
////                        if (!util.isPinValid) {
////                            Toast.makeText(activity, R.string.cert_not_valid, Toast.LENGTH_SHORT).show()
////                            syncPref.certFile = ""
////                        }
//                    }
//                } catch (e: Exception) {
//                    Timber.e("Error pfx file : Greska pri uzimanju fajla")
//                }
//            }
//        }
//    }

    fun updateView() {
        if (syncPref.certFile?.isNotEmpty() == true) {
            fiscalisationFilePreference?.summary = activity?.getString(R.string.certificate_is_selected)
            fiscalisationFilePreference?.setIcon(R.drawable.ic_round_check_24px)
        } else {
            fiscalisationFilePreference?.summary = activity?.getString(R.string.certificate_is_not_selected)
            fiscalisationFilePreference?.setIcon(R.drawable.ic_round_close_red_24px)
        }

        if (syncPref.certPIN?.isNotEmpty() == true) {
            fiscalisationPINPreference?.summary = activity?.getString(R.string.certificate_pin_filled)
            fiscalisationPINPreference?.setIcon(R.drawable.ic_round_check_24px)
        } else {
            fiscalisationPINPreference?.summary = activity?.getString(R.string.certificate_pin_not_filled)
            fiscalisationPINPreference?.setIcon(R.drawable.ic_round_close_red_24px)
        }

        if (syncPref.configurations.device?.tcrCode != null) {
            fiscalisationDevicePreference?.summary = activity?.getString(R.string.device_setted_up) + ": " + syncPref.configurations.device!!.tcrCode
            fiscalisationDevicePreference?.setIcon(R.drawable.ic_round_check_24px)
        } else {
            fiscalisationDevicePreference?.summary = activity?.getString(R.string.device_not_setted_up)
            fiscalisationDevicePreference?.setIcon(R.drawable.ic_round_close_red_24px)
        }
    }


    private fun openFileManager() {
        requestPermission.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        val intent = Intent()
        intent.type = "*/*"
        intent.action = Intent.ACTION_GET_CONTENT
        resultLauncher.launch(intent)
    }



    @SuppressLint("TimberArgCount")
    var resultLauncher = registerForActivityResult(StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // There are no request codes
            val data: Intent? = result.data
            if (data != null) {
                Timber.e("DATA0: " , data)
                Timber.e("DATA1: " , data.dataString)
                Timber.e("DATA2: " , data.action)
                Timber.e("DATA3: " , data.clipData)
            }
        }
    }


    companion object{
        const val PICKFILE_REQUEST_CODE = 109
    }
}