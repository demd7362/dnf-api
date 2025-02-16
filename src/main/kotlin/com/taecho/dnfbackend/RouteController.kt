package com.taecho.dnfbackend

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
class RouteController {
    // JvmStatic -> java static 처럼 사용
    // JvmField -> public으로 만들고 getter , setter 없이 직접 접근
    @GetMapping("/")
    fun indexRouter(): String {
        return "index"
    }
}
