# Módulos Nuevos - Grúas Heredianas

Este documento describe los tres nuevos módulos implementados para el sistema de gestión de Grúas Heredianas.

## 1. Módulo de Inventario

### Descripción
Sistema completo de gestión de inventario para repuestos, herramientas y equipos de las grúas.

### Componentes

#### Modelo: `ArticuloInventario`
Ubicación: `src/gruasheredianas/inventario/model/ArticuloInventario.java`

**Atributos:**
- `id`: Identificador único
- `nombre`: Nombre del artículo
- `tipo`: Tipo de artículo (REPUESTO, HERRAMIENTA, EQUIPO)
- `descripcion`: Descripción detallada
- `cantidad`: Cantidad en inventario
- `stockMinimo`: Stock mínimo requerido
- `ubicacion`: Ubicación física del artículo
- `precioUnitario`: Precio por unidad
- `fechaRegistro`: Fecha de registro en el sistema
- `fechaUltimaActualizacion`: Fecha de última actualización

**Métodos principales:**
- `tieneStockBajo()`: Verifica si el artículo tiene stock bajo
- `getValorTotal()`: Calcula el valor total del inventario del artículo

#### DAO: `InventarioDAO`
Ubicación: `src/gruasheredianas/inventario/dao/InventarioDAO.java`

**Métodos principales:**
- `registrar(ArticuloInventario)`: Registra nuevo artículo
- `actualizarCantidad(int, int)`: Actualiza cantidad de un artículo
- `usarArticulo(int, int)`: Reduce cantidad al usar artículo
- `reponerArticulo(int, int)`: Incrementa cantidad al reponer
- `listar()`: Lista todos los artículos
- `obtenerArticulosStockBajo()`: Obtiene artículos con stock bajo
- `listarPorTipo(String)`: Lista artículos por tipo

#### Servicio: `InventarioService`
Ubicación: `src/gruasheredianas/inventario/service/InventarioService.java`

**Funcionalidades:**
- Actualización automática de existencias
- Alertas de stock bajo
- Generación de reportes en tiempo real
- Cálculo de valor total de inventario
- Notificaciones automáticas

### Uso de ejemplo
```java
// Crear servicio
InventarioService service = new InventarioService();

// Registrar artículo
ArticuloInventario articulo = new ArticuloInventario();
articulo.setNombre("Filtro de aceite");
articulo.setTipo("REPUESTO");
articulo.setCantidad(20);
articulo.setStockMinimo(5);
service.registrarArticulo(articulo);

// Usar artículo (se genera alerta automática si stock bajo)
service.usarArticulo(1, 5);

// Generar reporte
String reporte = service.generarReporteInventario();
System.out.println(reporte);
```

## 2. Módulo de Seguimiento GPS

### Descripción
Sistema de seguimiento en tiempo real de grúas mediante GPS con historial de rutas y notificaciones.

### Componentes

#### Modelo: `UbicacionGPS`
Ubicación: `src/gruasheredianas/gps/model/UbicacionGPS.java`

**Atributos:**
- `id`: Identificador único
- `camionId`: ID del camión/grúa
- `latitud`: Coordenada de latitud
- `longitud`: Coordenada de longitud
- `fechaHora`: Fecha y hora de la ubicación
- `velocidad`: Velocidad en km/h
- `estado`: Estado de la grúa (EN_SERVICIO, DISPONIBLE, MANTENIMIENTO, INACTIVO)
- `direccion`: Dirección aproximada

**Métodos principales:**
- `calcularDistancia(UbicacionGPS)`: Calcula distancia usando fórmula de Haversine

#### Modelo: `RutaHistorial`
Ubicación: `src/gruasheredianas/gps/model/RutaHistorial.java`

**Atributos:**
- Puntos de inicio y fin (latitud/longitud)
- Fechas de inicio y fin
- Distancia recorrida
- Tiempo de servicio
- Tipo de servicio

**Métodos principales:**
- `getVelocidadPromedio()`: Calcula velocidad promedio del servicio

#### DAO: `UbicacionGPSDAO`
Ubicación: `src/gruasheredianas/gps/dao/UbicacionGPSDAO.java`

