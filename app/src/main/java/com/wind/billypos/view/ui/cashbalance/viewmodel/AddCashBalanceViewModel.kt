package com.wind.billypos.view.ui.cashbalance.viewmodel

import androidx.lifecycle.MutableLiveData
import com.wind.billypos.base.BaseViewModel
import com.wind.billypos.base.Error
import com.wind.billypos.data.remote.mapper.BillyPosMapper
import com.wind.billypos.data.remote.model.cashbalance.RemoteCashBalanceResponse
import com.wind.billypos.utils.extensions.toXMLString
import com.wind.billypos.view.mapper.ViewBillyPosMapper
import com.wind.billypos.view.model.CashBalance
import com.wind.billypos.view.model.Company
import com.wind.billypos.view.ui.cashbalance.repository.CashBalanceRepository
import timber.log.Timber


class AddCashBalanceViewModel(private val cashBalanceRepository: CashBalanceRepository): BaseViewModel() {
    private val viewBillyPosMapper: ViewBillyPosMapper = ViewBillyPosMapper()
    private val billyPosMapper: BillyPosMapper = BillyPosMapper()

    val mutableLiveDataHasCertificate = MutableLiveData<Boolean>()

    val mutableLiveDataCashBalance = MutableLiveData<CashBalance>()
    val mutableLiveDataRemoteCashBalance = MutableLiveData<RemoteCashBalanceResponse?>()

    val mutableLiveDataCompanyProfile = MutableLiveData<Company>()

    val mutableLiveDataFiscalDigitalKey = MutableLiveData<Company.FiscalDigitalKey>()
    val mutableLiveDataSignedDocument = MutableLiveData<String>()

    val mutableLiveDataOpenedDay= MutableLiveData<CashBalance>()
    val mutableLiveDataHasOpenedDay= MutableLiveData(false)

    val mutableLiveDataCashBalanceFCDC = MutableLiveData<String>()

    val mutableLiveDataProgressBar = MutableLiveData(false)

    init {
        getFiscalKey()
    }

    fun setCashBalance(cashBalance: CashBalance){
        cashBalance.code = Error.INITIALIZE
        mutableLiveDataCashBalance.value = cashBalance
    }

    fun insertCashBalance(cashBalance: CashBalance){
        addCompositeDisposable(
            cashBalanceRepository.insertCashBalance(
                viewBillyPosMapper.viewCashBalanceMapper.toLocalModel(cashBalance)
            ),
            onSuccess = {
                mutableLiveDataCashBalance.value = viewBillyPosMapper.viewCashBalanceMapper.toModel(it)
            }
        )
    }

    fun updateCashBalance(cashBalance: CashBalance){
        addCompositeDisposable(
            cashBalanceRepository.updateCashBalance(
                viewBillyPosMapper.viewCashBalanceMapper.toLocalModel(cashBalance)
            ),
            onSuccess = {
               // mutableLiveDataCashBalance.value = viewBillyPosMapper.viewCashBalanceMapper.toModel(it)
            }
        )
    }


    fun fiscalCashDeposit(cashDeposit: String){
        addCompositeDisposable(
            cashBalanceRepository.fiscalCashDeposit(cashDeposit = cashDeposit),
            onSuccess = {
                Timber.e("Proslo")
                mutableLiveDataCashBalanceFCDC.value = it.body.registerCashDepositResponse?.FCDC!!
//                val cashDepositResponse = mutableLiveDataCashBalance.value?.copy(
//                     fcdc = it.body.registerCashDepositResponse?.FCDC
//                )
//                cashDepositResponse?.code = SUCCESS
//                mutableLiveDataCashBalance.value = cashDepositResponse!!

            },
            handleError = true,
            onError = {
                mutableLiveDataCashBalanceFCDC.value = ""
                Timber.e("Error")
            }
        )
    }


    fun getCertificate(){
        addCompositeDisposable(
            cashBalanceRepository.getCertificate(),
            onSuccess = {
                mutableLiveDataHasCertificate.value = true
            }, onError = {
                mutableLiveDataHasCertificate.value = false
            }
        )
    }


    fun sendCashBalance(cashBalance: CashBalance?){
        addCompositeDisposable(
            cashBalanceRepository.sendCashBalance(
                viewBillyPosMapper.viewCashBalanceMapper.toLocalModel(cashBalance)
            ),
            onSuccess = {
                mutableLiveDataRemoteCashBalance.value = it
                Timber.e("Success : %s", it)
            },
            onError = {
                mutableLiveDataRemoteCashBalance.value = null
                Timber.e("Error na server")
            }
        )
    }

    fun getCompany() {
        addCompositeDisposable(
            cashBalanceRepository.getCompany()
                .map { viewBillyPosMapper.viewCompanyMapper.toModel(it) },
            onSuccess = {
                mutableLiveDataCompanyProfile.value  = it
            }
        )
    }

    fun getFiscalDigitalKey() = mutableLiveDataFiscalDigitalKey.value

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


    fun hasOpenDay(deviceId: String?) {
        addCompositeDisposable(
            cashBalanceRepository.hasOpenedDay(
                deviceId = deviceId
            ).map{ viewBillyPosMapper.viewCashBalanceMapper.toModel(it) },
            onSuccess = {
                mutableLiveDataOpenedDay.value = it
                mutableLiveDataHasOpenedDay.value = true
            }
        )
    }

    fun getCashBalance(): CashBalance? {
       return mutableLiveDataCashBalance.value
    }

}