package com.wind.billypos.data.remote.model.cashbalance

import com.google.gson.annotations.SerializedName

class RemoteCashBalanceRequest(
    val id: String? = null,
    val cashAmount: Double? = null,
    val operation: String? = null,
    val responseUUID: String? = null,
    val changeDateTime: String? = null,
    val fcdc: String? = null,
)