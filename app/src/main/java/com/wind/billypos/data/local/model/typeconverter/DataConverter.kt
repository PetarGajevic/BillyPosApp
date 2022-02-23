package com.wind.billypos.data.local.model.typeconverter

import androidx.room.TypeConverter
import org.threeten.bp.*
import java.io.ByteArrayInputStream
import java.security.KeyFactory
import java.security.PrivateKey
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate
import java.security.spec.PKCS8EncodedKeySpec

class DataConverter {

    /**
     * ZonedDateTime converter
     *
     * @param value
     * @return
     */
    @TypeConverter
    fun fromZonedTimestamp(value: Long?): ZonedDateTime? {
        return when (value) {
            null -> ZonedDateTime.now()
            else -> ZonedDateTime.ofInstant(Instant.ofEpochMilli(value), ZoneId.systemDefault())
        }
    }

    @TypeConverter
    fun toZonedTimestamp(date: ZonedDateTime?): Long {
        return when (date) {
            null -> 0L
            else -> date.toInstant().toEpochMilli()
        }
    }

    /**
     * LocalDate converter
     *
     * @param value
     * @return
     */
    @TypeConverter
    fun fromDateTimestamp(value: Long?): LocalDate? {
        return when (value) {
            null -> LocalDate.now()
            else -> Instant.ofEpochMilli(value).atZone(ZoneId.systemDefault()).toLocalDate()
        }
    }

    @TypeConverter
    fun toDateTimestamp(date: LocalDate?): Long {
        return when (date) {
            null -> 0L
            else -> date.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
        }
    }

    /**
     * LocalDateTime converter
     *
     * @param value
     * @return
     */
    @TypeConverter
    fun fromTimestamp(value: Long?): LocalDateTime? {
        return when (value) {
            null -> LocalDateTime.now()
            else -> LocalDateTime.ofInstant(Instant.ofEpochMilli(value), ZoneId.systemDefault())
        }
    }

    @TypeConverter
    fun toTimestamp(date: LocalDateTime?): Long {
        return when (date) {
            null -> 0L
            else -> date.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
        }
    }

    /**
     * X509Certificate converter
     *
     * @param value
     * @return
     */
    @TypeConverter
    fun fromX509Certificate(value: ByteArray?): X509Certificate =
        CertificateFactory.getInstance("X.509")
            .generateCertificate(ByteArrayInputStream(value)) as X509Certificate


    @TypeConverter
    fun toX509Certificate(x509Certificate: X509Certificate?): ByteArray? =
        x509Certificate?.encoded

    /**
     * PrivateKey converter
     *
     * @param value
     * @return
     */
    @TypeConverter
    fun fromPrivateKey(value: ByteArray?): PrivateKey? =
        KeyFactory.getInstance("RSA").generatePrivate(
            PKCS8EncodedKeySpec(
                value
            )
        )


    @TypeConverter
    fun toPrivateKey(privateKey: PrivateKey?): ByteArray? =
        privateKey?.encoded

}