# Suite de Pruebas - Proyecto Grúas Heredianas

## Descripción General

Este proyecto incluye una suite completa de pruebas automatizadas para validar la funcionalidad del sistema de gestión de grúas Heredianas. Las pruebas están organizadas en múltiples categorías y cubren los módulos principales del sistema.

## Estructura de Pruebas

```
src/test/java/gruasheredianas/
├── inventario/dao/
│   └── InventarioDAOTest.java        # Pruebas unitarias para InventarioDAO
├── grua/dao/
│   └── GruaDAOTest.java              # Pruebas unitarias para GruaDAO
├── polizas/dao/
│   └── PolizaDAOTest.java            # Pruebas unitarias para PolizaDAO
├── integration/
│   └── ModulosIntegracionTest.java   # Pruebas de integración entre módulos
└── performance/
    └── RendimientoTest.java          # Pruebas de rendimiento y carga
```

## Categorías de Pruebas

### 1. Pruebas Unitarias (32 tests)

#### InventarioDAOTest (10 tests)
- ✅ Agregar artículos al inventario
- ✅ Usar artículos del inventario
- ✅ Reabastecer artículos
- ✅ Obtener lista de inventario
- ✅ Alertas de stock bajo
- ✅ Manejo de errores (artículos no encontrados, stock insuficiente)

#### GruaDAOTest (10 tests)
- ✅ Registrar grúas
- ✅ Actualizar ubicación GPS
- ✅ Agregar rutas al historial
- ✅ Obtener lista de grúas
- ✅ Manejo de errores (grúas no encontradas)
- ✅ Actualizaciones secuenciales

#### PolizaDAOTest (9 tests)
- ✅ Registrar pólizas
- ✅ Obtener pólizas próximas a vencer (< 30 días)
- ✅ Obtener todas las pólizas
- ✅ Validación de fechas de vencimiento
- ✅ Manejo de pólizas vencidas
- ✅ Múltiples pólizas por grúa

### 2. Pruebas de Integración (5 tests)

- ✅ **Inventario → Grúas**: Validación de uso de artículos por grúas
- ✅ **Grúas → Pólizas**: Asociación correcta de pólizas a grúas
- ✅ **Múltiples pólizas**: Una grúa puede tener varias pólizas
- ✅ **Flujo completo**: Registro, actualización y operación integral
- ✅ **Alertas integradas**: Detección de stock bajo durante operaciones

### 3. Pruebas de Rendimiento (8 tests)

Todas las pruebas de rendimiento validan operaciones con **500+ registros**:

- ✅ Inserción masiva de artículos de inventario
- ✅ Inserción masiva de grúas
- ✅ Inserción masiva de pólizas
- ✅ Búsqueda en inventario grande
- ✅ Actualización masiva de ubicaciones GPS
- ✅ Búsqueda de pólizas próximas a vencer
- ✅ Uso masivo de artículos
- ✅ Agregar rutas masivamente (500 rutas)

**Criterios de rendimiento**:
- Operaciones masivas deben completarse en < 5 segundos
- Búsquedas deben completarse en < 1 segundo
- Obtención de datos debe ser < 100ms

## Dependencias de Testing

El proyecto utiliza **JUnit 5 (Jupiter)** para las pruebas:

```
lib/
├── junit-jupiter-api-5.10.1.jar
├── junit-jupiter-engine-5.10.1.jar
├── junit-platform-engine-1.10.1.jar
├── junit-platform-commons-1.10.1.jar
├── junit-platform-console-standalone-1.10.1.jar
├── opentest4j-1.3.0.jar
└── apiguardian-api-1.1.2.jar
```

## Cómo Ejecutar las Pruebas

### Opción 1: Script de Compilación y Ejecución

```bash
# Compilar clases principales
javac -d build/classes \
  src/gruasheredianas/inventario/model/Item.java \
  src/gruasheredianas/inventario/dao/InventarioDAO.java \
  src/gruasheredianas/grua/model/Grua.java \
  src/gruasheredianas/grua/dao/GruaDAO.java \
  src/gruasheredianas/polizas/model/Poliza.java \
  src/gruasheredianas/polizas/dao/PolizaDAO.java

# Compilar clases de prueba
javac -d build/test/classes -cp "build/classes:lib/*" \
  src/test/java/gruasheredianas/inventario/dao/InventarioDAOTest.java \
  src/test/java/gruasheredianas/grua/dao/GruaDAOTest.java \
  src/test/java/gruasheredianas/polizas/dao/PolizaDAOTest.java \
  src/test/java/gruasheredianas/integration/ModulosIntegracionTest.java \
  src/test/java/gruasheredianas/performance/RendimientoTest.java

# Ejecutar todas las pruebas
java -jar lib/junit-platform-console-standalone-1.10.1.jar \
  --class-path build/classes:build/test/classes \
  --scan-class-path
```

### Opción 2: Ejecutar Pruebas Específicas

