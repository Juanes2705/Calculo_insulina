# Calculadora de Insulina

## Descripción

Esta aplicación Android está diseñada para ayudar a pacientes diabéticos a calcular la dosis de insulina necesaria basándose en los alimentos que van a consumir y su nivel de glicemia actual.

## Características

### Funcionalidades Principales

1. **Cálculo de Insulina Total**: La aplicación calcula automáticamente la dosis de insulina basándose en:
   - Tipo de comida (Desayuno, Almuerzo, Cena)
   - Nivel de glicemia actual
   - Alimentos seleccionados y sus carbohidratos

2. **Base de Datos Nutricional**: Incluye una base de datos con:
   - 7 categorías de alimentos (Cereales, Tubérculos y Plátanos, Leguminosas, Lácteos, Verduras, Frutas, Procesados)
   - Más de 20 alimentos con información nutricional detallada
   - Porciones estándar y contenido de carbohidratos

3. **Ratios por Tipo de Comida**:
   - Desayuno: Ratio = 15
   - Almuerzo: Ratio = 12
   - Cena: Ratio = 15

4. **Factores de Corrección por Glicemia**:
   - GL = 70-100: FC = -1
   - GL = 140-200: FC = +2
   - GL = 200-250: FC = +3
   - GL > 250: FC = +4

5. **Historial de Registros**: Guarda y muestra todos los cálculos realizados

## Fórmulas de Cálculo

### Insulina para Alimentos (IA)
```
IA = Carbohidratos Totales / Ratio
```

### Insulina Total
```
Insulina Total = IA + Factor de Corrección
```

## Estructura del Proyecto

### Clases Principales

- **MainActivity**: Actividad principal con la interfaz de usuario
- **DatabaseHelper**: Manejo de la base de datos SQLite
- **CalculadoraInsulina**: Lógica de cálculo de insulina
- **Categoria**: Modelo para categorías de alimentos
- **Alimento**: Modelo para alimentos individuales
- **AlimentoSeleccionado**: Modelo para alimentos con cantidad
- **RegistroInsulina**: Modelo para registros de insulina

### Base de Datos

La aplicación utiliza SQLite con las siguientes tablas:

1. **categorias**: Categorías de alimentos
2. **alimentos**: Información nutricional de alimentos
3. **ratios**: Ratios por tipo de comida
4. **factores_correccion**: Factores de corrección por glicemia
5. **registros**: Historial de cálculos realizados

## Instalación y Uso

### Requisitos
- Android API 30 o superior
- Android Studio

### Instalación
1. Clona el repositorio
2. Abre el proyecto en Android Studio
3. Sincroniza las dependencias
4. Ejecuta la aplicación en un dispositivo o emulador

### Uso de la Aplicación

1. **Seleccionar Tipo de Comida**: Elige entre Desayuno, Almuerzo o Cena
2. **Ingresar Glicemia**: Introduce tu nivel de glicemia actual en mg/dL
3. **Seleccionar Alimentos**:
   - Elige una categoría de alimentos
   - Selecciona el alimento específico
   - Ajusta la cantidad usando los botones + y -
   - Presiona "Agregar Alimento"
4. **Calcular**: Presiona "Calcular Insulina" para ver los resultados
5. **Guardar**: Opcionalmente guarda el registro para el historial
6. **Ver Historial**: Accede a todos los registros anteriores

## Tecnologías Utilizadas

- **Android SDK**: Framework principal
- **SQLite**: Base de datos local
- **Java**: Lenguaje de programación
- **Material Design**: Componentes de UI
- **CardView**: Diseño de tarjetas
- **Spinner**: Selectores desplegables

## Contribución

Para contribuir al proyecto:

1. Fork el repositorio
2. Crea una rama para tu feature (`git checkout -b feature/nueva-funcionalidad`)
3. Commit tus cambios (`git commit -am 'Agregar nueva funcionalidad'`)
4. Push a la rama (`git push origin feature/nueva-funcionalidad`)
5. Crea un Pull Request

## Licencia

Este proyecto está bajo la Licencia MIT. Ver el archivo `LICENSE` para más detalles.

## Contacto

Para preguntas o soporte, contacta al equipo de desarrollo.

## Notas Médicas

**IMPORTANTE**: Esta aplicación es una herramienta de apoyo y no reemplaza la consulta médica. Siempre consulta con tu médico antes de ajustar tu dosis de insulina.

Los cálculos están basados en principios generales de terapia con insulina, pero cada paciente puede requerir ajustes individualizados según su condición médica específica. 