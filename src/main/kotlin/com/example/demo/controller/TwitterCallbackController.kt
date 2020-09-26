package com.example.demo.controller

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import twitter4j.Twitter
import twitter4j.TwitterFactory
import twitter4j.auth.RequestToken
import twitter4j.conf.ConfigurationBuilder
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import javax.servlet.http.HttpSession
import javax.swing.InputVerifier

@Controller
class TwitterCallbackController {
    @Autowired
    var session: HttpSession? = null

    fun buildTwitterClientByToken(token: String, tokenVerifier: String): Twitter{
        var twitter: Twitter? = null
        val consumerKey = "UdESkq1kKYNBeh9YwfcEuGIfr"
        val consumerSecret = "l768oF7q8Sfd8LZJa3ItCDUFhTl9zUYqGkwHYt2mCfydWVGTWQ"
        val builder = ConfigurationBuilder()
        builder.setOAuthConsumerKey(consumerKey)
        builder.setOAuthConsumerSecret(consumerSecret)
//        builder.setcon(token)
//        builder.setOAuthAccessTokenSecret(tokenVerifier)
        val configuration = builder.build()
        val factory = TwitterFactory(configuration)
        twitter = factory.instance
        return twitter
    }

    //This is where we land when we get back from Twitter
    @RequestMapping("/twitterCallback")
    fun twitterCallback(
            @RequestParam(value = "oauth_token", required = false) oauthToken: String?,
            @RequestParam(value = "oauth_verifier", required = false) oauthVerifier: String?,
                        @RequestParam(value = "denied", required = false) denied: String?,
                        request: HttpServletRequest, response: HttpServletResponse?, model: Model): String {


//        if (denied != null) {
//            return "redirect:twitterLogin"
//        }
//        val twitter = session!!.getAttribute("twitter") as Twitter
//        val requestToken = session!!.getAttribute("requestToken") as RequestToken


        val twitter = buildTwitterClientByToken(oauthToken!!, oauthVerifier!!)
        val aaa = twitter.getHomeTimeline()

        return "redirect:http://localhost:4000/callback"
//
//        return try {
//            //get the access token
//            val token = twitter.getOAuthAccessToken(requestToken, oauthVerifier)
//
//            session!!.setAttribute("AccessToken", token)
//            //take the request token out of the session
//            request.session.removeAttribute("requestToken")
//            //store the user name so we can display it on the web page
//            model.addAttribute("username", twitter.screenName)
//
//            return "redirect:http://localhost:4000/callback"
//        } catch (e: Exception) {
//            LOGGER.error("Problem getting token!", e)
//            return "redirect:http://localhost:4000"
//        }
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(TwitterCallbackController::class.java)
    }
}