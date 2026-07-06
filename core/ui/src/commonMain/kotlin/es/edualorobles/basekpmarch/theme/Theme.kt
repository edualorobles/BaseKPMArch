package es.edualorobles.basekpmarch.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

private val DarkColorScheme = darkColorScheme(
    primary = BrandPrimaryDark,
    onPrimary = BrandOnPrimaryDark,
    secondary = BrandSecondaryDark,
    onSecondary = BrandOnSecondaryDark,
    tertiary = BrandTertiaryDark,
    onTertiary = BrandOnTertiaryDark
)

private val LightColorScheme = lightColorScheme(
    primary = BrandPrimary,
    onPrimary = BrandOnPrimary,
    secondary = BrandSecondary,
    onSecondary = BrandOnSecondary,
    tertiary = BrandTertiary,
    onTertiary = BrandOnTertiary
)

/**
 * Design-system entry point. Every screen must be wrapped in [AppTheme] — never a bare
 * `MaterialTheme { ... }` — so colors, typography, shapes, and spacing tokens stay consistent
 * across the whole app. Access spacing via `MaterialTheme.spacing` (see [spacing]).
 */
@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    CompositionLocalProvider(LocalSpacing provides Spacing()) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            shapes = AppShapes,
            content = content
        )
    }
}

/** Convenience accessor for the spacing token scale, e.g. `MaterialTheme.spacing.medium`. */
val MaterialTheme.spacing: Spacing
    @Composable
    get() = LocalSpacing.current
