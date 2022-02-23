package com.wind.billypos.view.ui.settings

import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.wind.billypos.BR
import com.wind.billypos.R
import com.wind.billypos.base.BaseActivity
import com.wind.billypos.base.BaseFragment
import com.wind.billypos.databinding.ActivitySettingsBinding
import com.wind.billypos.view.ui.settings.viewmodel.SettingsViewModel
import kotlinx.android.synthetic.main.activity_settings.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class SettingsActivity : BaseActivity<ActivitySettingsBinding, SettingsViewModel>(),
    PreferenceFragmentCompat.OnPreferenceStartFragmentCallback {

    private val settingsViewModel: SettingsViewModel by viewModel()

    override val bindingVariable: Int
        get() = BR.settingsViewModel
    override val layoutId: Int
        get() = R.layout.activity_settings
    override val viewModel: SettingsViewModel
        get() = settingsViewModel


    private val TAG_TITLE = "settingsActivityTitle"
    private lateinit var navController: NavController


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.settingsNavHostFragment) as NavHostFragment
        navController = navHostFragment.findNavController()

        setSupportActionBar(toolbar_main)
        setupActionBarWithNavController(navController)
    }


    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        // Save current activity title so we can set it again after a configuration change
        outState.putCharSequence(TAG_TITLE, title)
    }


    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    override fun onPreferenceStartFragment(
        caller: PreferenceFragmentCompat?,
        pref: Preference?
    ): Boolean {
        // Initiate the new fragment
        val args = pref?.extras

        val fragment = pref?.fragment?.let {
            supportFragmentManager.fragmentFactory.instantiate(
                classLoader,
                it
            ).apply {
                arguments = args
                setTargetFragment(caller, 0)
            }
        }
        if (fragment != null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.settingsNavHostFragment, fragment)
                .addToBackStack(null)
                .commit()
        }
        return true
    }


}