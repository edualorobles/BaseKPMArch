package es.edualorobles.basekpmarch.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import es.edualorobles.basekpmarch.feature.dashboard.resources.Res
import es.edualorobles.basekpmarch.feature.dashboard.resources.dashboard_title
import es.edualorobles.basekpmarch.presentation.DashboardUiState
import es.edualorobles.basekpmarch.presentation.DashboardViewModel
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(stringResource(Res.string.dashboard_title)) })
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            when (val state = uiState) {
                is DashboardUiState.Loading -> CircularProgressIndicator()
                is DashboardUiState.Content -> Text(
                    text = state.data.message,
                    style = MaterialTheme.typography.bodyLarge
                )
                is DashboardUiState.Error -> Text(
                    text = state.message,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}
