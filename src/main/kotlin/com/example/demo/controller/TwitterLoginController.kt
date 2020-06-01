package com.example.demo.controller

import com.example.demo.service.GcpService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.CookieValue
import org.springframework.web.bind.annotation.RequestMapping
import javax.servlet.http.HttpSession

@Controller
class TwitterLoginController {
    @Autowired
    var session: HttpSession? = null

    @Autowired
    lateinit var gcp: GcpService

    @RequestMapping("/")
    fun home(model: Model?): String {
        return "redirect:twitterLogin"
    }

    @RequestMapping("/twitterLogin")
    fun twiterLogin(@CookieValue(name = "key1", required = false, defaultValue = "default value1") cookieValue: String?, model: Model): String {
        model.addAttribute("cookie", cookieValue)
        return "twitterLogin"
    }

    @RequestMapping("/logout")
    fun logout():String{
        session?.removeAttribute("twitter")
        session?.removeAttribute("requestToken")
        return "redirect:twitterLogin"
    }

}