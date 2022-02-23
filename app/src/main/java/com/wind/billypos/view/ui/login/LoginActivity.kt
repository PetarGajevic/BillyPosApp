package com.wind.billypos.view.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import com.wind.billypos.BR
import com.wind.billypos.R
import com.wind.billypos.base.BaseActivity
import com.wind.billypos.databinding.ActivityLoginBinding
import com.wind.billypos.utils.DeviceId
import com.wind.billypos.utils.PreferenceHelper.customPreference
import com.wind.billypos.utils.PreferenceHelper.deviceId
import com.wind.billypos.utils.PreferenceHelper.token
import com.wind.billypos.view.model.Configuration
import com.wind.billypos.view.model.License
import com.wind.billypos.view.model.LoginRequest
import com.wind.billypos.view.ui.MainActivity
import com.wind.billypos.view.ui.login.viewmodel.LoginViewModel
import com.wind.billypos.view.ui.setup.LicenceSetupActivity
import kotlinx.android.synthetic.main.activity_login.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber


class LoginActivity : BaseActivity<ActivityLoginBinding, LoginViewModel>() {

    private val loginViewModel: LoginViewModel by viewModel()

    private var licence: License? = null

    override val bindingVariable: Int
        get() = BR.loginViewModel
    override val layoutId: Int
        get() = R.layout.activity_login
    override val viewModel: LoginViewModel
        get() = loginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val pref = customPreference(applicationContext)

        Timber.e("Device id: %s", DeviceId(this).myDeviceId())
        Timber.e("Token: %s", pref.token)

        val observerLoginRequest = Observer<LoginRequest> {
            it?.let {
                if (it.hasChanges()) {
                    when {
                        it.isSuccess() -> {
                            customPreference(activity = this).deviceId = DeviceId(this).myDeviceId()
                            Timber.e("Usao u success")
                        }
                        it.isEmailInvalid() -> {
                            Timber.e("Usao u email")
                            enableClick()
                            tilEmail.error = getString(R.string.error_invalid_email)
                        }
                        it.isPasswordInvalid() -> {
                            Timber.e("Usao u password")
                            enableClick()
                            tilPassword.error = getString(R.string.error_invalid_password)
                        }
                        it.areDataCorrect() -> {
                            viewModel.login(loginRequest = it)
//                            navigateToMainActivity()
//                            finish()
                        }
                    }

                }
            }
        }

        email_sign_in_button.setOnClickListener { attemptLogin() }

        val observerError = Observer<Throwable> {
            Timber.e("Error %s", it)
            if (it != null) {
                enableClick()
                Toast.makeText(this, getString(R.string.network_unavailable), Toast.LENGTH_LONG).show()
            }
        }

        val observerToken = Observer<String?> { token ->
            pref.token = token
            viewModel.getConfiguration(authToken = token)
        }

        val observerHasLicence = Observer<License?> {
            licence = it
        }

        val observerConfiguration = Observer<Configuration> {
            //  TODO Maknuti komentar nakon testiranja

            //            if(it.device?.tcrCode.isNullOrEmpty()){
//                Toast.makeText(this, getString(R.string.TCR_register_fail), Toast.LENGTH_LONG).show()
//            }else{
//                viewModel.insertConfiguration(configuration = it)
//            }

            viewModel.insertConfiguration(configuration = it)
        }

        val observerLogin = Observer<Boolean> {
            it?.let {
                if (it) {
//                    if (licence != null) {
                        navigateToMainActivity()
//                    } else {
//                        navigateToLicenceSetupActivity()
//                    }
                } else {
                    Toast.makeText(
                        this,
                        getString(R.string.configuration_failed),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
            enableClick()
        }

        viewModel.mutableLiveDataLoginRequest.observe(this, observerLoginRequest)
        viewModel.mutableLiveDataErrorHandler.observe(this, observerError)
        viewModel.mutableLiveDataToken.observe(this, observerToken)
        viewModel.mutableLiveDataHasLicence.observe(this, observerHasLicence)
        viewModel.mutableLiveDataConfiguration.observe(this, observerConfiguration)
        viewModel.mutableLiveDataLogin.observe(this, observerLogin)

        viewModel.getLicence()
    }


    private fun attemptLogin() {
        disableClick()
        viewModel.setLoginRequest(
            LoginRequest(
                username = "TestIOS", // email.text.toString(),,
                password = "1234",// password.text.toString(),
                deviceId = "00000000" // DeviceId(this).myDeviceId()
            )
        )
    }

    private fun navigateToMainActivity() {
        val myIntent = Intent(this@LoginActivity, MainActivity::class.java)
        this@LoginActivity.startActivity(myIntent)
        finish()
    }

    private fun navigateToLicenceSetupActivity() {
        val myIntent = Intent(this@LoginActivity, LicenceSetupActivity::class.java)
        this@LoginActivity.startActivity(myIntent)
        finish()
    }


    override fun onBackPressed() {
        val a = Intent(Intent.ACTION_MAIN)
        a.addCategory(Intent.CATEGORY_HOME)
        a.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(a)
    }

    private fun enableClick() {
        Timber.e("Enable")
        email_sign_in_button.isEnabled = true
        loginProgressBar.visibility = View.GONE
    }

    private fun disableClick() {
        Timber.e("Disable")
        email_sign_in_button.isEnabled = false
        loginProgressBar.visibility = View.VISIBLE
    }


}
