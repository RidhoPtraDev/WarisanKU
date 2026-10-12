package com.example.warisan;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class layer9 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layer9);

        Button btnKembaliBawah = findViewById(R.id.btn_kembali);

        View.OnClickListener kembaliListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        };

        btnKembaliBawah.setOnClickListener(kembaliListener);
    }
}