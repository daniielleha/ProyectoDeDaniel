package com.vero.photoqueen;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class AlbumFragment extends Fragment {
    ArrayList<Integer> mImageIds = new ArrayList< >(Arrays.asList(
            R.id.imagenid
    ));
    ImageView imagen;
    Context thiscontext;
    String id, URL;
    //Una animacion de carga para la comunicacion del web service
    ProgressDialog progressDialog;
    //Almacenar la cadena
    RequestQueue requestQueue; String HttpURI = "http://192.168.1.74/cabina/public/andro-foto";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_album, container,false);

        thiscontext = container.getContext();
        GridView gridView = view.findViewById(R.id.myGrid);

        gridView.setAdapter(new ImageAdaptor(mImageIds,thiscontext));

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int item_pos = mImageIds.get(position);
                //ShowDialogBox(item_pos);
            }
        });
        // Inicializar a requestqueue y el progressDialog
        requestQueue = Volley.newRequestQueue(thiscontext); progressDialog = new ProgressDialog(thiscontext);

        traerimagen1();
        imagen = (ImageView) view.findViewById(R.id.imagenid);
        return view;
    }

    public  void traerimagen1() {
        //Tomar el valor escrito por el usuario
        SharedPreferences sp2 = this.getActivity().getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
        id =sp2.getString("IdUsuario","");
        if(id.isEmpty() )
            Toast.makeText(thiscontext.getApplicationContext(),"Debes llenar todos los campos", Toast.LENGTH_LONG).show();
        else { //Mostrar el progressDialog
            progressDialog.setMessage("Procesando...");
            progressDialog.show(); //Creacion de la cadena a  ejecutar en el web service mediante Volley
            StringRequest stringRequest = new StringRequest(Request.Method.POST, HttpURI, new Response.Listener<String>() {
                @Override
                public void onResponse(String serverResponse) {
                    //Ocultar el progressDialog
                    progressDialog.dismiss();
                    try {
                        JSONObject obj = new JSONObject(serverResponse);
                        String mensaje = obj.getString("mensaje");

                        String URLIMAGEN = "http://192.168.1.74/cabina/public/"+mensaje;

                        cargarWebServiceImagen(URLIMAGEN);
                        // Toast.makeText(thiscontext.getApplicationContext(),URLIMAGEN, Toast.LENGTH_LONG).show();
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                }
            },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) { //Ocultar el progressDialog
                            progressDialog.dismiss();
                           // Toast.makeText(thiscontext.getApplicationContext(),error.toString(),Toast.LENGTH_LONG).show();
                        }
                    }){
                protected Map<String, String> getParams(){
                    Map<String,String> parametros = new HashMap<>();
                    parametros.put("IdUsuario",id);
                    return parametros;
                }
            } ;
            requestQueue.add(stringRequest);
        }
    }
    private void cargarWebServiceImagen(String URLIMAGEN){
        URLIMAGEN = URLIMAGEN.replace(" ","%20");

        ImageRequest imageRequest = new ImageRequest(URLIMAGEN, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                imagen.setImageBitmap(response);
            }
        }, 0, 0, ImageView.ScaleType.CENTER, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Toast.makeText(thiscontext.getApplicationContext(),error.toString(),Toast.LENGTH_LONG).show();
            }
        });
        requestQueue.add(imageRequest);
    }


    public void ShowDialogBox(final int item_pos){
        final Dialog dialog = new Dialog(thiscontext);

        dialog.setContentView(R.layout.custom_dialog);

        //Getting custom dialog views
        TextView Image_name = dialog.findViewById(R.id.txt_Image_name);
        ImageView Image = dialog.findViewById(R.id.img);
        Button btn_Full = dialog.findViewById(R.id.btn_full);
        Button btn_Close = dialog.findViewById(R.id.btn_close);
        String title = getResources().getResourceName(item_pos);

        //extracting name
        int index = title.indexOf("/");
        String name = title.substring(index+1,title.length());
        Image_name.setText(name);

        Image.setImageResource(item_pos);

        btn_Close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btn_Full.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(thiscontext, full_view.class);
                i.putExtra("img_id", item_pos);
                startActivity(i);
            }
        });

        dialog.show();

    }
}
