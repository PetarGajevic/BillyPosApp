package com.wind.billypos.view.ui.cashbalance.repository

import com.wind.billypos.data.dao.CashBalanceDao
import com.wind.billypos.data.local.model.LocalCashBalance
import com.wind.billypos.data.local.model.LocalFiscalDigitalKey
import com.wind.billypos.data.remote.api.CashBalanceApi
import com.wind.billypos.data.remote.api.TaxApi
import com.wind.billypos.data.remote.mapper.BillyPosMapper
import com.wind.billypos.data.remote.model.cashbalance.RemoteCashBalanceResponse
import com.wind.billypos.data.remote.model.cashbalance.RemoteCashDepositResponse
import com.wind.billypos.data.remote.model.cashbalance.RemoteGetCashBalanceResponse
import com.wind.billypos.services.FiscalisationCertificationUtil
import com.wind.billypos.utils.convertToLocalDateTimeViaInstant
import com.wind.billypos.utils.enums.FiscalisationState
import com.wind.billypos.view.model.Company
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import okhttp3.RequestBody
import timber.log.Timber
import java.io.IOException
import java.io.InputStream
import java.security.KeyStore
import java.security.PrivateKey
import java.security.cert.X509Certificate

class CashBalanceRepository(
    private val cashBalanceDao: CashBalanceDao,
    private val cashBalanceApi: CashBalanceApi,
    private val taxApi: TaxApi
) {

    private val billyPosMapper: BillyPosMapper = BillyPosMapper()

    fun insertCashBalance(localCashBalance: LocalCashBalance): Single<LocalCashBalance?> =
        cashBalanceDao.insertCashBalance(localCashBalance = localCashBalance)
            .flatMap {
                cashBalanceDao.findCashBalanceById(it)
            }.doOnSuccess {
                it
            }


    fun updateCashBalance(localCashBalance: LocalCashBalance): Single<Int> =
        cashBalanceDao.updateCashBalance(localCashBalance = localCashBalance)


    fun getTotalCashAmount(): Maybe<Double> = cashBalanceDao.getTotalCashAmount().flatMap { cashBalance ->
        val total = cashBalance.plus(cashBalanceDao.getTodaySalesForTotal())
       Maybe.create {
           it.onSuccess(total)
       }
    }


    fun sendCashBalance(localCashBalance: LocalCashBalance): Observable<RemoteCashBalanceResponse> =
        cashBalanceApi.sendCashBalance(
            remoteCashBalance = billyPosMapper.cashBalanceMapper.toRemoteModel(localCashBalance)
        )


    fun getCompany() =
        cashBalanceDao.getCompany()

    fun hasOpenedDay(deviceId: String?): Maybe<LocalCashBalance> =
        cashBalanceDao.hasOpenedDay()

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
        cashBalanceDao.getFiscalDigitalKey()


    fun fiscalCashDeposit(cashDeposit: String): Observable<RemoteCashDepositResponse> =
        taxApi.fiscalCashDeposit(
            requestBody = RequestBody.create(
                okhttp3.MediaType.parse(FISCAL_MEDIA_TYPE), cashDeposit
            )
        )

    fun getTodaySales(): Maybe<Double> = cashBalanceDao.getTodaySales()

    fun getTodayStornoSales(): Maybe<Double> = cashBalanceDao.getTodayStornoSales()

    fun getTodayCashSales(): Maybe<Double> = cashBalanceDao.getTodayCashSales()

    fun getTodayCardSales(): Maybe<Double> = cashBalanceDao.getTodayCardSales()

    fun getUnfiscalizedCashBalance(): Maybe<List<LocalCashBalance>> =
        cashBalanceDao.getUnfiscalizedCashBalance()

    fun findCashBalanceByUUID(cashDeposit: RemoteCashDepositResponse.SoapBodyCashDepositResponse?): Single<Int> =
        cashBalanceDao.findCashBalanceByUUID(UUID = cashDeposit?.registerCashDepositResponse?.header?.requestUUID)
            .flatMap {
                cashBalanceDao.updateCashBalance(
                    localCashBalance = it.copy(
                        isFiscalised = true,
                        fcdc = cashDeposit?.registerCashDepositResponse?.FCDC,
                        cashBalanceState = FiscalisationState.FISCALIZED,
                        balanceState = FiscalisationState.FISCALIZED
                    )
                )
            }

    //    Cash Balance



    fun getNotSyncedCashBalance(): Maybe<List<LocalCashBalance>> = cashBalanceDao.getNotSyncedCashBalance()

    fun getCashBalance(): Maybe<RemoteGetCashBalanceResponse> = cashBalanceApi.getCashBalance()


    fun insertCashBalance(cashBalance: List<LocalCashBalance>) =
        cashBalanceDao.insertCashBalance(cashBalance = cashBalance)

    fun findCashBalanceByUUID(uuid: String?): Single<Int> =
        cashBalanceDao.findCashBalanceByUUID(UUID = uuid).map {
            cashBalanceDao.update(it.copy(isSync = true))
        }


    fun readDigitalKey(
        digitalKeyInputStream: InputStream?,
        digitalKeyPass: String
    ): Single<Company.FiscalDigitalKey> =
        Single.create {
            if (digitalKeyInputStream == null) {
                it.onError(NullPointerException("Input Stream for digital key is null!"))
            } else {
                try {
                    val digitalKeyPasswordCharArray = digitalKeyPass.toCharArray()
                    val keyStore = KeyStore.getInstance("PKCS12")
                    keyStore.load(digitalKeyInputStream, digitalKeyPasswordCharArray)

                    var keyStoreAlias = ""
                    var privateKey: PrivateKey? = null

                    for (alias in keyStore.aliases()) {
                        if (keyStore.isKeyEntry(alias)
                            && (keyStore.getKey(alias, digitalKeyPasswordCharArray) is PrivateKey)
                        ) {

                            keyStoreAlias = alias.toString()
                            privateKey = keyStore.getKey(
                                keyStoreAlias,
                                digitalKeyPass.toCharArray()
                            ) as PrivateKey
                            break
                        }
                    }

                    if (keyStoreAlias.isNotEmpty()) {
                        val certificate = keyStore.getCertificate(keyStoreAlias) as X509Certificate
                        val startIndex = certificate.subjectDN.name.lastIndexOf("VATME-")
                        val taxId = certificate.subjectDN.name.substring(
                            IntRange(
                                startIndex + 6,
                                startIndex + 13
                            )
                        )

                        it.onSuccess(
                            Company.FiscalDigitalKey(
                                privateKey = privateKey,
                                certificate = certificate,
                                expireDate = certificate.notAfter.convertToLocalDateTimeViaInstant(),
                                pib = taxId,
                            )
                        )
                    } else {
                        it.onError(IOException("PKCS12 key store mac invalid - wrong password or corrupted file"))
                    }
                } catch (io: IOException) {
                    it.onError(io)
                } catch (e: Exception) {
                    it.onError(e)
                }
            }
        }

    fun getCertificate(): Maybe<LocalFiscalDigitalKey> = cashBalanceDao.getCertificate()

    fun getCashAmountBalance(): Maybe<LocalCashBalance> = cashBalanceDao.getCashAmountBalance()

    fun getCashAmountIN(): Maybe<List<LocalCashBalance>> = cashBalanceDao.getCashAmountIN()

    fun getCashAmountOUT(): Maybe<List<LocalCashBalance>> = cashBalanceDao.getCashAmountOUT()

    companion object {
        const val FISCAL_MEDIA_TYPE = "text/xml; charset=utf-8"
    }

}