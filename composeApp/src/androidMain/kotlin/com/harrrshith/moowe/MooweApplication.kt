package com.harrrshith.moowe

import android.app.Application
import com.harrrshith.moowe.di.initKoin
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.logger.Level

class MooweApplication : Application(){
    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidLogger(Level.ERROR)
            androidContext(this@MooweApplication)
        }
    }
}