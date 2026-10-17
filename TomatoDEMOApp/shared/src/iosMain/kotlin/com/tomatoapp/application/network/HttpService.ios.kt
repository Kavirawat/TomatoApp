package com.tomatoapp.application.network

actual suspend fun makeHttpRequest(url: String, method: String, jsonBody: String?): String? {
    return null
}
