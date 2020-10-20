package com.vero.photoqueen;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.vero.photoqueen.data.SharedPreferencesConfig;
import com.vero.photoqueen.utils.Constants;

public class InfoFragment extends Fragment {

    private TextView tvNombre;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_info, container, false);

        tvNombre = (TextView) view.findViewById(R.id.tvNombree);

        if (getContext() != null) {
            SharedPreferences sharedPreferences = SharedPreferencesConfig.getPreferences(getContext());
            String nombre = sharedPreferences.getString(Constants.USER, "");
            tvNombre.setText(nombre);
        }


        return view;
    }

}
