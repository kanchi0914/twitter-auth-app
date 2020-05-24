package com.example.demo.controller

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.view.RedirectView
import twitter4j.Twitter
import twitter4j.TwitterFactory
import twitter4j.conf.ConfigurationBuilder
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpSession

@Controller
class GetTokenController {
    @Autowired
    var session: HttpSession? = null

    @RequestMapping("/getToken")
    fun getToken(request: HttpServletRequest?, model: Model?): RedirectView {
        //this will be the URL that we take the user to
        var twitterUrl = ""
        try {
            //get the Twitter object
            val twitter = twitter

            //get the callback url so they get back here
            val callbackUrl = "http://localhost:8090/twitterCallback"

            //go get the request token from Twitter
            val requestToken = twitter!!.getOAuthRequestToken(callbackUrl)
            //
//			//put the token in the session because we'll need it later
//			request.getSession().setAttribute("requestToken", requestToken);
//
//			//let's put Twitter in the session as well
//			request.getSession().setAttribute("twitter", twitter);
//
            session!!.setAttribute("requestToken", requestToken)
            session!!.setAttribute("twitter", twitter)

            //now get the authorization URL from the token
            twitterUrl = requestToken.authorizationURL
            LOGGER.info("Authorization url is $twitterUrl")
        } catch (e: Exception) {
            LOGGER.error("Problem logging in with Twitter!", e)
        }

        //redirect to the Twitter URL
        val redirectView = RedirectView()
        redirectView.url = twitterUrl
        return redirectView
    }//set the consumer key and secret for our app

    //build the configuration

    //instantiate the Twitter object with the configuration

    //Twitter twitter = TwitterFactory.getSingleton();
    //twitter.setOAuthConsumer("UdESkq1kKYNBeh9YwfcEuGIfr", "l768oF7q8Sfd8LZJa3ItCDUFhTl9zUYqGkwHYt2mCfydWVGTWQ");

    /*
     * Instantiates the Twitter object
     */
    val twitter: Twitter?
        get() {
            var twitter: Twitter? = null

            //set the consumer key and secret for our app
            val consumerKey = "UdESkq1kKYNBeh9YwfcEuGIfr"
            val consumerSecret = "l768oF7q8Sfd8LZJa3ItCDUFhTl9zUYqGkwHYt2mCfydWVGTWQ"

            //build the configuration
            val builder = ConfigurationBuilder()
            builder.setOAuthConsumerKey(consumerKey)
            builder.setOAuthConsumerSecret(consumerSecret)
            val configuration = builder.build()

            //instantiate the Twitter object with the configuration
            val factory = TwitterFactory(configuration)
            twitter = factory.instance

            //Twitter twitter = TwitterFactory.getSingleton();
            //twitter.setOAuthConsumer("UdESkq1kKYNBeh9YwfcEuGIfr", "l768oF7q8Sfd8LZJa3ItCDUFhTl9zUYqGkwHYt2mCfydWVGTWQ");
            return twitter
        }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(GetTokenController::class.java)
    }
}