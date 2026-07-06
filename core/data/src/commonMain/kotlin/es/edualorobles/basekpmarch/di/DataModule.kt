package es.edualorobles.basekpmarch.di

import es.edualorobles.basekpmarch.data.repository.DashboardRepositoryImpl
import es.edualorobles.basekpmarch.domain.repository.DashboardRepository
import es.edualorobles.basekpmarch.networking.createHttpClient
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val dataModule = module {
    single { createHttpClient() }
    singleOf(::DashboardRepositoryImpl) { bind<DashboardRepository>() }
}