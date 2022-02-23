package com.wind.billypos.view.ui.invoice.viewmodel

import androidx.lifecycle.MutableLiveData
import com.wind.billypos.base.BaseViewModel
import com.wind.billypos.data.remote.mapper.BillyPosMapper
import com.wind.billypos.view.mapper.ViewBillyPosMapper
import com.wind.billypos.view.model.TransactionBody
import com.wind.billypos.view.model.TransactionHead
import com.wind.billypos.view.ui.invoice.repository.PrintInvoiceRepository

class PrintInvoiceViewModel(private val printInvoiceRepository: PrintInvoiceRepository): BaseViewModel() {

    private val viewBillyPosMapper = ViewBillyPosMapper()

    val mutableLiveDataItems = MutableLiveData<List<TransactionBody>>()
    val mutableLiveDataQuantity = MutableLiveData<Double>()
    val mutableLiveDataTransactionHead = MutableLiveData<TransactionHead>()

    fun setTransactionHead(trnHead: TransactionHead){
        mutableLiveDataTransactionHead.value = trnHead
    }

    fun getItems(trnHeadUUID: String){
        addCompositeDisposable(
            printInvoiceRepository.getItems(trnHeadUUID = trnHeadUUID)
                .map { viewBillyPosMapper.viewTransactionBodyMapper.toListOfModel(it) }
                , onSuccess = { list ->
                mutableLiveDataItems.value = list
                mutableLiveDataQuantity.value = list.sumOf { it.quantity!! }
            }
        )
    }

}