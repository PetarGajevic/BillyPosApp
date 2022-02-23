package com.wind.billypos.data.remote.model.operator

import com.google.gson.annotations.SerializedName

data class RemoteOperatorResponse (
    @SerializedName("status") val status: String?,
    @SerializedName("data") val data: RemoteOperator?,
    @SerializedName("message") val message: String?,
    @SerializedName("requestStarted") val requestStarted: String?,
    @SerializedName("requestFinished") val requestFinished: String?,
    @SerializedName("requestTime") val requestTime: String?
        )