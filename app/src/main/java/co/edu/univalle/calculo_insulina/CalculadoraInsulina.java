package co.edu.univalle.calculo_insulina;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CalculadoraInsulina {
    private DatabaseHelper databaseHelper;

    public CalculadoraInsulina(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    /**
     * Calcula la insulina total basada en los parámetros proporcionados
     * @param tipoComida Tipo de comida (Desayuno, Almuerzo, Cena)
     * @param glicemia Valor de glicemia en mg/dL
     * @param alimentosSeleccionados Lista de alimentos seleccionados con sus cantidades
     * @return RegistroInsulina con todos los cálculos
     */
    public RegistroInsulina calcularInsulinaTotal(String tipoComida, int glicemia, List<AlimentoSeleccionado> alimentosSeleccionados) {
        // Calcular carbohidratos totales
        double carbohidratosTotales = calcularCarbohidratosTotales(alimentosSeleccionados);
        
        // Obtener ratio según el tipo de comida
        int ratio = databaseHelper.obtenerRatio(tipoComida);
        
        // Obtener factor de corrección según la glicemia
        int factorCorreccion = databaseHelper.obtenerFactorCorreccion(glicemia);
        
        // Calcular insulina para alimentos (IA)
        double insulinaAlimentos = carbohidratosTotales / ratio;
        
        // Calcular insulina total
        double insulinaTotal = insulinaAlimentos + factorCorreccion;
        
        // Crear y retornar el registro
        RegistroInsulina registro = new RegistroInsulina();
        registro.setFecha(obtenerFechaActual());
        registro.setTipoComida(tipoComida);
        registro.setGlicemia(glicemia);
        registro.setCarbohidratos(carbohidratosTotales);
        registro.setRatio(ratio);
        registro.setFactorCorreccion(factorCorreccion);
        registro.setInsulinaAlimentos(insulinaAlimentos);
        registro.setInsulinaTotal(insulinaTotal);
        
        return registro;
    }

    /**
     * Calcula los carbohidratos totales de los alimentos seleccionados
     * @param alimentosSeleccionados Lista de alimentos con sus cantidades
     * @return Total de carbohidratos en gramos
     */
    private double calcularCarbohidratosTotales(List<AlimentoSeleccionado> alimentosSeleccionados) {
        double total = 0;
        for (AlimentoSeleccionado alimentoSeleccionado : alimentosSeleccionados) {
            Alimento alimento = alimentoSeleccionado.getAlimento();
            int cantidad = alimentoSeleccionado.getCantidad();
            
            // Calcular carbohidratos por la cantidad de porciones
            double carbohidratosPorPorcion = alimento.getCarbohidratos();
            total += carbohidratosPorPorcion * cantidad;
        }
        return total;
    }

    /**
     * Obtiene la fecha y hora actual en formato legible
     * @return String con la fecha y hora actual
     */
    private String obtenerFechaActual() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        return sdf.format(new Date());
    }

    /**
     * Guarda el registro en la base de datos
     * @param registro Registro de insulina a guardar
     * @return ID del registro guardado
     */
    public long guardarRegistro(RegistroInsulina registro) {
        return databaseHelper.guardarRegistro(registro);
    }

    /**
     * Obtiene todos los registros de la base de datos
     * @return Lista de registros ordenados por fecha descendente
     */
    public List<RegistroInsulina> obtenerRegistros() {
        return databaseHelper.obtenerRegistros();
    }

    /**
     * Obtiene las categorías de alimentos
     * @return Lista de categorías
     */
    public List<Categoria> obtenerCategorias() {
        return databaseHelper.obtenerCategorias();
    }

    /**
     * Obtiene los alimentos por categoría
     * @param categoriaId ID de la categoría
     * @return Lista de alimentos de esa categoría
     */
    public List<Alimento> obtenerAlimentosPorCategoria(int categoriaId) {
        return databaseHelper.obtenerAlimentosPorCategoria(categoriaId);
    }

    /**
     * Obtiene el ratio para un tipo de comida específico
     * @param tipoComida Tipo de comida
     * @return Valor del ratio
     */
    public int obtenerRatio(String tipoComida) {
        return databaseHelper.obtenerRatio(tipoComida);
    }

    /**
     * Obtiene el factor de corrección para un valor de glicemia
     * @param glicemia Valor de glicemia en mg/dL
     * @return Factor de corrección
     */
    public int obtenerFactorCorreccion(int glicemia) {
        return databaseHelper.obtenerFactorCorreccion(glicemia);
    }

    /**
     * Obtiene un registro específico por su ID
     * @param registroId ID del registro a obtener
     * @return RegistroInsulina o null si no se encuentra
     */
    public RegistroInsulina obtenerRegistroPorId(long registroId) {
        return databaseHelper.obtenerRegistroPorId(registroId);
    }
} 