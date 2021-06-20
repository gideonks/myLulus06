<?php
error_reporting(E_ERROR | E_PARSE);
$connect = new mysqli("localhost", "root", "", "kelulusan_ubaya" );
if($connect->connect_errno){
    $arr = array("result" => "ERROR", "message" => "Failed to connect!");
    die(json_encode($arr));
}
if(isset($_POST["nrp"]) && isset($_POST["kode"])){
    $nrp = $_POST["nrp"];
    $kode = $_POST["kode"];
    $sql = "DELETE FROM mahasiswa_ambil_mk WHERE nrp = ? AND kode_mk = ?";
    if($stmt = $connect->prepare($sql)){
        $stmt->bind_param("ss", $nrp, $kode);
        $stmt->execute();
        $arr = array("result" => "OK", "message" => "Success Delete");
        echo json_encode($arr);
    }else{
        $arr = array("result" => "ERROR", "message" => "Database error!");
        die(json_encode($arr));
    }
}
?>