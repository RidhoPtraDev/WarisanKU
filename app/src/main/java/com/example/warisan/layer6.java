package com.example.warisan;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;
import java.text.NumberFormat;
import java.util.Locale;

public class layer6 extends AppCompatActivity {

    CheckBox cbRumah, cbKendaraan, cbTabungan, cbTanah, cbPerhiasan, cbPerusahaan;
    LinearLayout layoutRumah, layoutKendaraan, layoutTabungan, layoutPerusahaan, layoutTanah, layoutPerhiasan;
    LinearLayout layoutMobil, layoutMotor;
    EditText etRumahNilaiAset, etRumahHargaPasar,
            etMobilNilaiAset, etMobilHargaPasar,
            etMotorNilaiAset, etMotorHargaPasar,
            etTabungan,
            etTanahNilaiAset, etTanahHargaPasar,
            etPerhiasan,
            etPerusahaanNilaiAset;
    CheckBox cbMobil, cbMotor;
    TextView tvTotalKekayaan;
    Button btnLanjut, btnKembali;

    boolean nenekHidup, kakekHidup, ayahHidup, ibuHidup;
    int jumlahAnakL, jumlahAnakP;
    String jenisKelamin, statusPernikahan;

    private NumberFormat rupiahFormat = NumberFormat.getNumberInstance(new Locale("in", "ID"));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layer6);

        Intent intentFrom5 = getIntent();
        nenekHidup = intentFrom5.getBooleanExtra("nenekHidup", false);
        kakekHidup = intentFrom5.getBooleanExtra("kakekHidup", false);
        ayahHidup = intentFrom5.getBooleanExtra("ayahHidup", false);
        ibuHidup = intentFrom5.getBooleanExtra("ibuHidup", false);
        jumlahAnakL = intentFrom5.getIntExtra("saudaraL", 0);
        jumlahAnakP = intentFrom5.getIntExtra("saudaraP", 0);
        jenisKelamin = intentFrom5.getStringExtra("jenisKelamin");
        statusPernikahan = intentFrom5.getStringExtra("statusPernikahan");

        // Log data yang diterima
        Log.d("Layer6Debug", "jenisKelamin: " + jenisKelamin);
        Log.d("Layer6Debug", "statusPernikahan: " + statusPernikahan);
        Log.d("Layer6Debug", "ayahHidup: " + ayahHidup);
        Log.d("Layer6Debug", "ibuHidup: " + ibuHidup);
        Log.d("Layer6Debug", "saudaraL: " + jumlahAnakL);
        Log.d("Layer6Debug", "saudaraP: " + jumlahAnakP);

        // Inisialisasi komponen
        cbRumah = findViewById(R.id.cbRumah);
        cbKendaraan = findViewById(R.id.cbKendaraan);
        cbTabungan = findViewById(R.id.cbTabungan);
        cbTanah = findViewById(R.id.cbTanah);
        cbPerhiasan = findViewById(R.id.cbPerhiasan);
        cbPerusahaan = findViewById(R.id.cbPerusahaan);

        layoutRumah = findViewById(R.id.layoutRumah);
        layoutKendaraan = findViewById(R.id.layoutKendaraan);
        layoutTabungan = findViewById(R.id.layoutTabungan);
        layoutTanah = findViewById(R.id.layoutTanah);
        layoutPerhiasan = findViewById(R.id.layoutPerhiasan);
        layoutPerusahaan = findViewById(R.id.layoutPerusahaan);
        layoutMobil = findViewById(R.id.layoutMobil);
        layoutMotor = findViewById(R.id.layoutMotor);

        etRumahNilaiAset = findViewById(R.id.etRumahNilaiAset);
        etRumahHargaPasar = findViewById(R.id.etRumahHargaPasar);
        etMobilNilaiAset = findViewById(R.id.etMobilNilaiAset);
        etMobilHargaPasar = findViewById(R.id.etMobilHargaPasar);
        etMotorNilaiAset = findViewById(R.id.etMotorNilaiAset);
        etMotorHargaPasar = findViewById(R.id.etMotorHargaPasar);
        etTabungan = findViewById(R.id.etTabungan);
        etTanahNilaiAset = findViewById(R.id.etTanahNilaiAset);
        etTanahHargaPasar = findViewById(R.id.etTanahHargaPasar);
        etPerhiasan = findViewById(R.id.etPerhiasan);
        etPerusahaanNilaiAset = findViewById(R.id.etPerusahaanNilaiAset);

        cbMobil = findViewById(R.id.rbMobil);
        cbMotor = findViewById(R.id.rbMotor);

        tvTotalKekayaan = findViewById(R.id.tvTotalKekayaan);
        btnLanjut = findViewById(R.id.btnLanjutLayer6);
        btnKembali = findViewById(R.id.btnKembali);

        // Error handling untuk view yang null
        if (cbRumah == null || cbKendaraan == null || cbTabungan == null || cbTanah == null ||
                cbPerhiasan == null || cbPerusahaan == null || layoutRumah == null ||
                layoutKendaraan == null || layoutTabungan == null || layoutTanah == null ||
                layoutPerhiasan == null || layoutPerusahaan == null || layoutMobil == null ||
                layoutMotor == null || cbMobil == null || cbMotor == null ||
                tvTotalKekayaan == null || btnLanjut == null || btnKembali == null) {
            Log.e("Layer6Debug", "One or more views are null, check XML IDs");
            Toast.makeText(this, "Initialization failed: Missing UI components", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        // Debugging log untuk CheckBox kendaraan
        Log.d("Layer6Debug", "cbMobil initialized: " + (cbMobil != null));
        Log.d("Layer6Debug", "cbMotor initialized: " + (cbMotor != null));

        toggleLayout(cbRumah, layoutRumah);
        toggleLayout(cbKendaraan, layoutKendaraan);
        toggleLayout(cbTabungan, layoutTabungan);
        toggleLayout(cbTanah, layoutTanah);
        toggleLayout(cbPerhiasan, layoutPerhiasan);
        toggleLayout(cbPerusahaan, layoutPerusahaan);

        // Tambah TextWatcher untuk format rupiah di EditText
        setupRupiahFormatter(etRumahNilaiAset);
        setupRupiahFormatter(etRumahHargaPasar);
        setupRupiahFormatter(etMobilNilaiAset);
        setupRupiahFormatter(etMobilHargaPasar);
        setupRupiahFormatter(etMotorNilaiAset);
        setupRupiahFormatter(etMotorHargaPasar);
        setupRupiahFormatter(etTabungan);
        setupRupiahFormatter(etTanahNilaiAset);
        setupRupiahFormatter(etTanahHargaPasar);
        setupRupiahFormatter(etPerhiasan);
        setupRupiahFormatter(etPerusahaanNilaiAset);

        btnKembali.setOnClickListener(v -> finish());

        btnLanjut.setOnClickListener(v -> {
            double total_kekayaan = (double) calculateTotalKekayaan();
            Intent intent = new Intent(layer6.this, layer7.class);
            intent.putExtra("totalKekayaan", total_kekayaan);
            intent.putExtra("jenisKelamin", jenisKelamin);
            intent.putExtra("statusPernikahan", statusPernikahan);
            intent.putExtra("ayahHidup", ayahHidup);
            intent.putExtra("ibuHidup", ibuHidup);
            intent.putExtra("kakekHidup", kakekHidup);
            intent.putExtra("nenekHidup", nenekHidup);
            intent.putExtra("saudaraL", jumlahAnakL);
            intent.putExtra("saudaraP", jumlahAnakP);
            intent.putExtra("jenisHarta", getJenisHartaDipilih());

            // Log data yang dikirim
            Log.d("Layer6Debug", "Sending to layer7 - totalKekayaan: " + total_kekayaan);
            Log.d("Layer6Debug", "Sending to layer7 - statusPernikahan: " + statusPernikahan);
            startActivity(intent);
        });

        cbKendaraan.setOnCheckedChangeListener((buttonView, isChecked) -> {
            layoutKendaraan.setVisibility(isChecked ? View.VISIBLE : View.GONE);
            if (!isChecked) {
                cbMobil.setChecked(false);
                cbMotor.setChecked(false);
                layoutMobil.setVisibility(View.GONE);
                layoutMotor.setVisibility(View.GONE);
            }
            calculateAndDisplayTotal();
        });

        cbMobil.setOnCheckedChangeListener((buttonView, isChecked) -> {
            layoutMobil.setVisibility(isChecked ? View.VISIBLE : View.GONE);
            calculateAndDisplayTotal();
        });

        cbMotor.setOnCheckedChangeListener((buttonView, isChecked) -> {
            layoutMotor.setVisibility(isChecked ? View.VISIBLE : View.GONE);
            calculateAndDisplayTotal();
        });
    }

    private void toggleLayout(CheckBox checkBox, LinearLayout layout) {
        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            layout.setVisibility(isChecked ? View.VISIBLE : View.GONE);
            calculateAndDisplayTotal();
        });
    }

    private void setupRupiahFormatter(EditText editText) {
        editText.addTextChangedListener(new TextWatcher() {
            private String current = "";

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Tidak perlu aksi khusus
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Tidak perlu aksi khusus di sini
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().equals(current)) {
                    editText.removeTextChangedListener(this); // Hindari loop
                    String cleanString = s.toString().replaceAll("[Rp,.\\s]", "");
                    if (!cleanString.isEmpty()) {
                        try {
                            long parsed = Long.parseLong(cleanString);
                            String formatted = rupiahFormat.format(parsed);
                            current = formatted;
                            editText.setText(formatted);
                            editText.setSelection(formatted.length()); // Posisi kursor di akhir
                        } catch (NumberFormatException e) {
                            current = "";
                            editText.setText("");
                        }
                    } else {
                        current = "";
                        editText.setText("");
                    }
                    editText.addTextChangedListener(this); // Tambah kembali listener
                    calculateAndDisplayTotal(); // Update total
                }
            }
        });
    }

    private void calculateAndDisplayTotal() {
        long total = calculateTotalKekayaan();
        String rupiahFormatted = "Rp " + rupiahFormat.format(total);
        tvTotalKekayaan.setText("Total kekayaan " + rupiahFormatted);
    }

    private long calculateTotalKekayaan() {
        long total = 0;

        if (cbRumah != null && cbRumah.isChecked()) total += getLongFromEditText(etRumahNilaiAset);
        if (cbKendaraan != null && cbKendaraan.isChecked()) {
            if (cbMobil != null && cbMobil.isChecked()) total += getLongFromEditText(etMobilNilaiAset);
            if (cbMotor != null && cbMotor.isChecked()) total += getLongFromEditText(etMotorNilaiAset);
        }
        if (cbTabungan != null && cbTabungan.isChecked()) total += getLongFromEditText(etTabungan);
        if (cbTanah != null && cbTanah.isChecked()) total += getLongFromEditText(etTanahNilaiAset);
        if (cbPerhiasan != null && cbPerhiasan.isChecked()) total += getLongFromEditText(etPerhiasan);
        if (cbPerusahaan != null && cbPerusahaan.isChecked()) total += getLongFromEditText(etPerusahaanNilaiAset);

        return total;
    }

    private long getLongFromEditText(EditText editText) {
        if (editText == null) return 0;
        String text = editText.getText().toString().trim().replaceAll("[Rp,.\\s]", "");
        if (text.isEmpty()) return 0;

        try {
            return Long.parseLong(text);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private String getJenisHartaDipilih() {
        StringBuilder jenis = new StringBuilder();
        if (cbRumah != null && cbRumah.isChecked()) jenis.append("Rumah, ");
        if (cbKendaraan != null && cbKendaraan.isChecked()) {
            if (cbMobil != null && cbMobil.isChecked()) jenis.append("Mobil, ");
            if (cbMotor != null && cbMotor.isChecked()) jenis.append("Motor, ");
        }
        if (cbTabungan != null && cbTabungan.isChecked()) jenis.append("Tabungan, ");
        if (cbTanah != null && cbTanah.isChecked()) jenis.append("Tanah, ");
        if (cbPerhiasan != null && cbPerhiasan.isChecked()) jenis.append("Perhiasan, ");
        if (cbPerusahaan != null && cbPerusahaan.isChecked()) jenis.append("Perusahaan, ");

        if (jenis.length() > 0) {
            jenis.setLength(jenis.length() - 2);
        } else {
            jenis.append("-");
        }
        return jenis.toString();
    }
}