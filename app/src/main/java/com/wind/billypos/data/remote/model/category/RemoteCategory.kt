package com.wind.billypos.data.remote.model.category

import com.google.gson.annotations.SerializedName
import com.wind.billypos.data.remote.model.subcategory.RemoteSubCategory

data class RemoteCategory(
    @SerializedName("id") val id: Long? = null,
    @SerializedName("categoryName") val categoryName: String? = null,
    @SerializedName("categoryOrder") val categoryOrder: Int? = null,
    @SerializedName("categoryColor") val categoryColor: String? = null,
    @SerializedName("subcategories") val subcategories: List<RemoteSubCategory>? = null,
    @SerializedName("status") val status: Int? = 1
)