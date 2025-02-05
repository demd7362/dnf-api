package com.taecho.dnfbackend.common.utils

import java.time.format.DateTimeFormatter

object DateUtils {
    private val FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
    fun getCurrentDate(): String {
        return FORMATTER.format(java.time.LocalDateTime.now())
    }
}
