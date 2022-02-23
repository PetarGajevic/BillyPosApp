package com.wind.billypos.view.ui.settings.viewmodel

import android.app.AlertDialog
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.fragment.findNavController
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceCategory
import androidx.preference.PreferenceFragmentCompat
import com.wind.billypos.R
import com.wind.billypos.data.interfaces.YesNoDialogInterface
import com.wind.billypos.utils.ConfirmAlert
import com.wind.billypos.utils.LocaleManager
import com.wind.billypos.utils.PreferenceHelper
import com.wind.billypos.utils.PreferenceHelper.clearValues2
import com.wind.billypos.utils.PreferenceHelper.company_nuis
import com.wind.billypos.utils.PreferenceHelper.configurations
import com.wind.billypos.utils.PreferenceHelper.lang
import com.wind.billypos.utils.PreferenceHelper.last_company_nuis
import com.wind.billypos.utils.extensions.navigateSafe
import com.wind.billypos.view.ui.MainActivity
import com.wind.billypos.view.ui.login.LoginActivity
import com.wind.billypos.view.ui.settings.SyncAndBackupFragment

class SettingsPreference : PreferenceFragmentCompat() {

    private lateinit var prefs: SharedPreferences

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings_preference, rootKey)
        prefs =
            PreferenceHelper.customPreference((activity as AppCompatActivity).applicationContext)

        val configuration = prefs.configurations

        findPreference<Preference>(resources.getString(R.string.logout_key))!!.setOnPreferenceClickListener {

            context?.let { it1 ->
                ConfirmAlert(it1, object : YesNoDialogInterface {
                    override fun onConfirm(dialog: AlertDialog) {
                        val prefs =
                            PreferenceHelper.customPreference((activity as AppCompatActivity).applicationContext)

                        prefs.clearValues2()
                        prefs.last_company_nuis = prefs.company_nuis;

                        //TODO: dont clear all configurations
//                        startActivity(
//                            Intent(activity, LoginActivity::class.java)
//                                .putExtra("finish", true)
//                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
//                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//                        )
                        val i = Intent(activity, LoginActivity::class.java)
                        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(i)
                    }

                    override fun onCancel(dialog: AlertDialog) {
                    }
                }).showDialog(
                    getString(R.string.logout_msg),
                    getString(R.string.attention)
                )
            }
            true
        }

        // Get configuration details for info preference
        val versionName = getVersionName()
        var summary = String.format(
            getString(R.string.app_information),
            configuration.company?.name,
            configuration.company?.nuis,
            configuration.address?.fullAddress,
            configuration.userData?.fullName,
            configuration.device?.tcrCode,
            configuration.license?.licenseKey,
            versionName
        )

        // Info Preference
        findPreference<Preference>("details_key")?.summary = summary
        findPreference<PreferenceCategory>("details_header")?.title = getString(R.string.app_name)

        // Fiscalisation Preference
        findPreference<Preference>("fiscal_header")!!.setOnPreferenceClickListener {
            findNavController().navigateSafe(SettingsPreferenceDirections.actionSettingsPreferenceToFiscalisationFragment())
            true
        }

        // Language Preference
        findPreference<ListPreference>("lang_list")!!.entries =
            resources.getStringArray(R.array.supported_languages)
        findPreference<ListPreference>("lang_list")!!.entryValues =
            resources.getStringArray(R.array.supported_languages_values)
        findPreference<ListPreference>("lang_list")!!.value = prefs.lang
        findPreference<ListPreference>("lang_list")!!.setOnPreferenceChangeListener { preference, newValue ->
            context?.let { LocaleManager.setNewLocale(it, newValue.toString()) }
            requireActivity().finish()
            requireActivity().startActivity(requireActivity().intent)
            true
        }

        // Sync And Backup Preference
        findPreference<Preference>("sync_and_backup")!!.setOnPreferenceClickListener {
            findNavController().navigateSafe(SettingsPreferenceDirections.actionSettingsPreferenceToSyncAndBackupFragment())
            true
        }

        // Export Unfiscalised Preference
        findPreference<Preference>("export_unfiscalised")!!.setOnPreferenceClickListener {
            findNavController().navigateSafe(SettingsPreferenceDirections.actionSettingsPreferenceToExportUnfiscalisedFragment())
            true
        }

        // Sync Tool
        findPreference<Preference>("sync_header")!!.setOnPreferenceClickListener {
            findNavController().navigateSafe(SettingsPreferenceDirections.actionSettingsPreferenceToSyncFragment())
            true
        }

    }


    private fun getVersionName(): String {
        try {
            if (activity == null) {
                throw PackageManager.NameNotFoundException("Context is null")
            }
            val pInfo =
                requireActivity().packageManager.getPackageInfo(requireActivity().packageName, 0)
            return pInfo.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return "1.0"
    }
}