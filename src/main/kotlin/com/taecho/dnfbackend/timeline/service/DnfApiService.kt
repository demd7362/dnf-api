package com.taecho.dnfbackend.timeline.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.taecho.dnfbackend.common.config.Credentials
import com.taecho.dnfbackend.common.utils.DateUtils
import com.taecho.dnfbackend.logger
import com.taecho.dnfbackend.timeline.dto.*
import com.taecho.dnfbackend.timeline.entity.Timeline
import com.taecho.dnfbackend.timeline.entity.TimelineStatistics
import com.taecho.dnfbackend.timeline.repository.TimelineRepository
import com.taecho.dnfbackend.timeline.repository.TimelineStatisticsRepository
import com.taecho.dnfbackend.timeline.response.ApiResponse
import com.taecho.dnfbackend.timeline.response.ChannelFrequencyResponse
import com.taecho.dnfbackend.timeline.response.TimelineResponse
import org.springframework.core.ParameterizedTypeReference
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClient
import java.time.LocalDateTime

@Service
class DnfApiService(
    private val restClient: RestClient,
    private val channels: List<String>,
    private val crendentials: Credentials,
    private val timelineRepository: TimelineRepository,
    private val objectMapper: ObjectMapper,
    private val timelineStatisticsRepository: TimelineStatisticsRepository
) {
    private final val log = logger()

    companion object {
        private val CARD_DUNGEONS = setOf(
            "모독 : 적막의 회랑",
            "환란 : 별내림 숲",
            "달이 잠긴 호수",
            "꿈결 속 솔리다리스",
            "애쥬어 메인",
            "침묵의 성소", // 애쥬어 메인
            "꿈결 속 흰 구름 계곡",
            "광포 : 크루얼 비스트",
            "광포 : 청해의 심장",
            "모독 : 일렁이는 군도",
            "환란 : 길잡이 강",
            // TODO 베누스 레기온
        )
        private val COMMON_DUNGEON = setOf(
            "적막의 회랑",
            "별내림 숲",
            "크루얼 비스트",
            "청해의 심장",
            "일렁이는 군도",
            "길잡이 강",
        )
        private val HELL_DUNGEONS = setOf("종말의 숭배자", "심연 : 종말의 숭배자")
    }

    private fun getApiKey(): String = crendentials.apiKey

    fun searchCharacter(serverId: String, characterName: String): CharacterDto? {
        val result = restClient.get()
            .uri("/df/servers/$serverId/characters?characterName=$characterName&apikey=${getApiKey()}")
            .retrieve()
            .body(object : ParameterizedTypeReference<ApiResponse<CharacterDto>>() {})
        if (result!!.rows.isEmpty()) {
            return null
        }

        return result.rows[0] // 서버, 캐릭터 검색이므로 [0]
    }

    private fun filterTimeline(rawData: TimelineResponse): List<TimelineResponse.Timeline.TimelineRow> {
        // filtering + characterName 추가
        return rawData.timeline.rows.fold(emptyList()) { acc: List<TimelineResponse.Timeline.TimelineRow>, row ->
            val isHell = row.code == 505 && row.data.dungeonName in HELL_DUNGEONS
            val isCard = row.code == 513 && row.data.dungeonName in CARD_DUNGEONS
            val isBossDrop = row.code == 505 && row.data.dungeonName in COMMON_DUNGEON
            row.characterName = rawData.characterName
            if (isCard || isHell || isBossDrop) acc + listOf(row) else acc
        }
    }

    private fun countMostChannels(filteredTimelines: List<TimelineResponse.Timeline.TimelineRow>): MostChannelsDto {
        val taechoCounter = mutableMapOf<String, Int>()
        val epicCounter = mutableMapOf<String, Int>()
        val legendaryCounter = mutableMapOf<String, Int>()
        for (filteredTimeline in filteredTimelines) {
            val counter = when (filteredTimeline.data.itemRarity) {
                "태초" -> taechoCounter
                "에픽" -> epicCounter
                "레전더리" -> legendaryCounter
                else -> continue
            }
            val channel =
                "${filteredTimeline.data.channelName ?: continue} ${filteredTimeline.data.channelNo ?: continue}"
            counter[channel] = (counter[channel] ?: 0) + 1
        }
        return MostChannelsDto(
            mostTaechoChannel = taechoCounter.maxByOrNull { it.value }?.key,
            mostEpicChannel = epicCounter.maxByOrNull { it.value }?.key,
            mostLegendaryChannel = legendaryCounter.maxByOrNull { it.value }?.key
        )
    }

    fun refreshAdventureCharactersTimeline(adventureName: String): List<TimelineResponse.Timeline.TimelineRow> {
        val characterServers: List<CharacterServerDto> =
            timelineRepository.findCharacterServerByAdventureName(adventureName)
        val filteredTimelines: MutableList<TimelineResponse.Timeline.TimelineRow> = mutableListOf()
        for (characterServer in characterServers) {
            filteredTimelines += searchTimeline(characterServer.server, characterServer.characterName)
        }
        return filteredTimelines
    }

    fun searchAdventureTimelines(adventureName: String): List<TimelineResponse.Timeline.TimelineRow> {
        val timelines = timelineRepository.findAllByAdventureName(adventureName)
        val filteredTimelines = mutableListOf<TimelineResponse.Timeline.TimelineRow>()
        for (timeline in timelines) {
            val deserializedTimelines =
                objectMapper.readValue<List<TimelineResponse.Timeline.TimelineRow>>(timeline.filteredTimeline)
            filteredTimelines += deserializedTimelines
        }
        return filteredTimelines
    }

    fun searchTimeline(serverId: String, characterName: String): List<TimelineResponse.Timeline.TimelineRow> {
        val charactorDto = searchCharacter(serverId, characterName) ?: return emptyList()
        val timeline: Timeline? = timelineRepository.findTopByCharacterId(charactorDto.characterId)
//        if (timeline != null) { // 검색할때마다 db에 있는걸 꺼내와야할 이유가? 바로바로 보여주는게 맞다
//            val lastUpdatedAt = timeline.updatedAt
//            val now = LocalDateTime.now()
//            val diff = DateUtils.diff(lastUpdatedAt, now)
//            if (diff < 3600) { // 1시간 이내라면 db에 있는걸로 반환
//                return objectMapper.readValue(timeline.filteredTimeline)
//            }
//        }
        var rawData = restClient.get()
            .uri("/df/servers/$serverId/characters/${charactorDto.characterId}/timeline?apikey=${getApiKey()}&limit=100&code=505,513&startDate=2025-01-09 00:00&endDate=${DateUtils.getCurrentDate()}") // 던전 드랍, 카드 보상
            .retrieve()
            .body(TimelineResponse::class.java)!!
        val filteredTimelines: MutableList<TimelineResponse.Timeline.TimelineRow> =
            filterTimeline(rawData).toMutableList()

        while (rawData.timeline.next != null) {
            rawData = restClient.get()
                .uri("/df/servers/$serverId/characters/${charactorDto.characterId}/timeline?apikey=${getApiKey()}&limit=100&code=505,513&startDate=2025-01-09 00:00&endDate=${DateUtils.getCurrentDate()}&next=${rawData.timeline.next}") // 던전 드랍, 카드 보상
                .retrieve()
                .body(TimelineResponse::class.java)!!
            filteredTimelines += filterTimeline(rawData)
        }
        val newTimeline = if (timeline == null) {
            Timeline(
                server = serverId,
                characterName = characterName,
                adventureName = rawData.adventureName,
                filteredTimeline = objectMapper.writeValueAsString(filteredTimelines),
                characterId = rawData.characterId
            )
        } else {
            timeline.filteredTimeline = objectMapper.writeValueAsString(filteredTimelines)
            timeline.updatedAt = LocalDateTime.now()
            timeline.characterName = rawData.characterName
            timeline.adventureName = rawData.adventureName
            timeline
        }

        // timeline_statistics에 레어리티별 가장 많이 먹은 채널 추가
        val mostChannelsDto = countMostChannels(filteredTimelines)
        newTimeline.timelineStatistics = TimelineStatistics(
            id = timeline?.timelineStatistics?.id,
            mostTaechoChannel = mostChannelsDto.mostTaechoChannel,
            mostEpicChannel = mostChannelsDto.mostEpicChannel,
            mostLegendaryChannel = mostChannelsDto.mostLegendaryChannel
        )
        timelineRepository.save(newTimeline)


        return filteredTimelines
    }

    fun randomChannel(): String {
        return channels.random()
    }

    fun getChannelFrequencies(): ChannelFrequencyResponse {
        val taechoFrequency = timelineStatisticsRepository.countFrequencyByTaechoChannel()
        val epicFrequency = timelineStatisticsRepository.countFrequencyByEpicChannel()
        val legendaryFrequency = timelineStatisticsRepository.countFrequencyByLegendaryChannel()
        return ChannelFrequencyResponse(
            taecho = taechoFrequency,
            epic = epicFrequency,
            legendary = legendaryFrequency
        )
    }

}
