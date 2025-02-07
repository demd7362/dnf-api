package com.taecho.dnfbackend.api.repository

import com.taecho.dnfbackend.api.dto.CharacterServerDto
import com.taecho.dnfbackend.api.entity.Timeline
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface TimelineRepository:JpaRepository<Timeline, Long> {

    fun findTopByServerAndCharacterName(server: String, characterName: String): Timeline?
    fun findAllByAdventureName(adventureName: String): List<Timeline>

    @Query("select new com.taecho.dnfbackend.api.dto.CharacterServerDto(t.characterName, t.server) from Timeline t where t.adventureName = :adventureName")
    fun findCharacterServerByAdventureName(adventureName: String): List<CharacterServerDto>
}
