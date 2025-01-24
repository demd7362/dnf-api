package com.example.dnfbackend.common.handler

import com.example.dnfbackend.common.exception.ApiFailureException
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(ApiFailureException::class)
    fun handleApiFailureException(e: ApiFailureException): ResponseEntity<*> {
        return ResponseEntity.ok().body(e.message)
    }
}
