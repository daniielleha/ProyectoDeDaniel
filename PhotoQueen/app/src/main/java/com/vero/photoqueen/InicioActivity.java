package com.vero.photoqueen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.vero.photoqueen.data.SharedPreferencesConfig;
import com.vero.photoqueen.utils.Constants;
import com.vero.photoqueen.utils.Toolbox;

public class InicioActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private SharedPreferences sharedPreferences;
    private DrawerLayout drawer;
    private TextView tvUsuario, tvCorreo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);

        tvUsuario = (TextView) findViewById(R.id.tvUsuario);
        tvCorreo = (TextView) findViewById(R.id.tvcorreo);

        // La clase {Constants} te provee de esas variables
        sharedPreferences = SharedPreferencesConfig.getPreferences(getApplicationContext());

        // ELIMINAR: Recuerda que las variables que declares siempre tienen que iniciar con minúscula
        String usuario = sharedPreferences.getString(Constants.USER,"");
        String correo = sharedPreferences.getString(Constants.MAIL,"");

        tvUsuario.setText(usuario);
        tvCorreo.setText(correo);

        // Activar la accion del menu
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Drawer
        drawer = findViewById(R.id.drawer_layout);

        // Activar fragments
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Investigar para que es Toggle
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new InfoFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_info);
        }
    }

    //  implementar Navigation Item Selected
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_album:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AlbumFragment()).commit();
                break;
            case R.id.nav_foto:
                Intent intent1 = new Intent(getApplicationContext(),FotoActivity.class);
                startActivity(intent1);
                break;
            case R.id.nav_cita:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CitaFragment()).commit();
                break;
            case R.id.nav_info:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new InfoFragment()).commit();
                break;
            case R.id.nav_share:
                sendEmail();
                break;

        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //Aparicion del menu en su transisicion
    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();
        }
    }
    // enlazar menu, por lo que se deben sobreescribir dos funciones o metodos
    @Override // 1 para detectar las opciones del menu, enlazar el menu con la programacion
    public boolean onCreateOptionsMenu(Menu menu) {
        //enlazar menu con programacion de la vista
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_items, menu);
        return true;
    }

    //2 metodo seleccion de items en el menu- su comportamiento
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.itemCerrarS:
                cerrarSesion();
                break;
            case R.id.itemSalir:
                // Finalizar la aplicacion no importa si es una aplicacion secundaria
                finishAffinity();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void cerrarSesion(){
        sharedPreferences.edit().putBoolean(Constants.INGRESO, false).apply();

        Toolbox.createToast(getApplicationContext(), "Adios", false);

        Intent irMainActivity = new Intent(this, MainActivity.class);
        startActivity(irMainActivity);
    }

    protected void sendEmail() {
        String[] TO = {"citlalli_bl@tesch.edu.mx"}; //Direcciones email  a enviar.
        String[] CC = {""}; //Direcciones email con copia.

        Intent emailIntent = new Intent(Intent.ACTION_SEND);

        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_CC, CC);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Aplicacion");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "¡Cuentanos tu experiencia!   "); // * configurar email aquí!

        try {
            startActivity(Intent.createChooser(emailIntent, "Enviar email."));
            Log.i("EMAIL", "Enviando email...");
        }
        catch (android.content.ActivityNotFoundException e) {
            Toolbox.createToast(this, "N existe ningún cliente de email instalado!.", false);
        }
    }

}
