package com.wind.billypos.data.remote.model.cashbalance

import com.google.gson.annotations.SerializedName

data class RemoteCashBalanceResponse(
    @SerializedName("status") val status: String?,
    @SerializedName("data") val data: RemoteCashDepositData?,
    @SerializedName("message") val message: String?,
    @SerializedName("requestStarted") val requestStarted: String?,
    @SerializedName("requestFinished") val requestFinished: String?,
    @SerializedName("requestTime") val requestTime: String?
)

data class RemoteCashDepositData(
    @SerializedName("id") val id: String? = null,
    @SerializedName("deviceId") val deviceId: String?= null,
    @SerializedName("cashAmount") val cashAmount: Double?= null,
    @SerializedName("balanceState") val balanceState: String?= null,
    @SerializedName("operation") val operation: String?= null,
    @SerializedName("responseUUID") val responseUUID: String?= null,
    @SerializedName("changeDateTime") val changeDateTime: String?= null,
    @SerializedName("createdAt") val createdAt: String?= null,
)