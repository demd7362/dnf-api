package com.taecho.dnfbackend

import com.taecho.dnfbackend.timeline.service.DnfApiService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping

@Controller
class RouteController(private val dnfApiService: DnfApiService) {
    // JvmStatic -> java static 처럼 사용
    // JvmField -> public으로 만들고 getter , setter 없이 직접 접근
    @GetMapping("/")
    fun indexRouter(): String {
        return "index"
    }
    @GetMapping("/statistics")
    fun statisticsRouter(model: Model): String {
        val channelFrequenciesDto = dnfApiService.getChannelFrequencies()
        model.addAttribute("channelFrequencies", channelFrequenciesDto)
        return "statistics"
    }
}
