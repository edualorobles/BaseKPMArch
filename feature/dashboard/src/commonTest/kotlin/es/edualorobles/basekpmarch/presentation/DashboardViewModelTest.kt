package es.edualorobles.basekpmarch.presentation

import es.edualorobles.basekpmarch.domain.model.DashboardData
import es.edualorobles.basekpmarch.domain.repository.DashboardRepository
import es.edualorobles.basekpmarch.domain.usecase.GetDashboardDataUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

private class FakeDashboardRepository(
    private val result: Result<DashboardData>
) : DashboardRepository {
    override suspend fun getDashboardData(): DashboardData = result.getOrThrow()
}

@OptIn(ExperimentalCoroutinesApi::class)
class DashboardViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun emitsContentWhenUseCaseSucceeds() = runTest {
        val data = DashboardData(title = "Dashboard", message = "Welcome")
        val useCase = GetDashboardDataUseCase(FakeDashboardRepository(Result.success(data)))
        val viewModel = DashboardViewModel(useCase)

        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertIs<DashboardUiState.Content>(state)
        assertEquals(data, state.data)
    }

    @Test
    fun emitsErrorWhenUseCaseFails() = runTest {
        val useCase = GetDashboardDataUseCase(
            FakeDashboardRepository(Result.failure(IllegalStateException("boom")))
        )
        val viewModel = DashboardViewModel(useCase)

        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertIs<DashboardUiState.Error>(state)
        assertEquals("boom", state.message)
    }
}
