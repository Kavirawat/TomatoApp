package com.tomatoapp.application.network

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

actual suspend fun makeHttpRequest(url: String, method: String, jsonBody: String?): String? = withContext(Dispatchers.IO) {
    try {
        val conn = (URL(url).openConnection() as HttpURLConnection).apply {
            requestMethod = method
            connectTimeout = 3000
            readTimeout = 3000
            setRequestProperty("Content-Type", "application/json; charset=UTF-8")
            setRequestProperty("Accept", "application/json")
            if (jsonBody != null && (method == "POST" || method == "PATCH" || method == "PUT")) {
                doOutput = true
                OutputStreamWriter(outputStream, "UTF-8").use {
                    it.write(jsonBody)
                    it.flush()
                }
            }
        }
        val responseCode = conn.responseCode
        val stream = if (responseCode in 200..299) conn.inputStream else conn.errorStream
        if (stream != null) {
            BufferedReader(InputStreamReader(stream, "UTF-8")).use { it.readText() }
        } else {
            null
        }
    } catch (e: Exception) {
        null
    }
}
