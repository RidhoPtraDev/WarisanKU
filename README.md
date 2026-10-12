<p align="center">
  <img src="https://raw.githubusercontent.com/RidhoPtraDev/WarisanKU/main/docs/logo.jpg" alt="WarisanKU Logo" width="130" height="130" style="border-radius: 20%;" />
</p>

<h1 align="center">WarisanKU</h1>

<p align="center">
  <strong>Sistem Komputasi Waris Islam Modern & Terintegrasi Berbasis Android</strong>
</p>

<p align="center">
  <a href="https://developer.android.com"><img src="https://img.shields.io/badge/Platform-Android-3DDC84?style=flat-square&logo=android&logoColor=white" alt="Platform"></a>
  <a href="https://www.java.com"><img src="https://img.shields.io/badge/Language-Java%208%20%2F%2017-ED8B00?style=flat-square&logo=openjdk&logoColor=white" alt="Java"></a>
  <a href="https://firebase.google.com"><img src="https://img.shields.io/badge/Backend-Firebase-FFCA28?style=flat-square&logo=firebase&logoColor=black" alt="Firebase"></a>
  <a href="https://cloudinary.com"><img src="https://img.shields.io/badge/Storage-Cloudinary-3448C5?style=flat-square&logo=cloudinary&logoColor=white" alt="Cloudinary"></a>
  <a href="#license"><img src="https://img.shields.io/badge/License-All%20Rights%20Reserved-blue?style=flat-square" alt="License"></a>
</p>

---

## 📌 Ikhtisar Proyek (Executive Summary)

**WarisanKU** adalah aplikasi *mobile* cerdas yang dirancang untuk menyederhanakan, mengotomatiskan, dan mentransparansikan kalkulasi pembagian harta warisan sesuai kaidah fikih mawaris Islam (**Ilmu Faraidh**). 

Seringkali pembagian warisan menjadi persoalan yang kompleks akibat perbedaan ketentuan porsi (*Furudhul Muqaddarah*), relasi keluarga bertingkat, dan penghitungan berbagai instrumen aset. **WarisanKU** hadir sebagai solusi teknologi finansial syariah dengan mesin kalkulator adaptif yang mampu menganalisis hierarki ahli waris, mendeteksi hak pasangan, orang tua, kakek-nenek, dan anak, hingga mengkalkulasi porsi *Asabah* dan penyesuaian sisa secara proporsional.

---

## ✨ Fitur Utama (Key Features)

### 1. 🧮 Mesin Kalkulasi Mawaris Cerdas (Smart Faraidh Engine)
* **Pemberian Hak Otomatis**: Menghitung secara presisi porsi *Dzawil Furudh* (1/2, 1/4, 1/8, 2/3, 1/3, 1/6) sesuai dalil Al-Qur'an dan Sunnah.
* **Kompabilitas Logika Pasangan**: Filter dinamis antara jenis kelamin pewaris dan opsi pasangan guna memitigasi kesalahan input (*zero-error input guarantee*).
* **Distribusi Asabah & Anak**: Mengimplementasikan prinsip rasio waris anak 2:1 (laki-laki : perempuan) serta mekanisme *Asabah bil Ghair* dan *Asabah bin Nafs*.
* **Penyesuaian Sisa Dinamis**: Mencegah sisa harta tak terbagi (*unallocated shares*) dengan mekanisme distribusi proporsional (*Radd*).

### 2. 🏛️ Multi-Asset Wealth Aggregator
* Perhitungan valuasi menyeluruh berbagai instrumen kekayaan:
  * **Properti**: Rumah & Tanah.
  * **Transportasi**: Mobil & Sepeda Motor.
  * **Likuiditas**: Tabungan perbankan.
  * **Aset Berharga & Bisnis**: Perhiasan emas/logam mulia dan valuasi kepemilikan saham perusahaan.
* Format nilai mata uang otomatis (*Real-time Indonesian Rupiah Currency Formatter*).

### 3. ☁️ Sinkronisasi Cloud & Riwayat (Cloud Sync & History)
* Pencatatan riwayat kalkulasi langsung ke **Google Cloud Firestore**.
* Akses data detail interaktif: informasi ahli waris, nilai harta, persentase hak, dan tanggal perhitungan.
* Manajemen riwayat dengan fungsi peninjauan cepat (*expandable card*) dan penghapusan data aman.

### 4. 🔐 Autentikasi Pengguna & Keamanan
* Verifikasi email otomatis melalui **Firebase Authentication**.
* Manajemen profil terintegrasi dengan upload avatar berbasis CDN **Cloudinary** dan rendering performa tinggi menggunakan **Glide Caching**.

