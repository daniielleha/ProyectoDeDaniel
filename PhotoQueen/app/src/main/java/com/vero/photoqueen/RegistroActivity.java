package com.vero.photoqueen;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class RegistroActivity extends AppCompatActivity {
    //Declarar las variables
    TextInputLayout til_nombre, til_apellido, til_correo, til_usuario, til_password, til_telefono;
    EditText etNombre, etApellido, etCorreo, etUsuario, etPassword, etTelefono;
    Button btnRegistrar;
    String no, ap, cor, us, pa, te;

    //Una animacion de carga para la comunicacion del web service
    ProgressDialog progressDialog;

    //Almacenar la cadena
    RequestQueue requestQueue; String HttpURI = "http://192.168.1.74/cabina/public/andro-registro";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        // Primero activar la parte visual del boton PARA EL BOTON DE BACK O HOME
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Enlazar controladores
        til_nombre = (TextInputLayout) findViewById(R.id.til_nombre);
        til_apellido = (TextInputLayout) findViewById(R.id.til_apellido);
        til_usuario = (TextInputLayout) findViewById(R.id.til_usuario);
        til_password = (TextInputLayout) findViewById(R.id.til_password);
        til_correo = (TextInputLayout) findViewById(R.id.til_correo);
        til_telefono = (TextInputLayout) findViewById(R.id.til_telefono);
        etNombre = (EditText) findViewById(R.id.etNombre);
        etApellido = (EditText) findViewById(R.id.etApellido);
        etCorreo = (EditText) findViewById(R.id.etCorreoo);
        etUsuario = (EditText) findViewById(R.id.etUsuarioo);
        etPassword = (EditText) findViewById(R.id.etPasswordd);
        etTelefono = (EditText) findViewById(R.id.etTelefono);
        btnRegistrar =(Button) findViewById(R.id.btnRegistro);
        // Inicializar a requestqueue y el progressDialog
        requestQueue = Volley.newRequestQueue(RegistroActivity.this); progressDialog = new ProgressDialog(RegistroActivity.this);
        //la comprobación en tiempo real del texto que contiene un EditText
        // NOMBRE
        etNombre.addTextChangedListener(new TextWatcher() {@Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }@Override
        public void onTextChanged(CharSequence s, int start, int before, int count) { til_nombre.setError(null);
        }@Override
        public void afterTextChanged(Editable s) {
        }
        });
        // Apellido
        etApellido.addTextChangedListener(new TextWatcher() {@Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }@Override
        public void onTextChanged(CharSequence s, int start, int before, int count) { til_nombre.setError(null);
        }@Override
        public void afterTextChanged(Editable s) {
        }
        });
        //EMAIL
        etCorreo.addTextChangedListener(new TextWatcher() {@Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }@Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            //esEmailValido(String.valueOf(s));

        }@Override
        public void afterTextChanged(Editable s) {
        }
        });
        //USUARIO
        etUsuario.addTextChangedListener(new TextWatcher() {@Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }@Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            til_usuario.setError(null);
        }@Override
        public void afterTextChanged(Editable s) {
        }
        });
        //Programar el oyente
        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrar();
            }
        });
    }
    //CREAR SOBREESCRIBIR UN METODO
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed(); //Cambiar el retorno para mandarlo a la pantalla principal
        return true;
    }
    //FUNCION PRINCIPAL PARA LA VALIDACION
    private void registrar(){
        //Definir variables para pasar los datos del usuario por los métodos
        String n = til_nombre.getEditText().getText().toString();
        String a = til_apellido.getEditText().getText().toString();
        String u = til_usuario.getEditText().getText().toString();
        final String p = til_password.getEditText().getText().toString();
        String e = til_correo.getEditText().getText().toString();
        String t = til_telefono.getEditText().getText().toString();
       // retomar el alor boleano de cada método
        boolean nb = esNombreValido(n);
        boolean ab = esApellidoValido(a);
        boolean eb = esEmailValido(e);
        boolean ub = esUsuarioValido(u);
        boolean pb = esPasswordValida(p);
        boolean tb = esTelefonoValido(t);
        //Tomar el valor escrito por el usuario
        no = etNombre.getText().toString();
        ap = etApellido.getText().toString();
        us = etUsuario.getText().toString();
        pa = etPassword.getText().toString();
        cor = etCorreo.getText().toString();
        te = etTelefono.getText().toString();
        if(no.isEmpty() || ap.isEmpty() || te.isEmpty() || cor.isEmpty() || us.isEmpty() ||pa.isEmpty())
            Toast.makeText(getApplicationContext(),"Debes llenar todos los campos", Toast.LENGTH_LONG).show();
        else if (nb && ab && eb && ub && pb && tb) { //Mostrar el progressDialog
            progressDialog.setMessage("Procesando...");
            progressDialog.show(); //Creacion de la cadena a  ejecutar en el web service mediante Volley
            StringRequest stringRequest = new StringRequest(Request.Method.POST, HttpURI, new Response.Listener<String>() {
                @Override
                public void onResponse(String serverResponse) {
                    //Ocultar el progressDialog
                    progressDialog.dismiss();
                    try {
                        JSONObject obj = new JSONObject(serverResponse);
                        Boolean error = obj.getBoolean("error");
                        String mensaje = obj.getString("mensaje");
                        if (error == true)
                            Toast.makeText(getApplicationContext(),mensaje, Toast.LENGTH_LONG).show();
                        else {
                            // OK, se pasa a la siguiente acción
                            Toast.makeText(getApplicationContext(), mensaje, Toast.LENGTH_SHORT).show();
                            Intent intent1 = new Intent(getApplicationContext(),MainActivity.class);
                            startActivity(intent1);  //MANDARLO A EJECUTAR
                        }
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                }
            },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) { //Ocultar el progressDialog
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_LONG).show();
                        }
                    }){
                protected Map<String, String> getParams(){
                    Map<String,String> parametros = new HashMap<>();
                    parametros.put("Nombre",no);
                    parametros.put("Apellido",ap);
                    parametros.put("Usuario", us);
                    parametros.put("Correo",cor);
                    parametros.put("Contrasena",pa);
                    parametros.put("Telefono",te);
                    parametros.put("opcion","registro");
                    return parametros;
                }
            } ;
            requestQueue.add(stringRequest);
        } else {
            Toast.makeText(this, "Datos invalidos", Toast.LENGTH_SHORT).show();
        }
    }
    //MÉTODOS DE LA VALIDACION
    // metodo para validar nombre aceptar caracteres alfabéticos y espacios
    private boolean esNombreValido(String nombre) {
        Pattern patron = Pattern.compile("^[a-zA-Z ]+$");if (!patron.matcher(nombre).matches() || nombre.length() > 15) {
            til_nombre.setError("Nombre incorrecto"); return false; }
        else { til_nombre.setError(null); } return true;
    }
    // metodo para validar apellido aceptar caracteres alfabéticos y espacios
    private boolean esApellidoValido(String nombre) {
        Pattern patron = Pattern.compile("^[a-zA-Z ]+$");if (!patron.matcher(nombre).matches() || nombre.length() > 15) {
            til_apellido.setError("Apellido incorrecto"); return false; }
        else { til_apellido.setError(null); } return true;
    }
    //Metodo para validar la cadena especial de un correo
    private boolean esEmailValido(String correo) {
        if (correo.length()>30 || correo.length()<1) {
            til_correo.setError("Correo electronico incorrecto"); }
        else if (!Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
            til_correo.setError("Correo electronico incorrecto");
            return false; }
        else { til_correo.setError(null); }return true; }
    //valida al usuario
    private boolean esUsuarioValido(String direccion) { if (direccion.length() > 20 || direccion.length() < 1) {
        til_usuario.setError("Usuario incorrecto");return false; } else { til_usuario.setError(null); }
        return true; }
    private boolean esPasswordValida(String password) { if (password.length() > 16 || password.length() < 0) {
        til_password.setError("Contraseña incorrecta");return false; } else { til_password.setError(null); }
        return true; }
    // metodo para validad telefono la sintaxis del número. Añade esta lógica.
    private boolean esTelefonoValido(String telefono) {
        if (!Patterns.PHONE.matcher(telefono).matches() || telefono.length() > 10) {
            til_telefono.setError("Teléfono inválido");
            return false;
        } else { til_telefono.setError(null); }
        return true;
    }
}
