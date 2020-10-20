package com.vero.photoqueen;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class FullView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.full_view);
        ImageView imageView = findViewById(R.id.img_full);

        int idImg = getIntent().getExtras().getInt("img_id");
        imageView.setImageResource(idImg);
    }
}
