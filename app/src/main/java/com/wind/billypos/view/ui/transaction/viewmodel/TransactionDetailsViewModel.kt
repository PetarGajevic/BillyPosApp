package com.wind.billypos.view.ui.transaction.viewmodel

import androidx.databinding.ObservableInt
import androidx.lifecycle.MutableLiveData
import com.wind.billypos.R
import com.wind.billypos.base.BaseViewModel
import com.wind.billypos.data.remote.model.invoice.RemoteTransactionHeadResponse
import com.wind.billypos.utils.extensions.toXMLString
import com.wind.billypos.view.mapper.ViewBillyPosMapper
import com.wind.billypos.view.model.Company
import com.wind.billypos.view.model.TransactionBody
import com.wind.billypos.view.model.TransactionHead
import com.wind.billypos.view.model.TransactionPayment
import com.wind.billypos.view.ui.transaction.repository.TransactionDetailsRepository
import timber.log.Timber

class TransactionDetailsViewModel(private val transactionDetailsRepository: TransactionDetailsRepository): BaseViewModel() {

    private val viewBillyPosMapper = ViewBillyPosMapper()

    val mutableLiveDataTransactionHead = MutableLiveData<TransactionHead>()
    val mutableLiveDataIsFiscalized = MutableLiveData<String>()
    val mutableLiveDataTransactionBodyList = MutableLiveData<List<TransactionBody>>()
    val mutableLiveDataTransactionPaymentList = MutableLiveData<List<TransactionPayment>>()
    val mutableLiveDataInvoiceNum = MutableLiveData<Long>()
    val mutableLiveDataInsertedTransactionHead = MutableLiveData<Boolean>()

    val mutableLiveDataJIKR = MutableLiveData<String>()
    val mutableLiveDataFiscalDigitalKey = MutableLiveData<Company.FiscalDigitalKey>()
    val mutableLiveDataSignedDocument = MutableLiveData<String>()
    val mutableLiveDataIKOF = MutableLiveData<Pair<String, String>>()

    val mutableLiveDataRemoteTransactionHeadResponse = MutableLiveData<RemoteTransactionHeadResponse?>()

    fun setTransactionHead(trnHead: TransactionHead){
        mutableLiveDataTransactionHead.value = trnHead
    }

    fun setFiscalizeStatus(status: String){
        mutableLiveDataIsFiscalized.value = status
    }

    fun getPaymentMethods(trnHeadUUID: String){
        addCompositeDisposable(
            transactionDetailsRepository.getPaymentMethods(trnHeadUUID = trnHeadUUID)
                .map { viewBillyPosMapper.viewTransactionPaymentMapper.toListOfModel(it) }
                ,onSuccess = {
                mutableLiveDataTransactionPaymentList.value = it
            }
        )
    }

    fun getLastInvoiceNum(){
        addCompositeDisposable(
            transactionDetailsRepository.getLastInvoiceNum()
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

    private fun getTransactionHead() = mutableLiveDataTransactionHead.value

    fun getTransactionsBody(){
        addCompositeDisposable(
            transactionDetailsRepository.getTransactionsBody(getTransactionHead()?.uuid!!)
                .map { viewBillyPosMapper.viewTransactionBodyMapper.toListOfModel(it) }
            ,
            onSuccess = {
                mutableLiveDataTransactionBodyList.value = it
            }
        )
    }

    fun fiscalReceipt(receipt: String) {
        addCompositeDisposable(
            transactionDetailsRepository.fiscalReceipt(fiscalizeReceipt = receipt),
            onSuccess = {
                mutableLiveDataJIKR.value = it.body?.registerInvoiceResponse?.fic!!
            },
            handleError = true,
            onError = {
                mutableLiveDataJIKR.value = "error"
            }
        )
    }

    fun sendReceipt(transactionHead: TransactionHead){
        Timber.e("Send REciept View Model")
        addCompositeDisposable(
            transactionDetailsRepository.sendReceipt(localTransactionHead = viewBillyPosMapper.viewTransactionHeadMapper.toLocalModel(transactionHead)),
            onSuccess = {
                mutableLiveDataRemoteTransactionHeadResponse.value = it
                Timber.e("Success %s", it)
            },
            handleError = true,
            onError = {
                mutableLiveDataRemoteTransactionHeadResponse.value = null
                Timber.e("Error send receipt")
            }
        )
    }

    fun signDocument(anyObject: Any, elementToSign: String) {
        addCompositeDisposable(
            transactionDetailsRepository.signDocument(
                requestToSign = anyObject.toXMLString(),
                elementToSign = elementToSign,
                mutableLiveDataFiscalDigitalKey.value!!
            ),
            onSuccess = {
                Timber.e("Uspjesno potpisao balance")
                mutableLiveDataSignedDocument.value = it
            },
            onError = {
                Timber.e("Neuspjesno potpisao Invoice")
            }
        )
    }

    fun getFiscalKey(){
        addCompositeDisposable(
            transactionDetailsRepository.getFiscalDigitalKey()
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
            transactionDetailsRepository.generateIKOF(
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

    fun updateTransactionHead(trnHead: TransactionHead){
        addCompositeDisposable(
            transactionDetailsRepository.updateTransactionHead(trnHead = viewBillyPosMapper.viewTransactionHeadMapper.toLocalModel(trnHead)),
            onSuccess = {
                Timber.e("Updejt: %s", it)
            },
            onError = {
                Timber.e("Error Updejt")
            }
        )
    }

    fun insertTransactionHead(trnHead: TransactionHead) {
        addCompositeDisposable(
            transactionDetailsRepository.insertTransactionHead(trnHead = viewBillyPosMapper.viewTransactionHeadMapper.toLocalModel(trnHead)),
            onSuccess = {
                Timber.e("Insert: %s", it)
                mutableLiveDataInsertedTransactionHead.value = true
            },
            onError = {
                Timber.e("Error Insert")
                mutableLiveDataInsertedTransactionHead.value = false
            },
            handleError = true
        )
    }

    fun insertTransactionList(trnList: MutableList<TransactionBody>){
        addCompositeDisposable(
            transactionDetailsRepository.insertTransactionList(
                trnList = viewBillyPosMapper.viewTransactionBodyMapper.toLocalListOfModel(trnList.toList())
            ),
            onSuccess = {
                Timber.e("Lista transaction body %s", it)
            },
            onError = {
                Timber.e("Lista transaction error")
            }
        )
    }

    fun getIKOF() = mutableLiveDataIKOF.value

}