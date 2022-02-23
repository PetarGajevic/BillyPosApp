package com.wind.billypos.data.remote.model.category

import com.google.gson.annotations.SerializedName

data class RemoteCategoryRequest (
    @SerializedName("categoryName") val categoryName: String? = null,
    @SerializedName("categoryColor") val categoryColor: String? = null,
    @SerializedName("categoryOrder") val categoryOrder: String? = null,
    @SerializedName("status") val status: String? = null,
        )