package com.vero.photoqueen.utils;


import android.content.Context;

/**
 * En esta clase se declaran todas las variables contantes
 * para que solo se modifiquen desde aquí y no se busquen
 * en tdo el proyecto
 */
public class Constants {
    public static String NAME_SHARED_PREFERENCES = "MisPreferencias";
    public static String USER = "Usuario";
    public static String MAIL = "Correo";
    public static String INGRESO = "ingreso";

    public static String END_POINT_URL_REGISTER = "http://192.168.1.74/cabina/public/andro-registro";
    public static String END_POINT_URL_PHOTO = "http://192.168.1.74/cabina/public/andro-foto";

    public static int MODE_PRIVATE = Context.MODE_PRIVATE;
}
