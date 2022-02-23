package com.wind.billypos.view.ui.customer.repository

import com.wind.billypos.data.dao.CustomerDao
import com.wind.billypos.data.local.model.LocalCustomer
import com.wind.billypos.data.remote.api.CustomerApi
import com.wind.billypos.data.remote.mapper.BillyPosMapper
import com.wind.billypos.data.remote.model.customer.RemoteCustomerResponse
import io.reactivex.Maybe
import timber.log.Timber

class CustomerRepository(private val customerDao: CustomerDao, private val customerApi: CustomerApi) {

    private val billyPosMapper = BillyPosMapper()

    fun insertCustomer(customer: LocalCustomer): Maybe<LocalCustomer> {
        Timber.e("Local Customer insert %s", customer)
        return   customerDao.insert(customer = customer).flatMap {
            customerDao.findCustomerById(id = it)
        }
    }


    fun updateCustomer(customer: LocalCustomer): Maybe<Int> =
        customerDao.update(customer = customer)

    fun updateCustomerDetails(customer: LocalCustomer): Maybe<Int> {
        return customerDao.update(customer = customer)
    }


    fun sendCustomer(customer: LocalCustomer): Maybe<RemoteCustomerResponse> =
        customerApi.sendCustomer(customer = billyPosMapper.customerMapper.toRemoteModel(customer))

    fun getCustomers(): Maybe<List<LocalCustomer>> = customerDao.getCustomers()

    fun searchCustomers(input: String?): Maybe<List<LocalCustomer>> = customerDao.searchCustomers(input = input)


}