**Métodos principales:**
- `registrarUbicacion(UbicacionGPS)`: Registra nueva ubicación
- `obtenerUltimaUbicacion(int)`: Obtiene ubicación actual de una grúa
- `obtenerUbicacionesActuales()`: Obtiene ubicaciones de todas las grúas
- `obtenerUbicacionesPorPeriodo(int, Date, Date)`: Historial de ubicaciones
- `registrarRuta(RutaHistorial)`: Registra una ruta completada
- `obtenerHistorialRutas(int)`: Obtiene historial de rutas

#### Servicio: `SeguimientoGPSService`
Ubicación: `src/gruasheredianas/gps/service/SeguimientoGPSService.java`

**Funcionalidades:**
- Monitoreo en tiempo real
- Notificaciones de llegada/salida
- Cálculo de distancias y tiempos
- Búsqueda de grúas cercanas
- Generación de reportes de actividad
- Panel de control con mapa interactivo

### Uso de ejemplo
```java
// Crear servicio
SeguimientoGPSService service = new SeguimientoGPSService();

// Actualizar ubicación
UbicacionGPS ubicacion = new UbicacionGPS();
ubicacion.setCamionId(1);
ubicacion.setLatitud(9.9326);
ubicacion.setLongitud(-84.0769);
ubicacion.setEstado("EN_SERVICIO");
service.actualizarUbicacion(ubicacion);

// Verificar llegada a punto de servicio
boolean llego = service.verificarLlegadaPuntoServicio(1, 9.9326, -84.0769);

// Obtener grúas cercanas
List<Integer> gruasCercanas = service.obtenerGruasCercanas(9.9326, -84.0769, 5.0);

// Generar reporte
String reporte = service.generarReporteActividad(1, fechaInicio, fechaFin);
```

## 3. Módulo de Pólizas y Seguros

### Descripción
Sistema de gestión de pólizas de seguro con control de vencimientos y alertas automáticas.

### Componentes

#### Modelo: `PolizaSeguro`
Ubicación: `src/gruasheredianas/polizas/model/PolizaSeguro.java`

**Atributos:**
- `id`: Identificador único
- `camionId`: ID del camión asegurado
- `numeroPoliza`: Número de póliza
- `aseguradora`: Nombre de la aseguradora
- `fechaInicio`: Fecha de inicio de cobertura
- `fechaVencimiento`: Fecha de vencimiento
- `tipoCobertura`: Tipo de cobertura (TOTAL, PARCIAL, RESPONSABILIDAD_CIVIL)
- `montoCobertura`: Monto de cobertura
- `primaMensual`: Prima mensual a pagar
- `estado`: Estado de la póliza (ACTIVA, VENCIDA, CANCELADA)

**Métodos principales:**
- `getDiasHastaVencimiento()`: Calcula días restantes
- `estaPorVencer()`: Verifica si vence en 30 días
- `estaVencida()`: Verifica si está vencida
- `getCostoAnual()`: Calcula costo anual

#### DAO: `PolizaSeguroDAO`
Ubicación: `src/gruasheredianas/polizas/dao/PolizaSeguroDAO.java`

**Métodos principales:**
- `registrar(PolizaSeguro)`: Registra nueva póliza
- `actualizarEstado(int, String)`: Actualiza estado
- `renovarPoliza(int, Date)`: Renueva póliza
- `obtenerPolizaActiva(int)`: Obtiene póliza activa de una grúa
- `obtenerPolizasPorVencer(int)`: Obtiene pólizas por vencer
- `obtenerPolizasVencidas()`: Obtiene pólizas vencidas
- `obtenerPorAseguradora(String)`: Lista por aseguradora

#### Servicio: `PolizaService`
Ubicación: `src/gruasheredianas/polizas/service/PolizaService.java`

**Funcionalidades:**
- Control automático de fechas de vencimiento
- Alertas de vencimiento próximo (30 días)
- Gestión de renovaciones
- Reportes de cumplimiento
- Reportes de cobertura por aseguradora
- Verificación de cobertura vigente

