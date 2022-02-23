package com.wind.billypos.view.ui.invoice.viewmodel

import androidx.lifecycle.MutableLiveData
import com.wind.billypos.base.BaseViewModel
import com.wind.billypos.data.remote.mapper.BillyPosMapper
import com.wind.billypos.view.mapper.ViewBillyPosMapper
import com.wind.billypos.view.model.TransactionBody
import com.wind.billypos.view.ui.invoice.repository.TransactionRepository
import timber.log.Timber

class TransactionItemViewModel(private val transactionRepository: TransactionRepository): BaseViewModel() {

    val mutableLiveDataTransactionBody = MutableLiveData<TransactionBody>()
    val mutableLiveDataUpdate = MutableLiveData<Boolean>()
    val mutableLiveDataDelete = MutableLiveData<Int>()
    private val viewBillyPosMapper = ViewBillyPosMapper()

    fun setTransactionItem(trnBody: TransactionBody) {
        mutableLiveDataTransactionBody.value = trnBody
    }

    fun updateTransactionItem(trnBody: TransactionBody?){
        val trnItem = trnBody?.copy(
            total = trnBody.quantity?.times(trnBody.itemPrice!!),
            totalWithVat = trnBody.quantity?.times(trnBody.itemPriceWithDiscount!!)
        )
        addCompositeDisposable(
            transactionRepository.updateTransactionItem(
                trnBody = viewBillyPosMapper.viewTransactionBodyMapper.toLocalModel(trnItem)
            ),
            onSuccess = {
                Timber.e("Updejt")
                mutableLiveDataUpdate.value = true
            },
            onError = {
                mutableLiveDataUpdate.value = false
            }
        )
    }


    fun deleteTransactionItem(trnBody: TransactionBody?){
        addCompositeDisposable(
            transactionRepository.deleteTransactionItem(
                trnBody = viewBillyPosMapper.viewTransactionBodyMapper.toLocalModel(trnBody)
            ),
            onSuccess = {
                mutableLiveDataDelete.value = it
            }
        )
    }
}