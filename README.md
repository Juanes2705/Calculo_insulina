# Calculadora de Insulina - Android App

## Descripción
Aplicación móvil Android para el cálculo de dosis de insulina basado en carbohidratos y niveles de glicemia. La aplicación está diseñada para personas con diabetes que requieren calcular su dosis de insulina rápida antes de las comidas.

## Características Principales

### 🔐 Autenticación
- Login con Google
- Registro y login con email
- Gestión de sesión de usuario

### 🍽️ Gestión de Alimentos
- Base de datos con más de 50 alimentos comunes
- Categorización por grupos alimenticios
- Información nutricional detallada (porción, peso, carbohidratos)

### 💉 Cálculo de Insulina
- Ratios predefinidos por tipo de comida
- Factores de corrección según nivel de glicemia
- Algoritmo médico validado para el cálculo

### 📊 Historial y Registros
- Guardado automático de todos los cálculos
- Historial completo con fechas
- Vista detallada de cada registro

## Estructura del Proyecto

```
Calculo_insulina/
├── app/                          # Aplicación principal
│   ├── src/main/
│   │   ├── java/co/edu/univalle/calculo_insulina/
│   │   │   ├── Alimento.java
│   │   │   ├── CalculadoraInsulina.java
│   │   │   ├── DatabaseHelper.java
│   │   │   ├── HistorialActivity.java
│   │   │   ├── LoginActivity.java
│   │   │   ├── MainActivity.java
│   │   │   └── ... (otras clases)
│   │   ├── res/                  # Recursos de la aplicación
│   │   └── assets/
│   │       └── Calculo_insulina.db  # Base de datos SQLite
│   └── google-services.json      # Configuración Google Sign-In
├── Documentación/                # Documentación del proyecto
│   ├── Modelo_Relacional_BD.md
│   ├── Requerimientos_Desarrollo.md
│   └── Prototipos/              # Prototipos visuales
├── Datos_Acceso_Pruebas.txt     # Datos para pruebas
└── README.md                    # Este archivo
```

## Tecnologías Utilizadas

- **Lenguaje**: Java
- **Base de Datos**: SQLite
- **Autenticación**: Google Sign-In
- **UI**: Material Design Components
- **Arquitectura**: MVC (Model-View-Controller)

## Instalación y Configuración

### Prerrequisitos
- Android Studio (versión 4.0 o superior)
- Android SDK API 21 (Android 5.0) o superior
- Dispositivo Android o emulador

### Pasos de Instalación

1. **Clonar el repositorio**
   ```bash
   git clone [URL_DEL_REPOSITORIO]
   cd Calculo_insulina
   ```

2. **Abrir en Android Studio**
   - Abrir Android Studio
   - Seleccionar "Open an existing Android Studio project"
   - Navegar a la carpeta del proyecto y seleccionarla

3. **Sincronizar el proyecto**
   - Esperar a que Gradle sincronice las dependencias
   - Si hay errores, hacer "File > Invalidate Caches and Restart"

4. **Configurar Google Sign-In (Opcional)**
   - Crear proyecto en [Google Cloud Console](https://console.cloud.google.com/)
   - Habilitar Google Sign-In API
   - Descargar `google-services.json` y colocarlo en la carpeta `app/`
   - Agregar el SHA-1 del proyecto en la consola

5. **Ejecutar la aplicación**
   - Conectar dispositivo Android o iniciar emulador
   - Presionar "Run" en Android Studio

### Datos de Prueba

Si no se configura Google Sign-In, se puede usar el login por email:
- **Email**: test@demo.com
- **Contraseña**: 123456

## Funcionalidades

### 1. Autenticación
- **Login con Google**: Autenticación rápida y segura
- **Login por Email**: Registro tradicional con email y contraseña
- **Gestión de sesión**: Mantiene la sesión activa

### 2. Cálculo de Insulina
- **Selección de tipo de comida**: Desayuno, Almuerzo, Cena
- **Ingreso de glicemia**: Nivel actual de glucosa en sangre
- **Selección de alimentos**: Por categorías con información nutricional
- **Cálculo automático**: Basado en ratios médicos predefinidos

### 3. Algoritmo de Cálculo
```
Insulina para Alimentos = Carbohidratos Totales / Ratio del Tipo de Comida
Factor de Corrección = Según rango de glicemia
Insulina Total = Insulina Alimentos + Factor de Corrección
```

### 4. Historial
- **Registro automático**: Todos los cálculos se guardan
- **Vista de historial**: Lista ordenada por fecha
- **Detalle de registro**: Información completa de cada cálculo

## Base de Datos

La aplicación utiliza SQLite con las siguientes tablas:

- **CATEGORIAS**: Categorías de alimentos
- **ALIMENTOS**: Información nutricional de alimentos
- **RATIOS**: Ratios de insulina por tipo de comida
- **FACTORES_CORRECCION**: Factores según nivel de glicemia
- **USUARIOS**: Información de usuarios
- **REGISTROS**: Historial de cálculos

## Configuración de Ratios y Factores

### Ratios Predefinidos
- **Desayuno**: 15g carbohidratos por unidad de insulina
- **Almuerzo**: 12g carbohidratos por unidad de insulina
- **Cena**: 15g carbohidratos por unidad de insulina

### Factores de Corrección
- **Glicemia 70-100 mg/dL**: -1 unidad
- **Glicemia 140-200 mg/dL**: +2 unidades
- **Glicemia 200-250 mg/dL**: +3 unidades
- **Glicemia >250 mg/dL**: +4 unidades

## Documentación

La carpeta `Documentación/` contiene:

- **Modelo_Relacional_BD.md**: Esquema completo de la base de datos
- **Requerimientos_Desarrollo.md**: Especificación detallada de requerimientos
- **Prototipos/**: Prototipos visuales de la aplicación

## Solución de Problemas

### Error de compilación
- Verificar que Android Studio esté actualizado
- Limpiar proyecto: `Build > Clean Project`
- Reconstruir proyecto: `Build > Rebuild Project`

### Error de Google Sign-In
- Verificar configuración de `google-services.json`
- Verificar SHA-1 en Google Cloud Console
- Usar login por email como alternativa

### Base de datos no se crea
- Verificar permisos de escritura en el dispositivo
- Desinstalar y reinstalar la aplicación
- Verificar archivo `Calculo_insulina.db` en `assets/`

## Contribución

Este es un proyecto académico desarrollado para la Universidad del Valle. Para contribuciones o reportes de errores, contactar al equipo de desarrollo.

## Licencia

Este proyecto es propiedad de la Universidad del Valle y está destinado únicamente para fines educativos.

## Contacto

- **Universidad**: Universidad del Valle
- **Facultad**: Ingeniería
- **Programa**: Ingeniería de Sistemas
- **Asignatura**: Desarrollo de Aplicaciones Móviles

---

**Nota**: Esta aplicación es una herramienta de apoyo y no reemplaza la consulta médica profesional. Los cálculos están basados en estándares médicos generales y pueden requerir ajustes individuales por parte del médico tratante. 

