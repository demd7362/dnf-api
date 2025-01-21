package com.example.dnfbackend.api.dto

data class ApiResponseDto<T>(
    val rows: List<T>
)
