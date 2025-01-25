package com.example.dnfbackend.api.dto

import com.fasterxml.jackson.annotation.JsonIgnore
import kotlin.reflect.jvm.internal.impl.load.kotlin.JvmType

data class TimelineResponseDto(
    val adventureName: String,
    val characterId: String,
    val characterName: String,
    val fame: Int,
    val guildId: String,
    val guildName: String,
    val jobGrowId: String,
    val jobGrowName: String,
    val jobId: String,
    val jobName: String,
    val level: Int,
    val serverId: String,
    val timeline: TimeLine
) {
    data class TimeLine(
        val date: Date,
        val next: String?,
        val rows: List<TimelineRow>
    ) {
        data class Date(
            val start: String,
            val end: String
        )
        data class TimelineRow (
            val code: Int,
            val data: TimelineData,
            val date: String,
            val name: String,
        ) {
            data class TimelineData (
                val channelName: String?,
                val channelNo: Int?,
                val dungeonName: String,
                val itemId: String,
                val itemName: String,
                val itemRarity: String,
                val mistGear: Boolean
            )
        }
    }
}
