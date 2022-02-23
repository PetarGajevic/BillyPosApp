package com.wind.billypos.data.remote.model.typeconverter

import com.tickaroo.tikxml.TypeConverter
import com.wind.billypos.BuildConfig
import org.threeten.bp.LocalDate
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter

object RemoteTypeConverter {

    class LocalDateTimeVATConverter() : TypeConverter<ZonedDateTime> {
        private val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern(BuildConfig.DATE_TIME_VAT_FORMAT)

        override fun read(value: String?): ZonedDateTime {
            return ZonedDateTime.parse(value, formatter)
        }

        override fun write(value: ZonedDateTime?): String {
            return formatter.format(value)
        }
    }

    class LocalDateTimeISODateConverter() : TypeConverter<LocalDate> {

        override fun read(value: String?): LocalDate {
            return LocalDate.parse(value, DateTimeFormatter.ISO_DATE)
        }

        override fun write(value: LocalDate?): String {
            return DateTimeFormatter.ISO_DATE.format(value)
        }
    }

}