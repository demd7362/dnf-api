package com.taecho.dnfbackend.api.dto


data class CharacterDto (
    val characterId: String,
    val characterName: String,
    val fame: Int,
    val jobGrowId: String,
    val jobId: String,
    val jobName: String,
    val level: Int,
    val serverId: String
)
