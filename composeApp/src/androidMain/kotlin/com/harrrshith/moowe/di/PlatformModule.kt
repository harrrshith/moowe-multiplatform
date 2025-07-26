package com.harrrshith.moowe.di

import com.harrrshith.moowe.data.local.MooweDatabase
import com.harrrshith.moowe.data.local.getMovieDatabase
import com.harrrshith.moowe.getDatabaseBuilder
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

actual val platformModule = module {
    single<MooweDatabase> { 
        getMovieDatabase(getDatabaseBuilder(androidContext()))
    }
}