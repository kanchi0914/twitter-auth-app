package com.example.demo.controller

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.web.bind.annotation.GetMapping

@Controller
class HtmlController {

    @GetMapping("/test2")
    fun blog(model: Model): String {
        model["title"] = "Blog"
        println("omaegawarui")
        return "blog"
    }

}