package com.wind.billypos.data.remote.model.subcategory

import com.google.gson.annotations.SerializedName

data class RemoteSubCategory(
    @SerializedName("id") val id: Long? = null,
    @SerializedName("subcategoryName") val subcategoryName: String? = null,
    @SerializedName("subcategoryOrder") val subcategoryOrder: Int? = null,
    @SerializedName("status") val status: Int? = null,
)