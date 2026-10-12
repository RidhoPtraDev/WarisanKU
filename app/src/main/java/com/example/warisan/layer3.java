package com.example.warisan;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class layer3 extends AppCompatActivity {

    private static final String TAG = "layer3";
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inisialisasi Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Cek apakah sudah login
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Log.e(TAG, "currentUser is null, redirecting to layer2");
            startActivity(new Intent(layer3.this, layer2.class));
            finish();
            return;
        } else {
            Log.d(TAG, "currentUser found, UID: " + currentUser.getUid() + ", Email: " + currentUser.getEmail());
        }

        setContentView(R.layout.layer3);

        // Inisialisasi komponen lainnya
        ImageView icProfile = findViewById(R.id.ic_profile);
        if (icProfile != null) {
            icProfile.setOnClickListener(v -> {
                Intent intent = new Intent(layer3.this, layer11.class);
                startActivity(intent);
            });
        } else {
            Log.e(TAG, "ic_profile not found in layout! Check layer3.xml ID");
        }

        Button btnMulaiPerhitungan = findViewById(R.id.btnMulaiPerhitungan);
        if (btnMulaiPerhitungan != null) {
            btnMulaiPerhitungan.setOnClickListener(v -> {
                Intent intent = new Intent(layer3.this, layer4.class);
                startActivity(intent);
            });
        } else {
            Log.e(TAG, "btnMulaiPerhitungan not found in layout! Check layer3.xml ID");
        }

        Button btnPanduanIslam = findViewById(R.id.btnPanduanIslam);
        if (btnPanduanIslam != null) {
            btnPanduanIslam.setOnClickListener(v -> {
                Intent intent = new Intent(layer3.this, layer9.class);
                startActivity(intent);
            });
        } else {
            Log.e(TAG, "btnPanduanIslam not found in layout! Check layer3.xml ID");
        }

        Button btnRiwayatPerhitungan = findViewById(R.id.btnRiwayatPerhitungan);
        if (btnRiwayatPerhitungan != null) {
            btnRiwayatPerhitungan.setOnClickListener(v -> {
                Intent intent = new Intent(layer3.this, layer8.class);
                startActivity(intent);
            });
        } else {
            Log.e(TAG, "btnRiwayatPerhitungan not found in layout! Check layer3.xml ID");
        }
    }
}