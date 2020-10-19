package com.vero.photoqueen;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class full_view extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.full_view);
        ImageView imageView = findViewById(R.id.img_full);

        int img_id = getIntent().getExtras().getInt("img_id");
        imageView.setImageResource(img_id);
    }
}
