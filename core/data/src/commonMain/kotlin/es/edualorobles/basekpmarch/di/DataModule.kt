package es.edualorobles.basekpmarch.di

import org.koin.dsl.module

val dataModule = module {
    // Ejemplo: singleOf(::RepositoryImpl).bind<Repository>()
    // Aquí instancias tu Ktor Client, Room, DataSources, etc.
}