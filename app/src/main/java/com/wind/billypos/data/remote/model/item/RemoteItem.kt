package com.wind.billypos.data.remote.model.item

import com.google.gson.annotations.SerializedName

data class RemoteItem(
    @SerializedName("id") val id: Long? = null,
    @SerializedName("barcode") val barcode: String? = null,
    @SerializedName("idCompany") val idCompany: Long? = null,
    @SerializedName("itemDesc") val itemDesc: String? = null,
    @SerializedName("itemImage") val itemImage: String? = null,
    @SerializedName("itemName") val itemName: String? = null,
    @SerializedName("itemType") val itemType: String? = null,
    @SerializedName("itemUnit") val itemUnit: String? = null,
    @SerializedName("itemPrice") val itemPrice: Double = 0.0,
    @SerializedName("idAddress") val idAddress: Long? = null,
    @SerializedName("idCategory") val idCategory: Long? = null,
    @SerializedName("idSubcategory") val idSubcategory: Long? = null,
    @SerializedName("costPrice") val costPrice: Double = 0.0,
    @SerializedName("vat") val vat: Double? = 0.0,
    @SerializedName("discount") val discount: Double = 0.0,
)