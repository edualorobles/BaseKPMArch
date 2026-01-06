package es.edualorobles.basekpmarch

import androidx.compose.ui.window.ComposeUIViewController
import es.edualorobles.basekpmarch.di.initKoin

fun MainViewController() = ComposeUIViewController(
    configure = {
        initKoin() // Arranca Koin al crear el VC
    }
) {
    App()
}