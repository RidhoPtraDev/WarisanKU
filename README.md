<p align="center">
  <img src="https://raw.githubusercontent.com/RidhoPtraDev/WarisanKU/main/docs/logo.png" alt="WarisanKU Logo" height="90" />
</p>

<h1 align="center">WarisanKU</h1>

<p align="center">
  <strong>Sistem Komputasi Waris Islam Modern & Terintegrasi Berbasis Android dengan Mesin Faraidh Cerdas, Sinkronisasi Basis Data Awan, dan Layanan Media Terdistribusi</strong>
</p>

<p align="center">
  <a href="https://developer.android.com"><img src="https://img.shields.io/badge/Platform-Android%20Native-3DDC84?style=flat-square&logo=android&logoColor=white" alt="Platform"></a>
  <a href="https://www.java.com"><img src="https://img.shields.io/badge/Language-Java%2017%20%2F%208-ED8B00?style=flat-square&logo=openjdk&logoColor=white" alt="Java"></a>
  <a href="https://firebase.google.com"><img src="https://img.shields.io/badge/Auth-Firebase%20Auth-FFCA28?style=flat-square&logo=firebase&logoColor=black" alt="Firebase Auth"></a>
  <a href="https://firebase.google.com/docs/firestore"><img src="https://img.shields.io/badge/Database-Cloud%20Firestore-FFA000?style=flat-square&logo=firebase&logoColor=black" alt="Firestore"></a>
  <a href="https://cloudinary.com"><img src="https://img.shields.io/badge/Storage-Cloudinary%20CDN-3448C5?style=flat-square&logo=cloudinary&logoColor=white" alt="Cloudinary"></a>
  <a href="https://bumptech.github.io/glide"><img src="https://img.shields.io/badge/Image%20Loader-Bumptech%20Glide%20v4-4285F4?style=flat-square&logo=google&logoColor=white" alt="Glide"></a>
  <a href="https://gradle.org"><img src="https://img.shields.io/badge/Build%20Tool-Gradle%208.11-02303A?style=flat-square&logo=gradle&logoColor=white" alt="Gradle"></a>
  <a href="https://developer.android.com/about/versions/15"><img src="https://img.shields.io/badge/Target%20SDK-API%2035%20(Vanilla%20Ice%20Cream)-34A853?style=flat-square&logo=android&logoColor=white" alt="SDK"></a>
  <a href="#-lisensi-license"><img src="https://img.shields.io/badge/License-All%20Rights%20Reserved-0066FF?style=flat-square" alt="License"></a>
</p>

---

## 📌 Ikhtisar Proyek (Executive Summary)

**WarisanKU** adalah aplikasi *mobile* finansial syariah modern yang dirancang untuk menyederhanakan, mengotomatiskan, dan mentransparansikan kalkulasi pembagian harta warisan sesuai kaidah fikih mawaris Islam (**Ilmu Faraidh**).

Seringkali pembagian warisan menghadapi hambatan kompleks akibat ketidaktahuan porsi syariat (*Furudhul Muqaddarah*), kerumitan relasi hierarki keluarga bertingkat, serta kesulitan menghitung valuasi aset yang beragam. **WarisanKU** hadir sebagai solusi teknologi komputasi syariah terpadu dengan mesin kalkulasi adaptif yang mampu menganalisis status pewaris, hak pasangan, orang tua, kakek-nenek, anak-anak, hingga penyelesaian porsi sisa (*Asabah*) dan pembagian balik (*Radd*) secara presisi, adil, dan transparan.

---

## ✨ Fitur Utama (Key Features)

### 1. 🧮 Mesin Kalkulasi Mawaris Cerdas (Smart Faraidh Engine)
* **Kalkulasi Porsi Syariat Presisi**: Eksekusi otomatis porsi pasti *Dzawil Furudh* (1/2, 1/4, 1/8, 2/3, 1/3, 1/6) berdasarkan dalil nash Al-Qur'an dan As-Sunnah.
* **Validasi Relasi Pasangan Otomatis**: Penyaringan adaptif status pasangan berdasarkan jenis kelamin pewaris untuk mencegah inkonsistensi input (*zero-error input constraint*).
* **Penentuan Asabah & Rasio Anak**: Otomasi perhitungan porsi anak dengan kaidah *Li adz-dzakari mitslu hazhzhi al-untsayain* (rasio 2:1 antara anak laki-laki dan perempuan), serta status *Asabah bin Nafs* untuk kerabat laki-laki jika tanpa anak.
* **Mekanisme Distribusi Sisa (Radd)**: Alokasi proporsional sisa harta kembali kepada ahli waris yang berhak untuk mencegah terjadinya selisih saldo tak terbagi.

