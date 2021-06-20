<?php
error_reporting(E_ERROR | E_PARSE);
$connect = new mysqli("localhost", "root", "", "kelulusan_ubaya" );
if($connect->connect_errno){
    $arr = array("result" => "ERROR", "message" => "Failed to connect!");
    die(json_encode($arr));
}
if(isset($_POST["nrp"]) && isset($_POST["kode"]) && isset($_POST["semester"]) && isset($_POST["tahun"])
&& isset($_POST["nisbi"])){
    $nrp = $_POST["nrp"];
    $kode = $_POST["kode"];
    $semester = $_POST["semester"];
    $tahun = (int)$_POST["tahun"];
    $nisbi = $_POST["nisbi"];
    $sql = "INSERT INTO mahasiswa_ambil_mk(nrp, kode_mk, semester, tahun_ambil, nisbi)
    VALUES(?, ?, ?, ?, ?)";
    if($stmt = $connect->prepare($sql)){
        $stmt->bind_param("sssis", $nrp, $kode, $semester, $tahun, $nisbi);
        $stmt->execute();
        $arr = array("result" => "OK", "message" => "Success Insert");
        echo json_encode($arr);
    }else{
        $arr = array("result" => "ERROR", "message" => "Database error!");
        die(json_encode($arr));
    }
}
?>