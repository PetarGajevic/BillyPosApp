package com.wind.billypos.data.remote.model.item

import com.google.gson.annotations.SerializedName

data class RemoteTransactionItem(
    @SerializedName("id") val id: String? = null,
    @SerializedName("itemId") val itemId: String? = null,
    @SerializedName("unitPriceBefVAT") val unitPriceBefVAT: Double? = null,
    @SerializedName("unitPriceAfterVAT") val unitPriceAfterVAT: Double? = null,
    @SerializedName("priceBefVAT") val priceBefVAT: Double? = null,
    @SerializedName("priceAfterVAT") val priceAfterVAT: Double? = 0.0,
    @SerializedName("quantity") val quantity: Double? = 0.0,
    @SerializedName("vatRate") val vatRate: Double? = 0.0,
    @SerializedName("vatAmount") val vatAmount: Double? = 0.0
)