package es.edualorobles.basekpmarch

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import es.edualorobles.basekpmarch.navigation.AppNavigation
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    MaterialTheme {
        AppNavigation()
    }
}