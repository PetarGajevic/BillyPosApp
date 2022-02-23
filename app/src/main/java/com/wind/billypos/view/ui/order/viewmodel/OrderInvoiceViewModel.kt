package com.wind.billypos.view.ui.order.viewmodel

import androidx.lifecycle.MutableLiveData
import com.wind.billypos.base.BaseViewModel
import com.wind.billypos.data.local.model.LocalTransactionHead
import com.wind.billypos.view.mapper.ViewBillyPosMapper
import com.wind.billypos.view.model.EntityPoint
import com.wind.billypos.view.ui.order.repository.OrderInvoiceRepository
import io.reactivex.Maybe
import timber.log.Timber

class OrderInvoiceViewModel(private val orderInvoiceRepository: OrderInvoiceRepository): BaseViewModel() {

    private val viewBillyPosMapper = ViewBillyPosMapper()

    val mutableLiveDataEntityPointList = MutableLiveData<List<EntityPoint>>()
    val mutableLiveDataEntityPointIsOpen = MutableLiveData<String>()

    fun getEntityPoints(){
        addCompositeDisposable(
            orderInvoiceRepository.getEntityPoints().map {
                viewBillyPosMapper.viewEntityPointMapper.toListOfModel(it)
            },
            onSuccess = {
                mutableLiveDataEntityPointList.value = it
            }
        )
    }

    fun checkFreeEntityPoints(entityPointId: String?) {
        addCompositeDisposable(
            orderInvoiceRepository.checkFreeEntityPoints(entityPointId = entityPointId).map {
                viewBillyPosMapper.viewTransactionHeadMapper.toModel(it)
            },
            onSuccess = {
                mutableLiveDataEntityPointIsOpen.value = it.idEntity
            },
            onError = {
                mutableLiveDataEntityPointIsOpen.value = "error"
            }
        )
    }


}