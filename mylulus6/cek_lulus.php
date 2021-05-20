<?php
// ====================================================
// PENGATURAN
// ====================================================
// Ubah variabel-variabel koneksi ini sesuai pengaturan di komputer masing-masing
$db_user = "root";
$db_pass = "";
$db_name = "kelulusan_ubaya";
// Syarat kelulusan, dapat diubah untuk mempermudah pengecekan
// IPK minimum
$ipk_min = 2;
// Total SKS minimum
$sks_min = 29;
// Persentase total SKS nilai D maksimum
$d_max = 0.3;
// AKHIR PENGATURAN
// Ganti seperlunya (misalnya jika ada perhitungan yang keliru)
// ====================================================
$hasil = array();
$connect = new mysqli("localhost", $db_user, $db_pass, $db_name);
if($connect->error) {
    $hasil["error"] = "MySQL connection error";
    die(json_encode($hasil));
}
// Ambil parameter
if(!isset($_GET['nrp'])) {
    $hasil["error"] = "NRP kosong";
    die(json_encode($hasil));
}
$nrp = $_GET['nrp'];
// Ambil data MK yang sudah diambil mahasiswa ini
// Yang perlu diambil hanya kode, sks, dan nisbi
// Ganti SQL ini jika skema basis data berubah
$query = "SELECT ambil.kode_mk, mk.sks, ambil.nisbi FROM mahasiswa_ambil_mk ambil INNER JOIN mk ON mk.kode = ambil.kode_mk WHERE nrp = ?";
if($stmt = $connect->prepare($query)) {
    $stmt->bind_param("s", $nrp);
    $stmt->execute();
    $stmt->bind_result($kode_mk, $sks, $nisbi);
    // Persiapan variabel bantu
    $total_bobot = 0;
    $total_sks = 0;
    $total_d = 0;
    // Untuk konversi nisbi menjadi bobot
    $bobot = array("A" => 4, "AB" => 3.5, "B" => 3, "BC" => 2.5, "C" => 2, "D" => 1, "E" => 0);
    while($stmt->fetch()) {
        // Hitung bobot untuk menghitung IPK
        $total_bobot += $bobot[$nisbi] * $sks;
        $total_sks += $sks;
        // Kalau nisbinya D, hitung SKS-nya
        if($nisbi == "D") $total_d += $sks;
    }
    // Hitung IPK
    if($total_bobot > 0 || $total_sks > 0){
        $ipk = $total_bobot / $total_sks;
    }else{
        $ipk = 0;
    }
    
    // Susun hasil ke array
    $hasil["status"] = "ok";
    $hasil["ipk"] = $ipk;
    $hasil["ipk_min"] = $ipk_min;
    $hasil["sks"] = $total_sks;
    $hasil["sks_min"] = $sks_min;
    $hasil["nilai_d"] = $total_d;
    $total_d_max = ceil($d_max * $sks_min); //!!!!!!!!!
    $hasil["nilai_d_max"] = $total_d_max;
    // Syarat kelulusan:
    // 1. IPK di atas IPK minimum, DAN
    // 2. Total SKS di atas total SKS minimum, DAN
    // 3. Total SKS nilai D di bawah persentase tertentu
    $hasil["kesimpulan"] = ( $ipk >= $ipk_min && $total_sks >= $sks_min && $total_d <= $total_d_max ? "ok" : "ng");
    // Kembalikan dalam bentuk JSON
    echo json_encode($hasil);
} else {
    $hasil["status"] = "error";
    $hasil["error"] = "Database error: $connect->error";
    die(json_encode($hasil));
}