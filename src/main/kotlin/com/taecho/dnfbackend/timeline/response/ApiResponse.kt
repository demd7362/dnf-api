package com.taecho.dnfbackend.timeline.response

data class ApiResponse<T>(
    val rows: List<T>
)
