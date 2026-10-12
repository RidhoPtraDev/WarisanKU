package com.example.warisan;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

public class layer2 extends AppCompatActivity {

    private static final String TAG = "Layer2";
    private EditText etEmail, etPassword;
    private LinearLayout btnMasuk;
    private LinearLayout tvRegisterLayout;
    private TextView tvRegisterText;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.layer2);
            Log.d(TAG, "setContentView executed successfully");
        } catch (Exception e) {
            Log.e(TAG, "Failed to setContentView: " + e.getMessage());
            Toast.makeText(this, "Error: Layout failed to load", Toast.LENGTH_SHORT).show();
            return;
        }

        // Cek inisialisasi Firebase App
        if (FirebaseApp.getApps(this).isEmpty()) {
            Log.e(TAG, "Firebase not initialized! Check google-services.json or package name");
            Toast.makeText(this, "Error: Firebase not initialized", Toast.LENGTH_SHORT).show();
            return;
        } else {
            Log.d(TAG, "Firebase initialized successfully with app name: " + FirebaseApp.getInstance().getName());
        }

        // Inisialisasi Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        Log.d(TAG, "Firebase Auth initialized, App status: " + (mAuth.getApp() != null ? "OK" : "FAILED"));

        // Cek koneksi internet
        if (!isNetworkAvailable()) {
            Log.e(TAG, "No internet connection");
            Toast.makeText(this, "Periksa koneksi internet", Toast.LENGTH_SHORT).show();
        } else {
            Log.d(TAG, "Internet connection available");
        }

        // Inisialisasi komponen
        etEmail = findViewById(R.id.etEmail);
        if (etEmail == null) {
            Log.e(TAG, "etEmail not found in layout! Check layer2.xml ID");
            Toast.makeText(this, "Error: Email field not found", Toast.LENGTH_SHORT).show();
        }
        etPassword = findViewById(R.id.etPassword);
        if (etPassword == null) {
            Log.e(TAG, "etPassword not found in layout! Check layer2.xml ID");
            Toast.makeText(this, "Error: Password field not found", Toast.LENGTH_SHORT).show();
        }
        btnMasuk = findViewById(R.id.btnMasuk);
        if (btnMasuk == null) {
            Log.e(TAG, "btnMasuk not found in layout! Check layer2.xml ID");
            Toast.makeText(this, "Error: Login button not found", Toast.LENGTH_SHORT).show();
        }
        tvRegisterLayout = findViewById(R.id.tvRegister);
        if (tvRegisterLayout == null) {
            Log.e(TAG, "tvRegister not found in layout! Check layer2.xml ID");
            Toast.makeText(this, "Error: Register link not found", Toast.LENGTH_SHORT).show();
        } else {
            tvRegisterText = tvRegisterLayout.findViewById(R.id.tvRegisterText);
            if (tvRegisterText == null) {
                Log.e(TAG, "TextView inside tvRegister not found! Check layout structure");
                Toast.makeText(this, "Error: Register text not found", Toast.LENGTH_SHORT).show();
            }
        }

        // Nonaktifkan tombol login di awal (visual)
        if (btnMasuk != null) {
            btnMasuk.setAlpha(0.5f);
            Log.d(TAG, "btnMasuk initialized and set to alpha 0.5");
        }

        // Listener untuk inputan email & password
        TextWatcher inputWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (etEmail != null && etPassword != null && btnMasuk != null) {
                    String email = etEmail.getText().toString().trim();
                    String password = etPassword.getText().toString().trim();

                    boolean isValidGmail = isValidGmail(email);
                    boolean isPasswordFilled = password.length() >= 6;

                    Log.d(TAG, "Input Check - Email: " + email + ", Valid Gmail: " + isValidGmail +
                            ", Password: " + password + ", Filled: " + isPasswordFilled);

                    if (isValidGmail && isPasswordFilled) {
                        btnMasuk.setAlpha(1.0f);
                        Log.d(TAG, "btnMasuk enabled (alpha 1.0)");
                    } else {
                        btnMasuk.setAlpha(0.5f);
                        Log.d(TAG, "btnMasuk disabled (alpha 0.5)");
                    }

                    if (etEmail != null) {
                        if (!isValidGmail && !email.isEmpty()) {
                            etEmail.setError("Gunakan email Gmail yang valid (hanya @gmail.com)");
                            Log.d(TAG, "Email error set: Invalid Gmail");
                        } else {
                            etEmail.setError(null);
                            Log.d(TAG, "Email error cleared");
                        }
                    }
                    if (etPassword != null) {
                        if (!isPasswordFilled && !password.isEmpty()) {
                            etPassword.setError("Password minimal 6 karakter");
                            Log.d(TAG, "Password error set: Too short");
                        } else {
                            etPassword.setError(null);
                            Log.d(TAG, "Password error cleared");
                        }
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        };

        if (etEmail != null) etEmail.addTextChangedListener(inputWatcher);
        if (etPassword != null) etPassword.addTextChangedListener(inputWatcher);

        // Aksi tombol login
        if (btnMasuk != null) {
            btnMasuk.setOnClickListener(v -> {
                if (etEmail != null && etPassword != null) {
                    String email = etEmail.getText().toString().trim();
                    String password = etPassword.getText().toString().trim();

                    Log.d(TAG, "Login attempt - Raw Email: '" + email + "', Raw Password: '" + password + "'");

                    if (email.isEmpty()) {
                        etEmail.setError("Email tidak stod boleh kosong");
                        Log.e(TAG, "Email is empty");
                        return;
                    }
                    if (password.isEmpty()) {
                        etPassword.setError("Password tidak boleh kosong");
                        Log.e(TAG, "Password is empty");
                        return;
                    }

                    if (isValidGmail(email) && password.length() >= 6) {
                        if (isNetworkAvailable()) {
                            Log.d(TAG, "Starting Firebase login with email: " + email);
                            mAuth.signInWithEmailAndPassword(email, password)
                                    .addOnCompleteListener(task -> {
                                        if (task.isSuccessful()) {
                                            FirebaseUser user = mAuth.getCurrentUser();
                                            if (user != null && user.isEmailVerified()) {
                                                Log.d(TAG, "Login successful, UID: " + user.getUid());
                                                Toast.makeText(layer2.this, "Login berhasil", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(layer2.this, layer3.class);
                                                startActivity(intent);
                                                finish();
                                            } else {
                                                Toast.makeText(layer2.this, "Email belum diverifikasi. Cek inbox Anda!", Toast.LENGTH_SHORT).show();
                                                Log.e(TAG, "Email not verified");
                                            }
                                        } else {
                                            Exception exception = task.getException();
                                            String errorMsg = exception != null ? exception.getMessage() : "Unknown error";
                                            Log.e(TAG, "Login failed: " + errorMsg);
                                            if (exception instanceof FirebaseAuthInvalidUserException) {
                                                etEmail.setError("Email tidak terdaftar");
                                                Toast.makeText(layer2.this, "Email tidak terdaftar", Toast.LENGTH_SHORT).show();
                                            } else if (exception instanceof FirebaseAuthInvalidCredentialsException) {
                                                etPassword.setError("Kata sandi salah");
                                                Toast.makeText(layer2.this, "Kata sandi salah", Toast.LENGTH_SHORT).show();
                                            } else if (exception != null && exception.getMessage().contains("network")) {
                                                Toast.makeText(layer2.this, "Periksa koneksi internet", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(layer2.this, "Login gagal: " + errorMsg, Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        } else {
                            Log.e(TAG, "No internet connection during login attempt");
                            Toast.makeText(layer2.this, "Periksa koneksi internet", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        if (!isValidGmail(email)) {
                            etEmail.setError("Gunakan email Gmail yang valid");
                            Log.e(TAG, "Invalid Gmail format");
                        }
                        if (password.length() < 6) {
                            etPassword.setError("Password minimal 6 karakter");
                            Log.e(TAG, "Password too short");
                        }
                        Toast.makeText(layer2.this, "Periksa email dan password", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        // Aksi untuk Daftar Akun Baru
        if (tvRegisterLayout != null && tvRegisterText != null) {
            tvRegisterText.setOnClickListener(v -> {
                Intent intent = new Intent(layer2.this, layer10.class);
                startActivity(intent);
                Log.d(TAG, "Navigating to layer10");
            });
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null && currentUser.isEmailVerified()) {
            Log.d(TAG, "User already logged in, UID: " + currentUser.getUid());
            Intent intent = new Intent(layer2.this, layer3.class);
            startActivity(intent);
            finish();
        } else {
            Log.d(TAG, "No user logged in or email not verified");
        }
    }

    // Method untuk onClick di XML
    public void onMasukClicked(View view) {
        if (btnMasuk != null) btnMasuk.performClick();
    }

    // Method untuk onClick Daftar
    public void onRegisterClicked(View view) {
        if (tvRegisterLayout != null && tvRegisterText != null) tvRegisterLayout.performClick();
    }

    // Validasi email hanya Gmail
    private boolean isValidGmail(String email) {
        boolean isValid = email.endsWith("@gmail.com") && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
        Log.d(TAG, "isValidGmail - Email: '" + email + "', Result: " + isValid);
        return isValid;
    }

    // Cek koneksi internet
    private boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        Log.d(TAG, "Network available: " + isConnected);
        return isConnected;
    }
}