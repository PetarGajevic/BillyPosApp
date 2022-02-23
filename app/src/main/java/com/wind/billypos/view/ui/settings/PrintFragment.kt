package com.wind.billypos.view.ui.settings

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.wind.billypos.R


class PrintFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.printer_preferences, rootKey)



    }


}