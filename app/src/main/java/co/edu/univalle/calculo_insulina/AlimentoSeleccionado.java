package co.edu.univalle.calculo_insulina;

public class AlimentoSeleccionado {
    private Alimento alimento;
    private int cantidad;

    public AlimentoSeleccionado(Alimento alimento, int cantidad) {
        this.alimento = alimento;
        this.cantidad = cantidad;
    }

    public Alimento getAlimento() {
        return alimento;
    }

    public void setAlimento(Alimento alimento) {
        this.alimento = alimento;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getCarbohidratosTotales() {
        return alimento.getCarbohidratos() * cantidad;
    }

    @Override
    public String toString() {
        return alimento.getNombre() + " x" + cantidad + " (" + alimento.getPorcion() + ")";
    }
} 