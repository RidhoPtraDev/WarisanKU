package com.example.warisan;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;

public class layer1 extends AppCompatActivity {

    private static final int SPLASH_TIME_OUT = 2000; // 2 detik

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layer1);

        new Handler().postDelayed(() -> {
            Intent intent = new Intent(layer1.this, layer2.class); // Pindah ke layer2
            startActivity(intent);
            finish(); // Hapus splash screen dari backstack
        }, SPLASH_TIME_OUT);
    }
}
