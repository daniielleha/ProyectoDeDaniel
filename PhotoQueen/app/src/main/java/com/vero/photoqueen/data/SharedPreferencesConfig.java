package com.vero.photoqueen.data;

import android.content.Context;
import android.content.SharedPreferences;

import com.vero.photoqueen.utils.Constants;

/**
 * Esta clase provee las SharedPreferences con Inyección de dependencias manual
 * pasándole el contexto como helper para obtener el acceso a la clase padre de Android
 */

// TODO: Importante, estudiar Dependency Injection y entender por qué solo se
//  debe llamar una vez la creación de SharedPreferences

    // Se creó la nueva rama DEVELOP

public class SharedPreferencesConfig {

    public static SharedPreferences getPreferences(Context context){
        return context.getSharedPreferences(Constants.NAME_SHARED_PREFERENCES, Constants.MODE_PRIVATE);
    }
}
