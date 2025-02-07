package com.taecho.dnfbackend.common.utils

import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.abs

object DateUtils {
    private val FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
    fun getCurrentDate(): String {
        return FORMATTER.format(LocalDateTime.now())
    }

    fun diff(ldt1: LocalDateTime, ldt2: LocalDateTime): Long {
        return abs(Duration.between(ldt2, ldt1).seconds)
    }


}
