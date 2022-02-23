package com.wind.billypos.view.ui.home

import com.wind.billypos.data.dao.CashBalanceDao
import com.wind.billypos.data.dao.LicenceDao
import com.wind.billypos.data.local.model.LocalCashBalance
import com.wind.billypos.data.local.model.LocalLicense
import com.wind.billypos.utils.DeviceId
import io.reactivex.Maybe

class HomeRepository(private val cashBalanceDao: CashBalanceDao, private val licenceDao: LicenceDao) {

    fun hasOpenedDay(deviceId: String?): Maybe<LocalCashBalance> = cashBalanceDao.hasOpenedDay()

    fun getLicence(): Maybe<LocalLicense> = licenceDao.getLicence()
}