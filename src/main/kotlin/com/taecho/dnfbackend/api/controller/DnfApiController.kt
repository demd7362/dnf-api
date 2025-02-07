package com.taecho.dnfbackend.api.controller

import com.taecho.dnfbackend.api.service.DnfApiService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class DnfApiController(private val dnfApiService: DnfApiService) {

    // 미사용 주석처리
//    @GetMapping("/servers/{serverId}/characters/{characterName}")
//    fun searchCharacter(@PathVariable serverId: String, @PathVariable characterName: String): ResponseEntity<*> {
//        val result = dnfApiService.searchCharacter(serverId, characterName)
//        return ResponseEntity.ok(result)
//    }

    @GetMapping("/adventures/{adventureName}/refresh")
    fun refreshAdventureCharactersTimeline(@PathVariable adventureName: String): ResponseEntity<*>{
        val result = dnfApiService.refreshAdventureCharactersTimeline(adventureName)
        return ResponseEntity.ok(result)
    }


    @GetMapping("/servers/{serverId}/characters/{characterName}/timeline")
    fun searchTimeline(@PathVariable serverId: String, @PathVariable characterName: String): ResponseEntity<*> {
        val result = dnfApiService.searchTimeline(serverId, characterName)
        return ResponseEntity.ok(result)
    }

    @GetMapping("/adventures/{adventureName}")
    fun searchAdventureTimelines(@PathVariable adventureName: String): ResponseEntity<*> {
        val result = dnfApiService.searchAdventureTimelines(adventureName)
        return ResponseEntity.ok(result)
    }

    @GetMapping("/channels/random")
    fun randomChannel(): ResponseEntity<Any> {
        val result = dnfApiService.randomChannel()
        return ResponseEntity.ok(result)
    }
}
