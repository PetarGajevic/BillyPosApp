package com.wind.billypos.view.ui.invoice.repository

import com.wind.billypos.data.dao.CartPaymentDao
import com.wind.billypos.data.local.model.LocalTransactionBody
import com.wind.billypos.data.local.model.LocalTransactionHead
import com.wind.billypos.data.local.model.LocalTransactionPayment
import com.wind.billypos.data.remote.api.TaxApi
import com.wind.billypos.data.remote.api.TransactionApi
import com.wind.billypos.data.remote.mapper.BillyPosMapper
import com.wind.billypos.data.remote.model.RemoteFiscalizeReceiptResponse
import com.wind.billypos.data.remote.model.invoice.RemoteTransactionHeadResponse
import com.wind.billypos.services.FiscalisationCertificationUtil
import com.wind.billypos.utils.enums.FiscalisationState
import com.wind.billypos.view.model.Company
import com.wind.billypos.view.ui.cashbalance.repository.CashBalanceRepository.Companion.FISCAL_MEDIA_TYPE
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import okhttp3.RequestBody
import timber.log.Timber
import java.security.MessageDigest
import java.security.Signature
import java.util.*
import javax.xml.bind.DatatypeConverter

class CartPaymentRepository(
    private val cartPaymentDao: CartPaymentDao,
    private val taxApi: TaxApi,
    private val transactionApi: TransactionApi
) {

    private val billyPosMapper = BillyPosMapper()

    fun insertPayment(trnPayment: LocalTransactionPayment): Single<Long> =
        cartPaymentDao.insert(trnPayment = trnPayment)

    fun updateTransactionHead(trnHead: LocalTransactionHead): Single<Int> =
        cartPaymentDao.update(trnHead = trnHead)

    fun insertTransactionHead(trnHead: LocalTransactionHead): Single<LocalTransactionHead> =
        cartPaymentDao.insert(trnHead = trnHead)
            .flatMap {
                cartPaymentDao.findTransactionHeadById(idHead = it)
            }


    fun insertTransactionBodyList(trnBodyList: List<LocalTransactionBody>): Single<Boolean> {
        return Single.create {
            cartPaymentDao.insert(trnBodyList = trnBodyList)
            it.onSuccess(true)
        }
    }

    fun getItems(idTrnHead: String?): Maybe<List<LocalTransactionBody>> =
        cartPaymentDao.getItems(idTrnHead = idTrnHead)

    fun deletePaymentMethods(transactionUUID: String?): Single<Boolean> {
        return Single.create {
            cartPaymentDao.deletePaymentMethods(transactionUUID = transactionUUID)
            it.onSuccess(true)
        }
    }

    fun getFiscalDigitalKey() = cartPaymentDao.getFiscalDigitalKey()

    fun signDocument(
        requestToSign: String,
        elementToSign: String,
        fiscalDigitalKey: Company.FiscalDigitalKey
    ): Observable<String> =
        FiscalisationCertificationUtil.signDocument(
            requestToSign = requestToSign,
            elementToSign = elementToSign,
            fiscalDigitalKey = fiscalDigitalKey
        )


    fun generateIKOF(
        iicData: String,
        fiscalDigitalKey: Company.FiscalDigitalKey?
    ): Observable<Pair<String, String>> {
        return Observable.create {
            Timber.e("IIC DATA %s", iicData)
            Timber.e("fiscalDigitalKey %s", fiscalDigitalKey)
            try {
                // Kreiraj ikof po RSASSA-PKCS-v1_5
                val signature: Signature = Signature.getInstance("SHA256withRSA")
                signature.initSign(fiscalDigitalKey?.privateKey)
                signature.update(iicData.toByteArray())

                val iicSignature: ByteArray = signature.sign()
                val iicSignatureString: String =
                    DatatypeConverter.printHexBinary(iicSignature).toUpperCase(Locale.getDefault())


                // Heshovanje potpisa sa MD5 algoritmom za kreiranje IKOF-a
                val md: MessageDigest = MessageDigest.getInstance("MD5")
                val iic: ByteArray = md.digest(iicSignature)

                val iicString: String =
                    DatatypeConverter.printHexBinary(iic).toUpperCase(Locale.getDefault())


                it.onNext(Pair(first = iicString, second = iicSignatureString))

            } catch (e: Exception) {
                e.printStackTrace()
                it.onError(e)
            }
        }
    }

    fun fiscalReceipt(fiscalizeReceipt: String): Observable<RemoteFiscalizeReceiptResponse> =
        taxApi.fiscalReceipt(
            requestBody = RequestBody.create(
                okhttp3.MediaType.parse(FISCAL_MEDIA_TYPE), fiscalizeReceipt
            )
        )
            .doOnNext {
                it.body?.registerInvoiceResponse?.fic
            }
            .doOnComplete {
                Timber.e("Complete")
            }
            .doOnError {
                Timber.e("Error" + it)
            }

    fun sendReceipt(localTransactionHead: LocalTransactionHead): Observable<RemoteTransactionHeadResponse> =
        transactionApi.sendTransactionHead(
            remoteTransactionHead = billyPosMapper.transactionHeadMapper.toRemoteModel(
                localTransactionHead
            )
        )

    fun getInvoiceIIC(idEntity: String?): Maybe<List<LocalTransactionHead>> =
        cartPaymentDao.getInvoiceIIC(idEntity = idEntity)

    fun getSummaryTransactionBodies(id: List<String?>): Maybe<List<LocalTransactionBody>> =
        cartPaymentDao.getSummaryTransactionBodies(id = id)

    fun updateOrders(transactionHeadList: List<LocalTransactionHead>): Maybe<Int> =
        cartPaymentDao.updateOrders(transactionHeadList = transactionHeadList)

}