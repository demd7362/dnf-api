package com.taecho.dnfbackend.timeline.repository

import com.taecho.dnfbackend.timeline.dto.ChannelFrequencyDto
import com.taecho.dnfbackend.timeline.entity.TimelineStatistics
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface TimelineStatisticsRepository : JpaRepository<TimelineStatistics, Long> {

    @Query(
        """
        select new com.taecho.dnfbackend.timeline.dto.ChannelFrequencyDto(t.mostEpicChannel, count(1))
        from TimelineStatistics t
        where t.mostEpicChannel is not null
        group by t.mostEpicChannel
        order by count(1) desc
    """
    )
    fun countFrequencyByEpicChannel(): List<ChannelFrequencyDto>
    @Query(
        """
        select new com.taecho.dnfbackend.timeline.dto.ChannelFrequencyDto(t.mostLegendaryChannel, count(1))
        from TimelineStatistics t
        where t.mostLegendaryChannel is not null
        group by t.mostLegendaryChannel
        order by count(1) desc
    """
    )
    fun countFrequencyByLegendaryChannel(): List<ChannelFrequencyDto>
    @Query(
        """
        select new com.taecho.dnfbackend.timeline.dto.ChannelFrequencyDto(t.mostTaechoChannel, count(1))
        from TimelineStatistics t
        where t.mostTaechoChannel is not null
        group by t.mostTaechoChannel
        order by count(1) desc
    """
    )
    fun countFrequencyByTaechoChannel(): List<ChannelFrequencyDto>
}
