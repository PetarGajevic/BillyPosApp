package com.wind.billypos.view.ui.setup.viewmodel

import androidx.lifecycle.MutableLiveData
import com.wind.billypos.base.BaseViewModel
import com.wind.billypos.data.remote.mapper.BillyPosMapper
import com.wind.billypos.data.remote.model.RemoteLicenceResponse
import com.wind.billypos.data.remote.model.RemoteLicense
import com.wind.billypos.view.mapper.ViewBillyPosMapper
import com.wind.billypos.view.model.License
import com.wind.billypos.view.ui.setup.repository.LicenceSetupRepository
import timber.log.Timber

class LicenceSetupViewModel(private val licenceSetupRepository: LicenceSetupRepository): BaseViewModel() {


    val mutableLiveDataLicence = MutableLiveData<RemoteLicenceResponse?>()
    val mutableLiveDataInsertLicence = MutableLiveData<Boolean>()
    val mutableLiveDataHasCertificate = MutableLiveData<Boolean>()

    val mutableLiveDataHasLicence = MutableLiveData<License?>()

    val billyPosMapper = BillyPosMapper()
    val viewBillyPosMapper = ViewBillyPosMapper()

    fun checkLicence(licence: String){
        addCompositeDisposable(
            licenceSetupRepository.checkLicence(licence = licence),
            onSuccess = {
                Timber.e("Licence check %s", it.status)
                    mutableLiveDataLicence.value = it

            },
            onError = {
                Timber.e("Licence error")
                mutableLiveDataLicence.value = null
            }
        )
    }

    fun insertLicence(licence: RemoteLicense){
        addCompositeDisposable(
            licenceSetupRepository.insertLicence(licence = billyPosMapper.licenseMapper.toModel(licence)),
            onSuccess = {
                Timber.e("Long %s", it)
                mutableLiveDataInsertLicence.value = it>0
            },
            onError = {
                Timber.e("Error na insert")
                mutableLiveDataInsertLicence.value = false
            }
        )
    }

    fun getCertificate(){
        addCompositeDisposable(
            licenceSetupRepository.getCertificate(),
            onSuccess = {
                mutableLiveDataHasCertificate.value = true
            }, onError = {
                mutableLiveDataHasCertificate.value = false
            }
        )
    }

    fun getLicence() {
        addCompositeDisposable(
            licenceSetupRepository.getLicence().map { viewBillyPosMapper.viewLicenceMapper.toModel(it) },
            onSuccess = {
                mutableLiveDataHasLicence.value = it
            },
            onError = {
                mutableLiveDataHasLicence.value = null
            }
        )
    }

}