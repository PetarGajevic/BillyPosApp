package com.wind.billypos.view.ui.certificate.viewmodel

import androidx.lifecycle.MutableLiveData
import com.wind.billypos.base.BaseViewModel
import com.wind.billypos.base.Error.Companion.ERROR
import com.wind.billypos.base.Error.Companion.SUCCESS
import com.wind.billypos.view.mapper.ViewBillyPosMapper
import com.wind.billypos.view.model.Company
import com.wind.billypos.view.ui.certificate.repository.CertificateRepository
import timber.log.Timber
import java.io.InputStream

class CertificateSetupViewModel(private val certificateRepository: CertificateRepository): BaseViewModel() {

    private val viewBillyPosMapper = ViewBillyPosMapper()

    val mutableLiveDataFiscalDigitalKey = MutableLiveData<Company.FiscalDigitalKey?>()

    fun readDigitalKey(digitalKeyInputStream: InputStream?, digitalKeyPass: String) {
        addCompositeDisposable(
            certificateRepository.readDigitalKey(
                digitalKeyInputStream = digitalKeyInputStream,
                digitalKeyPass = digitalKeyPass
            ), onSuccess = {
                val fiscalDigitalKey = it.copy()
                fiscalDigitalKey.code = SUCCESS
                mutableLiveDataFiscalDigitalKey.value = fiscalDigitalKey
            }, onError = {
                val error = Company.FiscalDigitalKey()
                error.code = ERROR
                mutableLiveDataFiscalDigitalKey.value = error
            }, handleError = false
        )
    }

    fun insertFiscalDigitalKey(fiscalDigitalKey: Company.FiscalDigitalKey) =
        addCompositeDisposable(
            certificateRepository.insertFiscalDigitalKey(
                fiscalDigitalKey =  viewBillyPosMapper.viewFiscalDigitalKeyMapper.toLocalModel(fiscalDigitalKey)
            ),
            onSuccess = {
                Timber.e("Ubacen fiscal key")
            },
            onError = {
                Timber.e("Greska ")
            }
        )
}