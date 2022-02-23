package com.wind.billypos.data.remote.model.subcategory

import com.google.gson.annotations.SerializedName

data class RemoteSubCategoryUpdateResponse (
    @SerializedName("status") val status: String?,
    @SerializedName("data") val data: RemoteSubCategory?,
    @SerializedName("message") val message: String?,
    @SerializedName("requestStarted") val requestStarted: String?,
    @SerializedName("requestFinished") val requestFinished: String?,
    @SerializedName("requestTime") val requestTime: String?
        )