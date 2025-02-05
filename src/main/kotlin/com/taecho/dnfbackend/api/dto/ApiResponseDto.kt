package com.taecho.dnfbackend.api.dto

data class ApiResponseDto<T>(
    val rows: List<T>
)
