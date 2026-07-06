package es.edualorobles.basekpmarch

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import es.edualorobles.basekpmarch.navigation.AppNavigation
import es.edualorobles.basekpmarch.theme.AppTheme

@Composable
@Preview
fun App() {
    AppTheme {
        AppNavigation()
    }
}