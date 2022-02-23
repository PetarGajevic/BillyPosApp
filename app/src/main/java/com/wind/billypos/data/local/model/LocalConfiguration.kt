package com.wind.billypos.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.wind.billypos.base.Error
import org.threeten.bp.LocalDateTime
import java.security.PrivateKey
import java.security.cert.X509Certificate


@Entity(tableName = "configuration")
data class LocalConfiguration(
    @PrimaryKey
    @ColumnInfo(name = "id") val id: Long = 1,
    @Embedded(prefix = "company_") val company: LocalCompany? = null,
    @Embedded(prefix = "address_") val address: LocalAddress? = null,
    @Embedded(prefix = "user_") val userData: LocalUserData? = null,
    @Embedded(prefix = "device_") val device: LocalDevice? = null,
    @Embedded(prefix = "license_") val license: LocalLicense? = null,
    @ColumnInfo(name = "deviceId") val deviceId: Int? = null,
    @ColumnInfo(name = "maintainerCode") val maintainerCode: String? = null,
    @ColumnInfo(name = "softCode") val softCode: String? = null,
    @ColumnInfo(name = "verifyInvoiceURL") val verifyInvoiceURL: String? = null,
    @ColumnInfo(name = "nextInvOrdNum") val nextInvOrdNum: Long? = null
)


    @Entity(tableName = "company")
    data class LocalCompany(
        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = "id") val id: Long? = null,
        @ColumnInfo(name = "nuis") val nuis: String? = null,
        @ColumnInfo(name = "name") val name: String? = null,
        @ColumnInfo(name = "fullAddress") val fullAddress: String? = null,
        @ColumnInfo(name = "city") val city: String? = null,
        @ColumnInfo(name = "country") val country: String? = null,
        @ColumnInfo(name = "logoImg") val logoImg: String? = null,
        @Embedded(prefix = "fiscalDigitalKey_") val fiscalDigitalKey: LocalFiscalDigitalKey? = null
    ): Error()

        @Entity(tableName = "fiscal_digital_key")
        data class LocalFiscalDigitalKey(
            @PrimaryKey(autoGenerate = true)
            @ColumnInfo(name = "id") val id: Long? = null,
            @ColumnInfo(
                name = "certificate",
                typeAffinity = ColumnInfo.BLOB
            ) val certificate: X509Certificate? = null,
            @ColumnInfo(
                name = "privateKey",
                typeAffinity = ColumnInfo.BLOB
            ) val privateKey: PrivateKey? = null,
            @ColumnInfo(name = "pib") val pib: String? = null,
            @ColumnInfo(name = "expireDate") val expireDate: LocalDateTime = LocalDateTime.now(),
        ): Error()


    data class LocalAddress(
        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = "id") val id: Long? = null,
        @ColumnInfo(name = "name") val name: String? = null,
        @ColumnInfo(name = "logoImg") val logoImg: String? = null,
        @ColumnInfo(name = "fullAddress") val fullAddress: String? = null,
        @ColumnInfo(name = "businUnitCode") val businUnitCode: String? = null,
        @ColumnInfo(name = "city") val city: String? = null,
        @ColumnInfo(name = "country") val country: String? = null
    ): Error()

    data class LocalUserData(
        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = "id") val id: Long? = null,
        @ColumnInfo(name = "email") val email: String? = null,
        @ColumnInfo(name = "fullName") val fullName: String? = null,
        @ColumnInfo(name = "operatorCode") val operatorCode: String? = null,
        @ColumnInfo(name = "device") val device: String? = null
    ): Error()

    data class LocalDevice(
        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = "id") val id: Long? = null,
        @ColumnInfo(name = "deviceName") val deviceName: String? = null,
        @ColumnInfo(name = "deviceImei") val deviceImei: String? = null,
        @ColumnInfo(name = "licenseKey") val licenseKey: String? = null,
        @ColumnInfo(name = "tcrCode") val tcrCode: String? = null,
        @ColumnInfo(name = "validFrom") val validFrom: String? = null,
        @ColumnInfo(name = "validTo") val validTo: String? = null
    ): Error()

    @Entity(tableName = "licence")
    data class LocalLicense(
        @PrimaryKey
        @ColumnInfo(name = "id") val id: Long? = null,
        @ColumnInfo(name = "deviceId") val deviceId: Int? = null,
        @ColumnInfo(name = "deviceImei") val deviceImei: String? = null,
        @ColumnInfo(name = "licenseKey") val licenseKey: String? = null,
        @ColumnInfo(name = "issueDate") val issueDate: String? = null,
        @ColumnInfo(name = "expiryDate") val expiryDate: String? = null,
        @ColumnInfo(name = "validFrom") val validFrom: String? = null,
        @ColumnInfo(name = "validTo") val validTo: String? = null,
        @ColumnInfo(name = "state") val state: String? = null,
    ): Error()
