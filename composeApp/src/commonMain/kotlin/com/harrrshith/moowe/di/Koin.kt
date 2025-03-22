package com.harrrshith.moowe.di

import com.harrrshith.moowe.data.remote.MovieApi
import com.harrrshith.moowe.data.remote.TMDBHttpClient
import com.harrrshith.moowe.data.repository.MovieRepositoryImpl
import com.harrrshith.moowe.domain.repository.MovieRepository
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

val appModule = module {
    single { TMDBHttpClient.httpClient }

    single { MovieApi(get()) }

    singleOf(::MovieRepositoryImpl) { bind<MovieRepository>() }
}

fun initKoin(appDeclaration: KoinAppDeclaration = {}) {
    startKoin {
        appDeclaration()
        modules(appModule)
    }
}