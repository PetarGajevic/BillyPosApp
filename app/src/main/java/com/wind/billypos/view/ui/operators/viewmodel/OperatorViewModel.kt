package com.wind.billypos.view.ui.operators.viewmodel

import androidx.lifecycle.MutableLiveData
import com.wind.billypos.base.BaseViewModel
import com.wind.billypos.view.mapper.ViewBillyPosMapper
import com.wind.billypos.view.model.Operator
import com.wind.billypos.view.ui.operators.repository.OperatorRepository
import timber.log.Timber

class OperatorViewModel(private val operatorRepository: OperatorRepository): BaseViewModel() {

    private val viewBillyPosMapper = ViewBillyPosMapper()

    val mutableLiveDataOperators = MutableLiveData<List<Operator>>()
    val mutableLiveDataUpdateOperator = MutableLiveData<Boolean>()


    fun updateOperator(operator: Operator){
        addCompositeDisposable(
            operatorRepository.update(operator = viewBillyPosMapper.viewOperatorMapper.toLocalModel(operator)),
            onSuccess = {
                Timber.e("Updejt %s" , it)
                mutableLiveDataUpdateOperator.value = true
            },
            onError = {
                Timber.e("Updejt Error")
                mutableLiveDataUpdateOperator.value = true
            }
        )
    }

    fun getOperators(){
        addCompositeDisposable(
            operatorRepository.getOperators().map {
                viewBillyPosMapper.viewOperatorMapper.toListOfModel(it)
            },
            onSuccess = {
                mutableLiveDataOperators.value = it
            },
            onError = {
                mutableLiveDataOperators.value = listOf()
            }
        )
    }

    fun searchOperators(input: String?){
        addCompositeDisposable(
            operatorRepository.searchOperators(input = input).map {
                viewBillyPosMapper.viewOperatorMapper.toListOfModel(it)
            },
            onSuccess = {
                mutableLiveDataOperators.value = it
            },
            onError = {
                mutableLiveDataOperators.value = listOf()
            }
        )
    }

}