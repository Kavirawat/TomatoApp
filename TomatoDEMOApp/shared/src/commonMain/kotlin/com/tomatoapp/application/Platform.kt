package com.tomatoapp.application

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform