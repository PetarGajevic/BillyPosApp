package com.wind.billypos.view.ui.customer.viewmodel

import androidx.lifecycle.MutableLiveData
import com.wind.billypos.base.BaseViewModel
import com.wind.billypos.base.Error
import com.wind.billypos.view.mapper.ViewBillyPosMapper
import com.wind.billypos.view.model.Customer
import com.wind.billypos.view.ui.customer.repository.CustomerRepository
import timber.log.Timber

class AddCustomerViewModel(private val customerRepository: CustomerRepository): BaseViewModel() {

    private val viewBillyPosMapper = ViewBillyPosMapper()

    val mutableLiveDataCustomer = MutableLiveData<Customer>()
    val mutableLiveDataSync = MutableLiveData<Boolean>()
    val mutableLiveDataUpdated = MutableLiveData<Boolean>()

    fun setCustomer(customer: Customer){
        customer.code = Error.INITIALIZE
        mutableLiveDataCustomer.value = customer
    }

    fun getCustomer() = mutableLiveDataCustomer.value

    fun insertCustomer(customer: Customer){
        Timber.e(" Customer insert %s", customer)
        addCompositeDisposable(
            customerRepository.insertCustomer(customer = viewBillyPosMapper.viewCustomerMapper.toLocalModel(customer)).map {
                viewBillyPosMapper.viewCustomerMapper.toModel(it)
            },
            onSuccess = {
                Timber.e("Success Customer %s", it)
                mutableLiveDataCustomer.value = it
            },
            onError = {
                Timber.e("Error Customer")
            }
        )
    }

    fun updateCustomer(customer: Customer){
        addCompositeDisposable(
            customerRepository.updateCustomer(customer = viewBillyPosMapper.viewCustomerMapper.toLocalModel(customer)),
            onSuccess = {
                mutableLiveDataUpdated.value = it>0
            },
            onError = {
                mutableLiveDataUpdated.value = false
            }
        )
    }

    fun sendCustomer(customer: Customer){
        addCompositeDisposable(
            customerRepository.sendCustomer(customer = viewBillyPosMapper.viewCustomerMapper.toLocalModel(customer)),
            onSuccess = {
                mutableLiveDataSync.value = it.status == "OK"
            },
            onError = {
                mutableLiveDataSync.value = false
            }
        )
    }
}