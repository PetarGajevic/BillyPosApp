package com.wind.billypos.data.remote.model.cashbalance

import com.google.gson.annotations.SerializedName


data class RemoteGetCashBalanceResponse(
    @SerializedName("status") val status: String?,
    @SerializedName("data") val data: List<RemoteCashBalance>? = null,
    @SerializedName("message") val message: String?,
    @SerializedName("requestStarted") val requestStarted: String?,
    @SerializedName("requestFinished") val requestFinished: String?,
    @SerializedName("requestTime") val requestTime: String?
)