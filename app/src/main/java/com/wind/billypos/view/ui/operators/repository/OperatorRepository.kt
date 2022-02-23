package com.wind.billypos.view.ui.operators.repository

import com.wind.billypos.data.dao.OperatorDao
import com.wind.billypos.data.local.model.LocalItem
import com.wind.billypos.data.local.model.LocalOperator
import com.wind.billypos.data.remote.api.OperatorApi
import com.wind.billypos.data.remote.mapper.BillyPosMapper
import com.wind.billypos.data.remote.model.RemoteConfigurationResponse
import com.wind.billypos.data.remote.model.operator.RemoteOperatorResponse
import io.reactivex.Maybe
import io.reactivex.Single

class OperatorRepository(
    private val operatorDao: OperatorDao,
    private val operatorApi: OperatorApi
) {

    private val billyPosMapper = BillyPosMapper()

    fun update(operator: LocalOperator): Maybe<Int> = operatorDao.update(operator = operator)

    fun insertOperator(operator: LocalOperator): Maybe<LocalOperator> =
        operatorDao.insert(operator = operator).flatMap {
            findOperatorById(id = it)
        }

    fun updateOperator(operator: LocalOperator): Maybe<LocalOperator> =
        operatorDao.update(operator = operator).flatMap {
            findOperatorById(id = operator.id!!)
        }

    fun sendOperator(operator: LocalOperator): Maybe<RemoteOperatorResponse> =
        operatorApi.sendOperator(operator = billyPosMapper.operatorMapper.toRemoteModel(operator))

    fun sendUpdateOperator(operator: LocalOperator): Maybe<RemoteOperatorResponse> =
        operatorApi.updateOperator(
            operator = billyPosMapper.operatorMapper.toRemoteModel(operator),
            id = operator.id
        )


    fun checkPin(pin: String?): Maybe<Int> = operatorDao.checkPin(pin = pin)

    fun checkOperatorCode(operatorCode: String?): Maybe<Int> =
        operatorDao.checkOperatorCode(operatorCode = operatorCode)

    fun checkOperatorCode(operatorCode: String?, id: Long?): Maybe<Int> =
        operatorDao.checkOperatorCode(operatorCode = operatorCode, id = id)

    fun checkOldPin(id: Long?, oldPin: String?): Maybe<LocalOperator?> =
        operatorDao.checkOldPin(id = id, oldPin = oldPin)

    fun getOperators(): Maybe<List<LocalOperator>> = operatorDao.getOperators()

    fun searchOperators(input: String?): Maybe<List<LocalOperator>> =
        operatorDao.searchOperators(input = input)

    private fun findOperatorById(id: Long): Maybe<LocalOperator> =
        operatorDao.findOperatorById(id = id)

}