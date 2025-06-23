package com.harrrshith.moowe.utils

import com.harrrshith.moowe.di.appModule
import org.koin.core.context.startKoin

fun initKoin() {
    startKoin {
        modules(appModule)
    }
}