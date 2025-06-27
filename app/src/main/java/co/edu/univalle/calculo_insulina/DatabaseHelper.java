package co.edu.univalle.calculo_insulina;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "Calculo_insulina.db";
    private static final int DATABASE_VERSION = 2;

    // Tabla de categorías de alimentos
    public static final String TABLE_CATEGORIAS = "categorias";
    public static final String COLUMN_CATEGORIA_ID = "id";
    public static final String COLUMN_CATEGORIA_NOMBRE = "nombre";

    // Tabla de alimentos
    public static final String TABLE_ALIMENTOS = "alimentos";
    public static final String COLUMN_ALIMENTO_ID = "id";
    public static final String COLUMN_ALIMENTO_NOMBRE = "nombre";
    public static final String COLUMN_ALIMENTO_CATEGORIA_ID = "categoria_id";
    public static final String COLUMN_ALIMENTO_PORCION = "porcion";
    public static final String COLUMN_ALIMENTO_PESO_GRAMOS = "peso_gramos";
    public static final String COLUMN_ALIMENTO_CARBOHIDRATOS = "carbohidratos";

    // Tabla de ratios por tipo de comida
    public static final String TABLE_RATIOS = "ratios";
    public static final String COLUMN_RATIO_ID = "id";
    public static final String COLUMN_RATIO_TIPO_COMIDA = "tipo_comida";
    public static final String COLUMN_RATIO_VALOR = "valor";

    // Tabla de factores de corrección por glicemia
    public static final String TABLE_FACTORES_CORRECCION = "factores_correccion";
    public static final String COLUMN_FC_ID = "id";
    public static final String COLUMN_FC_RANGO_MIN = "rango_min";
    public static final String COLUMN_FC_RANGO_MAX = "rango_max";
    public static final String COLUMN_FC_VALOR = "valor";

    // Tabla de registros de insulina
    public static final String TABLE_REGISTROS = "registros";
    public static final String COLUMN_REGISTRO_ID = "id";
    public static final String COLUMN_REGISTRO_FECHA = "fecha";
    public static final String COLUMN_REGISTRO_TIPO_COMIDA = "tipo_comida";
    public static final String COLUMN_REGISTRO_GLICEMIA = "glicemia";
    public static final String COLUMN_REGISTRO_CARBOHIDRATOS = "carbohidratos";
    public static final String COLUMN_REGISTRO_RATIO = "ratio";
    public static final String COLUMN_REGISTRO_FC = "factor_correccion";
    public static final String COLUMN_REGISTRO_IA = "insulina_alimentos";
    public static final String COLUMN_REGISTRO_INSULINA_TOTAL = "insulina_total";

    // Tabla de usuarios
    public static final String TABLE_USUARIOS = "usuarios";
    public static final String COLUMN_USUARIO_ID = "id";
    public static final String COLUMN_USUARIO_NOMBRE = "nombre";
    public static final String COLUMN_USUARIO_EMAIL = "email";
    public static final String COLUMN_USUARIO_FOTO_URL = "foto_url";
    public static final String COLUMN_USUARIO_TIPO_LOGIN = "tipo_login";
    public static final String COLUMN_USUARIO_ID_EXTERNO = "id_externo";
    public static final String COLUMN_USUARIO_FECHA_REGISTRO = "fecha_registro";
    public static final String COLUMN_USUARIO_PASSWORD = "password";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Crear tabla de categorías
        String CREATE_CATEGORIAS_TABLE = "CREATE TABLE " + TABLE_CATEGORIAS + "("
                + COLUMN_CATEGORIA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_CATEGORIA_NOMBRE + " TEXT"
                + ")";

        // Crear tabla de alimentos
        String CREATE_ALIMENTOS_TABLE = "CREATE TABLE " + TABLE_ALIMENTOS + "("
                + COLUMN_ALIMENTO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_ALIMENTO_NOMBRE + " TEXT,"
                + COLUMN_ALIMENTO_CATEGORIA_ID + " INTEGER,"
                + COLUMN_ALIMENTO_PORCION + " TEXT,"
                + COLUMN_ALIMENTO_PESO_GRAMOS + " REAL,"
                + COLUMN_ALIMENTO_CARBOHIDRATOS + " REAL,"
                + "FOREIGN KEY(" + COLUMN_ALIMENTO_CATEGORIA_ID + ") REFERENCES " + TABLE_CATEGORIAS + "(" + COLUMN_CATEGORIA_ID + ")"
                + ")";

        // Crear tabla de ratios
        String CREATE_RATIOS_TABLE = "CREATE TABLE " + TABLE_RATIOS + "("
                + COLUMN_RATIO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_RATIO_TIPO_COMIDA + " TEXT,"
                + COLUMN_RATIO_VALOR + " INTEGER"
                + ")";

        // Crear tabla de factores de corrección
        String CREATE_FC_TABLE = "CREATE TABLE " + TABLE_FACTORES_CORRECCION + "("
                + COLUMN_FC_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_FC_RANGO_MIN + " INTEGER,"
                + COLUMN_FC_RANGO_MAX + " INTEGER,"
                + COLUMN_FC_VALOR + " INTEGER"
                + ")";

        // Crear tabla de registros
        String CREATE_REGISTROS_TABLE = "CREATE TABLE " + TABLE_REGISTROS + "("
                + COLUMN_REGISTRO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_REGISTRO_FECHA + " TEXT,"
                + COLUMN_REGISTRO_TIPO_COMIDA + " TEXT,"
                + COLUMN_REGISTRO_GLICEMIA + " INTEGER,"
                + COLUMN_REGISTRO_CARBOHIDRATOS + " REAL,"
                + COLUMN_REGISTRO_RATIO + " INTEGER,"
                + COLUMN_REGISTRO_FC + " INTEGER,"
                + COLUMN_REGISTRO_IA + " REAL,"
                + COLUMN_REGISTRO_INSULINA_TOTAL + " REAL"
                + ")";

        // Crear tabla de usuarios
        String CREATE_USUARIOS_TABLE = "CREATE TABLE " + TABLE_USUARIOS + "("
                + COLUMN_USUARIO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_USUARIO_NOMBRE + " TEXT,"
                + COLUMN_USUARIO_EMAIL + " TEXT UNIQUE,"
                + COLUMN_USUARIO_FOTO_URL + " TEXT,"
                + COLUMN_USUARIO_TIPO_LOGIN + " TEXT,"
                + COLUMN_USUARIO_ID_EXTERNO + " TEXT,"
                + COLUMN_USUARIO_FECHA_REGISTRO + " TEXT,"
                + COLUMN_USUARIO_PASSWORD + " TEXT"
                + ")";

        db.execSQL(CREATE_CATEGORIAS_TABLE);
        db.execSQL(CREATE_ALIMENTOS_TABLE);
        db.execSQL(CREATE_RATIOS_TABLE);
        db.execSQL(CREATE_FC_TABLE);
        db.execSQL(CREATE_REGISTROS_TABLE);
        db.execSQL(CREATE_USUARIOS_TABLE);

        // Insertar datos iniciales
        insertarDatosIniciales(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            // Agregar columna password a la tabla usuarios si no existe
            try {
                db.execSQL("ALTER TABLE " + TABLE_USUARIOS + " ADD COLUMN " + COLUMN_USUARIO_PASSWORD + " TEXT");
            } catch (Exception e) {
                // La columna ya existe, ignorar el error
            }
        }
        
        // Para futuras versiones, agregar más migraciones aquí
        if (oldVersion < newVersion) {
            // Si hay más migraciones futuras, agregarlas aquí
        }
    }

    private void insertarDatosIniciales(SQLiteDatabase db) {
        // Insertar categorías
        String[] categorias = {"Cereales", "Tubérculos y Plátanos", "Leguminosas", "Lácteos", "Verduras", "Frutas", "Procesados"};
        for (String categoria : categorias) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_CATEGORIA_NOMBRE, categoria);
            db.insert(TABLE_CATEGORIAS, null, values);
        }

        // Insertar ratios
        ContentValues ratioDesayuno = new ContentValues();
        ratioDesayuno.put(COLUMN_RATIO_TIPO_COMIDA, "Desayuno");
        ratioDesayuno.put(COLUMN_RATIO_VALOR, 15);
        db.insert(TABLE_RATIOS, null, ratioDesayuno);

        ContentValues ratioAlmuerzo = new ContentValues();
        ratioAlmuerzo.put(COLUMN_RATIO_TIPO_COMIDA, "Almuerzo");
        ratioAlmuerzo.put(COLUMN_RATIO_VALOR, 12);
        db.insert(TABLE_RATIOS, null, ratioAlmuerzo);

        ContentValues ratioCena = new ContentValues();
        ratioCena.put(COLUMN_RATIO_TIPO_COMIDA, "Cena");
        ratioCena.put(COLUMN_RATIO_VALOR, 15);
        db.insert(TABLE_RATIOS, null, ratioCena);

        // Insertar factores de corrección
        // GL = 70-100 entonces FC = -1
        ContentValues fc1 = new ContentValues();
        fc1.put(COLUMN_FC_RANGO_MIN, 70);
        fc1.put(COLUMN_FC_RANGO_MAX, 100);
        fc1.put(COLUMN_FC_VALOR, -1);
        db.insert(TABLE_FACTORES_CORRECCION, null, fc1);

        // GL = 140-200 entonces FC = +2
        ContentValues fc2 = new ContentValues();
        fc2.put(COLUMN_FC_RANGO_MIN, 140);
        fc2.put(COLUMN_FC_RANGO_MAX, 200);
        fc2.put(COLUMN_FC_VALOR, 2);
        db.insert(TABLE_FACTORES_CORRECCION, null, fc2);

        // GL = 200-250 entonces FC = +3
        ContentValues fc3 = new ContentValues();
        fc3.put(COLUMN_FC_RANGO_MIN, 200);
        fc3.put(COLUMN_FC_RANGO_MAX, 250);
        fc3.put(COLUMN_FC_VALOR, 3);
        db.insert(TABLE_FACTORES_CORRECCION, null, fc3);

        // GL > 250 entonces FC = +4
        ContentValues fc4 = new ContentValues();
        fc4.put(COLUMN_FC_RANGO_MIN, 251);
        fc4.put(COLUMN_FC_RANGO_MAX, 999);
        fc4.put(COLUMN_FC_VALOR, 4);
        db.insert(TABLE_FACTORES_CORRECCION, null, fc4);

        // Al final del método, agregar el usuario de pruebas:
        // Usuario de pruebas
        ContentValues usuarioPrueba = new ContentValues();
        usuarioPrueba.put(COLUMN_USUARIO_NOMBRE, "Usuario de Prueba");
        usuarioPrueba.put(COLUMN_USUARIO_EMAIL, "test@demo.com");
        usuarioPrueba.put(COLUMN_USUARIO_PASSWORD, "123456");
        usuarioPrueba.put(COLUMN_USUARIO_TIPO_LOGIN, "email");
        usuarioPrueba.put(COLUMN_USUARIO_FOTO_URL, "");
        usuarioPrueba.put(COLUMN_USUARIO_ID_EXTERNO, "");
        usuarioPrueba.put(COLUMN_USUARIO_FECHA_REGISTRO, obtenerFechaActual());
        db.insert(TABLE_USUARIOS, null, usuarioPrueba);

        // Insertar algunos alimentos de ejemplo
        insertarAlimentosEjemplo(db);
    }

    private void insertarAlimentosEjemplo(SQLiteDatabase db) {
        // Cereales
        insertarAlimento(db, "Arroz blanco", 1, "1 taza", 150, 45);
        insertarAlimento(db, "Pan integral", 1, "1 rebanada", 30, 15);
        insertarAlimento(db, "Avena", 1, "1/2 taza", 40, 27);

        // Tubérculos y Plátanos
        insertarAlimento(db, "Papa", 2, "1 unidad mediana", 150, 30);
        insertarAlimento(db, "Plátano", 2, "1 unidad", 120, 27);
        insertarAlimento(db, "Yuca", 2, "1 taza", 100, 38);

        // Leguminosas
        insertarAlimento(db, "Frijoles", 3, "1/2 taza", 90, 20);
        insertarAlimento(db, "Lentejas", 3, "1/2 taza", 100, 20);
        insertarAlimento(db, "Garbanzos", 3, "1/2 taza", 82, 22);

        // Lácteos
        insertarAlimento(db, "Leche", 4, "1 taza", 240, 12);
        insertarAlimento(db, "Yogur", 4, "1 taza", 245, 17);
        insertarAlimento(db, "Queso", 4, "1 onza", 28, 1);

        // Verduras
        insertarAlimento(db, "Brócoli", 5, "1 taza", 91, 6);
        insertarAlimento(db, "Espinaca", 5, "1 taza", 30, 1);
        insertarAlimento(db, "Zanahoria", 5, "1 unidad", 72, 7);

        // Frutas
        insertarAlimento(db, "Manzana", 6, "1 unidad", 182, 25);
        insertarAlimento(db, "Naranja", 6, "1 unidad", 131, 15);
        insertarAlimento(db, "Fresa", 6, "1 taza", 144, 12);

        // Procesados
        insertarAlimento(db, "Galletas", 7, "3 unidades", 30, 18);
        insertarAlimento(db, "Jugo de naranja", 7, "1 taza", 240, 26);
        insertarAlimento(db, "Helado", 7, "1/2 taza", 66, 16);
    }

    private void insertarAlimento(SQLiteDatabase db, String nombre, int categoriaId, String porcion, double peso, double carbohidratos) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_ALIMENTO_NOMBRE, nombre);
        values.put(COLUMN_ALIMENTO_CATEGORIA_ID, categoriaId);
        values.put(COLUMN_ALIMENTO_PORCION, porcion);
        values.put(COLUMN_ALIMENTO_PESO_GRAMOS, peso);
        values.put(COLUMN_ALIMENTO_CARBOHIDRATOS, carbohidratos);
        db.insert(TABLE_ALIMENTOS, null, values);
    }

    // Métodos para obtener datos
    public List<Categoria> obtenerCategorias() {
        List<Categoria> categorias = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_CATEGORIAS, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                Categoria categoria = new Categoria();
                categoria.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_CATEGORIA_ID)));
                categoria.setNombre(cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORIA_NOMBRE)));
                categorias.add(categoria);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return categorias;
    }

    public List<Alimento> obtenerAlimentosPorCategoria(int categoriaId) {
        List<Alimento> alimentos = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = COLUMN_ALIMENTO_CATEGORIA_ID + "=?";
        String[] selectionArgs = {String.valueOf(categoriaId)};
        Cursor cursor = db.query(TABLE_ALIMENTOS, null, selection, selectionArgs, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                Alimento alimento = new Alimento();
                alimento.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ALIMENTO_ID)));
                alimento.setNombre(cursor.getString(cursor.getColumnIndex(COLUMN_ALIMENTO_NOMBRE)));
                alimento.setCategoriaId(cursor.getInt(cursor.getColumnIndex(COLUMN_ALIMENTO_CATEGORIA_ID)));
                alimento.setPorcion(cursor.getString(cursor.getColumnIndex(COLUMN_ALIMENTO_PORCION)));
                alimento.setPesoGramos(cursor.getDouble(cursor.getColumnIndex(COLUMN_ALIMENTO_PESO_GRAMOS)));
                alimento.setCarbohidratos(cursor.getDouble(cursor.getColumnIndex(COLUMN_ALIMENTO_CARBOHIDRATOS)));
                alimentos.add(alimento);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return alimentos;
    }

    public int obtenerRatio(String tipoComida) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = COLUMN_RATIO_TIPO_COMIDA + "=?";
        String[] selectionArgs = {tipoComida};
        Cursor cursor = db.query(TABLE_RATIOS, null, selection, selectionArgs, null, null, null);

        int ratio = 0;
        if (cursor.moveToFirst()) {
            ratio = cursor.getInt(cursor.getColumnIndex(COLUMN_RATIO_VALOR));
        }
        cursor.close();
        return ratio;
    }

    public int obtenerFactorCorreccion(int glicemia) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = COLUMN_FC_RANGO_MIN + "<=? AND " + COLUMN_FC_RANGO_MAX + ">=?";
        String[] selectionArgs = {String.valueOf(glicemia), String.valueOf(glicemia)};
        Cursor cursor = db.query(TABLE_FACTORES_CORRECCION, null, selection, selectionArgs, null, null, null);

        int fc = 0;
        if (cursor.moveToFirst()) {
            fc = cursor.getInt(cursor.getColumnIndex(COLUMN_FC_VALOR));
        }
        cursor.close();
        return fc;
    }

    public long guardarRegistro(RegistroInsulina registro) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_REGISTRO_FECHA, registro.getFecha());
        values.put(COLUMN_REGISTRO_TIPO_COMIDA, registro.getTipoComida());
        values.put(COLUMN_REGISTRO_GLICEMIA, registro.getGlicemia());
        values.put(COLUMN_REGISTRO_CARBOHIDRATOS, registro.getCarbohidratos());
        values.put(COLUMN_REGISTRO_RATIO, registro.getRatio());
        values.put(COLUMN_REGISTRO_FC, registro.getFactorCorreccion());
        values.put(COLUMN_REGISTRO_IA, registro.getInsulinaAlimentos());
        values.put(COLUMN_REGISTRO_INSULINA_TOTAL, registro.getInsulinaTotal());
        return db.insert(TABLE_REGISTROS, null, values);
    }

    public List<RegistroInsulina> obtenerRegistros() {
        List<RegistroInsulina> registros = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_REGISTROS, null, null, null, null, null, COLUMN_REGISTRO_FECHA + " DESC");

        if (cursor.moveToFirst()) {
            do {
                RegistroInsulina registro = new RegistroInsulina();
                registro.setId(cursor.getLong(cursor.getColumnIndex(COLUMN_REGISTRO_ID)));
                registro.setFecha(cursor.getString(cursor.getColumnIndex(COLUMN_REGISTRO_FECHA)));
                registro.setTipoComida(cursor.getString(cursor.getColumnIndex(COLUMN_REGISTRO_TIPO_COMIDA)));
                registro.setGlicemia(cursor.getInt(cursor.getColumnIndex(COLUMN_REGISTRO_GLICEMIA)));
                registro.setCarbohidratos(cursor.getDouble(cursor.getColumnIndex(COLUMN_REGISTRO_CARBOHIDRATOS)));
                registro.setRatio(cursor.getInt(cursor.getColumnIndex(COLUMN_REGISTRO_RATIO)));
                registro.setFactorCorreccion(cursor.getInt(cursor.getColumnIndex(COLUMN_REGISTRO_FC)));
                registro.setInsulinaAlimentos(cursor.getDouble(cursor.getColumnIndex(COLUMN_REGISTRO_IA)));
                registro.setInsulinaTotal(cursor.getDouble(cursor.getColumnIndex(COLUMN_REGISTRO_INSULINA_TOTAL)));
                registros.add(registro);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return registros;
    }

    public RegistroInsulina obtenerRegistroPorId(long registroId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = COLUMN_REGISTRO_ID + "=?";
        String[] selectionArgs = {String.valueOf(registroId)};
        Cursor cursor = db.query(TABLE_REGISTROS, null, selection, selectionArgs, null, null, null);

        RegistroInsulina registro = null;
        if (cursor.moveToFirst()) {
            registro = new RegistroInsulina();
            registro.setId(cursor.getLong(cursor.getColumnIndex(COLUMN_REGISTRO_ID)));
            registro.setFecha(cursor.getString(cursor.getColumnIndex(COLUMN_REGISTRO_FECHA)));
            registro.setTipoComida(cursor.getString(cursor.getColumnIndex(COLUMN_REGISTRO_TIPO_COMIDA)));
            registro.setGlicemia(cursor.getInt(cursor.getColumnIndex(COLUMN_REGISTRO_GLICEMIA)));
            registro.setCarbohidratos(cursor.getDouble(cursor.getColumnIndex(COLUMN_REGISTRO_CARBOHIDRATOS)));
            registro.setRatio(cursor.getInt(cursor.getColumnIndex(COLUMN_REGISTRO_RATIO)));
            registro.setFactorCorreccion(cursor.getInt(cursor.getColumnIndex(COLUMN_REGISTRO_FC)));
            registro.setInsulinaAlimentos(cursor.getDouble(cursor.getColumnIndex(COLUMN_REGISTRO_IA)));
            registro.setInsulinaTotal(cursor.getDouble(cursor.getColumnIndex(COLUMN_REGISTRO_INSULINA_TOTAL)));
        }
        cursor.close();
        return registro;
    }

    // Métodos para manejar usuarios
    public long guardarUsuario(Usuario usuario) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USUARIO_NOMBRE, usuario.getNombre());
        values.put(COLUMN_USUARIO_EMAIL, usuario.getEmail());
        values.put(COLUMN_USUARIO_FOTO_URL, usuario.getFotoUrl());
        values.put(COLUMN_USUARIO_TIPO_LOGIN, usuario.getTipoLogin());
        values.put(COLUMN_USUARIO_ID_EXTERNO, usuario.getIdExterno());
        values.put(COLUMN_USUARIO_FECHA_REGISTRO, obtenerFechaActual());
        values.put(COLUMN_USUARIO_PASSWORD, usuario.getPassword());
        
        // Intentar insertar, si falla (email duplicado) actualizar
        long id = db.insertWithOnConflict(TABLE_USUARIOS, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        return id;
    }

    public Usuario obtenerUsuarioPorEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = COLUMN_USUARIO_EMAIL + "=?";
        String[] selectionArgs = {email};
        Cursor cursor = db.query(TABLE_USUARIOS, null, selection, selectionArgs, null, null, null);

        Usuario usuario = null;
        if (cursor.moveToFirst()) {
            usuario = new Usuario();
            usuario.setId(cursor.getLong(cursor.getColumnIndex(COLUMN_USUARIO_ID)));
            usuario.setNombre(cursor.getString(cursor.getColumnIndex(COLUMN_USUARIO_NOMBRE)));
            usuario.setEmail(cursor.getString(cursor.getColumnIndex(COLUMN_USUARIO_EMAIL)));
            usuario.setFotoUrl(cursor.getString(cursor.getColumnIndex(COLUMN_USUARIO_FOTO_URL)));
            usuario.setTipoLogin(cursor.getString(cursor.getColumnIndex(COLUMN_USUARIO_TIPO_LOGIN)));
            usuario.setIdExterno(cursor.getString(cursor.getColumnIndex(COLUMN_USUARIO_ID_EXTERNO)));
            usuario.setPassword(cursor.getString(cursor.getColumnIndex(COLUMN_USUARIO_PASSWORD)));
        }
        cursor.close();
        return usuario;
    }

    public Usuario obtenerUsuarioPorIdExterno(String idExterno) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = COLUMN_USUARIO_ID_EXTERNO + "=?";
        String[] selectionArgs = {idExterno};
        Cursor cursor = db.query(TABLE_USUARIOS, null, selection, selectionArgs, null, null, null);

        Usuario usuario = null;
        if (cursor.moveToFirst()) {
            usuario = new Usuario();
            usuario.setId(cursor.getLong(cursor.getColumnIndex(COLUMN_USUARIO_ID)));
            usuario.setNombre(cursor.getString(cursor.getColumnIndex(COLUMN_USUARIO_NOMBRE)));
            usuario.setEmail(cursor.getString(cursor.getColumnIndex(COLUMN_USUARIO_EMAIL)));
            usuario.setFotoUrl(cursor.getString(cursor.getColumnIndex(COLUMN_USUARIO_FOTO_URL)));
            usuario.setTipoLogin(cursor.getString(cursor.getColumnIndex(COLUMN_USUARIO_TIPO_LOGIN)));
            usuario.setIdExterno(cursor.getString(cursor.getColumnIndex(COLUMN_USUARIO_ID_EXTERNO)));
            usuario.setPassword(cursor.getString(cursor.getColumnIndex(COLUMN_USUARIO_PASSWORD)));
        }
        cursor.close();
        return usuario;
    }

    private String obtenerFechaActual() {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm", java.util.Locale.getDefault());
        return sdf.format(new java.util.Date());
    }
} 