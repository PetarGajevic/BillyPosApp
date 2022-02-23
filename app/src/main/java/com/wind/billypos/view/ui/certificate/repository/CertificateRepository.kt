package com.wind.billypos.view.ui.certificate.repository

import com.wind.billypos.data.dao.CompanyDao
import com.wind.billypos.data.local.model.LocalFiscalDigitalKey
import com.wind.billypos.utils.convertToLocalDateTimeViaInstant
import com.wind.billypos.view.model.Company
import io.reactivex.Single
import java.io.IOException
import java.io.InputStream
import java.security.KeyStore
import java.security.PrivateKey
import java.security.cert.X509Certificate

class CertificateRepository(private val companyDao: CompanyDao) {


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
                        val startIndex = certificate.subjectDN.name.lastIndexOf("SN =")
                        val taxId = certificate.subjectDN.name.substring(
                            IntRange(
                                startIndex + 4,
                                startIndex + 14
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


    fun insertFiscalDigitalKey(fiscalDigitalKey: LocalFiscalDigitalKey): Single<Long> =
        companyDao.insertFiscalDigitalKey(fiscalDigitalKey)
}