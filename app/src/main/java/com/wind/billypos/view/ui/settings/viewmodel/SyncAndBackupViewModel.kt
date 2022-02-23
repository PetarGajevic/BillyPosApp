package com.wind.billypos.view.ui.settings.viewmodel

import androidx.lifecycle.MutableLiveData
import com.wind.billypos.base.BaseViewModel
import com.wind.billypos.view.ui.settings.repository.SyncAndBackupRepository

class SyncAndBackupViewModel(private val syncAndBackupRepository: SyncAndBackupRepository): BaseViewModel() {

    val mutableLiveDataAllTransactions = MutableLiveData<Int>()
    val mutableLiveDataSyncedTransactions = MutableLiveData<Int>()
    val mutableLiveDataFiscalizedTransactions = MutableLiveData<Int>()

    val mutableLiveDataAllCashBalance = MutableLiveData<Int>()
    val mutableLiveDataSyncedCashBalance = MutableLiveData<Int>()
    val mutableLiveDataFiscalizedCashBalance = MutableLiveData<Int>()

    val mutableLiveDataAllItems = MutableLiveData<Int>()
    val mutableLiveDataSyncedItems = MutableLiveData<Int>()

    val mutableLiveDataAllCustomers = MutableLiveData<Int>()
    val mutableLiveDataSyncedCustomers = MutableLiveData<Int>()


//    Transactions

    fun getAllTransactions(){
        addCompositeDisposable(
            syncAndBackupRepository.getAllTransactions(),
            onSuccess = {
                mutableLiveDataAllTransactions.value = it
            },
            onError = {
                mutableLiveDataAllTransactions.value = 0
            }
        )
    }

    fun getSyncedTransactions(){
        addCompositeDisposable(
            syncAndBackupRepository.getSyncedTransactions(),
            onSuccess = {
                mutableLiveDataSyncedTransactions.value = it
            },
            onError = {
                mutableLiveDataSyncedTransactions.value = 0
            }
        )
    }

    fun getFiscalizedTransactions(){
        addCompositeDisposable(
            syncAndBackupRepository.getFiscalizedTransactions(),
            onSuccess = {
                mutableLiveDataFiscalizedTransactions.value = it
            },
            onError = {
                mutableLiveDataFiscalizedTransactions.value = 0
            }
        )
    }

//        Cash Balance

    fun getAllCashBalance(){
        addCompositeDisposable(
            syncAndBackupRepository.getAllCashBalance(),
            onSuccess = {
                mutableLiveDataAllCashBalance.value = it
            },
            onError = {
                mutableLiveDataAllCashBalance.value = 0
            }
        )
    }

    fun getSyncedCashBalance(){
        addCompositeDisposable(
            syncAndBackupRepository.getSyncedCashBalance(),
            onSuccess = {
                mutableLiveDataSyncedCashBalance.value = it
            },
            onError = {
                mutableLiveDataSyncedCashBalance.value = 0
            }
        )
    }

    fun getFiscalizedCashBalance(){
        addCompositeDisposable(
            syncAndBackupRepository.getFiscalizedCashBalance(),
            onSuccess = {
                mutableLiveDataFiscalizedCashBalance.value = it
            },
            onError = {
                mutableLiveDataFiscalizedCashBalance.value = 0
            }
        )
    }


//    Items

    fun getAllItems(){
        addCompositeDisposable(
            syncAndBackupRepository.getAllItems(),
            onSuccess = {
                mutableLiveDataAllItems.value = it
            },
            onError = {
                mutableLiveDataAllItems.value = 0
            }
        )
    }

    fun getSyncedItems(){
        addCompositeDisposable(
            syncAndBackupRepository.getSyncedItems(),
            onSuccess = {
                mutableLiveDataSyncedItems.value = it
            },
            onError = {
                mutableLiveDataSyncedItems.value = 0
            }
        )
    }

//    Customers

    fun getAllCustomers(){
        addCompositeDisposable(
            syncAndBackupRepository.getAllCustomers(),
            onSuccess = {
                mutableLiveDataAllCustomers.value = it
            },
            onError = {
                mutableLiveDataAllCustomers.value = 0
            }
        )
    }

    fun getSyncedCustomers(){
        addCompositeDisposable(
            syncAndBackupRepository.getSyncedCustomers(),
            onSuccess = {
                mutableLiveDataSyncedCustomers.value = it
            },
            onError = {
                mutableLiveDataSyncedCustomers.value = 0
            }
        )
    }

}