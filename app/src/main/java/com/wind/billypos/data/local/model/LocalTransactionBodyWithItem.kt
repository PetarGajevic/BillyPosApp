package com.wind.billypos.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import com.wind.billypos.base.Error


data class LocalTransactionBodyWithItem (
    @Embedded(prefix = "transactionBody_") val body: LocalTransactionBody? = null,
    @ColumnInfo(name = "item") val item: LocalItem? = null
        ): Error()