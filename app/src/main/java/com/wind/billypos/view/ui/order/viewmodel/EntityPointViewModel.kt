package com.wind.billypos.view.ui.order.viewmodel

import androidx.lifecycle.MutableLiveData
import com.wind.billypos.base.BaseViewModel
import com.wind.billypos.data.local.model.LocalEntityPoint
import com.wind.billypos.view.mapper.ViewBillyPosMapper
import com.wind.billypos.view.model.EntityPoint
import com.wind.billypos.view.model.TransactionHead
import com.wind.billypos.view.ui.order.repository.EntityPointRepository
import io.reactivex.Maybe
import timber.log.Timber

class EntityPointViewModel(private val entityPointRepository: EntityPointRepository) :
    BaseViewModel() {

    private val viewBillyPosMapper: ViewBillyPosMapper = ViewBillyPosMapper()

    val mutableLiveDataHasOpenedDay = MutableLiveData(false)
    val mutableLiveDataEntityTransactionList = MutableLiveData<List<TransactionHead>>()
    val mutableLiveDataInvoiceNum = MutableLiveData<Long>()

    fun hasOpenDay() {
        addCompositeDisposable(
            entityPointRepository.hasOpenedDay()
                .map { viewBillyPosMapper.viewCashBalanceMapper.toModel(it) },
            onSuccess = {
                mutableLiveDataHasOpenedDay.value = true
            },
            onError = {
                mutableLiveDataHasOpenedDay.value = false
            }
        )
    }

    fun getEntityTransactions(idEntity: String?){
        addCompositeDisposable(
            entityPointRepository.getEntityTransactions(idEntity = idEntity).map {
                viewBillyPosMapper.viewTransactionHeadMapper.toListOfModel(it)
            },
            onSuccess = {
                mutableLiveDataEntityTransactionList.value = it
            },
            onError = {
                mutableLiveDataEntityTransactionList.value = listOf()
            }
        )
    }

    fun getLastInvoiceNum(){
        addCompositeDisposable(
            entityPointRepository.getLastInvoiceNum()
            , onSuccess = {
                Timber.e("Last Num %s" , it.inc() )
                mutableLiveDataInvoiceNum.value = it.inc()
            }
            , onError = {
                Timber.e("Last Num Error %s" , "1" )
                mutableLiveDataInvoiceNum.value = 1
            }
        )
    }

}