package id.ac.ubaya.informatika.mylulus06

data class MataKuliah(
    val kode: String,
    val nama: String,
    val sks: Int,
    val smst: String = "",
    val tahun: Int = -1,
    val nisbi: String = ""
)
