package com.wind.billypos.data.remote.model

import com.google.gson.annotations.SerializedName

data class RemoteLicenceResponse(
    @SerializedName("status") val status: String?,
    @SerializedName("data") val data: RemoteLicense?,
    @SerializedName("message") val message: String?,
    @SerializedName("requestStarted") val requestStarted: String?,
    @SerializedName("requestFinished") val requestFinished: String?,
    @SerializedName("requestTime") val requestTime: String?
){

    data class RemoteLicenseDataResponse(
        @SerializedName("id") val id: Long?,
        @SerializedName("licenseKey") val data: String?,
        @SerializedName("issueDate") val issueDate: String?,
        @SerializedName("expiryDate") val expiryDate: String?,
        @SerializedName("deviceId") val deviceId: Long?,
        @SerializedName("deviceImei") val deviceImei: String?,
        @SerializedName("validFrom") val validFrom: String?,
        @SerializedName("state") val state: String?
    )

}

