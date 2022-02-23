package com.wind.billypos.data.remote.model.category

import com.google.gson.annotations.SerializedName
import com.wind.billypos.data.remote.model.item.RemoteItem


data class RemoteCategoryResponse(
    @SerializedName("status") val status: String?,
    @SerializedName("data") val data: List<RemoteCategory> = listOf(),
    @SerializedName("message") val message: String?,
    @SerializedName("requestStarted") val requestStarted: String?,
    @SerializedName("requestFinished") val requestFinished: String?,
    @SerializedName("requestTime") val requestTime: String?
)