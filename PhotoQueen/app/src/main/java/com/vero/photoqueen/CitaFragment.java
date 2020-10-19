package com.vero.photoqueen;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class CitaFragment extends DialogFragment implements View.OnClickListener {
    Button  bcita;
    ImageView bfecha,bhora;
    EditText efechaa, efecha,ehora,edireccion;
    Spinner spLista;
    String f,h, a, p, id;
    private  int dia,mes,ano,hora,minutos;
    Context thiscontext;
    //Una animacion de carga para la comunicacion del web service
    ProgressDialog progressDialog;
    //Almacenar la cadena
    RequestQueue requestQueue; String HttpURI = "http://192.168.1.74/cabina/public/andro-spaquete";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cita, container,false);
        thiscontext = container.getContext();
        bfecha=(ImageView) view.findViewById(R.id.bfecha);
        bhora=(ImageView) view.findViewById(R.id.bhora);
        efecha=(EditText)view.findViewById(R.id.efecha);
        efechaa=(EditText)view.findViewById(R.id.efechaa);
        edireccion=(EditText)view.findViewById(R.id.edireccion);
        ehora=(EditText)view.findViewById(R.id.ehora);
        bcita=(Button) view.findViewById(R.id.bcita);
        bfecha.setOnClickListener(this);
        bhora.setOnClickListener(this);
        spLista =(Spinner) view.findViewById(R.id.spLista);
        //Para poder usar el archivo array
        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(thiscontext,R.array.color, android.R.layout.simple_spinner_item);
        spLista.setAdapter(adapter);
        // Inicializar a requestqueue y el progressDialog
        requestQueue = Volley.newRequestQueue(thiscontext); progressDialog = new ProgressDialog(thiscontext);

        bcita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrar();
            }
        });

        return view;
    }

    //FUNCION PRINCIPAL PARA LA VALIDACION
    private void registrar(){
        String itemLista = spLista.getSelectedItem().toString();
        String item = String.valueOf(spLista.getSelectedItemId());
        //Tomar el valor escrito por el usuario
        f = efechaa.getText().toString();
        h = ehora.getText().toString();
        a = edireccion.getText().toString();
        p = item;
        SharedPreferences sp2 = this.getActivity().getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
        id =sp2.getString("IdUsuario","");
        if(f.isEmpty() || h.isEmpty() || a.isEmpty() || id.isEmpty() )
            Toast.makeText(thiscontext.getApplicationContext(),"Debes llenar todos los campos", Toast.LENGTH_LONG).show();
        else { //Mostrar el progressDialog
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
                            Toast.makeText(thiscontext.getApplicationContext(),"estamos teniendo problemas", Toast.LENGTH_LONG).show();
                        else {
                            // OK, se pasa a la siguiente acción
                            Toast.makeText(thiscontext.getApplicationContext(), "Cita agregada", Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(thiscontext.getApplicationContext(),"Asegurate de haber elegido un paquete",Toast.LENGTH_LONG).show();
                        }
                    }){
                protected Map<String, String> getParams(){
                    Map<String,String> parametros = new HashMap<>();
                    parametros.put("Fecha",f);
                    parametros.put("Hora",h);
                    parametros.put("Asunto", a);
                    parametros.put("Paquete",p);
                    parametros.put("IdUsuario",id);
                    return parametros;
                }
            } ;
            requestQueue.add(stringRequest);
        }
    }




    @Override
    public void onClick(View v) {
        if(v==bfecha){
            final Calendar c= Calendar.getInstance();
            dia=c.get(Calendar.DAY_OF_MONTH);
            mes=c.get(Calendar.MONTH);
            ano=c.get(Calendar.YEAR);

            DatePickerDialog datePickerDialog = new DatePickerDialog(thiscontext, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    efecha.setText(+dayOfMonth+"-"+(monthOfYear+1)+"-"+year);
                    efechaa.setText(+year+"-"+(monthOfYear+1)+"-"+dayOfMonth);
                }
            }
                    ,ano,mes,dia);
            datePickerDialog.show();
        }
        if (v==bhora){
            final Calendar c= Calendar.getInstance();
            hora=c.get(Calendar.HOUR_OF_DAY);
            minutos=c.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(thiscontext, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    ehora.setText(hourOfDay+":"+minute);
                }
            },hora,minutos,false);
            timePickerDialog.show();
        }
    }
}
