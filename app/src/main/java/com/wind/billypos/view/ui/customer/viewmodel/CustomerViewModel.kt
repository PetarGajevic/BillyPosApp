package com.wind.billypos.view.ui.customer.viewmodel

import androidx.lifecycle.MutableLiveData
import com.wind.billypos.base.BaseViewModel
import com.wind.billypos.view.mapper.ViewBillyPosMapper
import com.wind.billypos.view.model.Customer
import com.wind.billypos.view.ui.customer.repository.CustomerRepository

class CustomerViewModel(private val customerRepository: CustomerRepository) : BaseViewModel() {

    private val viewBillyPosMapper = ViewBillyPosMapper()

    val mutableLiveDataCustomers = MutableLiveData<List<Customer>>()

    fun getCustomers() {
        addCompositeDisposable(
            customerRepository.getCustomers().map {
                viewBillyPosMapper.viewCustomerMapper.toListOfModel(it)
            }, onSuccess = {
                mutableLiveDataCustomers.value = it
            }
        )
    }

    fun searchCustomers(input: String?) {
        addCompositeDisposable(
            customerRepository.searchCustomers(input = input).map {
                viewBillyPosMapper.viewCustomerMapper.toListOfModel(it)
            }, onSuccess = {
                mutableLiveDataCustomers.value = it
            }
        )
    }
}