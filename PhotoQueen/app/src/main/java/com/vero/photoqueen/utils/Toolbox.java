package com.vero.photoqueen.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * En esta clase se declaran todos los métodos que se usan muchas veces
 * ejemplo: Toast
 */
public class Toolbox {

    public static void createToast(Context context, String txt, Boolean isLong) {
        if (isLong){
            Toast.makeText(context, txt, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(context, txt, Toast.LENGTH_SHORT).show();
        }
    }

}
