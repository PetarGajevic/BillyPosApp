package com.wind.billypos.view.ui.operators.viewmodel

import androidx.lifecycle.MutableLiveData
import com.wind.billypos.base.BaseViewModel
import com.wind.billypos.base.Error
import com.wind.billypos.view.mapper.ViewBillyPosMapper
import com.wind.billypos.view.model.Operator
import com.wind.billypos.view.ui.operators.repository.OperatorRepository
import timber.log.Timber

class EditOperatorViewModel(private val operatorRepository: OperatorRepository): BaseViewModel() {

    val mutableLiveDataOperator = MutableLiveData<Operator>()
    val mutableLiveDataCanChangePin = MutableLiveData<Boolean>()
    val mutableLiveDataCheckOperatorCode = MutableLiveData<Int>()
    val mutableLiveDataCheckPin = MutableLiveData<Int>()
    val mutableLiveDataSyncOperator = MutableLiveData<String>()

    private val viewBillyPosMapper = ViewBillyPosMapper()

    fun setOperator(operator: Operator){
        operator.code = Error.INITIALIZE
        mutableLiveDataOperator.value = operator
    }

    fun updateOperator(operator: Operator) {
        addCompositeDisposable(
            operatorRepository.updateOperator(operator = viewBillyPosMapper.viewOperatorMapper.toLocalModel(operator))
                .map {
                    viewBillyPosMapper.viewOperatorMapper.toModel(it)
                },
            onSuccess = {
                it.code = Error.SUCCESS
                mutableLiveDataOperator.value = it
            },
            onError = {
                Timber.e("On Error operator")
            }
        )
    }

    fun checkOldPin(id: Long?, oldPin: String?){
        addCompositeDisposable(
            operatorRepository.checkOldPin(id = id, oldPin = oldPin),
            onSuccess = {
                mutableLiveDataCanChangePin.value = it != null
            },
            onError = {
                mutableLiveDataCanChangePin.value = false
            }
        )
    }

    fun checkPin(pin: String?){
        addCompositeDisposable(
            operatorRepository.checkPin(pin = pin),
            onSuccess = {
                mutableLiveDataCheckPin.value = it
            },
            onError = {
                mutableLiveDataCheckPin.value = 0
            }
        )
    }

    fun checkOperatorCode(operatorCode: String?, id: Long?){
        addCompositeDisposable(
            operatorRepository.checkOperatorCode(operatorCode = operatorCode, id = id),
            onSuccess = {
                mutableLiveDataCheckOperatorCode.value = it
            },
            onError = {
                mutableLiveDataCheckOperatorCode.value = 0
            }
        )
    }

    fun sendUpdateOperator(operator: Operator){
        addCompositeDisposable(
            operatorRepository.sendUpdateOperator(operator = viewBillyPosMapper.viewOperatorMapper.toLocalModel(operator)),
            onSuccess = {
                mutableLiveDataSyncOperator.value = it.status ?: "Error"
            },
            onError = {
                mutableLiveDataSyncOperator.value =  "Error"
            }
        )
    }
}