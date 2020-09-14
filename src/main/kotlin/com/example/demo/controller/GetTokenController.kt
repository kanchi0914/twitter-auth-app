package com.example.demo.controller

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.view.RedirectView
import twitter4j.Twitter
import twitter4j.TwitterFactory
import twitter4j.auth.RequestToken
import twitter4j.conf.ConfigurationBuilder
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import javax.servlet.http.HttpSession

@RestController
class GetTokenController {
    @Autowired
    private lateinit var session: HttpSession

    @CrossOrigin
    @RequestMapping(
            "api/getCallBackUrl",
            method = [RequestMethod.GET]
    )
    @ResponseStatus(HttpStatus.OK)
    fun getCallBackUrl(
            request: HttpServletRequest, response: HttpServletResponse?
    ): String{
        var twitterUrl = ""
        try {
            val twitter = twitter
            val callbackUrl = "http://localhost:8090/twitterCallback"
            val requestToken = twitter!!.getOAuthRequestToken(callbackUrl)
            val aaaaa = session!!.setAttribute("requestToken", requestToken)
            session!!.setAttribute("twitter", twitter)
            request.getSession(true).setAttribute("requestToken", requestToken );
            request.getSession(true).setAttribute("twitter", twitter );
            twitterUrl = requestToken.authorizationURL
            LOGGER.info("Authorization url is $twitterUrl")
            return ObjectMapper().writeValueAsString(requestToken)
        } catch (e: Exception) {
            LOGGER.error("Problem logging in with Twitter!", e)
            return ""
        }

    }

    @RequestMapping(
            "api/getT",
            method = [RequestMethod.GET]
    )
    @ResponseStatus(HttpStatus.OK)
    fun getT(): String{
        val twitter = twitter
        val callbackUrl = "http://localhost:4000/callback"
        val requestToken = twitter!!.getOAuthRequestToken(callbackUrl)
        ObjectMapper().writeValueAsString(requestToken)
        //ObjectMapper().readValue(requestToken!!, RequestToken::class.java)
        return ObjectMapper().writeValueAsString(requestToken)
    }

    @RequestMapping(
            "api/getT2",
            method = [RequestMethod.GET]
    )
    @ResponseStatus(HttpStatus.OK)
    fun getT2(): String{
        val twitter = twitter!!
        //twitter.setOAuthConsumer("UdESkq1kKYNBeh9YwfcEuGIfr", "l768oF7q8Sfd8LZJa3ItCDUFhTl9zUYqGkwHYt2mCfydWVGTWQ")
        return twitter.oAuth2Token.toString()
        //return twitter
    }

//
//
//    @CrossOrigin
//    @RequestMapping(
//            "api/callback2",
//            method = [RequestMethod.GET]
//    )
//    @ResponseStatus(HttpStatus.OK)
//    fun getCallBackUrl2(
//            @RequestParam(value = "oauth_verifier", required = false) oauthVerifier: String?,
//            @RequestParam(value = "denied", required = false) denied: String?,
//            request: HttpServletRequest, response: HttpServletResponse?
//    ): String{
//        val sssd= request.getSession(true).getAttribute("requestToken")
//        val sadadas = request.getSession(true).getAttribute("twitter")
//        var aaa = session!!.getAttribute("twitter")
//        var bbbbb =  session!!.getAttribute("requestToken")
//        val twitter = twitter
//        val callbackUrl = "http://localhost:4000/callback"
//        val requestToken = twitter!!.getOAuthRequestToken(callbackUrl)
//        return try {
//            val token = twitter.getOAuthAccessToken(requestToken, oauthVerifier)
//            session!!.setAttribute("AccessToken", token)
//            session!!.removeAttribute("requestToken")
//            return twitter.getHomeTimeline().first().toString()
//        } catch (e: Exception) {
//            //TwitterCallbackController.LOGGER.error("Problem getting token!", e)
//            return "redirect:http://localhost:4000"
//        }
//    }


    @CrossOrigin
    @RequestMapping(
            "api/callback3",
            method = [RequestMethod.GET]
    )
    @ResponseStatus(HttpStatus.OK)
    fun getCallBackUrl3(
            @RequestParam(value = "oauthToken", required = false) oauth_token: String?,
            @RequestParam(value = "oauthVerifier", required = false) oauthVerifier: String?,
            @RequestParam(value = "token", required = false) token: String?,
            @RequestParam(value = "tokenSecret", required = false) token_secret: String?
            //@RequestBody token: RequestModel
    ): String{
        val twitter = twitter
        //var token2 = twitter!!.getOAuthAccessToken(oauthVerifier)
        return try {
            val requestToken = RequestToken(token, token_secret)
            val accessToken = twitter!!.getOAuthAccessToken(requestToken, oauthVerifier)
            twitter.oAuthAccessToken = accessToken
            return twitter.getHomeTimeline().first().toString()
        } catch (e: Exception) {
            return "redirect:http://localhost:4000"
        }
    }

    @CrossOrigin
    @RequestMapping(
            "api/test",
            method = [RequestMethod.POST]
    )
    @ResponseStatus(HttpStatus.OK)
    fun getTest(
            @RequestBody token: RequestModel
    ): String{
        return token.toString()
    }

    /*
     * Instantiates the Twitter object
     */
    val twitter: Twitter?
        get() {
            var twitter: Twitter? = null
            val consumerKey = "UdESkq1kKYNBeh9YwfcEuGIfr"
            val consumerSecret = "l768oF7q8Sfd8LZJa3ItCDUFhTl9zUYqGkwHYt2mCfydWVGTWQ"
            val builder = ConfigurationBuilder()
            builder.setOAuthConsumerKey(consumerKey)
            builder.setOAuthConsumerSecret(consumerSecret)
            val configuration = builder.build()
            val factory = TwitterFactory(configuration)
            twitter = factory.instance
            return twitter
        }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(GetTokenController::class.java)
    }
}

data class RequestModel @JsonCreator constructor (
        @JsonProperty("token") val token: String,
        @JsonProperty("tokenSecret") val tokenSecret: String,
        @JsonProperty("authorizationURL") val authorizationURL: String,
        @JsonProperty("authenticationURL") val authenticationURL: String
){
}