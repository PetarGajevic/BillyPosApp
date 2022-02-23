package com.wind.billypos.view.ui.invoice.viewmodel

import androidx.lifecycle.MutableLiveData
import com.wind.billypos.base.BaseViewModel
import com.wind.billypos.view.model.TransactionHead

class CartDoneViewModel: BaseViewModel() {

     val mutableLiveDataTransactionHead = MutableLiveData<TransactionHead>()

    fun setTransactionHead(trnHead: TransactionHead){
        mutableLiveDataTransactionHead.value = trnHead
    }

}