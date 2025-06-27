package co.edu.univalle.calculo_insulina;

public class RegistroInsulina {
    private long id;
    private String fecha;
    private String tipoComida;
    private int glicemia;
    private double carbohidratos;
    private int ratio;
    private int factorCorreccion;
    private double insulinaAlimentos;
    private double insulinaTotal;

    public RegistroInsulina() {}

    public RegistroInsulina(String fecha, String tipoComida, int glicemia, double carbohidratos, 
                           int ratio, int factorCorreccion, double insulinaAlimentos, double insulinaTotal) {
        this.fecha = fecha;
        this.tipoComida = tipoComida;
        this.glicemia = glicemia;
        this.carbohidratos = carbohidratos;
        this.ratio = ratio;
        this.factorCorreccion = factorCorreccion;
        this.insulinaAlimentos = insulinaAlimentos;
        this.insulinaTotal = insulinaTotal;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getTipoComida() {
        return tipoComida;
    }

    public void setTipoComida(String tipoComida) {
        this.tipoComida = tipoComida;
    }

    public int getGlicemia() {
        return glicemia;
    }

    public void setGlicemia(int glicemia) {
        this.glicemia = glicemia;
    }

    public double getCarbohidratos() {
        return carbohidratos;
    }

    public void setCarbohidratos(double carbohidratos) {
        this.carbohidratos = carbohidratos;
    }

    public int getRatio() {
        return ratio;
    }

    public void setRatio(int ratio) {
        this.ratio = ratio;
    }

    public int getFactorCorreccion() {
        return factorCorreccion;
    }

    public void setFactorCorreccion(int factorCorreccion) {
        this.factorCorreccion = factorCorreccion;
    }

    public double getInsulinaAlimentos() {
        return insulinaAlimentos;
    }

    public void setInsulinaAlimentos(double insulinaAlimentos) {
        this.insulinaAlimentos = insulinaAlimentos;
    }

    public double getInsulinaTotal() {
        return insulinaTotal;
    }

    public void setInsulinaTotal(double insulinaTotal) {
        this.insulinaTotal = insulinaTotal;
    }
} 