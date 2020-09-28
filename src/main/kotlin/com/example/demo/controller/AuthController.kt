package com.example.demo.controller

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import twitter4j.Twitter
import twitter4j.TwitterFactory
import twitter4j.auth.RequestToken
import twitter4j.conf.ConfigurationBuilder
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import javax.servlet.http.HttpSession

@CrossOrigin
@Controller
class AuthController {
    @Autowired
    private lateinit var session: HttpSession

    /**
     * ログイン用エンドポイント
     */
    @CrossOrigin
    @RequestMapping(value = ["/login"], method = [RequestMethod.GET])
    private fun processFoarm(): String? {
        var twitterUrl = ""
        try {
            val twitter = twitter
            val callbackUrl = "http://localhost:8090/twitterCallback"
            val requestToken = twitter!!.getOAuthRequestToken(callbackUrl)
            session!!.setAttribute("requestToken", requestToken)
            return "redirect:${requestToken.authorizationURL}"
        } catch (e: Exception) {
            return ""
        }
    }

    /**
     * 認証後に戻ってくるコールバック用エンドポイント
     */
    @CrossOrigin
    @RequestMapping("/twitterCallback")
    fun twitterCallback(
            @RequestParam(value = "oauth_token", required = false) oauthToken: String?,
            @RequestParam(value = "oauth_verifier", required = false) oauthVerifier: String?,
            @RequestParam(value = "denied", required = false) denied: String?,
            request: HttpServletRequest, response: HttpServletResponse?, model: Model): String {
        // セッション情報からリクエストトークンを取得
        val requestToken = session!!.getAttribute("requestToken") as RequestToken
        val token = twitter!!.getOAuthAccessToken(requestToken, oauthVerifier)
        // フロントのログイン後の画面へ遷移
        return "redirect:http://localhost:4000/loggedIn?token=${token.token}&token_secret=${token.tokenSecret}"
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
}

