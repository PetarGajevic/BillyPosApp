package com.wind.billypos.view.ui.setup

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import com.wind.billypos.BR
import com.wind.billypos.R
import com.wind.billypos.base.BaseActivity
import com.wind.billypos.data.remote.model.RemoteLicenceResponse
import com.wind.billypos.databinding.ActivityLicenceSetupBinding
import com.wind.billypos.view.model.License
import com.wind.billypos.view.ui.MainActivity
import com.wind.billypos.view.ui.certificate.CertificateSetupActivity
import com.wind.billypos.view.ui.setup.viewmodel.LicenceSetupViewModel
import kotlinx.android.synthetic.main.activity_licence_setup.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.threeten.bp.LocalDate
import timber.log.Timber


class LicenceSetupActivity: BaseActivity<ActivityLicenceSetupBinding, LicenceSetupViewModel>() {

    private val licenceSetupViewModel: LicenceSetupViewModel by viewModel()

    override val bindingVariable: Int
        get() = BR.licenceSetupViewModel
    override val layoutId: Int
        get() = R.layout.activity_licence_setup
    override val viewModel: LicenceSetupViewModel
        get() = licenceSetupViewModel

    private var hasLicense = false

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_licence_setup)

        val observerHasLicence = Observer<License?> {
            it?.let {
                val expiryDate: CharSequence? = it.expiryDate
                if(it.state == "UNAVAILABLE" && LocalDate.parse(expiryDate) > LocalDate.now()){
                    navigateToMainActivity()
                }else{
                    Timber.e("Nije usao u has licence")
                }
            }

        }


        licenseNextBtn.setOnClickListener { checkLicence() }


        val observerLicence = Observer<RemoteLicenceResponse?> {
            if(it.data?.state == "UNAVAILABLE" && it.status == "OK"){
                viewModel.insertLicence(licence = it.data)
            }else{
                editLicenseKey.error = getString(R.string.license_key_not_valid)
            }
        }

        val observerInsertLicence = Observer<Boolean> {
            if(it){
                if(hasLicense){
                    navigateToMainActivity()
                }else{
                    navigateToCertificateSetupActivity()
                }
            }else{
                Toast.makeText(this, getString(R.string.licence_insert_failed), Toast.LENGTH_LONG).show()
            }
        }

        val observerHasCertificate = Observer<Boolean> {
            hasLicense = it
        }



        viewModel.mutableLiveDataLicence.observe(this, observerLicence)
        viewModel.mutableLiveDataInsertLicence.observe(this, observerInsertLicence)
        viewModel.mutableLiveDataHasCertificate.observe(this, observerHasCertificate)
        viewModel.mutableLiveDataHasLicence.observe(this, observerHasLicence)

        viewModel.getLicence()
        viewModel.getCertificate()
    }




    private fun checkLicence(){
        if(editLicenseKey.text.isNullOrEmpty()){
            editLicenseKey.error = getString(R.string.license_empty)
            return
        }

        viewModel.checkLicence(licence = editLicenseKey.text.toString())
//        viewModel.checkLicence(licence = "W6GWtryR")
    }

    private fun navigateToMainActivity() {
        val myIntent = Intent(this@LicenceSetupActivity, MainActivity::class.java)
        this@LicenceSetupActivity.startActivity(myIntent)
        finish()
    }

    private fun navigateToCertificateSetupActivity() {
        val myIntent = Intent(this@LicenceSetupActivity, CertificateSetupActivity::class.java)
        this@LicenceSetupActivity.startActivity(myIntent)
        finish()
    }


}