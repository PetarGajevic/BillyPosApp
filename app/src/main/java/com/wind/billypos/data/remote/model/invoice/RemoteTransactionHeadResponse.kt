package com.wind.billypos.data.remote.model.invoice

import com.google.gson.annotations.SerializedName
import com.wind.billypos.data.remote.model.RemotePaymentMethods
import com.wind.billypos.data.remote.model.item.RemoteItem

data class RemoteTransactionHeadResponse(
    @SerializedName("status") val status: String?,
    @SerializedName("data") val data: RemoteTransactionHeadResponseHeader?,
    @SerializedName("message") val message: String?,
    @SerializedName("requestStarted") val requestStarted: String?,
    @SerializedName("requestFinished") val requestFinished: String?,
    @SerializedName("requestTime") val requestTime: String?
)

data class RemoteTransactionHeadResponseHeader(
    @SerializedName("header") val header: RemoteTransactionHeadResponseBody?
)

data class RemoteTransactionHeadResponseBody(
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
    @SerializedName("items") val items: List<RemoteItem>? = arrayListOf(),
    @SerializedName("paymentMethods") val paymentMethods: List<RemotePaymentMethods>? = arrayListOf()
)