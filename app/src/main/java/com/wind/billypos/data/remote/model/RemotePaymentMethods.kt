package com.wind.billypos.data.remote.model

import com.google.gson.annotations.SerializedName

data class RemotePaymentMethods(
    @SerializedName("id") val id: String? = null,
    @SerializedName("paymentMethod") val paymentMethod: String? = null,
    @SerializedName("amount") val amount: Double? = null
)