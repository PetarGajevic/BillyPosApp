package com.wind.billypos.data.remote.model.customer

import com.google.gson.annotations.SerializedName
import com.wind.billypos.data.remote.model.item.RemoteGetItemResponse


data class RemoteGetCustomersResponse (
    @SerializedName("status") val status: String?,
    @SerializedName("data") val data: List<RemoteCustomer>,
    @SerializedName("message") val message: String?,
    @SerializedName("requestStarted") val requestStarted: String?,
    @SerializedName("requestFinished") val requestFinished: String?,
    @SerializedName("requestTime") val requestTime: String?
)