package com.wind.billypos.view.ui.login.repository

import com.wind.billypos.data.dao.LicenceDao
import com.wind.billypos.data.dao.LoginDao
import com.wind.billypos.data.local.model.*
import com.wind.billypos.data.remote.api.LoginApi
import com.wind.billypos.data.remote.mapper.BillyPosMapper
import com.wind.billypos.data.remote.model.RemoteLoginRequest
import com.wind.billypos.data.remote.model.RemoteLoginResponse
import io.reactivex.Maybe

class LoginRepository(
    private val loginDao: LoginDao,
    private val loginApi: LoginApi,
    private val licenceDao: LicenceDao
) {

    private val billyPosMapper = BillyPosMapper()

    fun login(loginRequest: RemoteLoginRequest): Maybe<RemoteLoginResponse?> =
        loginApi.login(login = loginRequest)

    fun insertCategory(category: LocalCategory) =
        loginDao.insertCategory(category = category)

    fun insertSubCategory(subCategory: LocalSubCategory) =
        loginDao.insertSubCategory(subCategory = subCategory)

    fun insertItem(item: LocalItem) =
        loginDao.insertItem(item = item)

    fun getLicence(): Maybe<LocalLicense> = licenceDao.getLicence()

    fun getConfiguration(authToken: String) = loginApi.configuration(authToken = authToken)

    fun insertConfiguration(configuration: LocalConfiguration) =
        loginDao.insertConfiguration(configuration = configuration)

}