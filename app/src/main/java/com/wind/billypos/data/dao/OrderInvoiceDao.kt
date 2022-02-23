package com.wind.billypos.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.wind.billypos.data.local.model.LocalEntityPoint
import com.wind.billypos.data.local.model.LocalTransactionHead
import com.wind.billypos.utils.enums.FiscalisationState
import io.reactivex.Maybe


@Dao
interface OrderInvoiceDao {

    @Query("SELECT * FROM entity_point")
    fun getEntityPoints(): Maybe<List<LocalEntityPoint>>

    @Query("SELECT * FROM transactions_head WHERE idEntity = :entityPointId AND isPaid = 0")
    fun checkFreeEntityPoints(entityPointId: String?): Maybe<LocalTransactionHead>

}