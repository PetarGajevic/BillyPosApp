package com.wind.billypos.data.remote.model.invoice

import com.google.gson.annotations.SerializedName
import com.wind.billypos.data.remote.model.RemotePaymentMethods
import com.wind.billypos.data.remote.model.item.RemoteTransactionItem


data class RemoteTransactionHead(
    @SerializedName("id") val id: String? = null,
    @SerializedName("iic") val iic: String? = null,
    @SerializedName("fic") val fic: String? = null,
    @SerializedName("invOrdNum") val invOrdNum: Long? = null,
    @SerializedName("invNum") val invNum: String? = null,
    @SerializedName("requestUUID") val requestUUID: String? = null,
    @SerializedName("totalWithoutVat") val totalWithoutVat: Double? = null,
    @SerializedName("totVatAmount") val totVatAmount: Double? = 0.0,
    @SerializedName("totalWithVat") val totalWithVat: Double? = 0.0,
    @SerializedName("customerId") val customerId: String = "",
    @SerializedName("isReversed") val isReversed: Int? = 0,
    @SerializedName("idTransactionReversed") val idTransactionReversed: String = "",
    @SerializedName("issueDateTime") val issueDateTime: String? = null,
    @SerializedName("fiscalizedAt") val fiscalizedAt: String? = null,
    @SerializedName("items") val items: List<RemoteTransactionItem>? = arrayListOf(),
    @SerializedName("paymentMethods") val paymentMethods: List<RemotePaymentMethods>? = arrayListOf()
)