package com.example.demo.service

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Service
import twitter4j.*
import twitter4j.auth.AccessToken
import twitter4j.auth.RequestToken
import twitter4j.conf.ConfigurationBuilder
import java.lang.Exception

@Service
class TwitterService(
        private val gcpService: GcpService,
        private val objectMapper: ObjectMapper = ObjectMapper()
) {

    data class TokenJson @JsonCreator constructor(
            @JsonProperty("token") val token: String,
            @JsonProperty("tokenSecret") val tokenSecret: String
    )

    data class ResultModel @JsonCreator constructor(
            @JsonProperty("message") val result: Boolean = true
    )

    data class StatusResponseModel @JsonCreator constructor(
            @JsonProperty("text") val text: String,
            @JsonProperty("name") val name: String,
            @JsonProperty("screenName") val screenName: String,
            @JsonProperty("profileImageURL") val profileImageURL: String,
            @JsonProperty("mediaEntities") val mediaEntities: List<MediaEntity>,
            @JsonProperty("sentimentScore") val sentimentScore: Float
    )

    fun buildClient(accessToken: String? = null, tokenSecret: String? = null): Twitter {
        var twitter: Twitter? = null
        val consumerKey = "UdESkq1kKYNBeh9YwfcEuGIfr"
        val consumerSecret = "l768oF7q8Sfd8LZJa3ItCDUFhTl9zUYqGkwHYt2mCfydWVGTWQ"
        val builder = ConfigurationBuilder()
        builder.setOAuthConsumerKey(consumerKey)
        builder.setOAuthConsumerSecret(consumerSecret)
        val configuration = builder.build()
        val factory = TwitterFactory(configuration)
        twitter = factory.instance
        if (accessToken != null && tokenSecret != null)
            twitter.oAuthAccessToken = AccessToken(accessToken, tokenSecret)
        return twitter
    }

    fun getAccessToken(token: String, tokenSecret: String, tokenVerifier: String): TokenJson {
        val requestToken = RequestToken(token, tokenSecret)
        val accessToken = buildClient().getOAuthAccessToken(requestToken, tokenVerifier)
        return TokenJson(accessToken.token, accessToken.tokenSecret)
    }

    fun getJudgedHomeTimeline(token: String, tokenSecret: String): String {
        try{
            val tweets = buildClient(token, tokenSecret).getHomeTimeline().subList(0, 10)
            val judgedTweets = tweets.map {
                StatusResponseModel(
                        text = it.text,
                        name = it.user.name,
                        screenName = it.user.screenName,
                        profileImageURL = it.user.profileImageURL,
                        mediaEntities = it.mediaEntities.toList(),
                        sentimentScore = gcpService.getSentimentScore(it.text) ?: -1f
                )
            }
            return ObjectMapper().writeValueAsString(judgedTweets)
        }catch(e: Exception){
            return ObjectMapper().writeValueAsString(ResultModel(false))
        }
    }

    /**
     * ホームタイムラインを取得し、Json形式で返す
     */
    fun getHomeTimeline(token: String, tokenSecret: String): String {
        val tweets = buildClient(token, tokenSecret).getHomeTimeline().subList(0, 10)
        return  objectMapper.writeValueAsString(tweets)
    }

    /**
     * お気に入りを追加
     */
    fun createFavorite(token: String, tokenSecret: String, tweetId: Long): String {
        try {
            val twitter = buildClient(token, tokenSecret)
            twitter.createFavorite(tweetId)
            return objectMapper.writeValueAsString(ResultModel(true))
        } catch (e: Exception) {
            return objectMapper.writeValueAsString(ResultModel(false))
        }
    }

    /**
     * ツイートする
     */
    fun tweet(token: String, tokenSecret: String, message: String): String {
        try {
            buildClient(token, tokenSecret).updateStatus(message)
            return objectMapper.writeValueAsString(ResultModel(true))
        } catch (e: Exception) {
            return objectMapper.writeValueAsString(ResultModel(false))
        }
    }
}
