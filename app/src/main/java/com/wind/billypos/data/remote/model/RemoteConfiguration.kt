package com.wind.billypos.data.remote.model

import com.google.gson.annotations.SerializedName

data class RemoteConfiguration (
    @SerializedName("company") val company: RemoteCompany? = null,
    @SerializedName("address") val address: RemoteAddress? = null,
    @SerializedName("user") val userData: RemoteUserData? = null,
    @SerializedName("device") val device: RemoteDevice? = null,
    @SerializedName("license") val license: RemoteLicense? = null,
    @SerializedName("deviceId") val deviceId: Int? = null,
    @SerializedName("maintainerCode") val maintainerCode: String? = null,
    @SerializedName("softCode") val softCode: String? = null,
    @SerializedName("verifyInvoiceURL") val verifyInvoiceURL: String? = null,
    @SerializedName("nextInvOrdNum") val nextInvOrdNum: Long? = null

)

    data class RemoteCompany(
        @SerializedName("id") val id: Long? = null,
        @SerializedName("nuis") val nuis : String? = null,
        @SerializedName("name") val name: String? = null,
        @SerializedName("fullAddress") val fullAddress: String? = null,
        @SerializedName("city") val city: String? = null,
        @SerializedName("country") val country: String? = null,
        @SerializedName("logoImg") val logoImg: String? = null
    )

    data class RemoteAddress(
        @SerializedName("id") val id: Long? = null,
        @SerializedName("name") val name: String? = null,
        @SerializedName("logoImg") val logoImg: String? = null,
        @SerializedName("fullAddress") val fullAddress: String? = null,
        @SerializedName("businUnitCode") val businUnitCode: String? = null,
        @SerializedName("city") val city: String? = null,
        @SerializedName("country") val country: String? = null
    )

    data class RemoteUserData(
        @SerializedName("id") val id: Long? = null,
        @SerializedName("email") val email: String? = null,
        @SerializedName("fullName") val fullName: String? = null,
        @SerializedName("operatorCode") val operatorCode: String? = null,
        @SerializedName("device") val device: String? = null
    )

    data class RemoteDevice(
        @SerializedName("id") val id: Long? = null,
        @SerializedName("deviceName") val deviceName: String? = null,
        @SerializedName("deviceImei") val deviceImei: String? = null,
        @SerializedName("licenseKey") val licenseKey: String? = null,
        @SerializedName("tcrCode") val tcrCode: String? = null,
        @SerializedName("validFrom") val validFrom: String? = null,
        @SerializedName("validTo") val validTo: String? = null
    )

    data class RemoteLicense(
        @SerializedName("id") val id: Long? = null,
        @SerializedName("deviceId") val deviceId: Int? = null,
        @SerializedName("deviceImei") val deviceImei: String? = null,
        @SerializedName("licenseKey") val licenseKey: String? = null,
        @SerializedName("issueDate") val issueDate: String? = null,
        @SerializedName("expiryDate") val expiryDate: String? = null,
        @SerializedName("validFrom") val validFrom: String? = null,
        @SerializedName("validTo") val validTo: String? = null,
        @SerializedName("state") val state: String? = null,
    )

