package com.taecho.dnfbackend.common.exception

class ApiFailureException(message: String, cause: Throwable?): RuntimeException(message, cause) {
}
