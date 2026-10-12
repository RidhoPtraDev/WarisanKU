package com.example.warisan;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class layer7 extends AppCompatActivity {

    private static final String TAG = "Layer7";
    private TextView tvGender, tvMaritalStatus, tvParentCondition, tvTotalWealth, tvSiblingCount;
    private TextView tvGrandfatherCondition, tvGrandmotherCondition, tvJenisHarta;
    private TableLayout tableResult;
    private Button btnSave, btnKembali;

    private String gender;
    private String statusPernikahan;
    private double totalKekayaan;
    private int totalAhliWaris;
    private boolean ayahHidup, ibuHidup, kakekHidup, nenekHidup;
    private int saudaraL, saudaraP;
    private String jenisHarta;
    private FirebaseFirestore db;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layer7);

        // Inisialisasi Firebase
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        if (db == null || auth == null) {
            Log.e(TAG, "Firestore atau Auth instance null");
            Toast.makeText(this, "Koneksi ke database gagal", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        Log.d(TAG, "Firestore dan Auth diinisialisasi");

        // Inisialisasi UI
        tvGender = findViewById(R.id.tvGender);
        tvMaritalStatus = findViewById(R.id.tvMaritalStatus);
        tvParentCondition = findViewById(R.id.tvParentCondition);
        tvTotalWealth = findViewById(R.id.tvTotalWealth);
        tvSiblingCount = findViewById(R.id.tvSiblingCount);
        tvGrandfatherCondition = findViewById(R.id.tvGrandfatherCondition);
        tvGrandmotherCondition = findViewById(R.id.tvGrandmotherCondition);
        tvJenisHarta = findViewById(R.id.tvJenisHarta);
        tableResult = findViewById(R.id.tableResult);
        btnSave = findViewById(R.id.btnSave);
        btnKembali = findViewById(R.id.btnKembali);

        // Validasi UI
        if (tvGender == null || tvMaritalStatus == null || tvParentCondition == null ||
                tvTotalWealth == null || tvSiblingCount == null || tvGrandfatherCondition == null ||
                tvGrandmotherCondition == null || tvJenisHarta == null || tableResult == null ||
                btnSave == null || btnKembali == null) {
            Log.e(TAG, "Salah satu view null, inisialisasi gagal");
            Toast.makeText(this, "Gagal inisialisasi UI", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Ambil data dari Intent
        Intent intent = getIntent();
        gender = intent.getStringExtra("jenisKelamin");
        statusPernikahan = intent.getStringExtra("statusPernikahan");
        jenisHarta = intent.getStringExtra("jenisHarta");
        saudaraL = intent.getIntExtra("saudaraL", 0);
        saudaraP = intent.getIntExtra("saudaraP", 0);
        totalKekayaan = intent.getDoubleExtra("totalKekayaan", 0.0);
        nenekHidup = intent.getBooleanExtra("nenekHidup", false);
        kakekHidup = intent.getBooleanExtra("kakekHidup", false);
        ayahHidup = intent.getBooleanExtra("ayahHidup", false);
        ibuHidup = intent.getBooleanExtra("ibuHidup", false);

        // Validasi status pernikahan
        if ("Laki-laki".equals(gender) && "Ada suami".equals(statusPernikahan)) {
            statusPernikahan = "Ada istri"; // Perbaikan bug
            Log.w(TAG, "Status pernikahan salah, diperbaiki menjadi 'Ada istri' untuk pewaris laki-laki");
        } else if ("Perempuan".equals(gender) && "Ada istri".equals(statusPernikahan)) {
            statusPernikahan = "Ada suami"; // Perbaikan bug
            Log.w(TAG, "Status pernikahan salah, diperbaiki menjadi 'Ada suami' untuk pewaris perempuan");
        }

        Log.d(TAG, "Data Intent - gender: " + gender + ", statusPernikahan: " + statusPernikahan +
                ", totalKekayaan: " + totalKekayaan + ", saudaraL: " + saudaraL + ", saudaraP: " + saudaraP);

        // Set data ke TextView
        tvGender.setText("Jenis Kelamin Pewaris: " + (gender != null ? gender : "-"));
        tvMaritalStatus.setText("Status: " + (statusPernikahan != null ? statusPernikahan : "-"));
        tvParentCondition.setText("Kondisi Orang Tua: Ayah " + (ayahHidup ? "Hidup" : "Meninggal") +
                ", Ibu " + (ibuHidup ? "Hidup" : "Meninggal"));
        tvTotalWealth.setText("Total Harta: " + formatRupiah(totalKekayaan));
        tvSiblingCount.setText("Jumlah Anak: Laki-laki " + saudaraL + ", Perempuan " + saudaraP);
        tvGrandfatherCondition.setText("Kondisi Kakek: " + (kakekHidup ? "Hidup" : "Meninggal"));
        tvGrandmotherCondition.setText("Kondisi Nenek: " + (nenekHidup ? "Hidup" : "Meninggal"));
        tvJenisHarta.setText("Jenis Harta: " + (jenisHarta != null ? jenisHarta : "-"));

        // Hitung warisan
        totalAhliWaris = calculateFullWarisan(totalKekayaan, saudaraL, saudaraP, ayahHidup, ibuHidup,
                kakekHidup, nenekHidup, gender, statusPernikahan);
        Log.d(TAG, "Total Ahli Waris: " + totalAhliWaris);

        // Tombol Kembali
        btnKembali.setOnClickListener(v -> {
            Intent backIntent = new Intent(layer7.this, layer6.class);
            backIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(backIntent);
            finish();
        });

        // Tombol Simpan
        btnSave.setOnClickListener(v -> saveWarisanData());
    }

    private int calculateFullWarisan(double total, int laki, int perempuan, boolean ayah, boolean ibu,
                                     boolean kakek, boolean nenek, String genderPewaris, String statusPernikahan) {
        double sisa = total;
        int count = 0;
        Log.d(TAG, "Mulai perhitungan - total: " + formatRupiah(total) + ", laki: " + laki +
                ", perempuan: " + perempuan + ", genderPewaris: " + genderPewaris);

        if (total <= 0) {
            Log.w(TAG, "Total harta <= 0, kembalikan 0 ahli waris");
            Toast.makeText(this, "Total harta tidak valid", Toast.LENGTH_SHORT).show();
            return count;
        }

        int totalAnak = laki + perempuan;

        // 1. Porsi Pasangan (Suami / Istri)
        double porsiPasangan = 0.0;
        String namaPasangan = null;
        if ("Perempuan".equals(genderPewaris) && "Ada suami".equals(statusPernikahan)) {
            namaPasangan = "Suami";
            porsiPasangan = totalAnak > 0 ? 0.25 : 0.50; // 1/4 jika ada anak, 1/2 jika tidak
        } else if ("Laki-laki".equals(genderPewaris) && "Ada istri".equals(statusPernikahan)) {
            namaPasangan = "Istri";
            porsiPasangan = totalAnak > 0 ? 0.125 : 0.25; // 1/8 jika ada anak, 1/4 jika tidak
        }

        // 2. Porsi Orang Tua
        double porsiAyah = 0.0;
        double porsiIbu = 0.0;
        if (ayah) {
            // Jika ada anak, porsi pasti 1/6 (sisanya asabah nanti jika ada)
            // Jika tidak ada anak, ayah berstatus asabah penuh (atau mengambil sisa)
            porsiAyah = totalAnak > 0 ? (1.0 / 6.0) : 0.0;
        }
        if (ibu) {
            // 1/6 jika ada anak atau ada 2+ saudara, 1/3 jika tidak ada anak
            porsiIbu = totalAnak > 0 ? (1.0 / 6.0) : (1.0 / 3.0);
        }

        // 3. Porsi Kakek / Nenek (Hanya jika orang tua meninggal)
        double porsiKakek = 0.0;
        double porsiNenek = 0.0;
        if (!ayah && kakek) {
            porsiKakek = totalAnak > 0 ? (1.0 / 6.0) : 0.0;
        }
        if (!ibu && nenek) {
            porsiNenek = 1.0 / 6.0;
        }

        // Terapkan dzawil furudh terlebih dahulu
        if (namaPasangan != null && porsiPasangan > 0) {
            double bagianPasangan = total * porsiPasangan;
            addRow(namaPasangan, bagianPasangan, porsiPasangan);
            sisa -= bagianPasangan;
            count++;
        }

        if (porsiIbu > 0) {
            double bagianIbu = total * porsiIbu;
            addRow("Ibu", bagianIbu, porsiIbu);
            sisa -= bagianIbu;
            count++;
        }

        if (porsiNenek > 0) {
            double bagianNenek = total * porsiNenek;
            addRow("Nenek", bagianNenek, porsiNenek);
            sisa -= bagianNenek;
            count++;
        }

        if (porsiAyah > 0) {
            double bagianAyah = total * porsiAyah;
            addRow("Ayah", bagianAyah, porsiAyah);
            sisa -= bagianAyah;
            count++;
        }

        if (porsiKakek > 0) {
            double bagianKakek = total * porsiKakek;
            addRow("Kakek", bagianKakek, porsiKakek);
            sisa -= bagianKakek;
            count++;
        }

        // 4. Porsi Anak (Asabah bil ghair) atau Ayah/Kakek sebagai Asabah bin nafs jika tanpa anak
        if (totalAnak > 0 && sisa > 0) {
            if (laki > 0) {
                // Anak laki-laki ada -> rasio 2:1
                int totalBagian = (2 * laki) + perempuan;
                double perBagian = sisa / totalBagian;
                double lakiTotal = perBagian * (2 * laki);
                addRow("Anak Laki-laki (" + laki + ")", lakiTotal, lakiTotal / total);
                sisa -= lakiTotal;
                count += laki;

                if (perempuan > 0) {
                    double perempuanTotal = perBagian * perempuan;
                    addRow("Anak Perempuan (" + perempuan + ")", perempuanTotal, perempuanTotal / total);
                    sisa -= perempuanTotal;
                    count += perempuan;
                }
            } else if (perempuan > 0) {
                // Hanya anak perempuan (dzawil furudh: 1/2 jika 1 orang, 2/3 jika 2+)
                double porsiAnakP = perempuan == 1 ? 0.5 : (2.0 / 3.0);
                double bagianAnakP = Math.min(sisa, total * porsiAnakP);
                addRow("Anak Perempuan (" + perempuan + ")", bagianAnakP, bagianAnakP / total);
                sisa -= bagianAnakP;
                count += perempuan;

                // Sisa jatuh ke Ayah/Kakek sebagai asabah jika masih hidup
                if (ayah && sisa > 0) {
                    addRow("Ayah (Sisa Asabah)", sisa, sisa / total);
                    sisa = 0;
                } else if (kakek && sisa > 0) {
                    addRow("Kakek (Sisa Asabah)", sisa, sisa / total);
                    sisa = 0;
                }
            }
        } else if (totalAnak == 0) {
            // Tidak ada anak sama sekali -> Ayah / Kakek mengambil seluruh sisa sebagai Asabah
            if (ayah && sisa > 0) {
                addRow("Ayah (Asabah)", sisa, sisa / total);
                sisa = 0;
                count++;
            } else if (kakek && sisa > 0) {
                addRow("Kakek (Asabah)", sisa, sisa / total);
                sisa = 0;
                count++;
            }
        }

        // 5. Penyesuaian Sisa (Radd) ke Ahli Waris Non-Pasangan jika masih ada sisa
        if (sisa > 0.01 && tableResult.getChildCount() > 1) {
            // Distribusikan sisa ke baris terakhir
            TableRow lastRow = (TableRow) tableResult.getChildAt(tableResult.getChildCount() - 1);
            TextView tv1 = (TextView) lastRow.getChildAt(0);
            TextView tv2 = (TextView) lastRow.getChildAt(1);
            double jumlahLama = (double) tv2.getTag();
            double jumlahBaru = jumlahLama + sisa;
            tv2.setTag(jumlahBaru);
            tv2.setText(formatRupiah(jumlahBaru));
            double persenBaru = jumlahBaru / total;
            DecimalFormat df = new DecimalFormat("0.00", DecimalFormatSymbols.getInstance(Locale.US));
            TextView tv3 = (TextView) lastRow.getChildAt(2);
            tv3.setText("(" + df.format(persenBaru * 100) + "%)");
            Log.d(TAG, "Radd/Penyesuaian sisa harta: " + formatRupiah(sisa) + " ke " + tv1.getText() +
                    ", total baru: " + formatRupiah(jumlahBaru));
            sisa = 0;
        }

        if (sisa > 0) {
            Log.w(TAG, "Sisa harta tidak terbagi: " + formatRupiah(sisa));
            Toast.makeText(this, "Ada sisa harta: " + formatRupiah(sisa), Toast.LENGTH_LONG).show();
        }
        Log.d(TAG, "Akhir perhitungan - sisa: " + formatRupiah(sisa) + ", total ahli waris: " + count);
        return count;
    }

    private void addRow(String ahliWaris, double jumlah, double persen) {
        if (jumlah > 0) {
            TableRow row = new TableRow(this);
            row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

            TextView tv1 = new TextView(this);
            tv1.setText(ahliWaris);
            tv1.setTextColor(0xFF000000);
            tv1.setPadding(16, 16, 16, 16);
            tv1.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));

            TextView tv2 = new TextView(this);
            tv2.setTag(jumlah);
            tv2.setText(formatRupiah(jumlah));
            tv2.setTextColor(0xFF000000);
            tv2.setPadding(16, 16, 16, 16);
            tv2.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));

            DecimalFormat df = new DecimalFormat("0.00", DecimalFormatSymbols.getInstance(Locale.US));
            TextView tv3 = new TextView(this);
            tv3.setText("(" + df.format(persen * 100) + "%)");
            tv3.setTextColor(0xFF000000);
            tv3.setPadding(16, 16, 16, 16);
            tv3.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));

            row.addView(tv1);
            row.addView(tv2);
            row.addView(tv3);

            tableResult.addView(row);
            Log.d(TAG, "Baris ditambahkan: " + ahliWaris + ", jumlah: " + formatRupiah(jumlah) +
                    ", persen: " + (persen * 100) + "%");
        }
    }

    private void saveWarisanData() {
        if (auth.getCurrentUser() == null) {
            Log.e(TAG, "Pengguna belum login");
            Toast.makeText(this, "Silakan login terlebih dahulu", Toast.LENGTH_SHORT).show();
            return;
        }

        String userId = auth.getCurrentUser().getUid();
        Log.d(TAG, "Menyimpan data untuk userId: " + userId);

        Map<String, Object> warisanData = new HashMap<>();
        warisanData.put("tanggal", getCurrentDate());
        warisanData.put("gender", gender != null ? gender : "-");
        warisanData.put("totalKekayaan", totalKekayaan);
        warisanData.put("totalAhliWaris", totalAhliWaris);
        warisanData.put("ayahHidup", ayahHidup);
        warisanData.put("ibuHidup", ibuHidup);
        warisanData.put("saudaraL", saudaraL);
        warisanData.put("saudaraP", saudaraP);
        warisanData.put("statusPernikahan", statusPernikahan != null ? statusPernikahan : "-");
        warisanData.put("kakekHidup", kakekHidup);
        warisanData.put("nenekHidup", nenekHidup);
        warisanData.put("jenisHarta", jenisHarta != null ? jenisHarta : "-");

        Map<String, Map<String, Object>> detailPembagian = new HashMap<>();
        for (int i = 1; i < tableResult.getChildCount(); i++) {
            TableRow row = (TableRow) tableResult.getChildAt(i);
            if (row != null) {
                TextView tv1 = (TextView) row.getChildAt(0);
                TextView tv2 = (TextView) row.getChildAt(1);
                TextView tv3 = (TextView) row.getChildAt(2);
                if (tv1 != null && tv2 != null && tv3 != null) {
                    String ahliWaris = tv1.getText().toString();
                    double jumlah = (double) tv2.getTag();
                    String persenStr = tv3.getText().toString().replace("(", "").replace(")", "").replace("%", "").replace(",", ".");
                    double persen;
                    try {
                        persen = Double.parseDouble(persenStr);
                    } catch (NumberFormatException e) {
                        Log.e(TAG, "Gagal parsing persentase: " + persenStr, e);
                        persen = 0.0; // Nilai default jika parsing gagal
                    }

                    Map<String, Object> detail = new HashMap<>();
                    detail.put("jumlah", jumlah);
                    detail.put("persen", persen / 100.0); // Konversi ke desimal
                    detailPembagian.put(ahliWaris, detail);
                    Log.d(TAG, "Data disimpan: " + ahliWaris + ", jumlah: " + formatRupiah(jumlah) +
                            ", persen: " + (persen) + "%");
                }
            }
        }
        warisanData.put("detailPembagian", detailPembagian);

        db.collection("users")
                .document(userId)
                .collection("warisanHistory")
                .add(warisanData)
                .addOnSuccessListener(documentReference -> {
                    Log.d(TAG, "Data berhasil disimpan dengan ID: " + documentReference.getId());
                    Toast.makeText(this, "Data berhasil disimpan", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(layer7.this, layer3.class);
                    intent.putExtra("totalAhliWaris", totalAhliWaris);
                    startActivity(intent);
                    finish();
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Gagal menyimpan data: " + e.getMessage());
                    Toast.makeText(this, "Gagal menyimpan data: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
    }

    private String formatRupiah(double number) {
        DecimalFormat format = new DecimalFormat("#,###.00", DecimalFormatSymbols.getInstance(Locale.forLanguageTag("id-ID")));
        return "Rp " + format.format(number);
    }

    private String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault());
        return sdf.format(new Date());
    }
}