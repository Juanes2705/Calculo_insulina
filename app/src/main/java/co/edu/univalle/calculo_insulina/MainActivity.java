package co.edu.univalle.calculo_insulina;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    
    private DatabaseHelper databaseHelper;
    private CalculadoraInsulina calculadoraInsulina;
    
    // Views
    private Spinner spinnerTipoComida;
    private EditText editTextGlicemia;
    private Spinner spinnerCategoria;
    private Spinner spinnerAlimento;
    private TextView textViewCantidad;
    private ImageButton buttonMenos;
    private ImageButton buttonMas;
    private Button buttonAgregarAlimento;
    private ListView listViewAlimentos;
    private TextView textViewCarbohidratosTotales;
    private Button buttonCalcular;
    private CardView cardViewResultados;
    private TextView textViewRatio;
    private TextView textViewFactorCorreccion;
    private TextView textViewInsulinaAlimentos;
    private TextView textViewInsulinaTotal;
    private Button buttonGuardar;
    private Button buttonHistorial;
    private TextView textViewUsuario;
    private Button buttonLogout;
    
    // Data
    private List<Categoria> categorias;
    private List<Alimento> alimentos;
    private List<AlimentoSeleccionado> alimentosSeleccionados;
    private ArrayAdapter<String> adapterAlimentos;
    private ArrayAdapter<String> adapterListaAlimentos;
    private int cantidadActual = 1;
    private RegistroInsulina registroActual;
    private Usuario usuarioLogueado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // Inicializar base de datos
        databaseHelper = new DatabaseHelper(this);
        calculadoraInsulina = new CalculadoraInsulina(databaseHelper);
        
        // Inicializar listas
        alimentosSeleccionados = new ArrayList<>();
        
        // Inicializar views
        inicializarViews();
        
        // Configurar spinners
        configurarSpinners();
        
        // Configurar botones
        configurarBotones();
        
        // Cargar datos iniciales
        cargarDatosIniciales();
        
        // Obtener usuario logueado
        obtenerUsuarioLogueado();
    }

    private void inicializarViews() {
        spinnerTipoComida = findViewById(R.id.spinnerTipoComida);
        editTextGlicemia = findViewById(R.id.editTextGlicemia);
        spinnerCategoria = findViewById(R.id.spinnerCategoria);
        spinnerAlimento = findViewById(R.id.spinnerAlimento);
        textViewCantidad = findViewById(R.id.textViewCantidad);
        buttonMenos = findViewById(R.id.buttonMenos);
        buttonMas = findViewById(R.id.buttonMas);
        buttonAgregarAlimento = findViewById(R.id.buttonAgregarAlimento);
        listViewAlimentos = findViewById(R.id.listViewAlimentos);
        textViewCarbohidratosTotales = findViewById(R.id.textViewCarbohidratosTotales);
        buttonCalcular = findViewById(R.id.buttonCalcular);
        cardViewResultados = findViewById(R.id.cardViewResultados);
        textViewRatio = findViewById(R.id.textViewRatio);
        textViewFactorCorreccion = findViewById(R.id.textViewFactorCorreccion);
        textViewInsulinaAlimentos = findViewById(R.id.textViewInsulinaAlimentos);
        textViewInsulinaTotal = findViewById(R.id.textViewInsulinaTotal);
        buttonGuardar = findViewById(R.id.buttonGuardar);
        buttonHistorial = findViewById(R.id.buttonHistorial);
        textViewUsuario = findViewById(R.id.textViewUsuario);
        buttonLogout = findViewById(R.id.buttonLogout);
        FloatingActionButton fabHistorial = findViewById(R.id.fabHistorial);
        
        // Configurar FloatingActionButton
        fabHistorial.setOnClickListener(v -> abrirHistorial());
    }

    private void configurarSpinners() {
        // Configurar spinner de tipo de comida
        String[] tiposComida = {"Desayuno", "Almuerzo", "Cena"};
        ArrayAdapter<String> adapterTipoComida = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, tiposComida);
        adapterTipoComida.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTipoComida.setAdapter(adapterTipoComida);

        // Configurar spinner de categorías
        categorias = calculadoraInsulina.obtenerCategorias();
        List<String> nombresCategorias = new ArrayList<>();
        for (Categoria categoria : categorias) {
            nombresCategorias.add(categoria.getNombre());
        }
        ArrayAdapter<String> adapterCategorias = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, nombresCategorias);
        adapterCategorias.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategoria.setAdapter(adapterCategorias);

        // Configurar listener para spinner de categorías
        spinnerCategoria.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cargarAlimentosPorCategoria(position + 1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // No hacer nada
            }
        });

        // Configurar adapter para lista de alimentos seleccionados
        adapterListaAlimentos = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new ArrayList<>());
        listViewAlimentos.setAdapter(adapterListaAlimentos);
    }

    private void configurarBotones() {
        // Botones de cantidad
        buttonMenos.setOnClickListener(v -> {
            if (cantidadActual > 1) {
                cantidadActual--;
                textViewCantidad.setText(String.valueOf(cantidadActual));
            }
        });

        buttonMas.setOnClickListener(v -> {
            cantidadActual++;
            textViewCantidad.setText(String.valueOf(cantidadActual));
        });

        // Botón agregar alimento
        buttonAgregarAlimento.setOnClickListener(v -> agregarAlimento());

        // Botón calcular
        buttonCalcular.setOnClickListener(v -> calcularInsulina());

        // Botón guardar
        buttonGuardar.setOnClickListener(v -> guardarRegistro());

        // Botón historial
        buttonHistorial.setOnClickListener(v -> abrirHistorial());

        // Botón logout
        buttonLogout.setOnClickListener(v -> logout());
    }

    private void cargarDatosIniciales() {
        // Cargar alimentos de la primera categoría
        if (!categorias.isEmpty()) {
            cargarAlimentosPorCategoria(1);
        }
    }

    private void cargarAlimentosPorCategoria(int categoriaId) {
        alimentos = calculadoraInsulina.obtenerAlimentosPorCategoria(categoriaId);
        List<String> nombresAlimentos = new ArrayList<>();
        for (Alimento alimento : alimentos) {
            nombresAlimentos.add(alimento.getNombre() + " (" + alimento.getPorcion() + ")");
        }
        adapterAlimentos = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, nombresAlimentos);
        adapterAlimentos.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAlimento.setAdapter(adapterAlimentos);
    }

    private void agregarAlimento() {
        if (alimentos.isEmpty()) {
            Toast.makeText(this, "No hay alimentos disponibles", Toast.LENGTH_SHORT).show();
            return;
        }

        int posicionAlimento = spinnerAlimento.getSelectedItemPosition();
        if (posicionAlimento >= 0 && posicionAlimento < alimentos.size()) {
            Alimento alimento = alimentos.get(posicionAlimento);
            AlimentoSeleccionado alimentoSeleccionado = new AlimentoSeleccionado(alimento, cantidadActual);
            alimentosSeleccionados.add(alimentoSeleccionado);
            
            actualizarListaAlimentos();
            actualizarCarbohidratosTotales();
            
            // Resetear cantidad
            cantidadActual = 1;
            textViewCantidad.setText("1");
            
            Toast.makeText(this, "Alimento agregado", Toast.LENGTH_SHORT).show();
        }
    }

    private void actualizarListaAlimentos() {
        List<String> listaAlimentos = new ArrayList<>();
        for (AlimentoSeleccionado alimentoSeleccionado : alimentosSeleccionados) {
            listaAlimentos.add(alimentoSeleccionado.toString());
        }
        adapterListaAlimentos.clear();
        adapterListaAlimentos.addAll(listaAlimentos);
        adapterListaAlimentos.notifyDataSetChanged();
    }

    private void actualizarCarbohidratosTotales() {
        double total = 0;
        for (AlimentoSeleccionado alimentoSeleccionado : alimentosSeleccionados) {
            total += alimentoSeleccionado.getCarbohidratosTotales();
        }
        textViewCarbohidratosTotales.setText(String.format("Carbohidratos totales: %.1f g", total));
    }

    private void calcularInsulina() {
        // Validar entrada de glicemia
        String glicemiaStr = editTextGlicemia.getText().toString().trim();
        if (glicemiaStr.isEmpty()) {
            Toast.makeText(this, "Por favor ingrese su glicemia", Toast.LENGTH_SHORT).show();
            return;
        }

        int glicemia = Integer.parseInt(glicemiaStr);
        if (glicemia <= 0) {
            Toast.makeText(this, "La glicemia debe ser mayor a 0", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validar que hay alimentos seleccionados
        if (alimentosSeleccionados.isEmpty()) {
            Toast.makeText(this, "Por favor seleccione al menos un alimento", Toast.LENGTH_SHORT).show();
            return;
        }

        // Obtener tipo de comida
        String tipoComida = spinnerTipoComida.getSelectedItem().toString();

        // Calcular insulina
        registroActual = calculadoraInsulina.calcularInsulinaTotal(tipoComida, glicemia, alimentosSeleccionados);

        // Mostrar resultados
        mostrarResultados();
    }

    private void mostrarResultados() {
        if (registroActual != null) {
            textViewRatio.setText(String.format("Ratio: %d", registroActual.getRatio()));
            textViewFactorCorreccion.setText(String.format("Factor de Corrección: %d", registroActual.getFactorCorreccion()));
            textViewInsulinaAlimentos.setText(String.format("Insulina para Alimentos: %.2f", registroActual.getInsulinaAlimentos()));
            textViewInsulinaTotal.setText(String.format("Insulina Total: %.2f", registroActual.getInsulinaTotal()));
            
            cardViewResultados.setVisibility(View.VISIBLE);
        }
    }

    private void guardarRegistro() {
        if (registroActual != null) {
            long id = calculadoraInsulina.guardarRegistro(registroActual);
            if (id > 0) {
                Toast.makeText(this, "Registro guardado exitosamente", Toast.LENGTH_SHORT).show();
                limpiarFormulario();
            } else {
                Toast.makeText(this, "Error al guardar el registro", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void limpiarFormulario() {
        // Limpiar campos
        editTextGlicemia.setText("");
        alimentosSeleccionados.clear();
        actualizarListaAlimentos();
        actualizarCarbohidratosTotales();
        cardViewResultados.setVisibility(View.GONE);
        registroActual = null;
        
        // Resetear cantidad
        cantidadActual = 1;
        textViewCantidad.setText("1");
    }

    private void abrirHistorial() {
        Intent intent = new Intent(this, HistorialActivity.class);
        startActivity(intent);
    }

    private void logout() {
        // Cerrar sesión de Google
        GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(
            this,
            new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()
        );
        googleSignInClient.signOut().addOnCompleteListener(this, task -> {
            Toast.makeText(this, "Sesión cerrada", Toast.LENGTH_SHORT).show();
            irALogin();
        });
    }

    private void obtenerUsuarioLogueado() {
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("usuario_id")) {
            long usuarioId = intent.getLongExtra("usuario_id", -1);
            String usuarioNombre = intent.getStringExtra("usuario_nombre");
            String usuarioEmail = intent.getStringExtra("usuario_email");
            
            if (usuarioId != -1) {
                usuarioLogueado = new Usuario();
                usuarioLogueado.setId(usuarioId);
                usuarioLogueado.setNombre(usuarioNombre);
                usuarioLogueado.setEmail(usuarioEmail);
                
                // Mostrar información del usuario
                textViewUsuario.setText("Usuario: " + usuarioNombre);
                
                // Mostrar mensaje de bienvenida
                Toast.makeText(this, "Bienvenido, " + usuarioNombre, Toast.LENGTH_SHORT).show();
            } else {
                // Si no hay usuario, volver al login
                irALogin();
            }
        } else {
            // Si no hay datos de usuario, volver al login
            irALogin();
        }
    }

    private void irALogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (databaseHelper != null) {
            databaseHelper.close();
        }
    }
}