package com.example.demo.controller

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import twitter4j.Twitter
import twitter4j.auth.RequestToken
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import javax.servlet.http.HttpSession

@Controller
class TwitterCallbackController {
    @Autowired
    var session: HttpSession? = null

    //This is where we land when we get back from Twitter
    @RequestMapping("/twitterCallback")
    fun twitterCallback(@RequestParam(value = "oauth_verifier", required = false) oauthVerifier: String?,
                        @RequestParam(value = "denied", required = false) denied: String?,
                        request: HttpServletRequest, response: HttpServletResponse?, model: Model): String {
        if (denied != null) {
            //if we get here, the user didn't authorize the app
            return "redirect:twitterLogin"
        }

        //get the objects from the session

        //Twitter twitter = (Twitter) request.getSession().getAttribute("twitter");
        //RequestToken requestToken = (RequestToken) request.getSession().getAttribute("requestToken");
        val twitter = session!!.getAttribute("twitter") as Twitter
        val requestToken = session!!.getAttribute("requestToken") as RequestToken
        println(twitter)
        println(requestToken)
        return try {
            //get the access token
            val token = twitter.getOAuthAccessToken(requestToken, oauthVerifier)
            session!!.setAttribute("AccessToken", token)

            //take the request token out of the session
            request.session.removeAttribute("requestToken")

            //store the user name so we can display it on the web page
            model.addAttribute("username", twitter.screenName)
            "twitterLoggedIn"
        } catch (e: Exception) {
            LOGGER.error("Problem getting token!", e)
            "redirect:twitterLogin"
        }
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(TwitterCallbackController::class.java)
    }
}