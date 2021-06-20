package id.ac.ubaya.informatika.mylulus06

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_register.*
import org.json.JSONObject

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        buttonRegister.setOnClickListener {
            if(checkTahun()&& checkNrp() && checkPin() && checkNama()){

                val nrp=textAddNrp.text.toString()
                val pin=textAddPin.text.toString()
                val nama=textAddNama.text.toString()
                val angkatan=textAddTahun.text.toString()

                val q = Volley.newRequestQueue(this)
                val url = "${Global.mainUrl}register.php"
                var stringRequest= object: StringRequest(
                    Request.Method.POST, url, Response.Listener<String>{
                        Log.d("cekparams", it)
                        val obj = JSONObject(it)
                        if(obj.getString("result") == "OK"){
                            Toast.makeText(this, "Mahasiswa berhasil didaftarkan", Toast.LENGTH_SHORT).show()
                            finish()
                        }
                        else{
                            Toast.makeText(this, "Gagal memasukkan. NRP sudah ada", Toast.LENGTH_SHORT).show()
                        }
                    }, Response.ErrorListener {
                        Log.d("cekparams", it.message.toString())
                    })
                {
                    override fun getParams(): MutableMap<String, String> {
                        val params = HashMap<String, String>()
                        params["nrp"] = nrp
                        params["pin"] = pin
                        params["nama"] = nama
                        params["angkatan"] = angkatan
                        return params
                    }
                }

                q.add(stringRequest)
            }
            else{
                Toast.makeText(this, "Mohon periksa kembali data yang anda masukkan", Toast.LENGTH_SHORT).show()
            }
        }
    }
    fun checkNama():Boolean{
        val nama=textAddNama.text.toString()

        if(nama.isEmpty()){
            textLayputAddNama.error="Nama tidak boleh kosong"
            return false
        }
        else{
            textLayputAddNama.error=null
            return true
        }
    }

    fun checkNrp():Boolean{
        val nrp=textAddNrp.text.toString()

        if(nrp.isNotEmpty()&&nrp.length==9){
            val tahun=textAddTahun.text.toString()
            val angkatan=tahun[2]+""+tahun[3]
            val angkatanNrp=nrp[4]+""+nrp[5]

            if(angkatan.equals(angkatanNrp)){
                textLayoutAddNrp.error=null
                return true
            }
            else{
                textLayoutAddNrp.error="NRP dan angkatan tidak sesuai"
                return false
            }

        }
        else{
            textLayoutAddNrp.error="Mohon masukkan NRP yang valid"
            return false
        }
    }

    fun checkPin():Boolean{
        val pin=textAddPin.text.toString()
        val checkPin=textCheckPin.text.toString()

        if(pin!=checkPin||checkPin.isEmpty())
        {
            textLayoutCheckPin.error="Masukkan kembali pin dengan benar"
            return false
        }
        else if(pin.isEmpty()||pin.length>8||pin.length<6)
        {
            textLayoutAddPin.error="Pin tidak boleh kurang dari 6 digit dan pin tidak boleh melebihi 8 digit"
            return false
        }
        else{
            textLayoutCheckPin.error=null
            textLayoutAddPin.error=null
            return true
        }
    }

    fun checkTahun():Boolean{
        val stringTahun=textAddTahun.text.toString()
        if(stringTahun.isNotEmpty()){
            val tahun=stringTahun.toInt()
            if(tahun>2010 && tahun<2030){
                textLayoutAddTahun.error=null
                return true
            }
            else{
                textLayoutAddTahun.error="Mohon masukkan tahun angkatan yang benar"
                return false
            }
        }
        else{
            textLayoutAddTahun.error="Tahun angkatan tidak boleh kosong"
            return false
        }
    }
}