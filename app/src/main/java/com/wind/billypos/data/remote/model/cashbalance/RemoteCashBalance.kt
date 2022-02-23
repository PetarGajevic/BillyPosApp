package com.wind.billypos.data.remote.model.cashbalance

import com.google.gson.annotations.SerializedName
import com.wind.billypos.data.remote.model.RemoteError

data class RemoteCashBalance(
    @SerializedName("id") val id: String? = null,
    @SerializedName("deviceId") val deviceId: String? = null,
    @SerializedName("cashAmount") val cashAmount: Double = 0.0,
    @SerializedName("balanceState") val balanceState: String? = null,
    @SerializedName("operation") val operation: String? = null,
    @SerializedName("responseUUID") val responseUUID: String? = null,
    @SerializedName("changeDateTime") val changeDateTime: String? = null,
    @SerializedName("createdAt") val createdAt: String? = null
) : RemoteError()