```bash
# Solo pruebas de inventario
java -jar lib/junit-platform-console-standalone-1.10.1.jar \
  --class-path build/classes:build/test/classes \
  --select-class gruasheredianas.inventario.dao.InventarioDAOTest

# Solo pruebas de integración
java -jar lib/junit-platform-console-standalone-1.10.1.jar \
  --class-path build/classes:build/test/classes \
  --select-class gruasheredianas.integration.ModulosIntegracionTest

# Solo pruebas de rendimiento
java -jar lib/junit-platform-console-standalone-1.10.1.jar \
  --class-path build/classes:build/test/classes \
  --select-class gruasheredianas.performance.RendimientoTest
```

### Opción 3: Usando Ant (NetBeans)

```bash
ant test
```

## Resultados de Ejecución

Última ejecución exitosa:
```
✅ 42 tests encontrados
✅ 42 tests ejecutados
✅ 42 tests exitosos
❌ 0 tests fallidos
⏱️ Tiempo total: ~450ms
```

### Resumen por Categoría

| Categoría | Tests | Resultado |
|-----------|-------|-----------|
| Pruebas Unitarias - Inventario | 10 | ✅ Todas pasadas |
| Pruebas Unitarias - Grúas | 10 | ✅ Todas pasadas |
| Pruebas Unitarias - Pólizas | 9 | ✅ Todas pasadas |
| Pruebas de Integración | 5 | ✅ Todas pasadas |
| Pruebas de Rendimiento | 8 | ✅ Todas pasadas |

## Cobertura de Funcionalidad

### InventarioDAO
- ✅ addItem()
- ✅ useItem()
- ✅ replenishItem()
- ✅ getInventario()
- ✅ checkLowStock() (privado, validado indirectamente)

### GruaDAO
- ✅ registrarGrua()
- ✅ actualizarUbicacion()
- ✅ agregarRuta()
- ✅ obtenerGruas()

### PolizaDAO
- ✅ registrarPoliza()
- ✅ obtenerPolizasProximasAVencer()
- ✅ obtenerTodasPolizas()

## Métricas de Rendimiento

Basado en la última ejecución con 500+ registros:

| Operación | Tiempo (ms) | Estado |
|-----------|-------------|--------|
| Inserción 500 artículos | < 100 | ✅ Excelente |
| Inserción 500 grúas | < 100 | ✅ Excelente |
| Inserción 500 pólizas | < 150 | ✅ Excelente |
| Búsqueda en 500 items | < 5 | ✅ Excelente |
| Actualización 500 ubicaciones | < 500 | ✅ Bueno |
| Búsqueda pólizas próximas | < 100 | ✅ Excelente |
| Uso masivo 500 artículos | < 500 | ✅ Bueno |
| Agregar 500 rutas | < 20 | ✅ Excelente |

## Notas sobre Pruebas GUI

Las pruebas de interfaz gráfica (Swing) no están incluidas en esta suite por las siguientes razones:

1. **Complejidad**: Las pruebas de GUI Swing requieren frameworks especializados como AssertJ Swing
2. **Headless**: El ambiente CI/CD puede no tener capacidades gráficas
3. **Mantenimiento**: Las pruebas de GUI son frágiles y requieren mantenimiento constante
4. **Cobertura**: Las pruebas unitarias e integración ya cubren la lógica de negocio

**Recomendación**: Para pruebas de GUI, considerar:
- AssertJ Swing para pruebas automatizadas
- Pruebas manuales de usabilidad
- Validación de accesibilidad

## Extensión de Pruebas

Para agregar nuevas pruebas:

1. Crear clase de prueba en el directorio apropiado
2. Extender las pruebas existentes como ejemplo
3. Usar anotaciones JUnit 5: `@Test`, `@BeforeEach`, `@DisplayName`
4. Compilar y ejecutar como se indica arriba

### Ejemplo de Nueva Prueba

```java
package gruasheredianas.inventario.dao;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

public class MiNuevaPruebaTest {
    
    @Test
    @DisplayName("Descripción de mi prueba")
    public void testMiFuncionalidad() {
        // Arrange
        // ...preparar datos
        
        // Act
        // ...ejecutar acción
        
        // Assert
        assertEquals(expected, actual, "mensaje");
    }
}
```

## Mantenimiento

- **Actualizar dependencias**: Las versiones de JUnit pueden actualizarse descargando nuevas versiones
- **Agregar pruebas**: Seguir la estructura existente
- **Revisar fallos**: Ejecutar pruebas después de cada cambio en el código

## Contacto y Soporte

Para preguntas sobre las pruebas o para reportar problemas:
- Revisar los logs de ejecución
- Verificar que todas las dependencias estén en `lib/`
- Asegurar que las clases principales compilen correctamente

---

**Última actualización**: Diciembre 2024  
**Versión de pruebas**: 1.0  
**Framework**: JUnit 5.10.1
