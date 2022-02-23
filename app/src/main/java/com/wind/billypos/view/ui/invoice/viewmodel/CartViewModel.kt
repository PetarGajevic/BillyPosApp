package com.wind.billypos.view.ui.invoice.viewmodel

import androidx.lifecycle.MutableLiveData
import com.wind.billypos.base.BaseViewModel
import com.wind.billypos.view.mapper.ViewBillyPosMapper
import com.wind.billypos.view.model.TransactionBody
import com.wind.billypos.view.model.TransactionHead
import com.wind.billypos.view.ui.invoice.repository.CartRepository
import timber.log.Timber

class CartViewModel(private val cartRepository: CartRepository): BaseViewModel() {

    val mutableLivaDataTransactionHead = MutableLiveData<TransactionHead?>()
    val mutableLiveDataCallTransactionBody = MutableLiveData<Boolean>()
    val mutableLiveDataTransactionBodyList = MutableLiveData<List<TransactionBody>>()
    val mutableLiveDataTransactionUpdatedBodyList = MutableLiveData<List<TransactionBody>>()
    val mutableLiveDataQuantity = MutableLiveData<Double>()
    val mutableLiveDataRefreshList = MutableLiveData<Boolean>()


    private val viewBillyPosMapper = ViewBillyPosMapper()

    fun setTransactionHead(trnHead: TransactionHead?) {
        mutableLivaDataTransactionHead.value = trnHead
      //  mutableLiveDataCallTransactionBody.value = true
    }

    fun setUpdatedTransactionHead(trnHead: TransactionHead?) {
        mutableLivaDataTransactionHead.value = trnHead
        Timber.e("Head Updated %s", trnHead)
        insertUpdatedTransactions(trnHead = trnHead)
    }

    fun insertTransactionHead(trnHead: TransactionHead?) {
        addCompositeDisposable(
            cartRepository.insertTransactionHead(
                trnHead = viewBillyPosMapper.viewTransactionHeadMapper.toLocalModel(trnHead)
            ),
            onSuccess = {
                mutableLiveDataRefreshList.value = true
            }
        )
    }
    private fun insertUpdatedTransactions(trnHead: TransactionHead?){
            addCompositeDisposable(
                cartRepository.insertTransactionHead(
                    trnHead = viewBillyPosMapper.viewTransactionHeadMapper.toLocalModel(trnHead)
                ),
                onSuccess = {
                    mutableLiveDataRefreshList.value = true
                }
            )
    }


    fun getTransactionHead() = mutableLivaDataTransactionHead.value

    fun getTransactionHeadUUID() = mutableLivaDataTransactionHead.value?.uuid

    fun getTransactions(transactionUUID: String?){
        addCompositeDisposable(
            cartRepository.getTransactions(transactionUUID = transactionUUID)
                .map { viewBillyPosMapper.viewTransactionBodyMapper.toListOfModel(it) },
            onSuccess = { list ->
                mutableLiveDataTransactionBodyList.value = list
            },
            onError = {
                Timber.e("Error")
            }
        )
    }

    fun getUpdatedTransactions(transactionUUID: String?){
        addCompositeDisposable(
            cartRepository.getTransactions(transactionUUID = transactionUUID)
                .map { viewBillyPosMapper.viewTransactionBodyMapper.toListOfModel(it) },
            onSuccess = { list ->
                mutableLiveDataTransactionUpdatedBodyList.value = list
            },
            onError = {
                Timber.e("Error")
            }
        )
    }

}