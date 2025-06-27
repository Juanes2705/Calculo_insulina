package co.edu.univalle.calculo_insulina;

public class Usuario {
    private long id;
    private String nombre;
    private String email;
    private String fotoUrl;
    private String tipoLogin; // "google", "facebook", o "email"
    private String idExterno; // ID de Google o Facebook
    private String password; // Contraseña para login tradicional

    public Usuario() {}

    public Usuario(String nombre, String email, String fotoUrl, String tipoLogin, String idExterno) {
        this.nombre = nombre;
        this.email = email;
        this.fotoUrl = fotoUrl;
        this.tipoLogin = tipoLogin;
        this.idExterno = idExterno;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFotoUrl() {
        return fotoUrl;
    }

    public void setFotoUrl(String fotoUrl) {
        this.fotoUrl = fotoUrl;
    }

    public String getTipoLogin() {
        return tipoLogin;
    }

    public void setTipoLogin(String tipoLogin) {
        this.tipoLogin = tipoLogin;
    }

    public String getIdExterno() {
        return idExterno;
    }

    public void setIdExterno(String idExterno) {
        this.idExterno = idExterno;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
} 