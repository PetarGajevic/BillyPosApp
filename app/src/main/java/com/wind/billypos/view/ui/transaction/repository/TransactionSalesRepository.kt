package com.wind.billypos.view.ui.transaction.repository

import com.wind.billypos.data.dao.TransactionDao
import com.wind.billypos.data.local.model.LocalTransactionBody
import com.wind.billypos.data.local.model.LocalTransactionHead
import com.wind.billypos.services.FiscalisationCertificationUtil
import com.wind.billypos.view.model.Company
import com.wind.billypos.view.model.report.TransactionsTodayOverview
import io.reactivex.Maybe
import io.reactivex.Observable
import timber.log.Timber
import java.security.MessageDigest
import java.security.Signature
import java.util.*
import javax.xml.bind.DatatypeConverter

class TransactionSalesRepository(private val transactionDao: TransactionDao) {

    fun getTodayTotalSales(): Maybe<TransactionsTodayOverview> = transactionDao.getTodayTotalSales()

    fun getAllTransactions(): Maybe<List<LocalTransactionHead>> = transactionDao.getAllTransactions()

    fun getItems(trnHeadUUID: String): Maybe<List<LocalTransactionBody>> = transactionDao.getTransactions(transactionUUID = trnHeadUUID)

    fun getAllUnfiscalizedTransactions(): Maybe<List<LocalTransactionHead>> = transactionDao.getAllUnfiscalizedTransactions()

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

    fun getFiscalDigitalKey() =
        transactionDao.getFiscalDigitalKey()

    fun generateIKOF(
        iicData: String,
        fiscalDigitalKey: Company.FiscalDigitalKey?
    ): Observable<Pair<String, String>> {
        return Observable.create {
            Timber.e("IIC DATA %s", iicData)
            Timber.e("fiscalDigitalKey %s", fiscalDigitalKey)
            try{
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

                val iicString: String = DatatypeConverter.printHexBinary(iic).toUpperCase(Locale.getDefault())


                it.onNext(Pair(first = iicString, second = iicSignatureString))

            }catch (e: Exception){
                e.printStackTrace()
                it.onError(e)
            }
        }
    }

}