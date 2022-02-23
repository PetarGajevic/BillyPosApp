package com.wind.billypos.data.remote.model.operator

import com.google.gson.annotations.SerializedName

data class RemoteOperator(
    @SerializedName("businUnitId") val businUnitId: BusinUnitId? = BusinUnitId(),
    @SerializedName("cashRegister") val cashRegister: CashRegister? = CashRegister(),
    @SerializedName("username") val username: String? = null,
    @SerializedName("password") val password: String? = null,
    @SerializedName("fullName") val fullName: String? = null,
    @SerializedName("userRole") val userRole: String? = "OPERATOR_MOBILE",
    @SerializedName("permissions") val permissions: List<Any>? = listOf(),
    @SerializedName("nationalIdNo") val nationalIdNo: String? = null,
    @SerializedName("operatorCode") val operatorCode: String? = null,
    @SerializedName("status") val status: Boolean? = true,
    @SerializedName("pdfFormatSType") val pdfFormatSType: String? = null
){

    data class BusinUnitId(
        @SerializedName("id") val id: Long? = null
    )

    data class CashRegister(
        @SerializedName("id") val id: Long? = null
    )

    data class Permissions(
        @SerializedName("id") val id: Long? = null,
        @SerializedName("key") val key: String = "",
        @SerializedName("value") val value: String? = null,
        @SerializedName("active") val active: Boolean? = null,
    )


}