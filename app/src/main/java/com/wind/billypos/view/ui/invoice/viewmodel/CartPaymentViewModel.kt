package com.wind.billypos.view.ui.invoice.viewmodel

import androidx.lifecycle.MutableLiveData
import com.wind.billypos.base.BaseViewModel
import com.wind.billypos.data.local.model.LocalTransactionHead
import com.wind.billypos.data.remote.model.invoice.RemoteTransactionHeadResponse
import com.wind.billypos.utils.extensions.toXMLString
import com.wind.billypos.view.mapper.ViewBillyPosMapper
import com.wind.billypos.view.model.*
import com.wind.billypos.view.ui.invoice.repository.CartPaymentRepository
import timber.log.Timber

class CartPaymentViewModel(private val cartPaymentRepository: CartPaymentRepository) :
    BaseViewModel() {

    private val viewBillyPosMapper = ViewBillyPosMapper()

    val mutableLiveDataProgressBar = MutableLiveData(false)
    val mutableLivaDataTransactionHead = MutableLiveData<TransactionHead>()
    val mutableLivaDataTransactionHeadId = MutableLiveData<Long>()
    val mutableLiveDataConfiguration = MutableLiveData(Configuration())
    val mutableLiveDataItems = MutableLiveData<List<TransactionBody>>()
    val mutableLiveDataSummaryItems = MutableLiveData<List<TransactionBody>>()
    val mutableLiveDataIICList = MutableLiveData<List<TransactionHead>>()
    val mutableLiveDataUpdatedOrders = MutableLiveData<Boolean>()

    val mutableLiveDataJIKR = MutableLiveData<String>()
    val mutableLiveDataFiscalDigitalKey = MutableLiveData<Company.FiscalDigitalKey>()
    val mutableLiveDataSignedDocument = MutableLiveData<String>()
    val mutableLiveDataIKOF = MutableLiveData<Pair<String, String>>()

    val mutableLiveDataCheckPayment = MutableLiveData<Boolean>()

    val mutableLiveDataRemoteTransactionHeadResponse =
        MutableLiveData<RemoteTransactionHeadResponse?>()


    fun setTransactionHead(trnHead: TransactionHead) {
        mutableLivaDataTransactionHead.value = trnHead
    }

    fun insertPayment(trnPayment: TransactionPayment) {
        addCompositeDisposable(
            cartPaymentRepository.insertPayment(
                trnPayment = viewBillyPosMapper.viewTransactionPaymentMapper.toLocalModel(trnPayment)
            ),
            onSuccess = {
                Timber.e("Insert")
            }
        )
    }

    fun updateTransactionHead(trnHead: TransactionHead) {
        Timber.e("Transaction head update fiskalizovan i sihronizovan %s", trnHead)
        addCompositeDisposable(
            cartPaymentRepository.updateTransactionHead(
                trnHead = viewBillyPosMapper.viewTransactionHeadMapper.toLocalModel(trnHead)
            ),
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
            cartPaymentRepository.insertTransactionHead(
                trnHead = viewBillyPosMapper.viewTransactionHeadMapper.toLocalModel(
                    trnHead
                )
            ),
            onSuccess = {
                Timber.e("Insert: %s", it)
                mutableLivaDataTransactionHead.value =
                    viewBillyPosMapper.viewTransactionHeadMapper.toModel(it)
            },
            onError = {
                Timber.e("Error Insert")
            }
        )
    }

    fun insertTransactionBodyList(trnBodyList: List<TransactionBody>) {
        addCompositeDisposable(
            cartPaymentRepository.insertTransactionBodyList(
                trnBodyList = viewBillyPosMapper.viewTransactionBodyMapper.toLocalListOfModel(
                    trnBodyList
                )
            ),
            onSuccess = {
                Timber.e("Return insertTransaction body list %s :", it)
            },
            onError = {
                Timber.e("Error insertTransaction body list")
            }
        )
    }


    fun fiscalReceipt(receipt: String) {
        addCompositeDisposable(
            cartPaymentRepository.fiscalReceipt(fiscalizeReceipt = receipt),
            onSuccess = {
                mutableLiveDataJIKR.value = it.body?.registerInvoiceResponse?.fic!!
            },
            onError = {
                mutableLiveDataJIKR.value = "error"
            }
        )
    }

    fun sendReceipt(transactionHead: TransactionHead) {
        Timber.e("Send REciept View Model")
        addCompositeDisposable(
            cartPaymentRepository.sendReceipt(
                localTransactionHead = viewBillyPosMapper.viewTransactionHeadMapper.toLocalModel(
                    transactionHead
                )
            ),
            onSuccess = {
                mutableLiveDataRemoteTransactionHeadResponse.value = it
                Timber.e("Success %s", it)
            },
            onError = {
                mutableLiveDataRemoteTransactionHeadResponse.value = null
                Timber.e("Error send receipt")
            }
        )
    }


    fun signDocument(anyObject: Any, elementToSign: String) {
        Timber.e("anyObject.toXMLString(): %s", anyObject.toXMLString())
        Timber.e("elementToSign: %s", elementToSign)
        addCompositeDisposable(
            cartPaymentRepository.signDocument(
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
                mutableLiveDataSignedDocument.value = "error"
            }
        )
    }

    fun generateIKOF(iicData: String) {
        addCompositeDisposable(
            cartPaymentRepository.generateIKOF(
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

    fun getItems(idTrnHead: String?) {
        addCompositeDisposable(
            cartPaymentRepository.getItems(
                idTrnHead = idTrnHead
            ).map {
                viewBillyPosMapper.viewTransactionBodyMapper.toListOfModel(it)
            },
            onSuccess = {
                mutableLiveDataItems.value = it
            },
            onError = {
                mutableLiveDataItems.value = listOf()
            }
        )
    }

    fun getFiscalKey() {
        addCompositeDisposable(
            cartPaymentRepository.getFiscalDigitalKey()
                .map { viewBillyPosMapper.viewFiscalDigitalKeyMapper.toModel(it) },
            onSuccess = {
                Timber.e("Fiscal Key %s", it)
                mutableLiveDataFiscalDigitalKey.value = it
            },
            onError = {
                Timber.e("Fiscal Key je null")
            }

        )
    }

    fun deletePaymentMethods(transactionUUID: String?) {
        addCompositeDisposable(
            cartPaymentRepository.deletePaymentMethods(transactionUUID = transactionUUID),
            onSuccess = {
                mutableLiveDataCheckPayment.value = it
            },
            onError = {
                mutableLiveDataCheckPayment.value = false
            }
        )
    }

    fun getInvoiceIIC(idEntity: String?) {
        addCompositeDisposable(
            cartPaymentRepository.getInvoiceIIC(idEntity = idEntity).map {
                viewBillyPosMapper.viewTransactionHeadMapper.toListOfModel(it)
            },
            onSuccess = {
                mutableLiveDataIICList.value = it
            },
            onError = {
                mutableLiveDataIICList.value = listOf()
            }
        )
    }

    fun getSummaryTransactionBodies(id: List<String?>) {
        addCompositeDisposable(
            cartPaymentRepository.getSummaryTransactionBodies(id = id).map {
                viewBillyPosMapper.viewTransactionBodyMapper.toListOfModel(it)
            },
            onSuccess = { mutableLiveDataSummaryItems.value = it },
            onError = { mutableLiveDataSummaryItems.value = listOf() }
        )
    }


    fun updateOrders(transactionHeadList: List<TransactionHead>) {
        addCompositeDisposable(
            cartPaymentRepository.updateOrders(
                transactionHeadList = viewBillyPosMapper.viewTransactionHeadMapper.toLocalListOfModel(
                    transactionHeadList
                )
            ),
            onSuccess = {
                Timber.e("List order is paid true")
                mutableLiveDataUpdatedOrders.value = true
            },
            onError = {
                Timber.e("List order is paid false")
                mutableLiveDataUpdatedOrders.value = false
            }
        )
    }

    fun getIKOF() = mutableLiveDataIKOF.value


}