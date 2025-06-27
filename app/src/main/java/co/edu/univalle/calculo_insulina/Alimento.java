package co.edu.univalle.calculo_insulina;

public class Alimento {
    private int id;
    private String nombre;
    private int categoriaId;
    private String porcion;
    private double pesoGramos;
    private double carbohidratos;

    public Alimento() {}

    public Alimento(int id, String nombre, int categoriaId, String porcion, double pesoGramos, double carbohidratos) {
        this.id = id;
        this.nombre = nombre;
        this.categoriaId = categoriaId;
        this.porcion = porcion;
        this.pesoGramos = pesoGramos;
        this.carbohidratos = carbohidratos;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getCategoriaId() {
        return categoriaId;
    }

    public void setCategoriaId(int categoriaId) {
        this.categoriaId = categoriaId;
    }

    public String getPorcion() {
        return porcion;
    }

    public void setPorcion(String porcion) {
        this.porcion = porcion;
    }

    public double getPesoGramos() {
        return pesoGramos;
    }

    public void setPesoGramos(double pesoGramos) {
        this.pesoGramos = pesoGramos;
    }

    public double getCarbohidratos() {
        return carbohidratos;
    }

    public void setCarbohidratos(double carbohidratos) {
        this.carbohidratos = carbohidratos;
    }

    @Override
    public String toString() {
        return nombre + " (" + porcion + ")";
    }
} 