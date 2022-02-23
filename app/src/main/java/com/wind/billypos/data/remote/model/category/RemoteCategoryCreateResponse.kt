package com.wind.billypos.data.remote.model.category

import com.google.gson.annotations.SerializedName

data class RemoteCategoryCreateResponse (
    @SerializedName("status") val status: String?,
    @SerializedName("data") val data: String?,
    @SerializedName("message") val message: String?,
    @SerializedName("requestStarted") val requestStarted: String?,
    @SerializedName("requestFinished") val requestFinished: String?,
    @SerializedName("requestTime") val requestTime: String?
    )