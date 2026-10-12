package com.example.warisan;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class layer5 extends AppCompatActivity {

    EditText etSaudaraLaki, etSaudaraPerempuan;
    Button btnKembali, btnLanjut;
    RadioGroup rgKakekNenek;

    String gender, statusPernikahan;
    boolean ayahHidup, ibuHidup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layer5);

        etSaudaraLaki = findViewById(R.id.etSaudaraLaki);
        etSaudaraPerempuan = findViewById(R.id.etSaudaraPerempuan);
        btnKembali = findViewById(R.id.btnKembali);
        btnLanjut = findViewById(R.id.btnLanjut);
        rgKakekNenek = findViewById(R.id.rgKakekNenek);

        Intent intent = getIntent();
        gender = intent.getStringExtra("jenisKelamin");
        statusPernikahan = intent.getStringExtra("statusPernikahan");
        ayahHidup = intent.getBooleanExtra("ayahHidup", false);
        ibuHidup = intent.getBooleanExtra("ibuHidup", false);

        // Log data yang diterima
        Log.d("Layer5Debug", "gender: " + gender);
        Log.d("Layer5Debug", "statusPernikahan: " + statusPernikahan);
        Log.d("Layer5Debug", "ayahHidup: " + ayahHidup);
        Log.d("Layer5Debug", "ibuHidup: " + ibuHidup);

        btnKembali.setOnClickListener(v -> finish());

        btnLanjut.setOnClickListener(v -> {
            String lakiStr = etSaudaraLaki.getText().toString().trim();
            String perempuanStr = etSaudaraPerempuan.getText().toString().trim();

            if (TextUtils.isEmpty(lakiStr) || TextUtils.isEmpty(perempuanStr)) {
                Toast.makeText(this, "Harap isi jumlah saudara laki-laki dan perempuan", Toast.LENGTH_SHORT).show();
                return;
            }

            int jumlahLaki = Integer.parseInt(lakiStr);
            int jumlahPerempuan = Integer.parseInt(perempuanStr);

            boolean nenekHidup = false;
            boolean kakekHidup = false;

            int selectedId = rgKakekNenek.getCheckedRadioButtonId();
            if (selectedId == -1) {
                Toast.makeText(this, "Harap pilih kondisi kakek dan nenek", Toast.LENGTH_SHORT).show();
                return;
            }

            if (selectedId == R.id.rbKakekHidupNenekMeninggal) {
                kakekHidup = true;
            } else if (selectedId == R.id.rbNenekHidupKakekMeninggal) {
                nenekHidup = true;
            } else if (selectedId == R.id.rbKeduanyaHidup) {
                nenekHidup = true;
                kakekHidup = true;
            }

            Intent nextIntent = new Intent(getApplicationContext(), layer6.class);
            nextIntent.putExtra("nenekHidup", nenekHidup);
            nextIntent.putExtra("kakekHidup", kakekHidup);
            nextIntent.putExtra("ayahHidup", ayahHidup);
            nextIntent.putExtra("ibuHidup", ibuHidup);
            nextIntent.putExtra("saudaraL", jumlahLaki);
            nextIntent.putExtra("saudaraP", jumlahPerempuan);
            nextIntent.putExtra("jenisKelamin", gender);
            nextIntent.putExtra("statusPernikahan", statusPernikahan);
            startActivity(nextIntent);

            // Log data yang dikirim
            Log.d("Layer5Debug", "Sending to layer6 - statusPernikahan: " + statusPernikahan);
        });
    }
}