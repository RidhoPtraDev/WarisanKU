package com.example.warisan;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class layer8 extends AppCompatActivity {

    private static final String TAG = "Layer8";
    private Button btnKembaliBawah;
    private LinearLayout layoutRiwayat;
    private FirebaseFirestore db;
    private List<CardView> cardViews = new ArrayList<>();
    private boolean[] isExpanded;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layer8);

        if (FirebaseApp.getApps(this).isEmpty()) {
            Log.w(TAG, "Firebase not initialized, initializing now");
            FirebaseApp.initializeApp(this);
        } else {
            Log.d(TAG, "Firebase already initialized");
        }
        db = FirebaseFirestore.getInstance();
        if (db == null) {
            Log.e(TAG, "Firestore initialization failed");
            Toast.makeText(this, "Firestore tidak tersedia", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        btnKembaliBawah = findViewById(R.id.btn_kembali);
        layoutRiwayat = findViewById(R.id.layoutRiwayat);

        if (btnKembaliBawah == null) {
            Log.e(TAG, "btnKembaliBawah is null");
            Toast.makeText(this, "UI initialization failed", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        if (layoutRiwayat == null) {
            Log.e(TAG, "layoutRiwayat is null");
            Toast.makeText(this, "UI initialization failed", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        tampilkanRiwayat();

        btnKembaliBawah.setOnClickListener(v -> finish());
    }

    private void tampilkanRiwayat() {
        if (layoutRiwayat == null) {
            Log.e(TAG, "layoutRiwayat is null, cannot proceed");
            Toast.makeText(this, "Layout riwayat tidak tersedia", Toast.LENGTH_SHORT).show();
            return;
        }

        layoutRiwayat.removeAllViews();
        cardViews.clear();

        if (db == null) {
            Log.e(TAG, "Firestore is null, cannot fetch data");
            Toast.makeText(this, "Firestore tidak tersedia", Toast.LENGTH_SHORT).show();
            return;
        }

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        db.collection("users")
                .document(userId)
                .collection("warisanHistory")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (queryDocumentSnapshots.isEmpty()) {
                        Log.w(TAG, "No data found in warisanHistory for user: " + userId);
                        Toast.makeText(this, "Tidak ada riwayat tersedia", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    isExpanded = new boolean[queryDocumentSnapshots.size()];
                    int index = 0;

                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        final int finalIndex = index;
                        String tanggal = document.getString("tanggal");
                        String gender = document.getString("gender");
                        Double totalKekayaan = document.getDouble("totalKekayaan");
                        Long totalAhliWaris = document.getLong("totalAhliWaris");
                        Boolean ayahHidup = document.getBoolean("ayahHidup");
                        Boolean ibuHidup = document.getBoolean("ibuHidup");
                        Long saudaraL = document.getLong("saudaraL");
                        Long saudaraP = document.getLong("saudaraP");
                        String statusPernikahan = document.getString("statusPernikahan");
                        String jenisHarta = document.getString("jenisHarta");
                        @SuppressWarnings("unchecked")
                        Map<String, Map<String, Object>> detailPembagian = (Map<String, Map<String, Object>>) document.get("detailPembagian");

                        CardView cardView = new CardView(this);
                        if (cardView == null) {
                            Log.e(TAG, "Failed to create CardView for index " + index);
                            continue;
                        }
                        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                        );
                        cardParams.setMargins(16, 16, 16, 8);
                        cardView.setLayoutParams(cardParams);
                        cardView.setCardElevation(6);
                        cardView.setRadius(12);
                        cardView.setPadding(16, 16, 16, 16);
                        cardView.setCardBackgroundColor(Color.parseColor("#76A67B"));

                        LinearLayout cardContent = new LinearLayout(this);
                        if (cardContent == null) {
                            Log.e(TAG, "Failed to create cardContent for index " + index);
                            continue;
                        }
                        cardContent.setOrientation(LinearLayout.VERTICAL);
                        cardContent.setLayoutParams(new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                        ));
                        cardContent.setPadding(8, 8, 8, 8);

                        TextView tvTanggal = new TextView(this);
                        if (tvTanggal != null) {
                            tvTanggal.setText("Tanggal: " + (tanggal != null ? tanggal : "-"));
                            tvTanggal.setTextColor(Color.WHITE);
                            tvTanggal.setTextSize(16);
                            tvTanggal.setTypeface(null, Typeface.BOLD);
                            tvTanggal.setPadding(0, 0, 0, 8);
                        }

                        TextView tvPewaris = new TextView(this);
                        if (tvPewaris != null) {
                            tvPewaris.setText("Pewaris: " + (gender != null ? gender : "-"));
                            tvPewaris.setTextColor(Color.WHITE);
                            tvPewaris.setPadding(0, 0, 0, 4);
                        }

                        TextView tvTotalHarta = new TextView(this);
                        if (tvTotalHarta != null) {
                            tvTotalHarta.setText("Total Harta: Rp " + formatRupiah(totalKekayaan != null ? totalKekayaan : 0.0));
                            tvTotalHarta.setTextColor(Color.WHITE);
                            tvTotalHarta.setPadding(0, 0, 0, 4);
                        }

                        TextView tvJumlahAhliWaris = new TextView(this);
                        if (tvJumlahAhliWaris != null) {
                            tvJumlahAhliWaris.setText("Jumlah Ahli Waris: " + (totalAhliWaris != null ? totalAhliWaris : 0));
                            tvJumlahAhliWaris.setTextColor(Color.WHITE);
                        }

                        View separator = new View(this);
                        LinearLayout.LayoutParams separatorParams = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT, 1);
                        separatorParams.setMargins(0, 8, 0, 8);
                        separator.setLayoutParams(separatorParams);
                        separator.setBackgroundColor(Color.WHITE);

                        if (tvTanggal != null) cardContent.addView(tvTanggal);
                        if (tvPewaris != null) cardContent.addView(tvPewaris);
                        if (tvTotalHarta != null) cardContent.addView(tvTotalHarta);
                        if (tvJumlahAhliWaris != null) cardContent.addView(tvJumlahAhliWaris);
                        cardContent.addView(separator);

                        LinearLayout detailLayout = new LinearLayout(this);
                        if (detailLayout == null) {
                            Log.e(TAG, "Failed to create detailLayout for index " + index);
                            continue;
                        }
                        detailLayout.setOrientation(LinearLayout.VERTICAL);
                        detailLayout.setVisibility(View.GONE);
                        detailLayout.setPadding(16, 8, 16, 8);

                        TextView tvStatusPernikahan = new TextView(this);
                        if (tvStatusPernikahan != null) {
                            tvStatusPernikahan.setText("Status: " + (statusPernikahan != null ? statusPernikahan : "-"));
                            tvStatusPernikahan.setTextColor(Color.WHITE);
                            tvStatusPernikahan.setPadding(0, 0, 0, 4);
                        }

                        TextView tvKondisiOrangTua = new TextView(this);
                        if (tvKondisiOrangTua != null) {
                            tvKondisiOrangTua.setText("Kondisi Orang Tua: Ayah " + (ayahHidup != null && ayahHidup ? "Hidup" : "Meninggal") +
                                    ", Ibu " + (ibuHidup != null && ibuHidup ? "Hidup" : "Meninggal"));
                            tvKondisiOrangTua.setTextColor(Color.WHITE);
                            tvKondisiOrangTua.setPadding(0, 0, 0, 4);
                        }

                        TextView tvJumlahAnak = new TextView(this);
                        if (tvJumlahAnak != null) {
                            tvJumlahAnak.setText("Jumlah Anak: Laki-laki " + (saudaraL != null ? saudaraL : 0) + ", Perempuan " + (saudaraP != null ? saudaraP : 0));
                            tvJumlahAnak.setTextColor(Color.WHITE);
                            tvJumlahAnak.setPadding(0, 0, 0, 4);
                        }

                        TextView tvJenisHartaDetail = new TextView(this);
                        if (tvJenisHartaDetail != null) {
                            tvJenisHartaDetail.setText("Jenis Harta: " + (jenisHarta != null ? jenisHarta : "-"));
                            tvJenisHartaDetail.setTextColor(Color.WHITE);
                        }

                        if (detailPembagian != null) {
                            TextView tvDetailHeader = new TextView(this);
                            if (tvDetailHeader != null) {
                                tvDetailHeader.setText("Detail Pembagian:");
                                tvDetailHeader.setTextColor(Color.WHITE);
                                tvDetailHeader.setTextSize(16);
                                tvDetailHeader.setTypeface(null, Typeface.BOLD);
                                tvDetailHeader.setPadding(0, 8, 0, 4);
                                detailLayout.addView(tvDetailHeader);
                            }

                            for (Map.Entry<String, Map<String, Object>> entry : detailPembagian.entrySet()) {
                                String ahliWaris = entry.getKey();
                                Map<String, Object> detail = entry.getValue();
                                Double jumlah = (Double) detail.get("jumlah");
                                if (jumlah != null && jumlah > 0) {
                                    TextView tvDetail = new TextView(this);
                                    if (tvDetail != null) {
                                        tvDetail.setText("- " + ahliWaris + ": Rp " + formatRupiah(jumlah));
                                        tvDetail.setTextColor(Color.WHITE);
                                        tvDetail.setPadding(16, 0, 0, 4);
                                        detailLayout.addView(tvDetail);
                                    }
                                }
                            }
                        }

                        if (tvStatusPernikahan != null) detailLayout.addView(tvStatusPernikahan);
                        if (tvKondisiOrangTua != null) detailLayout.addView(tvKondisiOrangTua);
                        if (tvJumlahAnak != null) detailLayout.addView(tvJumlahAnak);
                        if (tvJenisHartaDetail != null) detailLayout.addView(tvJenisHartaDetail);

                        cardContent.addView(detailLayout);
                        cardView.addView(cardContent);
                        layoutRiwayat.addView(cardView);
                        cardViews.add(cardView);

                        final LinearLayout finalDetailLayout = detailLayout;
                        cardView.setOnClickListener(v -> {
                            if (finalDetailLayout != null && finalIndex < isExpanded.length) {
                                isExpanded[finalIndex] = !isExpanded[finalIndex];
                                finalDetailLayout.setVisibility(isExpanded[finalIndex] ? View.VISIBLE : View.GONE);
                            }
                        });

                        cardView.setOnLongClickListener(v -> {
                            new AlertDialog.Builder(this)
                                    .setTitle("Hapus Riwayat")
                                    .setMessage("Apakah Anda yakin ingin menghapus riwayat ini?")
                                    .setPositiveButton("Ya", (dialog, which) -> hapusRiwayat(document.getId()))
                                    .setNegativeButton("Tidak", null)
                                    .show();
                            return true;
                        });

                        index++;
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error fetching data: " + e.getMessage());
                    Toast.makeText(this, "Gagal memuat riwayat: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
    }

    private void hapusRiwayat(String documentId) {
        if (db == null) {
            Log.e(TAG, "Firestore is null, cannot delete document");
            Toast.makeText(getApplicationContext(), "Firestore tidak tersedia", Toast.LENGTH_SHORT).show();
            return;
        }

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        db.collection("users")
                .document(userId)
                .collection("warisanHistory")
                .document(documentId)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "Document deleted with ID: " + documentId);
                    runOnUiThread(() -> {
                        if (layoutRiwayat != null) {
                            layoutRiwayat.removeAllViews();
                            tampilkanRiwayat();
                        } else {
                            Log.e(TAG, "layoutRiwayat is null after deletion");
                        }
                        Toast.makeText(getApplicationContext(), "Riwayat berhasil dihapus", Toast.LENGTH_SHORT).show();
                    });
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error deleting document: " + e.getMessage());
                    Toast.makeText(getApplicationContext(), "Gagal menghapus riwayat: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private String formatRupiah(double number) {
        NumberFormat format = NumberFormat.getNumberInstance(new Locale("in", "ID"));
        format.setMinimumFractionDigits(0);
        format.setMaximumFractionDigits(2);
        return format.format(number);
    }
}