package es.edualorobles.basekpmarch.di

import es.edualorobles.basekpmarch.networking.createHttpClient
import org.koin.dsl.module

val dataModule = module {
    single { createHttpClient() }
}