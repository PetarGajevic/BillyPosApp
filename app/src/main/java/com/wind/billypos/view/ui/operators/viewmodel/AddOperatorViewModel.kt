package com.wind.billypos.view.ui.operators.viewmodel

import androidx.lifecycle.MutableLiveData
import com.wind.billypos.base.BaseViewModel
import com.wind.billypos.base.Error
import com.wind.billypos.view.mapper.ViewBillyPosMapper
import com.wind.billypos.view.model.Item
import com.wind.billypos.view.model.Operator
import com.wind.billypos.view.ui.operators.repository.OperatorRepository
import timber.log.Timber

class AddOperatorViewModel(private val operatorRepository: OperatorRepository): BaseViewModel() {

    val mutableLiveDataOperator = MutableLiveData<Operator>()
    val mutableLiveDataCheckPin = MutableLiveData<Int>()
    val mutableLiveDataCheckOperatorCode = MutableLiveData<Int>()
    val mutableLiveDataSyncOperator = MutableLiveData<String>()

    private val viewBillyPosMapper = ViewBillyPosMapper()

    fun setOperator(operator: Operator){
        operator.code = Error.INITIALIZE
        mutableLiveDataOperator.value = operator
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

    fun checkOperatorCode(operatorCode: String?){
        addCompositeDisposable(
            operatorRepository.checkOperatorCode(operatorCode = operatorCode),
            onSuccess = {
                mutableLiveDataCheckOperatorCode.value = it
            },
            onError = {
                mutableLiveDataCheckOperatorCode.value = 0
            }
        )
    }

    fun insertOperator(operator: Operator) {
        addCompositeDisposable(
            operatorRepository.insertOperator(operator = viewBillyPosMapper.viewOperatorMapper.toLocalModel(operator))
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

    fun sendOperator(operator: Operator){
        addCompositeDisposable(
            operatorRepository.sendOperator(operator = viewBillyPosMapper.viewOperatorMapper.toLocalModel(operator)),
            onSuccess = {
                mutableLiveDataSyncOperator.value = it.status ?: "Error"
            },
            onError = {
                mutableLiveDataSyncOperator.value =  "Error"
            }
        )
    }


}