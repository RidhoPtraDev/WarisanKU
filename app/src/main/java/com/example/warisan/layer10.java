package com.example.warisan;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;

public class layer10 extends AppCompatActivity {

    private static final String TAG = "Layer10";
    private EditText edtName, edtEmail, edtPassword, edtConfirmPassword;
    private Button btnRegister;
    private TextView tvLogin;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layer10);

        // Inisialisasi Firebase Authentication dan Firestore
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        Log.d(TAG, "Firebase initialized");

        // Inisialisasi komponen
        edtName = findViewById(R.id.edtName);
        if (edtName == null) {
            Log.e(TAG, "edtName not found in layout! Check layer10.xml ID");
            Toast.makeText(this, "Error: Name field not found", Toast.LENGTH_SHORT).show();
        }
        edtEmail = findViewById(R.id.edtEmail);
        if (edtEmail == null) {
            Log.e(TAG, "edtEmail not found in layout! Check layer10.xml ID");
            Toast.makeText(this, "Error: Email field not found", Toast.LENGTH_SHORT).show();
        }
        edtPassword = findViewById(R.id.edtPassword);
        if (edtPassword == null) {
            Log.e(TAG, "edtPassword not found in layout! Check layer10.xml ID");
            Toast.makeText(this, "Error: Password field not found", Toast.LENGTH_SHORT).show();
        }
        edtConfirmPassword = findViewById(R.id.edtConfirmPassword);
        if (edtConfirmPassword == null) {
            Log.e(TAG, "edtConfirmPassword not found in layout! Check layer10.xml ID");
            Toast.makeText(this, "Error: Confirm Password field not found", Toast.LENGTH_SHORT).show();
        }
        btnRegister = findViewById(R.id.btnRegister);
        if (btnRegister == null) {
            Log.e(TAG, "btnRegister not found in layout! Check layer10.xml ID");
            Toast.makeText(this, "Error: Register button not found", Toast.LENGTH_SHORT).show();
        }
        tvLogin = findViewById(R.id.tvLogin);
        if (tvLogin == null) {
            Log.e(TAG, "tvLogin not found in layout! Check layer10.xml ID");
            Toast.makeText(this, "Error: Login link not found", Toast.LENGTH_SHORT).show();
        }

        // Nonaktifkan tombol daftar di awal
        if (btnRegister != null) {
            btnRegister.setEnabled(false);
            Log.d(TAG, "btnRegister initialized and disabled");
        } else {
            Log.e(TAG, "btnRegister is null, cannot set enabled");
        }

        // Listener untuk validasi input
        TextWatcher inputWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (edtName != null && edtEmail != null && edtPassword != null && edtConfirmPassword != null) {
                    String name = edtName.getText().toString().trim();
                    String email = edtEmail.getText().toString().trim();
                    String password = edtPassword.getText().toString().trim();
                    String confirmPassword = edtConfirmPassword.getText().toString().trim();

                    boolean isValidName = !name.isEmpty();
                    boolean isValidGmail = isValidGmail(email);
                    boolean isValidPassword = password.length() >= 6;
                    boolean isPasswordMatch = password.equals(confirmPassword) && !confirmPassword.isEmpty();

                    if (btnRegister != null) {
                        btnRegister.setEnabled(isValidName && isValidGmail && isValidPassword && isPasswordMatch);
                        Log.d(TAG, "btnRegister enabled: " + (isValidName && isValidGmail && isValidPassword && isPasswordMatch));
                    }

                    // Set error messages
                    if (!isValidName && !name.isEmpty()) edtName.setError("Nama tidak boleh kosong");
                    else edtName.setError(null);
                    if (!isValidGmail && !email.isEmpty()) edtEmail.setError("Gunakan email Gmail yang valid (@gmail.com)");
                    else edtEmail.setError(null);
                    if (!isValidPassword && !password.isEmpty()) edtPassword.setError("Password minimal 6 karakter");
                    else edtPassword.setError(null);
                    if (!isPasswordMatch && !confirmPassword.isEmpty()) edtConfirmPassword.setError("Password tidak cocok");
                    else edtConfirmPassword.setError(null);

                    Log.d(TAG, "Validation - Name: " + isValidName + ", Email: " + isValidGmail + ", Password: " + isValidPassword + ", Match: " + isPasswordMatch);
                } else {
                    Log.e(TAG, "One or more EditText fields are null, cannot validate");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        };

        // Tambahkan TextWatcher ke semua EditText
        if (edtName != null) edtName.addTextChangedListener(inputWatcher);
        else Log.e(TAG, "Failed to add TextWatcher to edtName");
        if (edtEmail != null) edtEmail.addTextChangedListener(inputWatcher);
        else Log.e(TAG, "Failed to add TextWatcher to edtEmail");
        if (edtPassword != null) edtPassword.addTextChangedListener(inputWatcher);
        else Log.e(TAG, "Failed to add TextWatcher to edtPassword");
        if (edtConfirmPassword != null) edtConfirmPassword.addTextChangedListener(inputWatcher);
        else Log.e(TAG, "Failed to add TextWatcher to edtConfirmPassword");

        // Aksi tombol daftar
        if (btnRegister != null) {
            btnRegister.setOnClickListener(v -> {
                if (edtName != null && edtEmail != null && edtPassword != null && edtConfirmPassword != null) {
                    String name = edtName.getText().toString().trim();
                    String email = edtEmail.getText().toString().trim();
                    String password = edtPassword.getText().toString().trim();
                    String confirmPassword = edtConfirmPassword.getText().toString().trim();

                    Log.d(TAG, "Register attempt - Name: " + name + ", Email: '" + email + "', Password length: " + password.length() + ", Confirm: " + confirmPassword);

                    if (email.isEmpty()) {
                        edtEmail.setError("Email tidak boleh kosong");
                        Log.e(TAG, "Email is empty");
                        return;
                    }
                    if (password.isEmpty()) {
                        edtPassword.setError("Password tidak boleh kosong");
                        Log.e(TAG, "Password is empty");
                        return;
                    }
                    if (!password.equals(confirmPassword)) {
                        edtConfirmPassword.setError("Password tidak cocok");
                        Log.e(TAG, "Passwords do not match");
                        return;
                    }

                    if (isValidGmail(email) && password.length() >= 6) {
                        Log.d(TAG, "Starting Firebase registration with email: " + email);
                        mAuth.createUserWithEmailAndPassword(email, password)
                                .addOnCompleteListener(this, task -> {
                                    Log.d(TAG, "Registration task completed - Success: " + task.isSuccessful());
                                    if (task.isSuccessful()) {
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        if (user != null) {
                                            Log.d(TAG, "User created, sending verification to: " + email);
                                            user.sendEmailVerification()
                                                    .addOnCompleteListener(task1 -> {
                                                        if (task1.isSuccessful()) {
                                                            Log.d(TAG, "Verification email sent to: " + email);
                                                            Toast.makeText(layer10.this, "Registrasi berhasil. Cek email untuk verifikasi!", Toast.LENGTH_SHORT).show();
                                                            Intent intent = new Intent(layer10.this, layer2.class);
                                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                            startActivity(intent);
                                                            finish();
                                                        } else {
                                                            Log.e(TAG, "Failed to send verification email: " + task1.getException().getMessage());
                                                            Toast.makeText(layer10.this, "Gagal kirim verifikasi. Coba lagi. Error: " + task1.getException().getMessage(), Toast.LENGTH_LONG).show();
                                                        }
                                                    });
                                            String userId = user.getUid();
                                            Log.d(TAG, "User registered: " + userId);
                                            saveUserDataToFirestore(userId, name);
                                        }
                                    } else {
                                        String errorMessage = task.getException() != null ? task.getException().getMessage() : "Registrasi gagal";
                                        Log.e(TAG, "Registration failed: " + errorMessage);
                                        if (errorMessage.contains("email-already-in-use")) {
                                            edtEmail.setError("Email sudah terdaftar");
                                            Toast.makeText(layer10.this, "Email sudah terdaftar", Toast.LENGTH_SHORT).show();
                                        } else if (errorMessage.contains("invalid-email")) {
                                            edtEmail.setError("Email tidak valid");
                                            Toast.makeText(layer10.this, "Email tidak valid", Toast.LENGTH_SHORT).show();
                                        } else if (errorMessage.contains("network")) {
                                            Toast.makeText(layer10.this, "Periksa koneksi internet", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(layer10.this, "Registrasi gagal: " + errorMessage, Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    } else {
                        if (!isValidGmail(email) && !email.isEmpty()) {
                            edtEmail.setError("Gunakan email Gmail yang valid (@gmail.com)");
                            Log.e(TAG, "Invalid Gmail format");
                        }
                        if (password.length() < 6 && !password.isEmpty()) {
                            edtPassword.setError("Password minimal 6 karakter");
                            Log.e(TAG, "Password too short");
                        }
                        Toast.makeText(this, "Periksa input Anda", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.e(TAG, "One or more EditText fields are null, cannot proceed with registration");
                    Toast.makeText(this, "Error: Input fields not found", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Log.e(TAG, "btnRegister is null, click listener not set");
            Toast.makeText(this, "Error: Register button not found", Toast.LENGTH_SHORT).show();
        }

        // Aksi kembali ke login
        if (tvLogin != null) {
            tvLogin.setOnClickListener(v -> {
                Intent intent = new Intent(layer10.this, layer2.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            });
        } else {
            Log.e(TAG, "tvLogin is null, click listener not set");
            Toast.makeText(this, "Error: Login link not found", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveUserDataToFirestore(String userId, String name) {
        try {
            HashMap<String, Object> userData = new HashMap<>();
            userData.put("name", name);
            db.collection("users").document(userId).set(userData)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User data saved to Firestore for UID: " + userId);
                        } else {
                            Log.e(TAG, "Failed to save user data", task.getException());
                        }
                    });
        } catch (Exception e) {
            Log.e(TAG, "Error saving to Firestore", e);
        }
    }

    // Validasi email hanya Gmail
    private boolean isValidGmail(String email) {
        boolean isValid = email.endsWith("@gmail.com") && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
        Log.d(TAG, "isValidGmail - Email: '" + email + "', Result: " + isValid);
        return isValid;
    }
}