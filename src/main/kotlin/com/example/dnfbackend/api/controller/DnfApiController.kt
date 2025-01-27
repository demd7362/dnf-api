package com.example.dnfbackend.api.controller

import com.example.dnfbackend.api.service.DnfApiService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/dnf")
class DnfApiController(private val dnfApiService: DnfApiService) {
    @GetMapping("/servers/{serverId}/characters/{characterName}")
    fun searchCharacter(@PathVariable serverId: String, @PathVariable characterName: String): ResponseEntity<Any> {
        val result = dnfApiService.searchCharacter(serverId, characterName)
        return ResponseEntity.ok(result)
    }

    @GetMapping("/servers/{serverId}/characters/{characterName}/timeline")
    fun searchTimeline(@PathVariable serverId: String, @PathVariable characterName: String): ResponseEntity<Any> {
        val result = dnfApiService.searchTimeline(serverId, characterName)
        return ResponseEntity.ok(result)
    }

    @GetMapping("/channels/random")
    fun randomChannel(): ResponseEntity<Any> {
        val result = dnfApiService.randomChannel()
        return ResponseEntity.ok(result)
    }
}
