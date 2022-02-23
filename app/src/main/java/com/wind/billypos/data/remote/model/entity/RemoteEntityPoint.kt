package com.wind.billypos.data.remote.model.entity

import com.google.gson.annotations.SerializedName

data class RemoteEntityPoint(
    @SerializedName("id") val id: String? = null,
    @SerializedName("entityName") val entityName: String? = null,
    @SerializedName("entityColor") val entityColor: String? = null,
    @SerializedName("idBusinessUnit") val idBusinessUnit: Int?  = null
)