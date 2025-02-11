package com.harrrshith.moowe

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform