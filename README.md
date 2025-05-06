Nama : M Fathir R
Kelas : D3IF-4701
NIM : 607062330014



# StudentDo - Task Manager for Students

StudentDo adalah aplikasi pengelola tugas (task manager) yang dibuat khusus untuk membantu mahasiswa mengatur dan mengorganisir tugas mereka dengan lebih baik.

## Fitur Utama

- **Pengelolaan Tugas**: Tambah, edit, dan hapus tugas dengan mudah
- **Prioritas Tugas**: Kategorikan tugas berdasarkan prioritas (Rendah, Sedang, Tinggi)
- **Tenggat Waktu**: Tetapkan tenggat waktu (deadline) untuk setiap tugas
- **Status Penyelesaian**: Tandai tugas sebagai selesai atau belum selesai
- **Recycle Bin**: Fitur tempat sampah untuk memulihkan tugas yang terhapus
- **Pengurutan**: Urutkan tugas berdasarkan tanggal, prioritas, atau nama
- **Filter**: Filter tugas berdasarkan tingkat prioritas
- **Dark Theme**: Dukungan untuk tema gelap (dark theme)
- **Preferensi Pengguna**: Simpan preferensi pengguna seperti tema dan pengurutan default

## Teknologi yang Digunakan

- **Bahasa**: Kotlin
- **UI Framework**: Jetpack Compose
- **Database**: Room Persistence Library
- **Arsitektur**: MVVM (Model-View-ViewModel)
- **Penyimpanan Preferensi**: DataStore
- **Navigasi**: Jetpack Navigation Compose
- **Desain**: Material Design 3

## Persyaratan Sistem

- Android SDK 23 (Android 6.0 Marshmallow) atau lebih tinggi
- Android Studio Ladybug atau lebih baru

## Struktur Proyek

Proyek ini mengikuti prinsip arsitektur bersih (clean architecture) dengan pembagian sebagai berikut:

- **data**: Berisi kelas-kelas yang berhubungan dengan penyimpanan data (Entity, DAO, Repository)
- **ui**: Berisi komponen UI, ViewModel, dan tema
  - **components**: Komponen UI yang dapat digunakan kembali
  - **screens**: Layar-layar utama aplikasi
  - **theme**: Tema dan styling aplikasi

## Cara Menggunakan

1. **Menambah Tugas**: Klik tombol + di layar utama untuk menambahkan tugas baru
2. **Mengedit Tugas**: Klik pada tugas untuk membuka layar edit
3. **Menghapus Tugas**: Di layar edit, klik ikon hapus untuk memindahkan tugas ke Recycle Bin
4. **Memulihkan Tugas**: Buka Recycle Bin dan klik tombol "Restore" pada tugas yang ingin dipulihkan
5. **Mengelola Preferensi**: Buka layar Settings untuk mengubah tema atau preferensi pengurutan default

## Fitur Lanjutan

- **Recycle Bin**: Tugas yang dihapus tidak langsung hilang secara permanen, tetapi masuk ke Recycle Bin
- **Konfirmasi Penghapusan**: Dialog konfirmasi sebelum menghapus tugas secara permanen
- **Validasi Input**: Validasi data masukan untuk memastikan tugas memiliki informasi yang lengkap
- **Pemilihan Tanggal**: Pemilih tanggal interaktif untuk menetapkan tenggat waktu
- **Indikator Prioritas**: Penanda warna visual untuk menunjukkan tingkat prioritas tugas

## Pengembangan

Aplikasi ini dibuat sebagai proyek ujian dengan mengikuti standar pengembangan aplikasi Android modern.
