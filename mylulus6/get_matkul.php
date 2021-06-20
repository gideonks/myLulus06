<?php
error_reporting(E_ERROR | E_PARSE);
$c = new mysqli("localhost", "root", "", "kelulusan_ubaya");
if($c->connect_errno) {
	echo json_encode(array(
		'result'=> 'ERROR',
		'message' => 'Failed to connect DB'));

die();
}

if(isset($_POST['nrp'])){
    $nrp = $_POST['nrp'];
    $sql = "SELECT * FROM mk m LEFT JOIN mahasiswa_ambil_mk k ON m.kode = k.kode_mk AND k.nrp = ?";
    if($stmt = $c->prepare($sql)){
        $stmt->bind_param("s", $nrp);
        $stmt->execute();
        $result = $stmt->get_result();
        while($row = $result->fetch_object()){
            $array[] = $row;
        }
        echo json_encode(array(
            "result" => "OK",
            "data" => $array
        ));
    }else{
        $arr = array("result" => "ERROR", "message" => "Database error!");
        die(json_encode($arr));
    }
}

?>