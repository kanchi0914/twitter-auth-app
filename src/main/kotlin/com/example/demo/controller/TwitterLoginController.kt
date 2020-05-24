package com.example.demo.controller

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.CookieValue
import org.springframework.web.bind.annotation.RequestMapping
import twitter4j.Twitter
import twitter4j.TwitterFactory
import twitter4j.auth.AccessToken
import javax.servlet.http.HttpSession

@Controller
class TwitterLoginController {
    @Autowired
    var session: HttpSession? = null

    //starting page for Twitter login demo
    @RequestMapping("/twitterLogin")
    fun twiterLogin(@CookieValue(name = "key1", required = false, defaultValue = "default value1") cookieValue: String?, model: Model): String {
        model.addAttribute("cookie", cookieValue)
        return "twitterLogin"
    }

    //redirect to demo if user hits the root
    @RequestMapping("/")
    fun home(model: Model?): String {
        return "redirect:twitterLogin"
    }

    @RequestMapping("/top")
    fun home2(model: Model): String {
        val tokenObj = session!!.getAttribute("AccessToken") as AccessToken
        try {
            val twitter = session!!.getAttribute("twitter") as Twitter
            val token = tokenObj.token
            val tokenSecret = tokenObj.tokenSecret
            model.addAttribute("username", twitter.screenName)
            model.addAttribute("favs", twitter.favorites)
            return "top"
        } catch (e: Exception) {
        }
        return "redirect:/twitterLogin"
    }

    @RequestMapping("/top2")
    fun mytop(@CookieValue(name = "twitterAccessToken", required = false) twitterAccessToken: String?,
              @CookieValue(name = "twitterTokenSecret", required = false) twitterTokenSecret: String?,
              model: Model): String {
        println("toeknsasasasasa$twitterAccessToken")
        return if (twitterAccessToken == null) {
            "redirect:twitterLogin"
        } else {
            val factory = TwitterFactory()
            val twitter = factory.instance
            twitter.setOAuthConsumer("UdESkq1kKYNBeh9YwfcEuGIfr", "l768oF7q8Sfd8LZJa3ItCDUFhTl9zUYqGkwHYt2mCfydWVGTWQ")
            model.addAttribute("token", twitterAccessToken)
            model.addAttribute("secret", twitterTokenSecret)
            val accessToken = AccessToken(twitterAccessToken, twitterTokenSecret)
            twitter.oAuthAccessToken = accessToken
            try {
                model.addAttribute("username", twitter.screenName)
                "top"
                //return "twitterLoggedIn";
            } catch (e: Exception) {
                //LOGGER.error("Problem getting token!",e);
                println(e)
                "redirect:twitterLogin"
            }


////            twitter.setOAuthConsumer("UdESkq1kKYNBeh9YwfcEuGIfr", "l768oF7q8Sfd8LZJa3ItCDUFhTl9zUYqGkwHYt2mCfydWVGTWQ");
//            AccessToken accessToken = new AccessToken(twitterAccessToken, twitterTokenSecret);
//            twitter.setOAuthAccessToken(accessToken);
//            try{
//                model.addAttribute("username", twitter.getScreenName());
//                return "twitterLoggedIn";
//            }catch (Exception e){
//                LOGGER.error("Problem getting token!",e);
//                System.out.println("koerhadae,eeee");
//                return "redirect:twitterLogin";
//            }
        }
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(TwitterCallbackController::class.java)
    }
}