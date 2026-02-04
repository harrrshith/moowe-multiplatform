package com.harrrshith.moowe.di

import com.harrrshith.moowe.data.local.CacheConfig
import com.harrrshith.moowe.data.local.MooweDao
import com.harrrshith.moowe.data.local.MooweDatabase
import com.harrrshith.moowe.data.remote.MooweApiHandler
import com.harrrshith.moowe.data.remote.MooweHttpClient
import com.harrrshith.moowe.data.repository.MovieRepositoryImpl
import com.harrrshith.moowe.domain.repository.MovieRepository
import com.harrrshith.moowe.ui.detail.DetailScreenViewModel
import com.harrrshith.moowe.ui.discover.DiscoverViewModel
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

expect val platformModule: Module

val databaseModule = module {
    single<MooweDao> { get<MooweDatabase>().getMooweDao() }
    single { CacheConfig.DEFAULT }
}

val networkModule = module {
    single { MooweHttpClient.httpClient }
    single { MooweApiHandler(get()) }
}

val repositoryModule = module {
    single<MovieRepository> { MovieRepositoryImpl(get(), get(), get()) }
}

val viewModelModule = module {
    factory { DiscoverViewModel(get()) }
    factory { id -> DetailScreenViewModel(id.get(), get()) }
}

fun initKoin(appDeclaration: KoinAppDeclaration = {}) {
    startKoin {
        appDeclaration()
        modules(
            platformModule,
            databaseModule,
            networkModule,
            repositoryModule,
            viewModelModule
        )
    }
}