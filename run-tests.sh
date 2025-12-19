#!/bin/bash

# Script para compilar y ejecutar las pruebas del proyecto Grúas Heredianas
# Uso: ./run-tests.sh [opción]
# Opciones:
#   all         - Ejecutar todas las pruebas (por defecto)
#   inventario  - Solo pruebas de inventario
#   gruas       - Solo pruebas de grúas
#   polizas     - Solo pruebas de pólizas
#   integration - Solo pruebas de integración
#   performance - Solo pruebas de rendimiento
#   clean       - Limpiar archivos compilados

set -e  # Detener en caso de error

# Colores para output
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m' # No Color

# Función para imprimir mensajes
print_info() {
    echo -e "${YELLOW}[INFO]${NC} $1"
}

print_success() {
    echo -e "${GREEN}[SUCCESS]${NC} $1"
}

print_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# Verificar que estamos en el directorio correcto
if [ ! -f "build.xml" ]; then
    print_error "Este script debe ejecutarse desde el directorio raíz del proyecto"
    exit 1
fi

# Crear directorios de build si no existen
mkdir -p build/classes
mkdir -p build/test/classes

# Función para compilar clases principales
compile_main() {
    print_info "Compilando clases principales..."
    javac -d build/classes \
        src/gruasheredianas/inventario/model/Item.java \
        src/gruasheredianas/inventario/dao/InventarioDAO.java \
        src/gruasheredianas/grua/model/Grua.java \
        src/gruasheredianas/grua/dao/GruaDAO.java \
        src/gruasheredianas/polizas/model/Poliza.java \
        src/gruasheredianas/polizas/dao/PolizaDAO.java
    print_success "Clases principales compiladas"
}

# Función para compilar clases de prueba
compile_tests() {
    print_info "Compilando clases de prueba..."
    javac -d build/test/classes -cp "build/classes:lib/*" \
        src/test/java/gruasheredianas/inventario/dao/InventarioDAOTest.java \
        src/test/java/gruasheredianas/grua/dao/GruaDAOTest.java \
        src/test/java/gruasheredianas/polizas/dao/PolizaDAOTest.java \
        src/test/java/gruasheredianas/integration/ModulosIntegracionTest.java \
        src/test/java/gruasheredianas/performance/RendimientoTest.java 2>&1 | grep -v "warning: unknown enum constant" || true
    print_success "Clases de prueba compiladas"
}

# Función para ejecutar todas las pruebas
run_all_tests() {
    print_info "Ejecutando todas las pruebas..."
    java -jar lib/junit-platform-console-standalone-1.10.1.jar \
        --class-path build/classes:build/test/classes \
        --scan-class-path
}

# Función para ejecutar pruebas específicas
run_specific_test() {
    local test_class=$1
    local test_name=$2
    print_info "Ejecutando pruebas de $test_name..."
    java -jar lib/junit-platform-console-standalone-1.10.1.jar \
        --class-path build/classes:build/test/classes \
        --select-class $test_class
}

# Función para limpiar
clean() {
    print_info "Limpiando archivos compilados..."
    rm -rf build/classes/*
    rm -rf build/test/classes/*
    print_success "Limpieza completada"
}

# Procesar argumentos
OPTION=${1:-all}

case $OPTION in
    all)
        compile_main
        compile_tests
        run_all_tests
        ;;
    inventario)
        compile_main
        compile_tests
        run_specific_test "gruasheredianas.inventario.dao.InventarioDAOTest" "Inventario"
        ;;
    gruas)
        compile_main
        compile_tests
        run_specific_test "gruasheredianas.grua.dao.GruaDAOTest" "Grúas"
        ;;
    polizas)
        compile_main
        compile_tests
        run_specific_test "gruasheredianas.polizas.dao.PolizaDAOTest" "Pólizas"
        ;;
    integration)
        compile_main
        compile_tests
        run_specific_test "gruasheredianas.integration.ModulosIntegracionTest" "Integración"
        ;;
    performance)
        compile_main
        compile_tests
        run_specific_test "gruasheredianas.performance.RendimientoTest" "Rendimiento"
        ;;
    clean)
        clean
        ;;
    *)
        print_error "Opción no válida: $OPTION"
        echo "Uso: $0 [all|inventario|gruas|polizas|integration|performance|clean]"
        exit 1
        ;;
esac

print_success "Ejecución completada"
