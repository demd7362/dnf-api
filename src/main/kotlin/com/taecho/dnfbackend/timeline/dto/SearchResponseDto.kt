package com.taecho.dnfbackend.timeline.dto

import com.taecho.dnfbackend.timeline.response.TimelineResponse

data class SearchResponseDto(
    val timelines: List<TimelineResponse.Timeline.TimelineRow>,
    val groupedByAbyss: Map<String, List<*>>
)
