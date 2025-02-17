package com.taecho.dnfbackend.timeline.response

import com.taecho.dnfbackend.timeline.dto.ChannelFrequencyDto

data class ChannelFrequencyResponse (
    val taecho: List<ChannelFrequencyDto>,
    val epic: List<ChannelFrequencyDto>,
    val legendary: List<ChannelFrequencyDto>
)
