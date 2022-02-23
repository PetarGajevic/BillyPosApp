package com.wind.billypos.view.ui.transaction.viewmodel

import androidx.lifecycle.MutableLiveData
import com.wind.billypos.base.BaseViewModel
import com.wind.billypos.utils.extensions.toXMLString
import com.wind.billypos.view.mapper.ViewBillyPosMapper
import com.wind.billypos.view.model.CashBalance
import com.wind.billypos.view.model.Company
import com.wind.billypos.view.model.TransactionBody
import com.wind.billypos.view.model.TransactionHead
import com.wind.billypos.view.model.report.TransactionsTodayOverview
import com.wind.billypos.view.ui.transaction.repository.TransactionSalesRepository
import timber.log.Timber

class TransactionViewModel(private val transactionSalesRepository: TransactionSalesRepository): BaseViewModel() {

    private val viewBillyPosMapper = ViewBillyPosMapper()

    val mutableLiveDataTransactionsTodayOverview = MutableLiveData<TransactionsTodayOverview>()
    val mutableLiveDataTransactionHeadList = MutableLiveData<List<TransactionHead>>()

    val mutableLiveDataItems = MutableLiveData<List<TransactionBody>>()
    val mutableLiveDataQuantity = MutableLiveData<Double>()
    val mutableLiveDataTransactionHead = MutableLiveData<TransactionHead>()

    val mutableLiveDataTransactionBody = MutableLiveData<TransactionBody>()
    val mutableLiveDataUpdate = MutableLiveData<Boolean>()
    val mutableLiveDataDelete = MutableLiveData<Int>()

    private val mutableLiveDataFiscalDigitalKey = MutableLiveData<Company.FiscalDigitalKey>()
    val mutableLiveDataSignedDocument = MutableLiveData<String>()
    val mutableLiveDataUnfiscalizedTransactionHeadList = MutableLiveData<List<TransactionHead>>()
    val mutableLiveDataIKOF = MutableLiveData<Pair<String, String>>()


    init {
        getFiscalKey()
    }

    fun todayTotalSales(){
        addCompositeDisposable(
            transactionSalesRepository.getTodayTotalSales(),
            onSuccess = {
                Timber.e("Transactions: %s", it)
                mutableLiveDataTransactionsTodayOverview.value = it
            },
            onError = {
                Timber.e("Transactions error ")
            }
        )
    }

    fun getAllTransactions(){
        addCompositeDisposable(
            transactionSalesRepository.getAllTransactions()
                .map { viewBillyPosMapper.viewTransactionHeadMapper.toListOfModel(it) }
            ,
            onSuccess = {
                mutableLiveDataTransactionHeadList.value = it
            }
        )
    }


    fun setTransactionHead(trnHead: TransactionHead){
        mutableLiveDataTransactionHead.value = trnHead
    }

    fun getItems(trnHeadUUID: String){
        addCompositeDisposable(
            transactionSalesRepository.getItems(trnHeadUUID = trnHeadUUID)
                .map { viewBillyPosMapper.viewTransactionBodyMapper.toListOfModel(it) }
            , onSuccess = { list ->
                mutableLiveDataItems.value = list
                mutableLiveDataQuantity.value = list.sumOf { it.quantity!! }
            }
        )
    }

    fun getAllUnfiscalizedTransactions(){
        addCompositeDisposable(
            transactionSalesRepository.getAllUnfiscalizedTransactions().map{
                viewBillyPosMapper.viewTransactionHeadMapper.toListOfModel(it)
            },
            onSuccess = {
                mutableLiveDataUnfiscalizedTransactionHeadList.value = it
            }
        )
    }

    fun signDocument(anyObject: Any, elementToSign: String) {
        addCompositeDisposable(
            transactionSalesRepository.signDocument(
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
            transactionSalesRepository.getFiscalDigitalKey()
                .map { viewBillyPosMapper.viewFiscalDigitalKeyMapper.toModel(it) }
            ,
            onSuccess = {
                mutableLiveDataFiscalDigitalKey.value = it
            },
            onError = {
                Timber.e("Fiscal Key je null")
            }

        )
    }

    fun generateIKOF(iicData: String) {
        addCompositeDisposable(
            transactionSalesRepository.generateIKOF(
                iicData = iicData,
                fiscalDigitalKey = mutableLiveDataFiscalDigitalKey.value
            ),
            onSuccess = {
                Timber.e("IKOF %s", it.toString())
                mutableLiveDataIKOF.value = it
            },
            onError = {
                Timber.e("Error generate ikof")
            },
            handleError = false
        )
    }

    fun getIKOF() = mutableLiveDataIKOF.value

}