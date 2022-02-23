package com.wind.billypos.view.ui.login.viewmodel

import com.wind.billypos.base.BaseViewModel
import com.wind.billypos.base.Error
import com.wind.billypos.data.remote.model.RemoteLoginRequest
import com.wind.billypos.view.ui.login.repository.LoginRepository
import androidx.lifecycle.MutableLiveData
import com.wind.billypos.data.remote.mapper.BillyPosMapper
import com.wind.billypos.view.mapper.ViewBillyPosMapper
import com.wind.billypos.view.model.*
import org.threeten.bp.LocalDateTime
import timber.log.Timber

class LoginViewModel(private val loginRepository: LoginRepository) : BaseViewModel() {
    private val viewBillyPosMapper: ViewBillyPosMapper = ViewBillyPosMapper()
    private val billyPosMapper: BillyPosMapper = BillyPosMapper()

    val mutableLiveDataLoginRequest = MutableLiveData<LoginRequest>()

    val mutableLiveDataEmailError = MutableLiveData(false)
    val mutableLiveDataEmailErrorText = MutableLiveData("")

    val mutableLiveDataToken = MutableLiveData<String?>()
    val test = MutableLiveData("Test")

    private val mutableLiveDataPasswordError = MutableLiveData(false)
    private val mutableLiveDataPasswordErrorText = MutableLiveData("")

    val mutableLiveDataHasLicence = MutableLiveData<License?>()
    val mutableLiveDataConfiguration = MutableLiveData<Configuration>()
    val mutableLiveDataLogin = MutableLiveData<Boolean>()

    init {
        insertCategory()
        insertCategory1()
        insertSubCategory()
        insertItem()
        insertItem1()
    }

    fun login(loginRequest: LoginRequest) {
        addCompositeDisposable(
            loginRepository.login(
                loginRequest = billyPosMapper.loginRequestMapper.toRemoteModel(loginRequest)
            ),
            handleError = true,
            onSuccess = {
                mutableLiveDataToken.value = it?.authorization
            },
            onError = {
                Timber.e("Usao on Error")
            }
        )
    }

    fun setLoginRequest(loginRequest: LoginRequest) {
        loginRequest.code = Error.INITIALIZE
        mutableLiveDataLoginRequest.value = loginRequest
    }

    fun onEmailInvalid(message: String) {
        mutableLiveDataEmailError.value = true
        mutableLiveDataEmailErrorText.value = message
    }

    fun onPasswordInvalid(message: String) {
        mutableLiveDataPasswordError.value = true
        mutableLiveDataPasswordErrorText.value = message
    }


    fun insertCategory() {
        val category = Category(
            id = 332,
            idCompany = 1,
            categoryName = "Restorant",
            categoryColor = "#@@@",
            categoryOrder = 1
        )
        addCompositeDisposable(
            loginRepository.insertCategory(
                category = viewBillyPosMapper.viewCategoryMapper.toLocalModel(
                    category
                )
            ), onSuccess = {
                Timber.e("Category %s", it)
            },
            onError = {
                Timber.e("Category error")
            }
        )
    }

    fun insertCategory1() {
        val category = Category(
            id = 334,
            idCompany = 1,
            categoryName = "Restorant123",
            categoryColor = "#@@@",
            categoryOrder = 1
        )
        addCompositeDisposable(
            loginRepository.insertCategory(
                category = viewBillyPosMapper.viewCategoryMapper.toLocalModel(
                    category
                )
            ), onSuccess = {
                Timber.e("Category %s", it)
            },
            onError = {
                Timber.e("Category error")
            }
        )
    }


    fun insertSubCategory() {
        val subCategory = SubCategory(
            id = 333,
            idCompany = 1,
            id_category = 332,
            subcategoryName = "SubRestorant",
            subcategoryOrder = 1,
            status = true,
        )
        addCompositeDisposable(
            loginRepository.insertSubCategory(
                subCategory = viewBillyPosMapper.viewSubCategoryMapper.toLocalModel(
                    subCategory
                )
            ), onSuccess = {
                Timber.e("SubCategory %s", it)
            },
            onError = {
                Timber.e("SubCategory error")
            }
        )
    }


    fun insertItem() {
        val item = Item(
            id = 1,
            uuid = "00ec9e13-3c93-499d-bf22-5cc59a15239a",
            idCompany = 0,
            idAddress = 36,
            idCategory = 332,
            idSubcategory = 333,
            itemName = "Terapi injeksion",
            itemDesc = null,
            barcode = null,
            itemImage = null,
            itemUnit = "XPP",
            itemType = "ITEM",
            itemPrice = 500.0,
            itemPriceWithDiscount = 600.0,
            vat = 20.0,
            status = 0,
            createdAt = LocalDateTime.now(),
            categoryColor = "#3B4453",
            lastServerSync = null,
        )
        addCompositeDisposable(
            loginRepository.insertItem(
                item = viewBillyPosMapper.viewItemMapper.toLocalModel(item)
            ),
            onSuccess = {
                Timber.e("Item s")
            },
            onError = {
                Timber.e("Item error")
            }
        )
    }

    fun insertItem1() {
        val item = Item(
            id = 2,
            uuid = "00ec9e13-3c93-499d-bf22-5cc59a15231k",
            idCompany = 0,
            idAddress = 36,
            idCategory = 334,
            idSubcategory = 0,
            itemName = "Stapici",
            itemDesc = null,
            barcode = null,
            itemImage = null,
            itemUnit = "XPP",
            itemType = "ITEM",
            itemPrice = 10.0,
            itemPriceWithDiscount = 12.0,
            vat = 20.0,
            status = 0,
            createdAt = LocalDateTime.now(),
            categoryColor = "#3B4453",
            lastServerSync = null,
        )
        addCompositeDisposable(
            loginRepository.insertItem(
                item = viewBillyPosMapper.viewItemMapper.toLocalModel(item)
            ),
            onSuccess = {
                Timber.e("Item s")
            },
            onError = {
                Timber.e("Item error")
            }
        )
    }

    fun getLicence() {
        addCompositeDisposable(
            loginRepository.getLicence().map { viewBillyPosMapper.viewLicenceMapper.toModel(it) },
            onSuccess = {
                mutableLiveDataHasLicence.value = it
            },
            onError = {
                mutableLiveDataHasLicence.value = null
            }
        )
    }

    fun getConfiguration(authToken: String){
        addCompositeDisposable(
            loginRepository.getConfiguration(authToken = authToken),
            onSuccess = {
                if(it.status == "OK"){
                    val localConfiguration = billyPosMapper.configurationMapper.toModel(it.data)
                    mutableLiveDataConfiguration.value = viewBillyPosMapper.viewConfigurationMapper.toModel(localConfiguration)
                }
            },
            onError = {

            }
        )
    }

    fun insertConfiguration(configuration: Configuration){
        addCompositeDisposable(
            loginRepository.insertConfiguration(configuration = viewBillyPosMapper.viewConfigurationMapper.toLocalModel(configuration)),
            onSuccess = {
                mutableLiveDataLogin.value = true
                Timber.e("ON SUCCESS %s", it)
            },
            onError = {
                mutableLiveDataLogin.value = false
                Timber.e("ON ERROR")
            }
        )
    }

}