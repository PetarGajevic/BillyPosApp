package com.wind.billypos.data.remote.model.invoice

import com.google.gson.annotations.SerializedName

data class RemoteGetTransactionHeadResponse(
    @SerializedName("status") val status: String?,
    @SerializedName("data") val data: List<RemoteTransactionHeadResponseBody?>,
    @SerializedName("message") val message: String?,
    @SerializedName("requestStarted") val requestStarted: String?,
    @SerializedName("requestFinished") val requestFinished: String?,
    @SerializedName("requestTime") val requestTime: String?
)