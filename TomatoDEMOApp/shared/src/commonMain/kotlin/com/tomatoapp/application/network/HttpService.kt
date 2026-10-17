package com.tomatoapp.application.network

expect suspend fun makeHttpRequest(url: String, method: String, jsonBody: String? = null): String?
