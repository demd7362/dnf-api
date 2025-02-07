package com.taecho.dnfbackend.api.repository

import com.taecho.dnfbackend.api.entity.Timeline
import org.springframework.data.jpa.repository.JpaRepository

interface TimelineRepository:JpaRepository<Timeline, Long> {

    fun findTopByServerAndCharacterName(server: String, characterName: String): Timeline?
    fun findAllByAdventureName(adventureName: String): List<Timeline>
}
