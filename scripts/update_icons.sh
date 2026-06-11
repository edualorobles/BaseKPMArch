#!/bin/bash
# update_icons.sh
#
# Generates all required app icon and splash logo sizes for Android and iOS
# from two source PNG images (ideally 1024x1024 or larger).
#
# Usage:
#   ./scripts/update_icons.sh --icon path/to/icon.png --splash path/to/splash_logo.png
#
# Requirements:
#   - macOS (uses 'sips' and 'iconutil', both built-in)
#   - Source images should be square PNGs, minimum 1024x1024

set -e

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
ROOT_DIR="$(dirname "$SCRIPT_DIR")"

ICON_SRC=""
SPLASH_SRC=""

# --- Argument parsing ---
while [[ $# -gt 0 ]]; do
    case "$1" in
        --icon)   ICON_SRC="$2"; shift 2 ;;
        --splash) SPLASH_SRC="$2"; shift 2 ;;
        *) echo "Unknown argument: $1"; exit 1 ;;
    esac
done

if [[ -z "$ICON_SRC" && -z "$SPLASH_SRC" ]]; then
    echo "Usage: $0 --icon <icon.png> [--splash <splash.png>]"
    exit 1
fi

resize() {
    local src="$1" dst="$2" size="$3"
    sips -z "$size" "$size" "$src" --out "$dst" > /dev/null
    echo "  ✓ $(basename "$dst") (${size}x${size})"
}

# ─────────────────────────────────────────────
# ANDROID ICONS
# ─────────────────────────────────────────────
if [[ -n "$ICON_SRC" ]]; then
    echo ""
    echo "→ Android launcher icons"
    ANDROID_RES="$ROOT_DIR/androidApp/src/main/res"

    resize "$ICON_SRC" "$ANDROID_RES/mipmap-mdpi/ic_launcher.png"       48
    resize "$ICON_SRC" "$ANDROID_RES/mipmap-mdpi/ic_launcher_round.png" 48
    resize "$ICON_SRC" "$ANDROID_RES/mipmap-hdpi/ic_launcher.png"       72
    resize "$ICON_SRC" "$ANDROID_RES/mipmap-hdpi/ic_launcher_round.png" 72
    resize "$ICON_SRC" "$ANDROID_RES/mipmap-xhdpi/ic_launcher.png"      96
    resize "$ICON_SRC" "$ANDROID_RES/mipmap-xhdpi/ic_launcher_round.png" 96
    resize "$ICON_SRC" "$ANDROID_RES/mipmap-xxhdpi/ic_launcher.png"     144
    resize "$ICON_SRC" "$ANDROID_RES/mipmap-xxhdpi/ic_launcher_round.png" 144
    resize "$ICON_SRC" "$ANDROID_RES/mipmap-xxxhdpi/ic_launcher.png"    192
    resize "$ICON_SRC" "$ANDROID_RES/mipmap-xxxhdpi/ic_launcher_round.png" 192

    echo ""
    echo "  Note: the adaptive icon foreground/background are vectors."
    echo "  Edit these manually if needed:"
    echo "    androidApp/src/main/res/drawable/ic_launcher_background.xml"
    echo "    androidApp/src/main/res/drawable-v24/ic_launcher_foreground.xml"
fi

# ─────────────────────────────────────────────
# IOS APP ICON
# ─────────────────────────────────────────────
if [[ -n "$ICON_SRC" ]]; then
    echo ""
    echo "→ iOS AppIcon"
    IOS_ICON="$ROOT_DIR/iosApp/iosApp/Assets.xcassets/AppIcon.appiconset"

    resize "$ICON_SRC" "$IOS_ICON/40.png"   40
    resize "$ICON_SRC" "$IOS_ICON/60.png"   60
    resize "$ICON_SRC" "$IOS_ICON/29.png"   29
    resize "$ICON_SRC" "$IOS_ICON/58.png"   58
    resize "$ICON_SRC" "$IOS_ICON/87.png"   87
    resize "$ICON_SRC" "$IOS_ICON/80.png"   80
    resize "$ICON_SRC" "$IOS_ICON/120.png"  120
    resize "$ICON_SRC" "$IOS_ICON/57.png"   57
    resize "$ICON_SRC" "$IOS_ICON/114.png"  114
    resize "$ICON_SRC" "$IOS_ICON/180.png"  180
    resize "$ICON_SRC" "$IOS_ICON/1024.png" 1024
fi

# ─────────────────────────────────────────────
# IOS SPLASH LOGO
# ─────────────────────────────────────────────
if [[ -n "$SPLASH_SRC" ]]; then
    echo ""
    echo "→ iOS SplashLogo"
    IOS_SPLASH="$ROOT_DIR/iosApp/iosApp/Assets.xcassets/SplashLogo.imageset"

    resize "$SPLASH_SRC" "$IOS_SPLASH/SplashLogo.png"    100
    resize "$SPLASH_SRC" "$IOS_SPLASH/SplashLogo@2x.png" 200
    resize "$SPLASH_SRC" "$IOS_SPLASH/SplashLogo@3x.png" 300
fi

echo ""
echo "Done. Rebuild the app to see the changes."
