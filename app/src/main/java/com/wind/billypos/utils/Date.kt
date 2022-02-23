package com.wind.billypos.utils

import org.threeten.bp.Instant
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId
import java.util.*

fun Date.convertToLocalDateTimeViaInstant(): LocalDateTime {
    return Instant.ofEpochMilli(this.time)
        .atZone(ZoneId.systemDefault())
        .toLocalDateTime()
}