### 2. 🏛️ Kalkulator Valuasi Multi-Aset (Multi-Asset Wealth Aggregator)
* **Agregasi Beragam Instrumen Harta**:
  * **Properti**: Nilai aset rumah dan estimasi harga pasar tanah.
  * **Kendaraan**: Valuasi kepemilikan mobil dan sepeda motor.
  * **Likuiditas Finansial**: Total saldo tabungan perbankan dan kas.
  * **Aset Berharga & Perusahaan**: Valuasi emas/perhiasan dan nilai kepemilikan bisnis.
* **Format Mata Uang Real-Time**: Konversi pengetikan angka otomatis menjadi format standar Rupiah Indonesia (*IDR Currency Auto-Formatter*).

### 3. ☁️ Sinkronisasi Awan & Riwayat Perhitungan (Cloud Sync & History)
* **Penyimpanan Riwayat Terpusat**: Dokumentasi hasil perhitungan otomatis tersimpan ke **Google Cloud Firestore**.
* **Tampilan Interaktif Berbasis Kartu**: Rincian ahli waris, nilai nominal yang didapat, persentase hak waris, dan tanggal pencatatan dapat dibuka-tutup secara fleksibel (*expandable card list*).
* **Manajemen Data Riwayat**: Fasilitas penghapusan riwayat tersimpan yang aman melalui dialog konfirmasi interaktif.

### 4. 🔐 Autentikasi Pengguna & Manajemen Profil (Auth & Profile)
* **Autentikasi & Verifikasi Akun**: Registrasi dan sistem masuk aman dengan verifikasi tautan email melalui **Firebase Authentication**.
* **Layanan Unggah Media Terdistribusi**: Unggah foto profil pengguna langsung ke **Cloudinary CDN** dengan enkripsi signature SHA-1.
* **Optimalisasi Rendering Avatar**: Penerapan caching cerdas dan decoding memori efisien menggunakan **Bumptech Glide**.

### 5. 📖 Direktori Edukasi Faraidh (Islamic Knowledge Base)
* Modul ringkasan fikih mawaris komprehensif yang mencakup rukun waris, syarat sah penerimaan, klasifikasi *dzawil furudh*, konsep *asabah*, serta penghalang waris (*Hijab Hirman & Nuqsan*).

---

## 🏗️ Arsitektur & Tech Stack (Architecture & Technology)

Aplikasi dibangun mengacu pada pola arsitektur *Component-Based Mobile Architecture* dengan pemisahan lapisan pengendali dan layanan awan:

```
Warisan/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/example/warisan/    # Application Controllers & Business Logic
│   │   │   │   ├── layer1.java               # Splash Screen Router & Initializer
│   │   │   │   ├── layer2.java               # Auth & Login Activity (Firebase Auth)
│   │   │   │   ├── layer3.java               # Dashboard Menu & Navigation Hub
│   │   │   │   ├── layer4.java               # Heir Profile, Gender & Marriage Conditions
│   │   │   │   ├── layer5.java               # Sibling Assessment & Grandparent Conditions
│   │   │   │   ├── layer6.java               # Multi-Asset Wealth Valuation Engine
│   │   │   │   ├── layer7.java               # Core Faraidh Engine & Distribution Calculator
│   │   │   │   ├── layer8.java               # Historical Records Explorer (Cloud Firestore)
│   │   │   │   ├── layer9.java               # Islamic Inheritance Knowledge Directory
│   │   │   │   ├── layer10.java              # User Registration & Email Verification Flow
│   │   │   │   └── layer11.java              # User Profile & Cloudinary Avatar Sync
│   │   │   ├── res/                          # Vector Drawables, Layouts, Values, Styles
│   │   │   └── AndroidManifest.xml           # Android App Manifest & Hardware Permissions
│   │   └── test/                             # JVM Local Unit Tests
│   └── build.gradle                          # App Module Dependencies & Build Rules
├── docs/
│   └── logo.jpg                              # Official Application Logo Asset
├── gradle/                                   # Gradle Wrapper & Version Catalogs
├── build.gradle                              # Top-level Project Build Configuration
└── settings.gradle                           # Repository & Module Discovery Settings
```

| Komponen | Teknologi | Keterangan |
| :--- | :--- | :--- |
| **Mobile Platform** | Android Native (API 24 - 35) | Sistem operasi target Android 7.0 hingga Android 15 |
| **Bahasa Pemrograman** | Java 17 (Java 8 Language Level) | Bahasa inti logika bisnis, validasi, dan siklus hidup Activity |
| **Antarmuka Pengguna** | Android XML Layouts & Material 3 | Desain responsif, Custom Drawables, Gradients, & TableLayout |
| **Autentikasi Akun** | Firebase Authentication | Autentikasi kredensial email/password dan verifikasi email |
| **Basis Data Awan** | Google Cloud Firestore | Penyimpanan NoSQL terkelola untuk dokumen profil dan riwayat |
| **Penyimpanan Media & CDN** | Cloudinary Android SDK | Penyimpanan aset gambar avatar di awan dengan kompresi optimal |
| **Image Loading Engine** | Bumptech Glide v4.16 | Pemuatan gambar asinkron dengan *disk & memory cache* |
| **Async Processing** | `ExecutorService` & `Handler` | Komputasi latar belakang modern bebas resiko *memory leak* |
| **Sistem Kompilasi** | Gradle 8.11.1 & AGP | Otomasi proses kompilasi dependensi dan perakitan APK |

