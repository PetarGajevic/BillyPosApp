package com.wind.billypos.view.ui.transaction.repository

import com.wind.billypos.data.dao.TransactionDao
import com.wind.billypos.data.local.model.LocalTransactionBody
import com.wind.billypos.data.local.model.LocalTransactionHead
import com.wind.billypos.data.local.model.LocalTransactionPayment
import com.wind.billypos.data.remote.api.TaxApi
import com.wind.billypos.data.remote.api.TransactionApi
import com.wind.billypos.data.remote.mapper.BillyPosMapper
import com.wind.billypos.data.remote.model.RemoteFiscalizeReceiptResponse
import com.wind.billypos.data.remote.model.invoice.RemoteTransactionHeadResponse
import com.wind.billypos.services.FiscalisationCertificationUtil
import com.wind.billypos.view.model.Company
import com.wind.billypos.view.model.TransactionBody
import com.wind.billypos.view.ui.cashbalance.repository.CashBalanceRepository
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import okhttp3.RequestBody
import timber.log.Timber
import java.security.MessageDigest
import java.security.Signature
import java.util.*
import javax.xml.bind.DatatypeConverter

class TransactionDetailsRepository(
    private val transactionDao: TransactionDao,
    private val taxApi: TaxApi,
    private val transactionApi: TransactionApi
) {

    private val billyPosMapper = BillyPosMapper()

    fun getTransactionsBody(trnHeadUUID: String): Maybe<List<LocalTransactionBody>> =
        transactionDao.getTransactionsBody(trnHeadUUID = trnHeadUUID)

    fun getPaymentMethods(trnHeadUUID: String): Maybe<List<LocalTransactionPayment>> =
        transactionDao.getPaymentMethods(trnHeadUUID = trnHeadUUID)

    fun updateTransactionHead(trnHead: LocalTransactionHead): Single<Int> =
        transactionDao.update(trnHead = trnHead)

    fun insertTransactionHead(trnHead: LocalTransactionHead): Single<Long> {
        Timber.e("Transactino headd %s", trnHead)
        return transactionDao.insert(trnHead = trnHead)
    }

    fun sendReceipt(localTransactionHead: LocalTransactionHead): Observable<RemoteTransactionHeadResponse> =
        transactionApi.sendTransactionHead(
            remoteTransactionHead = billyPosMapper.transactionHeadMapper.toRemoteModel(localTransactionHead)
        )

    fun insertTransactionList(trnList: List<LocalTransactionBody>): Maybe<List<Long>> =
        transactionDao.insertTransactionList(trnList = trnList)

    fun getLastInvoiceNum(): Maybe<Long> = transactionDao.getLastInvoiceNum()

    fun getFiscalDigitalKey() = transactionDao.getFiscalDigitalKey()

    fun fiscalReceipt(fiscalizeReceipt: String): Observable<RemoteFiscalizeReceiptResponse> =
        taxApi.fiscalReceipt(
            requestBody = RequestBody.create(
                okhttp3.MediaType.parse(CashBalanceRepository.FISCAL_MEDIA_TYPE), fiscalizeReceipt
            )
        )
            .doOnNext {
                Timber.e(it.body?.registerInvoiceResponse?.fic)
            }
            .doOnComplete {
                Timber.e("Complete")
            }
            .doOnError {
                Timber.e("Error" + it)
            }

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


}