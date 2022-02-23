package com.wind.billypos.view.ui.setup.repository

import com.wind.billypos.data.dao.LicenceDao
import com.wind.billypos.data.local.model.LocalFiscalDigitalKey
import com.wind.billypos.data.local.model.LocalLicense
import com.wind.billypos.data.remote.api.LicenceApi
import com.wind.billypos.data.remote.model.RemoteLicenceResponse
import io.reactivex.Maybe
import okhttp3.RequestBody
import org.json.JSONObject

class LicenceSetupRepository(private val licenceApi: LicenceApi, private val licenceDao: LicenceDao) {

    fun checkLicence(licence: String): Maybe<RemoteLicenceResponse> =
        licenceApi.checkLicence(
            requestBody = RequestBody.create(
                okhttp3.MediaType.parse("application/json; charset=utf-8"), JSONObject(
                    mapOf("licenseKey" to licence)
                ).toString()
            )
        )

    fun insertLicence(licence: LocalLicense): Maybe<Long> = licenceDao.insert(licence = licence)

    fun getLicence(): Maybe<LocalLicense> = licenceDao.getLicence()

    fun getCertificate(): Maybe<LocalFiscalDigitalKey> = licenceDao.getCertificate()

}