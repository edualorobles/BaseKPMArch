#!/bin/bash

# setup_project.sh
# Script para renombrar la plantilla BaseKPMArch a un nuevo proyecto.

# Salir si hay algún error
set -e

# --- Configuración Inicial ---
DEFAULT_OLD_PACKAGE="es.edualorobles.basekpmarch"
DEFAULT_OLD_NAME="BaseKPMArch"

echo "🚀 Iniciando configuración del nuevo proyecto..."
echo ""

# 1. Solicitar datos al usuario
read -p "Introduce el NUEVO NOMBRE del proyecto (ej. GymTracker): " NEW_NAME
read -p "Introduce el NUEVO PAQUETE del proyecto (ej. com.miempresa.gymtracker): " NEW_PACKAGE

if [ -z "$NEW_NAME" ] || [ -z "$NEW_PACKAGE" ]; then
    echo "❌ Error: Debes introducir un nombre y un paquete."
    exit 1
fi

echo ""
echo "🔄 Renombrando de '$DEFAULT_OLD_NAME' a '$NEW_NAME'..."
echo "📦 Cambiando paquete de '$DEFAULT_OLD_PACKAGE' a '$NEW_PACKAGE'..."
echo ""

# Convertir paquetes a rutas de directorios (ej. com.example -> com/example)
OLD_PATH_DIR="${DEFAULT_OLD_PACKAGE//.//}"
NEW_PATH_DIR="${NEW_PACKAGE//.//}"

# 2. Reemplazo de TEXTO en todos los archivos
# (Excluye .git, .gradle, build, y el propio script)
echo "📝 Actualizando contenidos de los archivos..."
grep -rRl "$DEFAULT_OLD_PACKAGE" . --exclude-dir={.git,.gradle,build} --exclude=setup_project.sh | xargs sed -i '' "s/$DEFAULT_OLD_PACKAGE/$NEW_PACKAGE/g"
grep -rRl "$DEFAULT_OLD_NAME" . --exclude-dir={.git,.gradle,build} --exclude=setup_project.sh | xargs sed -i '' "s/$DEFAULT_OLD_NAME/$NEW_NAME/g"

# Nota: Si tienes el paquete 'es.pitchpulse' mezclado, descomenta la siguiente línea:
# grep -rRl "es.pitchpulse" . --exclude-dir={.git,.gradle,build} | xargs sed -i '' "s/es.pitchpulse/$NEW_PACKAGE/g"

# 3. Mover Directorios (Refactor de Paquetes)
echo "📂 Moviendo directorios de código..."

# Buscar todas las carpetas que terminen en la ruta antigua
find . -type d -path "*/src/*/kotlin/$OLD_PATH_DIR" | while read old_dir; do
    # Construir la nueva ruta
    # Ejemplo: ./androidApp/src/main/kotlin/es/edualorobles/basekpmarch -> ./androidApp/src/main/kotlin/com/nuevo/paquete
    base_src_path="${old_dir%/$OLD_PATH_DIR}" # Elimina la parte del paquete antiguo del path
    new_dir="$base_src_path/$NEW_PATH_DIR"

    echo "   -> Moviendo $old_dir a $new_dir"

    mkdir -p "$new_dir"
    mv "$old_dir"/* "$new_dir"/

    # Limpiar directorios vacíos antiguos (hacia arriba)
    # Intenta borrar el directorio antiguo y sus padres si quedan vacíos
    rmdir -p "$old_dir" 2>/dev/null || true
done

# 4. Configuración específica de iOS (Xcode)
echo "🍎 Ajustando configuración de iOS..."
# En iOS el BundleID suele estar en el project.pbxproj, ya fue reemplazado en el paso 2 por el grep
# Pero a veces los nombres de carpetas no coinciden.
# Si tuvieras carpetas con el nombre del proyecto, aquí las renombraríamos.

# 5. Limpieza final
echo "🧹 Limpiando archivos temporales y de compilación..."
./gradlew clean > /dev/null 2>&1 || true
rm -rf .idea
rm -rf .gradle
rm -rf build

echo ""
echo "✅ ¡Proyecto '$NEW_NAME' configurado con éxito!"
echo "👉 Ahora puedes borrar este script: rm setup_project.sh"
echo "👉 Abre Android Studio y sincroniza el proyecto."