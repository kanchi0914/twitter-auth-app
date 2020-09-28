package com.example.demo.controller

import com.example.demo.service.GcpService
import com.example.demo.service.TwitterService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@CrossOrigin
@RestController
@RequestMapping("api/")
class TwitterApiController(
        private val twitterSevice: TwitterService,
        private val gcpService: GcpService
) {
    @RequestMapping(
            "getTimeLine",
            method = [RequestMethod.GET]
    )
    @ResponseStatus(HttpStatus.OK)
    fun getTimeLine(
            @RequestParam(value = "token", required = false) token: String,
            @RequestParam(value = "token_secret", required = false) tokenSecret: String
    ): String{
        return twitterSevice.getHomeTimeline(token, tokenSecret)
    }

    /**
     * スコア判定されたホームタイムラインを返す
     */
    @RequestMapping(
            "getJudgedTimeLine",
            method = [RequestMethod.GET]
    )
    @ResponseStatus(HttpStatus.OK)
    fun getJudgedTimeLine(
            @RequestParam(value = "token", required = false) token: String,
            @RequestParam(value = "token_secret", required = false) tokenSecret: String
    ): String{
        return twitterSevice.getJudgedHomeTimeline(token, tokenSecret)
    }

    @RequestMapping(
            "createFavorite",
            method = [RequestMethod.PUT]
    )
    @ResponseStatus(HttpStatus.OK)
    fun createFavorite(
            @RequestParam(value = "token", required = false) token: String,
            @RequestParam(value = "token_secret", required = false) tokenSecret: String,
            @RequestParam(value = "tweetId", required = false) tweetId: Long
    ): String{
        return twitterSevice.createFavorite(token, tokenSecret, tweetId)
    }

    @RequestMapping(
            "tweet",
            method = [RequestMethod.POST]
    )
    @ResponseStatus(HttpStatus.OK)
    fun tweet(
            @RequestParam(value = "token", required = false) token: String,
            @RequestParam(value = "token_secret", required = false) tokenSecret: String,
            @RequestParam(value = "message", required = false) message: String
    ): String{
        return twitterSevice.tweet(token, tokenSecret, message)
    }

    @RequestMapping(
            "getTest",
            method = [RequestMethod.GET]
    )
    @ResponseStatus(HttpStatus.OK)
    fun getTest(
            @RequestParam(value = "message", required = false) message: String
    ): String{
        return gcpService.getSentimentScore(message).toString()
    }


}
