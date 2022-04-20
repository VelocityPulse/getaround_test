package com.example.getaround.utils

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL

object Utils {

    suspend fun httpGet(url: String) = withContext(Dispatchers.IO) {
        val url = URL(url)

        with(url.openConnection() as HttpURLConnection) {
            requestMethod = "GET"  // optional default is GET

            println("\nSent 'GET' request to URL : $url; Response Code : $responseCode")

            inputStream.bufferedReader().use {
                return@with it.readText()
            }
        }
    }
}