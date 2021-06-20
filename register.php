<?php
	error_reporting(E_ERROR | E_PARSE);
	$c = new mysqli("localhost", "root","","kelulusan_ubaya");
	
	if($c->connect_errno) {	echo json_encode(array('result'=> 'ERROR', 'message' => 'Failed to connect DB'));
	die();
	}


if($_SERVER["REQUEST_METHOD"]=="POST"){
$nrp=$_POST["nrp"];
$pin=$_POST["pin"];
$nama=$_POST["nama"];
$angkatan=$_POST["angkatan"];
$result=$c-> query($sql);

$sqlCheck="SELECT * FROM mahasiswa WHERE nrp='$nrp'";
$result=$c-> query($sqlCheck);

if($result->num_rows>0){
	echo json_encode(array('result'=>'error'));
		die();
}
else{
	$sql="INSERT INTO mahasiswa(nrp,pin,nama,angkatan) VALUES ('$nrp', '$pin', '$nama', '$angkatan')";

	$c->query($sql);

	echo json_encode(array("result"=>"OK","sql"=>$sql, "message"=>"Data mahasiswa berhasil ditambahkan"));
}


}

?>