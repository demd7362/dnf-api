package com.taecho.dnfbackend.timeline.repository

import com.taecho.dnfbackend.timeline.dto.CharacterServerDto
import com.taecho.dnfbackend.timeline.entity.Timeline
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface TimelineRepository:JpaRepository<Timeline, Long> {

    fun findTopByCharacterId(characterId: String): Timeline?
    fun findTopByCharacterNameAndServer(characterName: String, server: String): Timeline?
    fun findAllByAdventureName(adventureName: String): List<Timeline>
    fun findAllByCharacterIdStartsWith(characterId: String): List<Timeline>

    @Query("select new com.taecho.dnfbackend.timeline.dto.CharacterServerDto(t.characterName, t.server) from Timeline t where t.adventureName = :adventureName")
    fun findCharacterServerByAdventureName(adventureName: String): List<CharacterServerDto>
}
