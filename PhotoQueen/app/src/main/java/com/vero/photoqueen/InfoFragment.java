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

public class InfoFragment extends Fragment {
    TextView Nombre;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_info, container,false);

        Nombre =(TextView) view.findViewById(R.id.tvNombree);
        SharedPreferences sp2 = this.getActivity().getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
        String   NOMBRE =sp2.getString("Usuario","");
        Nombre.setText(NOMBRE);
        return view;
    }

}
