package com.example.demo.service

import com.google.cloud.language.v1.Document
import com.google.cloud.language.v1.LanguageServiceClient
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Service
import twitter4j.Status
import java.util.*

@Service
class GcpService(

){
    private var client: LanguageServiceClient? = null

    init {
        try {
            val google: MutableMap<String, String> = HashMap()
            google["GOOGLE_APPLICATION_CREDENTIALS"] = ClassPathResource("MyTwitterProject-1389e6d939b8.json").uri.path
            setEnv(google)
            client = LanguageServiceClient.create()
        } catch (e: java.lang.Exception) {
            println(e)
        }
    }



    //ref https://stackoverflow.com/questions/51127494/define-google-application-credentials-for-google-cloud-speech-java-desktop-app
    fun setEnv(newenv: Map<String, String>?) {
        try {
            val processEnvironmentClass = Class.forName("java.lang.ProcessEnvironment")
            val theEnvironmentField = processEnvironmentClass.getDeclaredField("theEnvironment")
            theEnvironmentField.isAccessible = true
            val env = theEnvironmentField[null] as MutableMap<String, String>
            env.putAll(newenv!!)
            val theCaseInsensitiveEnvironmentField = processEnvironmentClass
                    .getDeclaredField("theCaseInsensitiveEnvironment")
            theCaseInsensitiveEnvironmentField.isAccessible = true
            val cienv = theCaseInsensitiveEnvironmentField[null] as MutableMap<String, String>
            cienv.putAll(newenv)
        } catch (e: NoSuchFieldException) {
            val classes = Collections::class.java.declaredClasses
            val env = System.getenv()
            for (cl in classes) {
                if ("java.util.Collections\$UnmodifiableMap" == cl.name) {
                    val field = cl.getDeclaredField("m")
                    field.isAccessible = true
                    val obj = field[env]
                    val map = obj as MutableMap<String, String>
                    map.clear()
                    map.putAll(newenv!!)
                }
            }
        }
    }

//    fun getText(text: String):String{
//        if (client != null){
//            val doc = Document.newBuilder().setContent(text).setType(Document.Type.PLAIN_TEXT).setLanguage("ja").build()
//            val sentiment = client?.analyzeSentiment(doc)?.documentSentiment
//            return ("Sentiment: ${sentiment?.score}, ${sentiment?.magnitude}")
//        }
//        return ""
//    }

    fun getSentimentScore(text: String):Float?{
        if (client != null){
            val doc = Document.newBuilder().setContent(text).setType(Document.Type.PLAIN_TEXT).setLanguage("ja").build()
            val sentiment = client?.analyzeSentiment(doc)?.documentSentiment
            return sentiment?.score
        }
        return -1f
    }

    fun isPositiveText(text: String): Boolean{
        return (getSentimentScore(text)!! > 0f)
    }

}