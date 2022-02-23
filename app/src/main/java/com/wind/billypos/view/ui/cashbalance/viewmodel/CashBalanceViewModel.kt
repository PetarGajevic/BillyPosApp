package com.wind.billypos.view.ui.cashbalance.viewmodel

import androidx.lifecycle.MutableLiveData
import com.wind.billypos.base.BaseViewModel
import com.wind.billypos.data.remote.mapper.BillyPosMapper
import com.wind.billypos.data.remote.model.cashbalance.RemoteCashBalance
import com.wind.billypos.data.remote.model.cashbalance.RemoteCashDepositResponse
import com.wind.billypos.utils.extensions.toXMLString
import com.wind.billypos.view.mapper.ViewBillyPosMapper
import com.wind.billypos.view.model.CashBalance
import com.wind.billypos.view.model.Company
import com.wind.billypos.view.ui.cashbalance.repository.CashBalanceRepository
import timber.log.Timber

class CashBalanceViewModel(private val cashBalanceRepository: CashBalanceRepository) :
    BaseViewModel() {

    private val viewBillyPosMapper: ViewBillyPosMapper = ViewBillyPosMapper()
    private val billyPosMapper = BillyPosMapper()

    val mutableLiveDataCashAmountBalance = MutableLiveData(0.0)
    val mutableLiveDataTotalAmountIN = MutableLiveData(0.0)
    val mutableLiveDataTotalAmountOUT = MutableLiveData(0.0)
    val mutableLiveDataTransactionAmount = MutableLiveData(0.0)
    val mutableLiveDataTransactionCash = MutableLiveData(0.0)
    val mutableLiveDataTransactionCard = MutableLiveData(0.0)
    val mutableLiveDataTransactionStorno = MutableLiveData(0.0)
    val mutableLiveDataTodayTotal = MutableLiveData(0.0)

    val mutableLiveDataUnfiscalizedCashBalance = MutableLiveData<List<CashBalance>>()
    val mutableLiveDataFiscalDigitalKey = MutableLiveData<Company.FiscalDigitalKey>()
    val mutableLiveDataSignedDocument = MutableLiveData<String>()
    val mutableLiveDataCashBalanceResponse = MutableLiveData<RemoteCashDepositResponse.SoapBodyCashDepositResponse?>()

    //    CashBalance
    val mutableLiveDataNotSyncedCashBalance = MutableLiveData<List<CashBalance>>()
    val mutableLiveDataSyncedCashBalance = MutableLiveData<CashBalance?>()
    val mutableLiveDataGetCashBalanceResponse = MutableLiveData<List<RemoteCashBalance>>()
    val mutableLiveDataSendCashBalance = MutableLiveData<Boolean>()

    init {
        updateCashBalanceView()
        getFiscalKey()
    }

    fun getCashAmountBalance() {
        addCompositeDisposable(
            cashBalanceRepository.getCashAmountBalance()
                .map { viewBillyPosMapper.viewCashBalanceMapper.toModel(it) },
            onSuccess = {
                Timber.e("CashBalance: %s", it.toString())
                mutableLiveDataCashAmountBalance.value = it.cashAmount
            },
            onError = {
                Timber.e("CashBalance: null")
            }
        )
    }

    fun getCashAmountIN() {
        addCompositeDisposable(
            cashBalanceRepository.getCashAmountIN()
                .map { viewBillyPosMapper.viewCashBalanceMapper.toListOfModel(it) },
            onSuccess = { cashBalance ->
                mutableLiveDataTotalAmountIN.value = cashBalance.sumOf { it.cashAmount!! }
            },
            onError = {
                Timber.e("CashBalanceIN: null")
            }
        )
    }

    fun getCashAmountOUT() {
        addCompositeDisposable(
            cashBalanceRepository.getCashAmountOUT()
                .map { viewBillyPosMapper.viewCashBalanceMapper.toListOfModel(it) },
            onSuccess = { cashBalance ->
                mutableLiveDataTotalAmountOUT.value = cashBalance.sumOf { it.cashAmount!! }
            },
            onError = {
                Timber.e("CashBalanceIN: null")
            }
        )
    }

    fun getTotalCashAmount() {
        addCompositeDisposable(
            cashBalanceRepository.getTotalCashAmount(),
            onSuccess = {
                mutableLiveDataTodayTotal.value = it
            }
        )
    }

    fun getTodaySales() {
        addCompositeDisposable(
            cashBalanceRepository.getTodaySales(),
            onSuccess = {
                mutableLiveDataTransactionAmount.value = it
            }
        )
    }

    fun getTodayStornoSales() {
        addCompositeDisposable(
            cashBalanceRepository.getTodayStornoSales(),
            onSuccess = {
                mutableLiveDataTransactionStorno.value = it
            }
        )
    }

    fun getTodayCashSales() {
        addCompositeDisposable(
            cashBalanceRepository.getTodayCashSales(),
            onSuccess = {
                mutableLiveDataTransactionCash.value = it
            }
        )
    }

    fun getTodayCardSales() {
        addCompositeDisposable(
            cashBalanceRepository.getTodayCardSales(),
            onSuccess = {
                mutableLiveDataTransactionCard.value = it
            }
        )
    }

    fun getUnfiscalizedCashBalance() {
        addCompositeDisposable(
            cashBalanceRepository.getUnfiscalizedCashBalance().map {
                viewBillyPosMapper.viewCashBalanceMapper.toListOfModel(it)
            },
            onSuccess = {
                mutableLiveDataUnfiscalizedCashBalance.value = it
            },
            onError = {
                mutableLiveDataUnfiscalizedCashBalance.value = listOf()
            }
        )
    }

    fun signDocument(anyObject: Any, elementToSign: String) {
        addCompositeDisposable(
            cashBalanceRepository.signDocument(
                requestToSign = anyObject.toXMLString(),
                elementToSign = elementToSign,
                mutableLiveDataFiscalDigitalKey.value!!
            ),
            onSuccess = {
                Timber.e("Uspjesno potpisao balance")
                mutableLiveDataSignedDocument.value = it
            },
            onError = {
                Timber.e("Neuspjesno potpisao balance")
            }
        )
    }

    private fun getFiscalKey(){
        addCompositeDisposable(
            cashBalanceRepository.getFiscalDigitalKey()
                .map { viewBillyPosMapper.viewFiscalDigitalKeyMapper.toModel(it) }
            ,
            onSuccess = {
                Timber.e("Fiscal Key %s", it)
                mutableLiveDataFiscalDigitalKey.value = it
            },
            onError = {
                Timber.e("Fiscal Key je null")
            }

        )
    }

    fun fiscalCashDeposit(cashDeposit: String){
        addCompositeDisposable(
            cashBalanceRepository.fiscalCashDeposit(cashDeposit = cashDeposit),
            onSuccess = {
                Timber.e("Proslo")
                mutableLiveDataCashBalanceResponse.value = it.body
            },
            handleError = true,
            onError = {
                mutableLiveDataCashBalanceResponse.value = null
                Timber.e("Error")
            }
        )
    }

    fun findCashBalanceByUUID(cashDeposit: RemoteCashDepositResponse.SoapBodyCashDepositResponse?){
        addCompositeDisposable(
            cashBalanceRepository.findCashBalanceByUUID(cashDeposit = cashDeposit),
            onSuccess = {

            }
        )
    }

    //    Cash Balance

    fun sendCashBalance(cashBalance: CashBalance){
        addCompositeDisposable(
            cashBalanceRepository.sendCashBalance(localCashBalance = viewBillyPosMapper.viewCashBalanceMapper.toLocalModel(cashBalance)),
            onSuccess = {
                if(it.status == "OK"){
                    val localCustomer = billyPosMapper.getCashBalanceMapper.toModel(it.data)
                    mutableLiveDataSyncedCashBalance.value = viewBillyPosMapper.viewCashBalanceMapper.toModel(localCustomer)
                }else{
                    mutableLiveDataSyncedCashBalance.value = null
                }
                Timber.e("Remote Customer Response %s", it)
            },
            onError = {
                mutableLiveDataSyncedCashBalance.value = null
                Timber.e("Error")
            }
        )
    }

    fun getCashBalance() {
        addCompositeDisposable(
            cashBalanceRepository.getCashBalance(),
            onSuccess = {
                Timber.e("OnSuccess %s", it)
                mutableLiveDataGetCashBalanceResponse.value = it.data!!
            },
            onError = {
                Timber.e("On error")
                mutableLiveDataGetCashBalanceResponse.value = listOf()
            }
        )
    }

    fun insertCashBalance(cashBalance: List<RemoteCashBalance>){
        addCompositeDisposable(
            cashBalanceRepository.insertCashBalance(cashBalance = billyPosMapper.remoteCashBalanceMapper.toListOfModel(cashBalance)),
            onSuccess = {
                Timber.e("Usao na success")
                mutableLiveDataSendCashBalance.value = true
            },
            onError = {
                Timber.e("Usao na error")
                mutableLiveDataSendCashBalance.value = true
            }
        )
    }

    fun getNotSyncedCashBalance(){
        addCompositeDisposable(
            cashBalanceRepository.getNotSyncedCashBalance().map {
                viewBillyPosMapper.viewCashBalanceMapper.toListOfModel(it)
            },
            onSuccess = {
                Timber.e("Lista itema %s", it)
                mutableLiveDataNotSyncedCashBalance.value = it
            },
            onError = {
                mutableLiveDataNotSyncedCashBalance.value = listOf()
            }
        )
    }

    fun findCashBalanceByUUID(cashBalance: CashBalance){
        Timber.e("Customer %s", cashBalance)
        addCompositeDisposable(
            cashBalanceRepository.findCashBalanceByUUID(uuid = cashBalance.uuid),
            onSuccess = {
                Timber.e("Uspjesno updejtovan %s" , it)
            }
        )
    }

    fun updateCashBalanceView() {
        getCashAmountBalance()
        getCashAmountIN()
        getCashAmountOUT()
        getTotalCashAmount()
        getTodaySales()
        getTodayStornoSales()
        getTodayCashSales()
        getTodayCardSales()
    }

}