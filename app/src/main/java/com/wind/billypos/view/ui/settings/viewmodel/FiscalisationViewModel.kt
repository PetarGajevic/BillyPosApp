package com.wind.billypos.view.ui.settings.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.wind.billypos.R
import com.wind.billypos.base.BaseViewModel
import com.wind.billypos.view.model.Company
import com.wind.billypos.view.model.Configuration
import com.wind.billypos.view.ui.settings.repository.FiscalisationRepository

class FiscalisationViewModel(private val fiscalisationRepository: FiscalisationRepository): BaseViewModel() {

    private val mutableLiveDataCompany = MutableLiveData<Company>()

//    init {
//        getCompany()
//    }
//
//     fun getCompany() {
//        addCompositeDisposable(
//            fiscalisationRepository.getCompany(),
//            onSuccess = {
//                mutableLiveDataCompany.value = it
//            }
//        )
//    }


}