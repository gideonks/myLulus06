package id.ac.ubaya.informatika.mylulus06

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.RadioButton
import androidx.appcompat.app.AlertDialog
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_ambil_mat_kul.*

class AmbilMatKulActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ambil_mat_kul)

        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, Global.nisbi)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerNisbi.adapter = spinnerAdapter

        txtTitle.text = "Ambil Mata Kuliah"
        txtKodeAmbil.text = intent.getStringExtra(Global.KODE_MATKUL)
        txtNamaAmbil.text = intent.getStringExtra(Global.NAMA_MATKUL)
        txtSksAmbil.text = intent.getIntExtra(Global.SKS_MATKUL, 0).toString() + " sks"

        btnSimpan.setOnClickListener {
            val tahun = txtInputTahun.editText?.text.toString()
            if(tahun.isNotEmpty() && radioGroupSmst.checkedRadioButtonId != -1){
                val tahunDouble = tahun.toDouble()
                if(tahunDouble == Math.ceil(tahunDouble)){
                    val queue = Volley.newRequestQueue(applicationContext)
                    val url = "${Global.mainUrl}ambil_matkul.php"
                    val stringRequest = object: StringRequest(
                            Request.Method.POST,
                            url,
                            Response.Listener {
                                Log.d("ambil_matkul", it)
                            },
                            Response.ErrorListener {
                                Log.e("ambil_matkul", it.toString())
                            }
                    )
                    {
                        override fun getParams(): MutableMap<String, String> {
                            val params = HashMap<String, String>()
                            params["nrp"]  = Global.nrp
                            params["kode"] = txtKodeAmbil.text.toString()
                            val rbGroupId = radioGroupSmst.checkedRadioButtonId
                            val rb:RadioButton = findViewById(rbGroupId)
                            params["semester"] = rb.text.toString()
                            params["tahun"] = tahun
                            params["nisbi"] = spinnerNisbi.selectedItem.toString()
                            return params
                        }
                    }
                    queue.add(stringRequest)
                    finish()
                }else{
                    AlertDialog.Builder(this).apply {
                        setMessage("Harap periksa Input!")
                        setTitle("Kesalahan")
                        setPositiveButton("Ok", null)
                        create().show()
                    }
                }
            }else{
                AlertDialog.Builder(this).apply {
                    setMessage("Harap periksa Input!")
                    setTitle("Kesalahan")
                    setPositiveButton("Ok", null)
                    create().show()
                }
            }
        }
    }
}