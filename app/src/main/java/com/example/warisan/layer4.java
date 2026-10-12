package com.example.warisan;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class layer4 extends AppCompatActivity {

    private RadioGroup rgJenisKelamin, rgPernikahan, rgOrangTua;
    private Button btnKembali, btnLanjut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layer4);

        // Inisialisasi komponen
        rgJenisKelamin = findViewById(R.id.rgJenisKelamin);
        rgPernikahan = findViewById(R.id.rgPernikahan);
        rgOrangTua = findViewById(R.id.rgOrangTua);
        btnKembali = findViewById(R.id.btnKembali);
        btnLanjut = findViewById(R.id.btnLanjut);

        btnKembali.setOnClickListener(v -> finish());

        // Otomatis sesuaikan pilihan pasangan berdasarkan jenis kelamin pewaris
        rgJenisKelamin.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton rbSuami = findViewById(R.id.rbSuami);
            RadioButton rbIstri = findViewById(R.id.rbIstri);
            if (checkedId == R.id.rbLaki) {
                if (rbSuami != null) rbSuami.setVisibility(android.view.View.GONE);
                if (rbIstri != null) rbIstri.setVisibility(android.view.View.VISIBLE);
                if (rgPernikahan.getCheckedRadioButtonId() == R.id.rbSuami) {
                    rgPernikahan.clearCheck();
                }
            } else if (checkedId == R.id.rbPerempuan) {
                if (rbSuami != null) rbSuami.setVisibility(android.view.View.VISIBLE);
                if (rbIstri != null) rbIstri.setVisibility(android.view.View.GONE);
                if (rgPernikahan.getCheckedRadioButtonId() == R.id.rbIstri) {
                    rgPernikahan.clearCheck();
                }
            }
        });

        btnLanjut.setOnClickListener(v -> {
            int selectedGenderId = rgJenisKelamin.getCheckedRadioButtonId();
            int selectedStatusId = rgPernikahan.getCheckedRadioButtonId();
            int selectedOrtuId = rgOrangTua.getCheckedRadioButtonId();

            if (selectedGenderId == -1 || selectedStatusId == -1 || selectedOrtuId == -1) {
                Toast.makeText(this, "Harap isi semua pilihan terlebih dahulu", Toast.LENGTH_SHORT).show();
                return;
            }

            RadioButton rbGender = findViewById(selectedGenderId);
            RadioButton rbStatus = findViewById(selectedStatusId);
            RadioButton rbOrtu = findViewById(selectedOrtuId);

            String jenisKelamin = rbGender.getText().toString().trim();
            String statusPernikahan = rbStatus.getText().toString().trim();
            String kondisiOrangTua = rbOrtu.getText().toString().trim();

            boolean ayahHidup = false;
            boolean ibuHidup = false;

            // Perbaikan logika kondisi orang tua
            switch (kondisiOrangTua.toLowerCase()) {
                case "ayah hidup, ibu meninggal":
                    ayahHidup = true;
                    ibuHidup = false;
                    break;
                case "ayah meninggal, ibu hidup":
                    ayahHidup = false;
                    ibuHidup = true;
                    break;
                case "keduanya hidup":
                    ayahHidup = true;
                    ibuHidup = true;
                    break;
                case "keduanya meninggal":
                    ayahHidup = false;
                    ibuHidup = false;
                    break;
                default:
                    Log.w("Layer4Debug", "Kondisi orang tua tidak dikenali: " + kondisiOrangTua);
                    break;
            }

            // Log data yang dikirim
            Log.d("Layer4Debug", "jenisKelamin: " + jenisKelamin);
            Log.d("Layer4Debug", "statusPernikahan: " + statusPernikahan);
            Log.d("Layer4Debug", "kondisiOrangTua: " + kondisiOrangTua);
            Log.d("Layer4Debug", "ayahHidup: " + ayahHidup);
            Log.d("Layer4Debug", "ibuHidup: " + ibuHidup);

            Intent intent = new Intent(layer4.this, layer5.class);
            intent.putExtra("jenisKelamin", jenisKelamin);
            intent.putExtra("statusPernikahan", statusPernikahan);
            intent.putExtra("ayahHidup", ayahHidup);
            intent.putExtra("ibuHidup", ibuHidup);
            startActivity(intent);
        });
    }
}