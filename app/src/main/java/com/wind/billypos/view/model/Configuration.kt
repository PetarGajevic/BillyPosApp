package com.wind.billypos.view.model

import com.wind.billypos.base.Error
import org.threeten.bp.LocalDateTime
import java.security.PrivateKey
import java.security.cert.X509Certificate

data class Configuration(
    val company: Company? = null,
    val address: Address? = null,
    val userData: UserData? = null,
    val device: Device? = null,
    val license: License? = null,
    val deviceId: Int? = null,
    val maintainerCode: String? = null,
    val softCode: String? = null,
    val verifyInvoiceURL: String? = null,
    val nextInvOrdNum: Long? = null
){
    fun iicData(createdDateTime: String?, invoiceNumber: String?, totalPrice: String?) =
        "${company?.nuis}|${createdDateTime}|$invoiceNumber|${address?.businUnitCode}" +
                "|${device?.tcrCode}|${softCode}|$totalPrice"
}

    data class Company(
        val id: Long? = null,
        val nuis: String? = null,
        val name: String? = null,
        val fullAddress: String? = null,
        val city: String? = null,
        val country: String? = null,
        val logoImg: String? = null,
        val fiscalDigitalKey: FiscalDigitalKey? = null,
        val currencyCode: String? = null
    ): Error(){

        data class FiscalDigitalKey(
            val id: Long? = null,
            val certificate: X509Certificate? = null,
            val privateKey: PrivateKey? = null,
            val pib: String? = null,
            val expireDate: LocalDateTime = LocalDateTime.now(),
        ): Error()

    }

    data class Address(
        val id: Long? = null,
        val name: String? = null,
        val logoImg: String? = null,
        val fullAddress: String? = null,
        val businUnitCode: String? = null,
        val city: String? = null,
        val country: String? = null
    ): Error()

    data class UserData(
        val id: Long? = null,
        val email: String? = null,
        val fullName: String? = null,
        val operatorCode: String? = null,
        val device: String? = null
    ): Error()

    data class Device(
        val id: Long? = null,
        val deviceName: String? = null,
        val deviceImei: String? = null,
        val licenseKey: String? = null,
        val tcrCode: String? = null,
        val validFrom: String? = null,
        val validTo: String? = null
    ): Error()

    data class License(
        val id: Long? = null,
        val deviceId: Int? = null,
        val deviceImei: String? = null,
        val licenseKey: String? = null,
        val issueDate: String? = null,
        val expiryDate: String? = null,
        val validFrom: String? = null,
        val validTo: String? = null,
        val state: String? = null,
    ): Error()

