package com.smoothie.notabug

import android.util.Log
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException
import java.net.CookieManager
import java.net.CookiePolicy
import java.net.SocketTimeoutException

class Utilities {
    companion object {

        const val SHARED_PREFERENCES_NAME = "com.smoothie.notabug.preferences"

        private val MEDIA_TYPE_FORM_URLENCODED = "application/x-www-form-urlencoded".toMediaType()

        private lateinit var client: OkHttpClient
        private var isInitialized = false

        fun getClient() = client

        fun initialize() {
            if (isInitialized) {
                Log.w("Utilities", "Utilities already initialized!")
                return
            }
            val manager = CookieManager()
            manager.setCookiePolicy(CookiePolicy.ACCEPT_ALL)
            client = OkHttpClient.Builder()
                .cookieJar(JavaNetCookieJar(manager))
                .build()
            isInitialized = true
        }

        fun get(url: String, retryOnTimeout: Boolean): String {
            while(true) {
                try {
                    val request = Request.Builder().url(url).build()
                    client.newCall(request).execute().use { response ->
                        if (response.isSuccessful) return response.body!!.string()
                        else throw IOException("Request not successful.\n$request\n$response")
                    }
                } catch (exception: SocketTimeoutException) {
                    if (!retryOnTimeout) throw exception
                    else Log.d("Utilities.get(...)","Encountered SocketTimeoutException.\n${exception.stackTrace}")
                }
            }
        }

        fun get(url: String) = get(url, true)

        fun post(url: String, body:String, retryOnTimeout: Boolean): String {
            while(true) {
                try {
                    val request = Request.Builder()
                        .url(url)
                        .post(body.toRequestBody(MEDIA_TYPE_FORM_URLENCODED))
                        .build()
                    client.newCall(request).execute().use { response ->
                        if (response.isSuccessful) return response.body!!.string()
                        else throw IOException("Request not successful.\n$request\n$response")
                    }
                }
                catch (exception: SocketTimeoutException) {
                    if (!retryOnTimeout) throw exception
                    else Log.d("Utilities.post(...)","Encountered SocketTimeoutException.\n${exception.stackTrace}")
                }
            }
        }

        fun post(url: String, body: String) = post(url, body, true)

    }

    class SharedPrefsNames {
        companion object {
            const val USERNAME = "username"
            const val PASSWORD = "password"
            const val ANONYMOUS_BROWSING_ENABLED = "anonymous"
        }
    }
}