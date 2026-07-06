package es.edualorobles.basekpmarch.di

import es.edualorobles.basekpmarch.domain.usecase.GetDashboardDataUseCase
import es.edualorobles.basekpmarch.presentation.DashboardViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val dashboardModule = module {
    factoryOf(::GetDashboardDataUseCase)
    viewModelOf(::DashboardViewModel)
}