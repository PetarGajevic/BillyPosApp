package com.wind.billypos.data.remote.model

import com.google.gson.annotations.SerializedName


class RemoteLoginResponse(
    @SerializedName("authorization") val authorization: String?,
    @SerializedName("operators") val operators: List<RemoteLoginResponseOperators?>,
    @SerializedName("businUnits") val businUnits: List<RemoteLoginResponseBusinUnit?>,
    @SerializedName("certificate") val certificate: RemoteLoginResponseCertificate?,
    @SerializedName("licenses") val licenses: List<RemoteLoginResponseLicense?>
    )

class RemoteLoginResponseOperators(
    @SerializedName("username") val username: String?,
    @SerializedName("password") val password: String?
)

class RemoteLoginResponseBusinUnit(
    @SerializedName("id") val id: Long?,
    @SerializedName("name") val name: String?,
    @SerializedName("businUnitCode") val businUnitCode: String?,
    @SerializedName("address") val address: String?,
    @SerializedName("city") val city: String?,
    @SerializedName("country") val country: String?,
    @SerializedName("status") val status: String?,
    @SerializedName("desc") val desc: String?,
)

class RemoteLoginResponseCertificate(
    @SerializedName("data") val data: String?,
    @SerializedName("validFrom") val validFrom: String?,
    @SerializedName("validTo") val validTo: String?
)

class RemoteLoginResponseLicense(
    @SerializedName("id") val id: Long?,
    @SerializedName("licenseKey") val licenseKey: String?,
    @SerializedName("issueDate") val issueDate: String?,
    @SerializedName("state") val state: String?,
)