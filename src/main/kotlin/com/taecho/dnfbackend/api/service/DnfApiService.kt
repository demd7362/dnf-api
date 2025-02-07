package com.taecho.dnfbackend.api.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.taecho.dnfbackend.api.dto.ApiResponseDto
import com.taecho.dnfbackend.api.dto.CharacterDto
import com.taecho.dnfbackend.api.dto.CharacterServerDto
import com.taecho.dnfbackend.api.dto.TimelineResponseDto
import com.taecho.dnfbackend.api.entity.Timeline
import com.taecho.dnfbackend.api.repository.TimelineRepository
import com.taecho.dnfbackend.common.config.Credentials
import com.taecho.dnfbackend.common.utils.DateUtils
import com.taecho.dnfbackend.logger
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
    private val objectMapper: ObjectMapper
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
            .body(object : ParameterizedTypeReference<ApiResponseDto<CharacterDto>>() {})
        if (result!!.rows.isEmpty()) {
            return null
        }
        return result.rows[0] // 서버, 캐릭터 검색이므로 [0]
    }

    private fun filterTimeline(rawData: TimelineResponseDto): List<TimelineResponseDto.Timeline.TimelineRow> {
        // filtering + characterName 추가
        return rawData.timeline.rows.fold(emptyList()) { acc: List<TimelineResponseDto.Timeline.TimelineRow>, row ->
            val isHell = row.code == 505 && row.data.dungeonName in HELL_DUNGEONS
            val isCard = row.code == 513 && row.data.dungeonName in CARD_DUNGEONS
            val isBossDrop = row.code == 505 && row.data.dungeonName in COMMON_DUNGEON
            row.characterName = rawData.characterName
            if (isCard || isHell || isBossDrop) acc + listOf(row) else acc
        }
    }

    fun refreshAdventureCharactersTimeline(adventureName: String): List<TimelineResponseDto.Timeline.TimelineRow> {
        val characterServers: List<CharacterServerDto> =
            timelineRepository.findCharacterServerByAdventureName(adventureName)
        val filteredTimeline: MutableList<TimelineResponseDto.Timeline.TimelineRow> = mutableListOf()
        for (characterServer in characterServers) {
            filteredTimeline += searchTimeline(characterServer.server, characterServer.characterName)
        }
        return filteredTimeline
    }

    fun searchAdventureTimelines(adventureName: String): List<TimelineResponseDto.Timeline.TimelineRow> {
        val timelines = timelineRepository.findAllByAdventureName(adventureName)
        val filteredTimelines = mutableListOf<TimelineResponseDto.Timeline.TimelineRow>()
        for (timeline in timelines) {
            val deserializedTimelines =
                objectMapper.readValue<List<TimelineResponseDto.Timeline.TimelineRow>>(timeline.filteredTimeline)
            filteredTimelines += deserializedTimelines
        }
        return filteredTimelines
    }

    fun searchTimeline(serverId: String, characterName: String): List<TimelineResponseDto.Timeline.TimelineRow> {
        val charactorDto = searchCharacter(serverId, characterName) ?: return emptyList()
        val timeline: Timeline? = timelineRepository.findTopByServerAndCharacterName(serverId, characterName)
//        if (timeline != null) { // 검색할때마다 db에 있는걸 꺼내와야할 이유가? 바로바로 보여주는게 맞다
//            val lastUpdatedAt = timeline.updatedAt
//            val now = LocalDateTime.now()
//            val diff = DateUtils.diff(lastUpdatedAt, now)
//            if (diff < 3600) { // 1시간 이내라면 db에 있는걸로 반환
//                return objectMapper.readValue(timeline.filteredTimeline)
//            }
//        }
        var rawData = restClient.get()
            .uri("/df/servers/$serverId/characters/${charactorDto.characterId}/timeline?apikey=${getApiKey()}&limit=100&code=505,513&start=2025-01-09 00:00&end=${DateUtils.getCurrentDate()}") // 던전 드랍, 카드 보상
            .retrieve()
            .body(TimelineResponseDto::class.java)!!
        val filteredTimeline: MutableList<TimelineResponseDto.Timeline.TimelineRow> =
            filterTimeline(rawData).toMutableList()

        while (rawData.timeline.next != null) {
            rawData = restClient.get()
                .uri("/df/servers/$serverId/characters/${charactorDto.characterId}/timeline?apikey=${getApiKey()}&limit=100&code=505,513&start=2025-01-09 00:00&end=${DateUtils.getCurrentDate()}&next=${rawData.timeline.next}") // 던전 드랍, 카드 보상
                .retrieve()
                .body(TimelineResponseDto::class.java)!!
            filteredTimeline += filterTimeline(rawData)
        }
        val newTimeline = if (timeline == null) {
            Timeline(
                server = serverId,
                characterName = characterName,
                adventureName = rawData.adventureName,
                filteredTimeline = objectMapper.writeValueAsString(filteredTimeline),
            )
        } else {
            timeline.filteredTimeline = objectMapper.writeValueAsString(filteredTimeline)
            timeline.updatedAt = LocalDateTime.now()
            timeline
        }
        timelineRepository.save(newTimeline)


        return filteredTimeline
    }

    fun randomChannel(): String {
        return channels.random()
    }

}
