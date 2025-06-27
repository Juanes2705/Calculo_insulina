package co.edu.univalle.calculo_insulina;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegistroActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;
    private EditText editTextNombre;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private EditText editTextConfirmPassword;
    private Button buttonRegistrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        // Inicializar base de datos
        databaseHelper = new DatabaseHelper(this);

        // Inicializar views
        inicializarViews();

        // Configurar botones
        configurarBotones();
    }

    private void inicializarViews() {
        editTextNombre = findViewById(R.id.editTextNombre);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextConfirmPassword = findViewById(R.id.editTextConfirmPassword);
        buttonRegistrar = findViewById(R.id.buttonRegistrar);
        ImageButton buttonBack = findViewById(R.id.buttonBack);

        buttonBack.setOnClickListener(v -> finish());
    }

    private void configurarBotones() {
        buttonRegistrar.setOnClickListener(v -> registrarUsuario());
    }

    private void registrarUsuario() {
        // Obtener datos de los campos
        String nombre = editTextNombre.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString();
        String confirmPassword = editTextConfirmPassword.getText().toString();

        // Validaciones
        if (!validarCampos(nombre, email, password, confirmPassword)) {
            return;
        }

        // Verificar si el email ya existe
        Usuario usuarioExistente = databaseHelper.obtenerUsuarioPorEmail(email);
        if (usuarioExistente != null) {
            Toast.makeText(this, "El email ya está registrado", Toast.LENGTH_SHORT).show();
            return;
        }

        // Crear nuevo usuario
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setNombre(nombre);
        nuevoUsuario.setEmail(email);
        nuevoUsuario.setPassword(password);
        nuevoUsuario.setFotoUrl("");
        nuevoUsuario.setTipoLogin("email");
        nuevoUsuario.setIdExterno("");

        // Logs de diagnóstico
        Log.d("RegistroActivity", "Creando usuario: " + nombre);
        Log.d("RegistroActivity", "Email: " + email);
        Log.d("RegistroActivity", "Contraseña: " + password);
        Log.d("RegistroActivity", "Tipo login: " + nuevoUsuario.getTipoLogin());

        // Guardar usuario en la base de datos
        long id = databaseHelper.guardarUsuario(nuevoUsuario);
        Log.d("RegistroActivity", "ID del usuario guardado: " + id);
        
        if (id > 0) {
            nuevoUsuario.setId(id);
            Log.d("RegistroActivity", "Usuario guardado exitosamente");
            Toast.makeText(this, "Cuenta creada exitosamente", Toast.LENGTH_SHORT).show();
            
            // Verificar que se guardó correctamente
            Usuario usuarioVerificado = databaseHelper.obtenerUsuarioPorEmail(email);
            if (usuarioVerificado != null) {
                Log.d("RegistroActivity", "Verificación: Usuario encontrado en BD");
                Log.d("RegistroActivity", "Verificación: Contraseña guardada: " + usuarioVerificado.getPassword());
            } else {
                Log.d("RegistroActivity", "Verificación: ERROR - Usuario NO encontrado en BD");
            }
            
            // Ir a MainActivity
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("usuario_id", nuevoUsuario.getId());
            intent.putExtra("usuario_nombre", nuevoUsuario.getNombre());
            intent.putExtra("usuario_email", nuevoUsuario.getEmail());
            startActivity(intent);
            finish();
        } else {
            Log.d("RegistroActivity", "ERROR al guardar usuario");
            Toast.makeText(this, "Error al crear la cuenta", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validarCampos(String nombre, String email, String password, String confirmPassword) {
        // Validar nombre
        if (TextUtils.isEmpty(nombre)) {
            editTextNombre.setError("El nombre es requerido");
            editTextNombre.requestFocus();
            return false;
        }

        if (nombre.length() < 3) {
            editTextNombre.setError("El nombre debe tener al menos 3 caracteres");
            editTextNombre.requestFocus();
            return false;
        }

        // Validar email
        if (TextUtils.isEmpty(email)) {
            editTextEmail.setError("El email es requerido");
            editTextEmail.requestFocus();
            return false;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Ingrese un email válido");
            editTextEmail.requestFocus();
            return false;
        }

        // Validar contraseña
        if (TextUtils.isEmpty(password)) {
            editTextPassword.setError("La contraseña es requerida");
            editTextPassword.requestFocus();
            return false;
        }

        if (password.length() < 6) {
            editTextPassword.setError("La contraseña debe tener al menos 6 caracteres");
            editTextPassword.requestFocus();
            return false;
        }

        // Validar confirmación de contraseña
        if (TextUtils.isEmpty(confirmPassword)) {
            editTextConfirmPassword.setError("Confirme la contraseña");
            editTextConfirmPassword.requestFocus();
            return false;
        }

        if (!password.equals(confirmPassword)) {
            editTextConfirmPassword.setError("Las contraseñas no coinciden");
            editTextConfirmPassword.requestFocus();
            return false;
        }

        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (databaseHelper != null) {
            databaseHelper.close();
        }
    }
} 