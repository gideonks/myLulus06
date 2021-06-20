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
    $sql = "UPDATE mahasiswa_ambil_mk SET semester = ?, tahun_ambil = ?, nisbi = ?
    WHERE nrp = ? AND kode_mk = ?";
    if($stmt = $connect->prepare($sql)){
        $stmt->bind_param("sisss", $semester, $tahun, $nisbi, $nrp, $kode);
        $stmt->execute();
        $arr = array("result" => "OK", "message" => "Success Update");
        echo json_encode($arr);
    }else{
        $arr = array("result" => "ERROR", "message" => "Database error!");
        die(json_encode($arr));
    }
}
?>