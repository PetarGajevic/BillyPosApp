package com.wind.billypos.view.ui.customer.viewmodel

import androidx.lifecycle.MutableLiveData
import com.wind.billypos.base.BaseViewModel
import com.wind.billypos.base.Error
import com.wind.billypos.view.mapper.ViewBillyPosMapper
import com.wind.billypos.view.model.Customer
import com.wind.billypos.view.ui.customer.repository.CustomerRepository
import timber.log.Timber

class CustomerDetailsViewModel(private val customerRepository: CustomerRepository):
    BaseViewModel() {

    private val viewBillyPosMapper = ViewBillyPosMapper()

    val mutableLiveDataCustomer = MutableLiveData<Customer>()
    val mutableLiveDataUpdated = MutableLiveData<Boolean>()
    val mutableLiveDataSync = MutableLiveData<Boolean>()

    fun setCustomer(customer: Customer) {
        customer.code = Error.INITIALIZE
        mutableLiveDataCustomer.value = customer
    }

    fun getCustomer() = mutableLiveDataCustomer.value

    fun updateCustomer(customer: Customer) {
        Timber.e("Customer %s", customer)
        addCompositeDisposable(
            customerRepository.updateCustomerDetails(
                customer = viewBillyPosMapper.viewCustomerMapper.toLocalModel(
                    customer
                )
            ),
            onSuccess = {
                Timber.e("On success %s", it)
                val updatedCustomer = getCustomer()!!
                updatedCustomer.code = Error.SUCCESS
                mutableLiveDataCustomer.value = updatedCustomer
            },
            onError = {
                Timber.e("On error")
                //  mutableLiveDataCustomer.value = null
            }
        )
    }

    fun sendCustomer(customer: Customer){
        Timber.e("Send customer")
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

    fun insertUpdatedCustomer(customer: Customer){
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


}