### 5. 📖 Direktori Edukasi Faraidh
* Modul ringkasan hukum Islam, rukun waris, syarat sah, hijab (*Mahjub/Hijab Hirman*), serta istilah-istilah mawaris untuk edukasi literasi masyarakat.

---

## 🏗️ Arsitektur & Tech Stack (Architecture & Technology)

Aplikasi dibangun mengacu pada standar pengembangan Android modern:

```
WarisanKU/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/example/warisan/    # Application Controllers & Business Logic
│   │   │   │   ├── layer1.java               # Splash Screen Router
│   │   │   │   ├── layer2.java               # Auth & Login Activity
│   │   │   │   ├── layer3.java               # Dashboard / Hub Menu
│   │   │   │   ├── layer4.java               # Heir Profile & Marriage Condition
│   │   │   │   ├── layer5.java               # Children & Grandparent Assessment
│   │   │   │   ├── layer6.java               # Multi-Asset Valuation Calculator
│   │   │   │   ├── layer7.java               # Core Faraidh Engine & Distribution Results
│   │   │   │   ├── layer8.java               # Calculation History (Firestore)
│   │   │   │   ├── layer9.java               # Islamic Inheritance Knowledge Base
│   │   │   │   ├── layer10.java              # Registration & Email Verification
│   │   │   │   └── layer11.java              # User Profile & Cloudinary Avatar Sync
│   │   │   ├── res/                          # Vector Graphics, Animations, Layouts
│   │   │   └── AndroidManifest.xml           # Permissions & Component Declarations
├── gradle/                                   # Gradle Wrapper & Version Catalogs
└── build.gradle                              # Dependencies & Compiler Configuration
```

| Komponen | Teknologi | Keterangan |
| :--- | :--- | :--- |
| **Bahasa Utama** | Java (JDK 17) | Bahasa inti logika bisnis & Activity lifecycle |
| **UI Design** | Android XML Layouts | Material Components, Gradient Shapes, Constraint & Table Layout |
| **Authentication** | Firebase Auth | Autentikasi Email/Password & Email Verification Flow |
| **Database** | Cloud Firestore | Dokumen NoSQL real-time untuk riwayat dan profil |
| **Storage & CDN** | Cloudinary Mobile SDK | Optimasi kompresi dan hosting media foto profil |
| **Image Loader** | Bumptech Glide 4.16 | Image caching, decoding, dan smooth rendering |
| **Async Processing** | `ExecutorService` & `Handler` | Concurrency background thread bebas memory leak |
| **Build Tools** | Gradle 8.11 | Automated build pipeline & Android Gradle Plugin (AGP) |

---

## 🚀 Panduan Memulai (Getting Started)

### Prasyarat (Prerequisites)
* Android Studio Ladybug | 2024.2+ atau versi terbaru.
* JDK 17 (Java Development Kit).
* Android SDK (API Level 24 minimum, target SDK 35).
* Perangkat Android fisik atau Emulator (Android 7.0 Nougat ke atas).

### Instalasi & Setup Lokal

1. **Clone Repository:**
   ```bash
   git clone https://github.com/RidhoPtraDev/WarisanKU.git
   cd WarisanKU
   ```

2. **Konfigurasi Firebase:**
   * Masukkan file kredensial `google-services.json` yang telah terdaftar di konsol Firebase ke dalam folder root modul `app/`.

3. **Kompilasi & Build Project:**
   ```bash
   # Di Windows Command Prompt / PowerShell:
   .\gradlew.bat compileDebugSources

   # Menjalankan Unit Testing:
   .\gradlew.bat testDebugUnitTest
   ```

4. **Jalankan Aplikasi:**
   * Buka project pada Android Studio, sambungkan perangkat atau emulator Anda, lalu klik tombol **Run** (`Shift + F10`).

---

## 🔒 Standar Keamanan & Reliabilitas (Security & Reliability)

* **Form Validation**: Proteksi input ketat berbasis regex dan watcher dinamis untuk menjamin integritas data sebelum terkirim ke cloud.
* **Leak-Free Concurrency**: Menghindari penggunaan legacy `AsyncTask` dan mengadopsi threading modern yang taat pada daur hidup *Activity Lifecycle*.
* **Permission Hardening**: Penanganan perizinan penyimpanan modular mengikuti standar Android 13+ (`READ_MEDIA_IMAGES`) dengan backward compatibility untuk Android versi lama.

---

## 📄 Lisensi (License)

Seluruh hak cipta dilindungi undang-undang (**All Rights Reserved**).

Proyek ini merupakan perangkat lunak berhak cipta eksklusif. Dilarang keras menggandakan, menyalin, mendistribusikan ulang, atau memanfaatkan kode sumber ini untuk keperluan komersial tanpa persetujuan tertulis dari pemilik hak cipta.
