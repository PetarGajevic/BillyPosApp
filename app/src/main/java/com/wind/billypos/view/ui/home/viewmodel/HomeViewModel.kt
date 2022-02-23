package com.wind.billypos.view.ui.home.viewmodel

import androidx.lifecycle.MutableLiveData
import com.wind.billypos.base.BaseViewModel
import com.wind.billypos.data.dao.CashBalanceDao
import com.wind.billypos.utils.DeviceId
import com.wind.billypos.view.mapper.ViewBillyPosMapper
import com.wind.billypos.view.model.License
import com.wind.billypos.view.ui.home.HomeRepository
import org.threeten.bp.LocalDate
import timber.log.Timber

class HomeViewModel(private val homeRepository: HomeRepository) : BaseViewModel() {

    private val viewBillyPosMapper: ViewBillyPosMapper = ViewBillyPosMapper()

    val mutableLiveDataHasOpenedDay = MutableLiveData<Boolean>()
    val mutableLiveDataHasLicence = MutableLiveData<License?>()

    fun hasOpenedDay(deviceId: String?) {
        addCompositeDisposable(
            homeRepository.hasOpenedDay(deviceId = deviceId)
                .map { viewBillyPosMapper.viewCashBalanceMapper.toModel(it) },
            onSuccess = {
                Timber.e("Cash balance Datum %s %s", it.cashDepositDate, LocalDate.now())
                mutableLiveDataHasOpenedDay.value = it != null
            },
            onError = {
                mutableLiveDataHasOpenedDay.value = false
            }
        )
    }

    fun getLicence() {
        addCompositeDisposable(
            homeRepository.getLicence().map { viewBillyPosMapper.viewLicenceMapper.toModel(it) },
            onSuccess = {
                mutableLiveDataHasLicence.value = it
            },
            onError = {
                mutableLiveDataHasLicence.value = null
            }
        )
    }


}