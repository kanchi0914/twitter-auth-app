package com.example.demo.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import com.example.demo.service.GcpService
import org.springframework.ui.Model
import twitter4j.Status
import twitter4j.Twitter
import twitter4j.auth.AccessToken
import javax.servlet.http.HttpSession

@Controller
class MainController {

    @Autowired
    var session: HttpSession? = null

    @Autowired
    lateinit var gcp: GcpService

    @RequestMapping("/top")
    fun home2(model: Model): String {
        val tokenObj = session!!.getAttribute("AccessToken") as AccessToken
        try {
            val twitter = session!!.getAttribute("twitter") as Twitter
            val statusesTmp = twitter.getHomeTimeline();
            var statuses = mutableListOf<Status>()
            for (status in statusesTmp) {
                if (gcp.isPositive(status.text)){
                    statuses.add(status)
                }
            }
            model.addAttribute("statuses", statuses)
            return "top"
        } catch (e: Exception) {
            return "redirect:/twitterLogin"
        }
    }

}