---

## 👥 Tim & Pembagian Modul Kerja (Team & Module Responsibilities)

Pengembangan repositori ini dikelola secara terstruktur dan terdistribusi sesuai dengan tanggung jawab teknis:

| Anggota Tim | Peran Teknis | Tanggung Jawab Modul |
| :--- | :--- | :--- |
| **Ridho Putra Aulia** | **Lead Mobile Engineer & Cloud Architect** | Core Faraidh Engine, Integrasi Firebase, Cloudinary CDN, Async Refactor, Repositori |
| **Andika Wahyu Pratama** | **Mobile Developer & QA** | Form Input Pewaris, Alur Validasi Antarmuka, Pengujian Fungsional |
| **Mohamad Yusuf** | **Mobile Developer** | Modul Valuasi Harta Kekayaan, Format Mata Uang Real-Time |
| **Syamsir Alam, S.Kom., M.T.** | **Technical Advisor & Supervisor** | Penjaminan Kualitas Sistem, Review Arsitektur & Perhitungan Syariat |
| **Muhammad Vicky Haikal** | **UI/UX & Frontend Implementer** | Desain Visual Tata Letak, Drawable Vektor, Palet Warna Syariah |
| **Rifky Nabil Firdaus** | **Content & Knowledge Base Specialist**| Penyusunan Materi Edukasi Faraidh, Alur Pengujian Pengguna |

---

## 🚀 Panduan Memulai (Getting Started)

### Prasyarat (Prerequisites)
* Android Studio (Ladybug | 2024.2 atau versi lebih baru direkomendasikan).
* JDK 17 (Java Development Kit) terpasang di sistem.
* Android SDK dengan Build-Tools 35.0.0.
* Perangkat Android fisik dengan opsi pengembang aktif, atau Android Virtual Device (AVD).

### Instalasi & Setup Lokal

1. **Clone Repository:**
   ```bash
   git clone https://github.com/RidhoPtraDev/WarisanKU.git
   cd WarisanKU
   ```

2. **Konfigurasi Kredensial Firebase:**
   * Pastikan file `google-services.json` yang sah dari Firebase Console diletakkan di direktori:
     ```
     WarisanKU/app/google-services.json
     ```

3. **Kompilasi & Build Project:**
   ```bash
   # Di Windows PowerShell / Command Prompt:
   .\gradlew.bat compileDebugSources

   # Menjalankan Unit Testing:
   .\gradlew.bat testDebugUnitTest
   ```

4. **Jalankan Aplikasi:**
   * Buka proyek melalui Android Studio, pilih perangkat target, lalu tekan **Run 'app'** (`Shift + F10`).

---

## 🔒 Standar Keamanan & Reliabilitas (Security & Reliability)

* **Zero-Leak Background Processing**: Mengeliminasi penggunaan legacy `AsyncTask` dan mengadopsi `ExecutorService` berpasangan dengan verifikasi siklus hidup (`isFinishing() / isDestroyed()`).
* **Integritas Input Data**: Validasi masukan berbasis ekspresi reguler (*Regex*) dan pencegahan data kosong (*empty states*) pada setiap formulir pewaris.
* **Proteksi Akses Data Pengguna**: Seluruh query Firestore dibatasi secara ketat berdasarkan User ID (`UID`) akun aktif pengguna yang terverifikasi.
* **Kompatibilitas Izin Penyimpanan**: Penanganan izin akses galeri modular yang mematuhi standar privasi Android 13+ (`READ_MEDIA_IMAGES`) dengan backward compatibility untuk versi Android terdahulu.

---

## 📄 Lisensi (License)

Didistribusikan di bawah perlindungan hak cipta eksklusif (**All Rights Reserved**).

Dilarang keras menyalin, merekayasa balik (*reverse engineering*), memodifikasi, mendistribusikan, atau memanfaatkan source code ini untuk tujuan komersial tanpa persetujuan tertulis dari pemilik hak cipta.

<div align="center">
  <sub>Dibangun untuk memberikan kemudahan dan transparansi perhitungan waris syariah bagi masyarakat. © 2026 Tim Pengembang WarisanKU.</sub>
</div>
