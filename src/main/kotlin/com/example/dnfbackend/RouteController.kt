package com.example.dnfbackend

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute

@Controller
class RouteController {

    @GetMapping("/")
    fun indexRouter(): String {
        return "index"
    }
}
