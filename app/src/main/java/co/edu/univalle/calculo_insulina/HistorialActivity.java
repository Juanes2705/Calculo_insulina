package co.edu.univalle.calculo_insulina;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HistorialActivity extends AppCompatActivity implements HistorialAdapter.OnItemClickListener {

    private DatabaseHelper databaseHelper;
    private CalculadoraInsulina calculadoraInsulina;
    private RecyclerView recyclerViewHistorial;
    private LinearLayout layoutEmpty;
    private HistorialAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial);

        // Inicializar base de datos
        databaseHelper = new DatabaseHelper(this);
        calculadoraInsulina = new CalculadoraInsulina(databaseHelper);

        // Inicializar views
        inicializarViews();

        // Configurar RecyclerView
        configurarRecyclerView();

        // Cargar datos
        cargarHistorial();
    }

    private void inicializarViews() {
        recyclerViewHistorial = findViewById(R.id.recyclerViewHistorial);
        layoutEmpty = findViewById(R.id.layoutEmpty);
        ImageButton buttonBack = findViewById(R.id.buttonBack);

        buttonBack.setOnClickListener(v -> finish());
    }

    private void configurarRecyclerView() {
        recyclerViewHistorial.setLayoutManager(new LinearLayoutManager(this));
        adapter = new HistorialAdapter(null, this);
        recyclerViewHistorial.setAdapter(adapter);
    }

    private void cargarHistorial() {
        List<RegistroInsulina> registros = calculadoraInsulina.obtenerRegistros();
        
        if (registros.isEmpty()) {
            mostrarVistaVacia();
        } else {
            mostrarListaRegistros(registros);
        }
    }

    private void mostrarVistaVacia() {
        recyclerViewHistorial.setVisibility(View.GONE);
        layoutEmpty.setVisibility(View.VISIBLE);
    }

    private void mostrarListaRegistros(List<RegistroInsulina> registros) {
        recyclerViewHistorial.setVisibility(View.VISIBLE);
        layoutEmpty.setVisibility(View.GONE);
        adapter.updateData(registros);
    }

    @Override
    public void onItemClick(RegistroInsulina registro) {
        // Abrir actividad de detalles
        Intent intent = new Intent(this, DetalleRegistroActivity.class);
        intent.putExtra("registro_id", registro.getId());
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Recargar datos cuando se regrese a esta actividad
        cargarHistorial();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (databaseHelper != null) {
            databaseHelper.close();
        }
    }
} 