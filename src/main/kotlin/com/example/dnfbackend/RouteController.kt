package com.example.dnfbackend

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
class RouteController {

    @GetMapping("/")
    fun indexRouter(): String {
        return "index"
    }
}
