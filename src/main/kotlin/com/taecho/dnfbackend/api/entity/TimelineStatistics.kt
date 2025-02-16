package com.taecho.dnfbackend.api.entity

import jakarta.persistence.*
import org.hibernate.annotations.Comment

@Entity
@Table(name = "timeline_statistics")
class TimelineStatistics (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long,

    @Column(name = "most_taecho_channel")
    @Comment("가장 태초 많이 먹은 채널")
    var mostTaechoChannel: String? = null,

    @Column(name = "most_epic_channel")
    @Comment("가장 에픽 많이 먹은 채널")
    var mostEpicChannel: String? = null,

    @Column(name = "most_legendary_channel")
    @Comment("가장 레전더리 많이 먹은 채널")
    var mostLegendaryChannel: String? = null
)
