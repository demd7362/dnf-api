package com.taecho.dnfbackend.timeline.entity

import jakarta.persistence.*
import org.hibernate.annotations.Comment

@Entity
@Table(name = "timeline_statistics")
class TimelineStatistics (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(name = "most_taecho_channel", columnDefinition = "VARCHAR(20)")
    @Comment("가장 태초 많이 먹은 채널")
    var mostTaechoChannel: String? = null,

    @Column(name = "most_epic_channel", columnDefinition = "VARCHAR(20)")
    @Comment("가장 에픽 많이 먹은 채널")
    var mostEpicChannel: String? = null,

    @Column(name = "most_legendary_channel", columnDefinition = "VARCHAR(20)")
    @Comment("가장 레전더리 많이 먹은 채널")
    var mostLegendaryChannel: String? = null
)
