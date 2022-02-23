package com.wind.billypos.view.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.wind.billypos.R
import com.wind.billypos.utils.PreferenceHelper.customPreference
import com.wind.billypos.utils.PreferenceHelper.token
import com.wind.billypos.view.ui.login.LoginActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController

    /*==============================================================================================
    ======================================BLUETOOTH PART============================================
    ==============================================================================================*/
    val PERMISSION_BLUETOOTH = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val pref = customPreference(applicationContext)

        // TODO maknuti komentar nakon testiranja
//        if(pref.token.isNullOrEmpty()){
//            startActivity(Intent(this@MainActivity, LoginActivity::class.java))
//            finish()
//        }



        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        navController = navHostFragment.findNavController()

        // Remove back arrow from Cart Done Fragment
        val appBarConfiguration = AppBarConfiguration
            .Builder(R.id.cartDoneFragment, R.id.homeFragment)
            .build()

        setSupportActionBar(toolbar_main)
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

//    override fun onBackPressed() {
//        val fragment =
//            this.supportFragmentManager.findFragmentById(R.id.navHostFragment)
//        (fragment as? IOnBackPressed)?.onBackPressed()?.not()?.let {
//            super.onBackPressed()
//        }
//    }


}