package co.edu.univalle.calculo_insulina;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import java.util.Arrays;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private static final int RC_SIGN_IN = 9001;

    private DatabaseHelper databaseHelper;
    private GoogleSignInClient mGoogleSignInClient;
    private CallbackManager callbackManager;
    private EditText editTextLoginEmail;
    private EditText editTextLoginPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Inicializar base de datos
        databaseHelper = new DatabaseHelper(this);

        // Configurar Google Sign-In
        configurarGoogleSignIn();

        // Configurar Facebook Login
        configurarFacebookLogin();

        // Configurar botones de desarrollo
        configurarBotonesDesarrollo();

        // Configurar login tradicional
        configurarLoginTradicional();

        // Verificar si ya hay un usuario logueado
        verificarUsuarioLogueado();
    }

    private void configurarGoogleSignIn() {
        // Configurar Google Sign-In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestProfile()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        SignInButton signInButton = findViewById(R.id.buttonGoogleSignIn);
        signInButton.setOnClickListener(v -> signInGoogle());
    }

    private void configurarFacebookLogin() {
        callbackManager = CallbackManager.Factory.create();

        com.facebook.login.widget.LoginButton loginButton = findViewById(R.id.buttonFacebookLogin);
        loginButton.setReadPermissions(Arrays.asList("email", "public_profile"));

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "Facebook login success");
                // Obtener información del usuario de Facebook
                obtenerInformacionFacebook(loginResult);
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "Facebook login cancelled");
                Toast.makeText(LoginActivity.this, "Inicio de sesión cancelado", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Log.e(TAG, "Facebook login error", error);
                Toast.makeText(LoginActivity.this, "Error en el inicio de sesión: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void signInGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void obtenerInformacionFacebook(LoginResult loginResult) {
        // Aquí normalmente obtendrías la información del usuario de Facebook
        // Por simplicidad, usaremos datos de ejemplo
        String userId = loginResult.getAccessToken().getUserId();
        
        // Verificar si el usuario ya existe
        Usuario usuarioExistente = databaseHelper.obtenerUsuarioPorIdExterno(userId);
        if (usuarioExistente != null) {
            // Usuario ya existe, ir a MainActivity
            irAMainActivity(usuarioExistente);
        } else {
            // Crear nuevo usuario (con datos de ejemplo)
            Usuario nuevoUsuario = new Usuario();
            nuevoUsuario.setNombre("Usuario Facebook");
            nuevoUsuario.setEmail("usuario@facebook.com");
            nuevoUsuario.setFotoUrl("");
            nuevoUsuario.setTipoLogin("facebook");
            nuevoUsuario.setIdExterno(userId);
            
            long id = databaseHelper.guardarUsuario(nuevoUsuario);
            if (id > 0) {
                nuevoUsuario.setId(id);
                irAMainActivity(nuevoUsuario);
            } else {
                Toast.makeText(this, "Error al guardar usuario", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void verificarUsuarioLogueado() {
        // Verificar si hay un usuario de Google logueado
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account != null) {
            // Usuario ya logueado con Google
            Usuario usuario = new Usuario();
            usuario.setNombre(account.getDisplayName());
            usuario.setEmail(account.getEmail());
            usuario.setFotoUrl(account.getPhotoUrl() != null ? account.getPhotoUrl().toString() : "");
            usuario.setTipoLogin("google");
            usuario.setIdExterno(account.getId());
            
            irAMainActivity(usuario);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Resultado de Google Sign-In
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }

        // Resultado de Facebook Login
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            // Inicio de sesión exitoso
            Log.d(TAG, "Google sign in success: " + account.getEmail());
            
            // Verificar si el usuario ya existe
            Usuario usuarioExistente = databaseHelper.obtenerUsuarioPorEmail(account.getEmail());
            if (usuarioExistente != null) {
                // Usuario ya existe, ir a MainActivity
                irAMainActivity(usuarioExistente);
            } else {
                // Crear nuevo usuario
                Usuario nuevoUsuario = new Usuario();
                nuevoUsuario.setNombre(account.getDisplayName());
                nuevoUsuario.setEmail(account.getEmail());
                nuevoUsuario.setFotoUrl(account.getPhotoUrl() != null ? account.getPhotoUrl().toString() : "");
                nuevoUsuario.setTipoLogin("google");
                nuevoUsuario.setIdExterno(account.getId());
                
                long id = databaseHelper.guardarUsuario(nuevoUsuario);
                if (id > 0) {
                    nuevoUsuario.setId(id);
                    irAMainActivity(nuevoUsuario);
                } else {
                    Toast.makeText(this, "Error al guardar usuario", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (ApiException e) {
            // Inicio de sesión fallido
            Log.w(TAG, "Google sign in failed", e);
            Toast.makeText(this, "Error en el inicio de sesión: " + e.getStatusCode(), Toast.LENGTH_SHORT).show();
        }
    }

    private void irAMainActivity(Usuario usuario) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("usuario_id", usuario.getId());
        intent.putExtra("usuario_nombre", usuario.getNombre());
        intent.putExtra("usuario_email", usuario.getEmail());
        startActivity(intent);
        finish(); // Cerrar LoginActivity para que no se pueda volver atrás
    }

    private void configurarBotonesDesarrollo() {
        Button buttonGoogleDev = findViewById(R.id.buttonLoginGoogleDev);
        Button buttonFacebookDev = findViewById(R.id.buttonLoginFacebookDev);

        buttonGoogleDev.setOnClickListener(v -> loginGoogleSimulado());
        buttonFacebookDev.setOnClickListener(v -> loginFacebookSimulado());
    }

    private void loginGoogleSimulado() {
        // Crear usuario simulado de Google
        Usuario usuario = new Usuario();
        usuario.setNombre("Usuario Google (Dev)");
        usuario.setEmail("usuario.google@dev.com");
        usuario.setFotoUrl("");
        usuario.setTipoLogin("google");
        usuario.setIdExterno("google_dev_123");
        
        guardarYContinuar(usuario);
    }

    private void loginFacebookSimulado() {
        // Crear usuario simulado de Facebook
        Usuario usuario = new Usuario();
        usuario.setNombre("Usuario Facebook (Dev)");
        usuario.setEmail("usuario.facebook@dev.com");
        usuario.setFotoUrl("");
        usuario.setTipoLogin("facebook");
        usuario.setIdExterno("facebook_dev_456");
        
        guardarYContinuar(usuario);
    }

    private void guardarYContinuar(Usuario usuario) {
        // Verificar si el usuario ya existe
        Usuario usuarioExistente = databaseHelper.obtenerUsuarioPorEmail(usuario.getEmail());
        if (usuarioExistente != null) {
            // Usuario ya existe, ir a MainActivity
            irAMainActivity(usuarioExistente);
        } else {
            // Crear nuevo usuario
            long id = databaseHelper.guardarUsuario(usuario);
            if (id > 0) {
                usuario.setId(id);
                irAMainActivity(usuario);
            } else {
                Toast.makeText(this, "Error al guardar usuario", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void configurarLoginTradicional() {
        editTextLoginEmail = findViewById(R.id.editTextLoginEmail);
        editTextLoginPassword = findViewById(R.id.editTextLoginPassword);
        Button buttonLoginEmail = findViewById(R.id.buttonLoginEmail);
        Button buttonRegistrarse = findViewById(R.id.buttonRegistrarse);

        buttonLoginEmail.setOnClickListener(v -> loginConEmail());
        buttonRegistrarse.setOnClickListener(v -> irARegistro());
    }

    private void loginConEmail() {
        String email = editTextLoginEmail.getText().toString().trim();
        String password = editTextLoginPassword.getText().toString();

        // Logs de diagnóstico
        Log.d("LoginActivity", "Intentando login con email: " + email);
        Log.d("LoginActivity", "Contraseña ingresada: " + password);

        // Validaciones básicas
        if (email.isEmpty()) {
            editTextLoginEmail.setError("Ingrese su email");
            editTextLoginEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            editTextLoginPassword.setError("Ingrese su contraseña");
            editTextLoginPassword.requestFocus();
            return;
        }

        // Buscar usuario en la base de datos
        Usuario usuario = databaseHelper.obtenerUsuarioPorEmail(email);
        
        // Logs de diagnóstico
        if (usuario != null) {
            Log.d("LoginActivity", "Usuario encontrado: " + usuario.getNombre());
            Log.d("LoginActivity", "Contraseña guardada: " + usuario.getPassword());
            Log.d("LoginActivity", "Tipo de login: " + usuario.getTipoLogin());
        } else {
            Log.d("LoginActivity", "Usuario NO encontrado en la base de datos");
        }
        
        if (usuario != null && usuario.getPassword() != null && usuario.getPassword().equals(password)) {
            // Login exitoso
            Log.d("LoginActivity", "Login exitoso - navegando a MainActivity");
            irAMainActivity(usuario);
        } else {
            Log.d("LoginActivity", "Login fallido - contraseñas no coinciden o usuario no encontrado");
            Toast.makeText(this, "Email o contraseña incorrectos", Toast.LENGTH_SHORT).show();
        }
    }

    private void irARegistro() {
        Intent intent = new Intent(this, RegistroActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (databaseHelper != null) {
            databaseHelper.close();
        }
    }
} 