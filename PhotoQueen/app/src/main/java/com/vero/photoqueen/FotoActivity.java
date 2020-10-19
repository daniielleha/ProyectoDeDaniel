package com.vero.photoqueen;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ViewSwitcher;

import java.util.Timer;
import java.util.TimerTask;

public class FotoActivity extends AppCompatActivity {

    private ImageSwitcher imageSwitcher;
    private int[] gallery = { R.drawable.imagen1, R.drawable.imagen2, R.drawable.imagen3,
            R.drawable.imagen4};
    private int position;
    private static final Integer DURATION = 2500;
    private Timer timer = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foto);

        // Primero activar la parte visual del boton PARA EL BOTON DE BACK O HOME
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        imageSwitcher = (ImageSwitcher) findViewById(R.id.imageSwitcher);
        imageSwitcher.setFactory(new ViewSwitcher.ViewFactory() {

            public View makeView() {
                return new ImageView(FotoActivity.this);
            }
        });

        // Set animations
        // https://danielme.com/2013/08/18/diseno-android-transiciones-entre-activities/
        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        Animation fadeOut = AnimationUtils.loadAnimation(this, R.anim.fade_out);
        imageSwitcher.setInAnimation(fadeIn);
        imageSwitcher.setOutAnimation(fadeOut);

        startSlider();
    }

    //CREAR SOBREESCRIBIR UN METODO
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed(); //Cambiar el retorno para mandarlo a la pantalla principal
        return true;
    }
    //Accion de Boton Stop
    public void stop(View button) {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    public void startSlider() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {

            public void run() {
                // avoid exception:
                // "Only the original thread that created a view hierarchy can touch its views"
                runOnUiThread(new Runnable() {
                    public void run() {
                        imageSwitcher.setImageResource(gallery[position]);
                        position++;
                        if (position == gallery.length) {
                            position = 0;
                        }
                    }
                });
            }

        }, 0, DURATION);
    }

}
