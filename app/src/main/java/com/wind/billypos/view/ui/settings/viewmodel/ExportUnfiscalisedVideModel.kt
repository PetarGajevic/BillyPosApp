package com.wind.billypos.view.ui.settings.viewmodel

import androidx.lifecycle.MutableLiveData
import com.wind.billypos.base.BaseViewModel
import com.wind.billypos.view.mapper.ViewBillyPosMapper
import com.wind.billypos.view.model.CashBalance
import com.wind.billypos.view.model.TransactionHead
import com.wind.billypos.view.ui.settings.repository.ExportUnfiscalisedRepository

class ExportUnfiscalisedVideModel(private val exportUnfiscalisedRepository: ExportUnfiscalisedRepository) :
    BaseViewModel() {

        private val viewBillyPosMapper = ViewBillyPosMapper()

        val mutableLiveDataCountTransactionHead = MutableLiveData<Int>()
        val mutableLiveDataTransactionHeadList = MutableLiveData<List<TransactionHead>>()
        val mutableLiveDataCashBalanceList = MutableLiveData<List<CashBalance>>()

        fun findAllNotFiscalisedInvoices(){
            addCompositeDisposable(
                exportUnfiscalisedRepository.findAllNotFiscalisedInvoices().map{
                    viewBillyPosMapper.viewTransactionHeadMapper.toListOfModel(it)
                },
                onSuccess = {
                    mutableLiveDataTransactionHeadList.value = it
                    mutableLiveDataCountTransactionHead.value = it.size
                },
                onError = {
                    mutableLiveDataTransactionHeadList.value = listOf()
                    mutableLiveDataCountTransactionHead.value = 0
                }
            )
        }


    fun findAllNotFiscalisedCashBalance(){
        addCompositeDisposable(
            exportUnfiscalisedRepository.findAllNotFiscalisedCashBalance().map {
                 viewBillyPosMapper.viewCashBalanceMapper.toListOfModel(it)
            },
            onSuccess = {
                mutableLiveDataCashBalanceList.value = it
            },
            onError = {
                mutableLiveDataCashBalanceList.value
            }
        )
    }


}