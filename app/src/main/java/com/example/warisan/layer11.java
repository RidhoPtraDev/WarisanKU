package com.example.warisan;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.InputStream;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class layer11 extends AppCompatActivity {

    private static final int REQUEST_STORAGE_PERMISSION = 2;
    private static final String TAG = "layer11";
    private TextView tvUserName;
    private TextView tvEmail;
    private TextView tvEdit;
    private ImageButton btnBack;
    private ImageButton btnLogout;
    private CircleImageView ivProfileImage;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private Uri imageUri;
    private Cloudinary cloudinary;
    private ActivityResultLauncher<Intent> pickImageLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layer11);

        // Inisialisasi Cloudinary dengan kredensial yang benar
        Map<String, String> config = new HashMap<>();
        config.put("cloud_name", "dcvb6cgih");
        config.put("api_key", "562148832126349");
        config.put("api_secret", "0lPBSLPPB-WoBH74XAIAvbUMLN4");
        config.put("timeout", "30000"); // Timeout 30 detik
        cloudinary = new Cloudinary(config);
        Log.d(TAG, "Cloudinary initialized with cloud_name: dcvb6cgih");

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        tvUserName = findViewById(R.id.tvUserName);
        tvEmail = findViewById(R.id.tvEmail);
        tvEdit = findViewById(R.id.tvEdit);
        btnBack = findViewById(R.id.btnBack);
        btnLogout = findViewById(R.id.btnLogout);
        ivProfileImage = findViewById(R.id.ivProfileImage);

        if (tvUserName == null || tvEmail == null || btnBack == null || btnLogout == null || ivProfileImage == null || tvEdit == null) {
            Log.e(TAG, "UI initialization error: One or more views are null");
            Toast.makeText(this, "UI Error", Toast.LENGTH_SHORT).show();
            return;
        }

        pickImageLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        imageUri = result.getData().getData();
                        if (imageUri != null) {
                            ivProfileImage.setImageURI(imageUri);
                            Toast.makeText(this, "Gambar dipilih", Toast.LENGTH_SHORT).show();
                            if (checkPermission() && isNetworkAvailable()) {
                                uploadImage();
                            } else if (!checkPermission()) {
                                requestPermission();
                                String permissionMessage = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU ?
                                        "Izin foto diperlukan. Izinkan di Pengaturan > Aplikasi > WarisanKU > Izin." :
                                        "Izin penyimpanan diperlukan. Izinkan di Pengaturan > Aplikasi > WarisanKU > Izin.";
                                Toast.makeText(this, permissionMessage, Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(this, "Tidak ada koneksi internet", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Log.e(TAG, "Image URI is null after selection");
                            Toast.makeText(this, "Gagal memilih gambar", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Log.d(TAG, "Pemilihan gambar dibatalkan atau error: " + result.getResultCode());
                        Toast.makeText(this, "Pemilihan gambar dibatalkan", Toast.LENGTH_SHORT).show();
                    }
                });

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            String email = currentUser.getEmail();
            DocumentReference docRef = db.collection("users").document(userId);
            docRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null && document.exists()) {
                        String name = document.getString("name");
                        tvUserName.setText(name != null && !name.isEmpty() ? name : "Nama tidak ditemukan");
                        String photoUrl = document.getString("photoUrl");
                        if (photoUrl != null && !photoUrl.isEmpty()) {
                            Log.d(TAG, "Loading photoUrl: " + photoUrl);
                            Glide.with(layer11.this)
                                    .load(photoUrl)
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .into(ivProfileImage);
                        } else {
                            ivProfileImage.setImageResource(R.drawable.ic_profile);
                        }
                    } else {
                        tvUserName.setText("Data tidak ada");
                        ivProfileImage.setImageResource(R.drawable.ic_profile);
                    }
                } else {
                    tvUserName.setText("Gagal load data");
                    ivProfileImage.setImageResource(R.drawable.ic_profile);
                    Log.e(TAG, "Firestore error: " + task.getException());
                }
                tvEmail.setText(email != null && !email.isEmpty() ? email : "Email tidak ditemukan");
            });
        } else {
            tvUserName.setText("No login");
            tvEmail.setText("No email");
            Intent intent = new Intent(layer11.this, layer2.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }

        btnBack.setOnClickListener(v -> onBackPressed());
        btnLogout.setOnClickListener(v -> {
            mAuth.signOut();
            Intent intent = new Intent(layer11.this, layer2.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
        tvEdit.setOnClickListener(v -> showProfileOptions());
    }

    private void showProfileOptions() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            DocumentReference docRef = db.collection("users").document(userId);
            docRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    String[] options;
                    if (document != null && document.exists() && document.getString("photoUrl") != null && !document.getString("photoUrl").isEmpty()) {
                        options = new String[]{"Ganti Foto", "Hapus Foto"};
                    } else {
                        options = new String[]{"Tambah Foto"};
                    }
                    new AlertDialog.Builder(this)
                            .setTitle("Pilih")
                            .setItems(options, (dialog, which) -> {
                                if (document != null && document.exists() && document.getString("photoUrl") != null && !document.getString("photoUrl").isEmpty()) {
                                    if (which == 0) openGallery();
                                    else if (which == 1) removePhoto();
                                } else if (which == 0) {
                                    if (checkPermission()) {
                                        openGallery();
                                    } else {
                                        requestPermission();
                                    }
                                }
                            })
                            .setNegativeButton("Batal", (dialog, which) -> dialog.dismiss())
                            .show();
                } else {
                    Log.e(TAG, "Failed to fetch photoUrl: " + task.getException());
                    Toast.makeText(this, "Gagal memuat opsi foto", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private boolean checkPermission() {
        String permission = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU ?
                Manifest.permission.READ_MEDIA_IMAGES : Manifest.permission.READ_EXTERNAL_STORAGE;
        int permissionCheck = ContextCompat.checkSelfPermission(this, permission);
        Log.d(TAG, "Permission check for " + permission + ": " + permissionCheck);
        return permissionCheck == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        String permission = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU ?
                Manifest.permission.READ_MEDIA_IMAGES : Manifest.permission.READ_EXTERNAL_STORAGE;
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
            new AlertDialog.Builder(this)
                    .setTitle("Izin Diperlukan")
                    .setMessage("Aplikasi memerlukan izin untuk mengakses " + (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU ? "foto" : "penyimpanan") +
                            ". Silakan izinkan di Pengaturan > Aplikasi > WarisanKU > Izin.")
                    .setPositiveButton("Buka Pengaturan", (dialog, which) -> {
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivity(intent);
                    })
                    .setNegativeButton("Batal", (dialog, which) -> dialog.dismiss())
                    .show();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{permission}, REQUEST_STORAGE_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_STORAGE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (imageUri != null && isNetworkAvailable()) {
                    uploadImage();
                } else if (imageUri != null) {
                    openGallery(); // Jika izin diberikan tapi tidak ada internet, coba ulang
                } else {
                    openGallery();
                }
            } else {
                Log.d(TAG, "Permission denied by user: " + (grantResults.length > 0 ? grantResults[0] : "No result"));
                String permissionMessage = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU ?
                        "Izin ditolak, fitur galeri tidak bisa digunakan. Izinkan di Pengaturan > Aplikasi > WarisanKU > Izin." :
                        "Izin penyimpanan ditolak, fitur galeri tidak bisa digunakan. Izinkan di Pengaturan > Aplikasi > WarisanKU > Izin.";
                Toast.makeText(this, permissionMessage, Toast.LENGTH_LONG).show();
                if (!ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[0])) {
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                    intent.setData(uri);
                    startActivity(intent);
                }
            }
        }
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        if (intent.resolveActivity(getPackageManager()) != null) {
            pickImageLauncher.launch(intent);
        } else {
            Log.e(TAG, "Tidak ada aplikasi galeri yang tersedia");
            Toast.makeText(this, "Tidak ada aplikasi galeri terdeteksi", Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadImage() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null || imageUri == null) {
            Toast.makeText(this, "Pengguna tidak login atau gambar belum dipilih", Toast.LENGTH_SHORT).show();
            return;
        }

        String userId = currentUser.getUid();
        Toast.makeText(this, "Mengunggah gambar...", Toast.LENGTH_SHORT).show();

        java.util.concurrent.ExecutorService executor = java.util.concurrent.Executors.newSingleThreadExecutor();
        android.os.Handler handler = new android.os.Handler(android.os.Looper.getMainLooper());

        executor.execute(() -> {
            String resultUrl = null;
            String errorMessage = null;

            try (InputStream inputStream = getContentResolver().openInputStream(imageUri)) {
                if (inputStream == null) {
                    errorMessage = "Gagal membuka file gambar";
                } else {
                    String mimeType = getContentResolver().getType(imageUri);
                    if (mimeType == null || !mimeType.startsWith("image/")) {
                        errorMessage = "Hanya file gambar yang didukung";
                    } else {
                        Map<String, Object> uploadParams = new HashMap<>();
                        uploadParams.put("folder", "profiles/" + userId);
                        uploadParams.put("public_id", "profile_" + userId);
                        uploadParams.put("overwrite", true);
                        uploadParams.put("resource_type", "image");
                        uploadParams.put("upload_preset", "warisan_preset");

                        long currentTimestamp = System.currentTimeMillis() / 1000;
                        String signature = generateSignature(uploadParams, String.valueOf(currentTimestamp));
                        if (signature != null) {
                            uploadParams.put("timestamp", currentTimestamp);
                            uploadParams.put("signature", signature);
                        }

                        Map resultMap = cloudinary.uploader().upload(inputStream, uploadParams);
                        if (resultMap != null) {
                            if (resultMap.containsKey("error")) {
                                errorMessage = (String) ((Map) resultMap.get("error")).get("message");
                            } else {
                                resultUrl = (String) resultMap.get("secure_url");
                            }
                        } else {
                            errorMessage = "Tidak ada respons dari Cloudinary";
                        }
                    }
                }
            } catch (Exception e) {
                errorMessage = e.getMessage();
                Log.e(TAG, "Upload exception: " + e.getMessage(), e);
            }

            final String finalResultUrl = resultUrl;
            final String finalErrorMessage = errorMessage;

            handler.post(() -> {
                if (isFinishing() || isDestroyed()) return;

                if (finalResultUrl != null && finalResultUrl.startsWith("http")) {
                    Log.d(TAG, "Upload successful, photoUrl: " + finalResultUrl);
                    DocumentReference docRef = db.collection("users").document(userId);
                    docRef.update("photoUrl", finalResultUrl)
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(layer11.this, "Foto diunggah dan disimpan", Toast.LENGTH_SHORT).show();
                                Glide.with(layer11.this)
                                        .load(finalResultUrl)
                                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                                        .into(ivProfileImage);
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(layer11.this, "Gagal simpan URL ke Firestore: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                Log.e(TAG, "Firestore error: " + e.getMessage(), e);
                                ivProfileImage.setImageURI(imageUri);
                            });
                } else {
                    String msg = finalErrorMessage != null ? finalErrorMessage : "Gagal mengunggah foto";
                    Log.e(TAG, "Upload error untuk userId: " + userId + ": " + msg);
                    Toast.makeText(layer11.this, msg, Toast.LENGTH_SHORT).show();
                    ivProfileImage.setImageURI(imageUri);
                }
            });
        });
    }

    private void removePhoto() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            DocumentReference docRef = db.collection("users").document(userId);
            docRef.update("photoUrl", null)
                    .addOnSuccessListener(aVoid -> {
                        ivProfileImage.setImageResource(R.drawable.ic_profile);
                        imageUri = null;
                        Toast.makeText(this, "Foto dihapus", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Gagal hapus: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "Firestore error: " + e.getMessage(), e);
                    });
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private String generateSignature(Map<String, Object> params, String timestamp) {
        try {
            String apiSecret = "0lPBSLPPB-WoBH74XAIAvbUMLN4";
            Map<String, Object> sortedParams = new TreeMap<>(params);
            sortedParams.put("timestamp", timestamp);

            StringBuilder toSign = new StringBuilder();
            for (Map.Entry<String, Object> entry : sortedParams.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue() != null ? entry.getValue().toString() : "";
                if (!key.equals("resource_type") && !key.equals("signature")) {
                    toSign.append(key).append("=").append(value).append("&");
                }
            }
            String toSignStr = toSign.length() > 0 ? toSign.substring(0, toSign.length() - 1) + apiSecret : apiSecret;

            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            byte[] hash = digest.digest(toSignStr.getBytes("UTF-8"));
            StringBuilder signature = new StringBuilder();
            for (byte b : hash) {
                signature.append(String.format("%02x", b));
            }

            Log.d(TAG, "Signature input: " + toSignStr + ", Generated signature: " + signature.toString());
            return signature.toString();
        } catch (Exception e) {
            Log.e(TAG, "Signature generation failed: " + e.getMessage(), e);
            return null;
        }
    }
}