### Uso de ejemplo
```java
// Crear servicio
PolizaService service = new PolizaService();

// Registrar póliza
PolizaSeguro poliza = new PolizaSeguro();
poliza.setCamionId(1);
poliza.setNumeroPoliza("POL-2024-001");
poliza.setAseguradora("INS Costa Rica");
poliza.setFechaVencimiento(fechaVencimiento);
poliza.setMontoCobertura(50000.00);
service.registrarPoliza(poliza);

// Verificar cobertura
boolean tieneCovertura = service.tieneCobertura(1);

// Obtener alertas
List<String> alertas = service.generarAlertasVencimiento();

// Generar reportes
String reporteCumplimiento = service.generarReporteCumplimiento();
String reporteCobertura = service.generarReporteCobertura();
```

## Pruebas Unitarias

Cada módulo incluye un conjunto completo de pruebas unitarias:

- `test/gruasheredianas/inventario/InventarioTest.java`
- `test/gruasheredianas/gps/SeguimientoGPSTest.java`
- `test/gruasheredianas/polizas/PolizasTest.java`

### Ejecutar todas las pruebas
```bash
# Compilar
javac -d build/classes -sourcepath src src/gruasheredianas/**/*.java
javac -d build/test -cp build/classes -sourcepath test test/gruasheredianas/**/*.java

# Ejecutar
java -cp build/test:build/classes gruasheredianas.TestRunner
```

## Requisitos de Base de Datos

### Tabla: inventario
```sql
CREATE TABLE inventario (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    tipo VARCHAR(20) NOT NULL,
    descripcion TEXT,
    cantidad INT NOT NULL,
    stock_minimo INT NOT NULL,
    ubicacion VARCHAR(100),
    precio_unitario DECIMAL(10,2),
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_ultima_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

### Tabla: ubicacion_gps
```sql
CREATE TABLE ubicacion_gps (
    id INT AUTO_INCREMENT PRIMARY KEY,
    camion_id INT NOT NULL,
    latitud DOUBLE NOT NULL,
    longitud DOUBLE NOT NULL,
    fecha_hora TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    velocidad DOUBLE,
    estado VARCHAR(20),
    direccion VARCHAR(200),
    FOREIGN KEY (camion_id) REFERENCES camiones(id)
);
```

### Tabla: ruta_historial
```sql
CREATE TABLE ruta_historial (
    id INT AUTO_INCREMENT PRIMARY KEY,
    camion_id INT NOT NULL,
    fecha_inicio TIMESTAMP,
    fecha_fin TIMESTAMP,
    latitud_inicio DOUBLE,
    longitud_inicio DOUBLE,
    latitud_fin DOUBLE,
    longitud_fin DOUBLE,
    distancia_recorrida DOUBLE,
    tiempo_servicio BIGINT,
    tipo_servicio VARCHAR(50),
    FOREIGN KEY (camion_id) REFERENCES camiones(id)
);
```

### Tabla: poliza_seguro
```sql
CREATE TABLE poliza_seguro (
    id INT AUTO_INCREMENT PRIMARY KEY,
    camion_id INT NOT NULL,
    numero_poliza VARCHAR(50) UNIQUE NOT NULL,
    aseguradora VARCHAR(100) NOT NULL,
    fecha_inicio DATE NOT NULL,
    fecha_vencimiento DATE NOT NULL,
    tipo_cobertura VARCHAR(50),
    monto_cobertura DECIMAL(12,2),
    prima_mensual DECIMAL(10,2),
    estado VARCHAR(20) DEFAULT 'ACTIVA',
    observaciones TEXT,
    FOREIGN KEY (camion_id) REFERENCES camiones(id)
);
```

## Tecnologías Utilizadas

- **Java 22**: Lenguaje de programación principal
- **JDBC**: Conexión a base de datos MySQL
- **MySQL**: Sistema de gestión de base de datos
- **Patrón DAO**: Separación de lógica de negocio y acceso a datos
- **Arquitectura de servicios**: Capa de servicios para lógica de negocio

## Autor

Franco - Grúas Heredianas

## Licencia

Proyecto privado - Grúas Heredianas
