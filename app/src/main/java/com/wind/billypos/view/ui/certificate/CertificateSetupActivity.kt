package com.wind.billypos.view.ui.certificate

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import com.wind.billypos.BR
import com.wind.billypos.R
import com.wind.billypos.base.BaseActivity
import com.wind.billypos.databinding.ActivityCertificateSetupBinding
import com.wind.billypos.view.model.Company
import com.wind.billypos.view.ui.certificate.viewmodel.CertificateSetupViewModel
import kotlinx.android.synthetic.main.activity_certificate_setup.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class CertificateSetupActivity: BaseActivity<ActivityCertificateSetupBinding, CertificateSetupViewModel>() {

    private val certificateSetupViewModel: CertificateSetupViewModel by viewModel()

    private lateinit var fiscalDigitalKeyUri: Uri

    override val bindingVariable: Int
        get() = BR.certificateViewModel
    override val layoutId: Int
        get() = R.layout.activity_certificate_setup
    override val viewModel: CertificateSetupViewModel
        get() = certificateSetupViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_certificate_setup)

        val observerFiscalDigitalKey = Observer<Company.FiscalDigitalKey?> {
            it?.let {
                if (it.hasChanges()) {
                    when {
                        it.hasError() -> {
                          //  viewModel.onDigitalKeyInvalid()
                        }
                        it.isSuccess() -> {
                            viewModel.insertFiscalDigitalKey(it)
                        }
                    }
                }
            }
        }

        editSelectCertificate.setOnClickListener {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.type = "*/*"
            resultLauncher.launch(intent)
        }

        certificateNextBtn.setOnClickListener {
            viewModel.readDigitalKey(
                digitalKeyInputStream = contentResolver.openInputStream(
                    fiscalDigitalKeyUri
                ),
                digitalKeyPass = editCertificatePIN.text.toString()
            )
        }

        viewModel.mutableLiveDataFiscalDigitalKey.observe(
            this,
            observerFiscalDigitalKey
        )

    }

    var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // There are no request codes
            val data: Intent? = result.data
            if (data != null) {
                fiscalDigitalKeyUri = data.data ?: Uri.EMPTY
            }
        }
    }
}