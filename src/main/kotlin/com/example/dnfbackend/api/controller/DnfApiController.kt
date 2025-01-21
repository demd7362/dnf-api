package com.example.dnfbackend.api.controller

import com.example.dnfbackend.api.service.DnfApiService
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestClient

@RestController
@RequestMapping("/dnf")
class DnfApiController(private val dnfApiService: DnfApiService) {
    @GetMapping("/test")
    fun test():String {
        return "test"
    }
}
