package com.wind.billypos.data.remote.model

import com.google.gson.annotations.SerializedName


data class RemoteConfigurationResponse(
    @SerializedName("status") val status: String?,
    @SerializedName("data") val data: RemoteConfiguration?,
    @SerializedName("message") val message: String?,
    @SerializedName("requestStarted") val requestStarted: String?,
    @SerializedName("requestFinished") val requestFinished: String?,
    @SerializedName("requestTime") val requestTime: String?
)