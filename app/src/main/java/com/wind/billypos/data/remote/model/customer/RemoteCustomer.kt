package com.wind.billypos.data.remote.model.customer

import com.google.gson.annotations.SerializedName
import org.threeten.bp.LocalDateTime

data class RemoteCustomer(
    @SerializedName("id") val id: String? = null,
    @SerializedName("name") val name: String? = null,
    @SerializedName("idType") val idType: String? = null,
    @SerializedName("idNum") val idNum: String? = null,
    @SerializedName("email") val email: String? = null,
    @SerializedName("address") val address: String? = null,
    @SerializedName("city") val city: String? = null,
    @SerializedName("country") val country: String? = null,
    @SerializedName("isSync") val isSync: Boolean? = null,
    @SerializedName("lastServerSync") val lastServerSync: LocalDateTime? = null
)