package com.example.dnfbackend.common.exception

class ApiFailureException(message: String, cause: Throwable?): RuntimeException(message, cause) {
}
