package com.vero.photoqueen;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.text.Editable;

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

public class MainActivity extends AppCompatActivity {
    //DESPUES DE QUE SE DISEÑO SE HACE UNA PRUEBA SE GENERARA UN INTENT PARA HACER UUN CAMBIO
    // PROGRAMAR UN BOTON - DECLARAR OBJETOS MEDIANTE EL TIPEO DE DATOS
    TextInputLayout til_usuario;
    Button btnAcceder;
    TextView etRegistrar;
    EditText etUsuario, etPassword;
    String e,p;

    //Una animacion de carga para la comunicacion del web service
    ProgressDialog progressDialog;
    //Almacenar la cadena
    RequestQueue requestQueue;

    String HttpURI = "http://192.168.1.74/cabina/public/andro-login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //PARA QUITAR LA BARRA SUPERIOR
        getSupportActionBar().hide();
        verificar();
        //ENLAZAR CONTROLADORES DE VISTA MEDIANTE LA PROGRAMACION
        //Enlazar controladores con las vista
        btnAcceder = (Button) findViewById(R.id.btnAcceder);
        etRegistrar =(TextView) findViewById(R.id.etRegistrar);
        etUsuario = (EditText) findViewById(R.id.etUsuario);
        etPassword = (EditText) findViewById(R.id.etPassword);
        til_usuario =(TextInputLayout) findViewById(R.id.til_usuario);
        //añadir text watchters
        // etUsuario.addTextChangedListener(new TextWatcher() {
        //    @Override
        //  public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        //  }
        //  @Override
        //  public void onTextChanged(CharSequence s, int start, int before, int count) {
        //      esCorreoValido(String.valueOf(s));
        //  }
        //  @Override
        //  public void afterTextChanged(Editable s) {
        //  }
        //});
        // Inicializar a requestqueue y el progressDialog
        requestQueue = Volley.newRequestQueue(MainActivity.this);
        progressDialog = new ProgressDialog(MainActivity.this);
        //programar oyente PARA EVENTO DE CLIC
        btnAcceder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //METODO PORQUE EL CICLO DE VIDA NO PERMITE CIERTAS FUNCIONES
                Acceder();
            }
        });
        etRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Registrar();
            }
        });
        //PASAR A LA VALIDACION DE DATOS
        //CREACION DE SHARED PREFERENCES
        CrearSharedPreferences();
    }
    // FUNCION AL HACER CLIC EN EL BOTON ES PRIVADA PORQUE NO ES DE INTERES QUE OTRAS APLICACIONES ACCEDAN
    //SIN PARAMETROS NI RETORNOS- SE PROGRAMA EL INTENT PARA ENLAZARA A LA OTRA ACTIVITY
    private void Acceder(){
        e = etUsuario.getText().toString();
        p = etPassword.getText().toString();
        if(e.isEmpty() || p.isEmpty())
            Toast.makeText(getApplicationContext(),"Debes introducir los dos campos", Toast.LENGTH_LONG).show();
        else{
            //Mostrar el progressDialog
            progressDialog.setMessage("Procesando...");
            progressDialog.show();

            //Creacion de la cadena a  ejecutar en el web service mediante Volley
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
                            String IdUsuario = obj.getString("Id");
                            String Usuario = obj.getString("Usuario");
                            String Correo = obj.getString("Correo");
                            //AQUI SE DEBE PONER  LAS VALIDACIONES
                            boolean co = esCorreoValido(e);
                            if (co){
                                Toast.makeText(getApplicationContext(),"Acceso correcto", Toast.LENGTH_LONG).show();
                                SharedPreferences sp2 = getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
                                boolean ingreso = true;
                                sp2.edit().putBoolean("ingreso",ingreso).commit();
                                sp2.edit().putString("IdUsuario",IdUsuario).commit();
                                sp2.edit().putString("Usuario",Usuario).commit();
                                sp2.edit().putString("Correo",Correo).commit();
                                Intent intent1 = new Intent(getApplicationContext(),InicioActivity.class);
                                startActivity(intent1);  //MANDARLO A EJECUTAR
                            }
                        }
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                }
            },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //Ocultar el progressDialog
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_LONG).show();
                        }
                    }){
                protected Map<String, String> getParams(){
                    Map<String,String> parametros = new HashMap<>();
                    parametros.put("Correo",e);
                    parametros.put("Contrasena",p);
                    return parametros;
                }
            } ;
            requestQueue.add(stringRequest);
        }
    }
    private void CrearSharedPreferences(){
        SharedPreferences prefer1 = getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefer1.edit(); //activar edicion
        editor.putString("IdUsuario", "id");
        editor.putString("Correo", "correo");
        editor.putString("Usuario", "Usuario");
        editor.putBoolean("ingreso",false);
        editor.commit();
    }
    //MÉTODOS DE LA VALIDACION
    private boolean esCorreoValido(String correo) {
        if (!Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
            til_usuario.setError("Correo electrónico inválido");
            return false;
        } else {
            til_usuario.setError(null);
        }

        return true;
    }
    public void verificar(){
        SharedPreferences sp1 = getSharedPreferences("MisPreferencias",Context.MODE_PRIVATE);
        Boolean ingreso = sp1.getBoolean("ingreso", false);
        if (ingreso.equals(true)){
            Intent intent2 = new Intent(getApplicationContext(),InicioActivity.class);
            startActivity(intent2);
        }
    }
    private void Registrar(){
        Intent intent2 = new Intent(getApplicationContext(),RegistroActivity.class);
        startActivity(intent2);
    }
}
