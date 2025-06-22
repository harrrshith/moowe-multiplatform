package com.harrrshith.moowe.di

import com.harrrshith.moowe.data.remote.MooweApiHandler
import com.harrrshith.moowe.data.remote.MooweHttpClient
import com.harrrshith.moowe.data.repository.MovieRepositoryImpl
import com.harrrshith.moowe.domain.repository.MovieRepository
import com.harrrshith.moowe.ui.discover.DiscoverViewModel
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

val appModule = module {
    single { MooweHttpClient.httpClient }
    single { MooweApiHandler(get()) }
    singleOf(::MovieRepositoryImpl) { bind<MovieRepository>() }
}

val viewModelModule = module {
    factory { DiscoverViewModel(get()) }
}

fun initKoin(appDeclaration: KoinAppDeclaration = {}) {
    startKoin {
        appDeclaration()
        modules(
            appModule, viewModelModule
        )
    }
}