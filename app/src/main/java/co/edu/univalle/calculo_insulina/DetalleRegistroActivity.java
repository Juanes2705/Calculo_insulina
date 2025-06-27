package co.edu.univalle.calculo_insulina;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

public class DetalleRegistroActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;
    private CalculadoraInsulina calculadoraInsulina;
    private RegistroInsulina registro;

    // Views
    private TextView textViewFecha;
    private TextView textViewTipoComida;
    private TextView textViewGlicemia;
    private TextView textViewCarbohidratos;
    private TextView textViewRatio;
    private TextView textViewFactorCorreccion;
    private TextView textViewInsulinaAlimentos;
    private TextView textViewInsulinaTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_registro);

        // Inicializar base de datos
        databaseHelper = new DatabaseHelper(this);
        calculadoraInsulina = new CalculadoraInsulina(databaseHelper);

        // Inicializar views
        inicializarViews();

        // Obtener ID del registro
        long registroId = getIntent().getLongExtra("registro_id", -1);
        if (registroId != -1) {
            cargarRegistro(registroId);
        }
    }

    private void inicializarViews() {
        textViewFecha = findViewById(R.id.textViewFecha);
        textViewTipoComida = findViewById(R.id.textViewTipoComida);
        textViewGlicemia = findViewById(R.id.textViewGlicemia);
        textViewCarbohidratos = findViewById(R.id.textViewCarbohidratos);
        textViewRatio = findViewById(R.id.textViewRatio);
        textViewFactorCorreccion = findViewById(R.id.textViewFactorCorreccion);
        textViewInsulinaAlimentos = findViewById(R.id.textViewInsulinaAlimentos);
        textViewInsulinaTotal = findViewById(R.id.textViewInsulinaTotal);

        ImageButton buttonBack = findViewById(R.id.buttonBack);
        buttonBack.setOnClickListener(v -> finish());
    }

    private void cargarRegistro(long registroId) {
        // Obtener el registro de la base de datos
        registro = calculadoraInsulina.obtenerRegistroPorId(registroId);
        
        if (registro != null) {
            mostrarDetalles();
        }
    }

    private void mostrarDetalles() {
        if (registro != null) {
            textViewFecha.setText(registro.getFecha());
            textViewTipoComida.setText(registro.getTipoComida());
            textViewGlicemia.setText(registro.getGlicemia() + " mg/dL");
            textViewCarbohidratos.setText(String.format("%.1f g", registro.getCarbohidratos()));
            textViewRatio.setText(String.valueOf(registro.getRatio()));
            textViewFactorCorreccion.setText(String.valueOf(registro.getFactorCorreccion()));
            textViewInsulinaAlimentos.setText(String.format("%.2f", registro.getInsulinaAlimentos()));
            textViewInsulinaTotal.setText(String.format("%.2f", registro.getInsulinaTotal()));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (databaseHelper != null) {
            databaseHelper.close();
        }
    }
} 