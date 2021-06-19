package id.ac.ubaya.informatika.mylulus06

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {
    val NAMA_MHS="NAMAMHS"
    val TAHUN_MHS="ANGKATAN"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        buttonMasuk.setOnClickListener {
            if(checkNRPLogin()&&checkPinLogin()){
                val nrp=textNRP.text.toString()
                val pin=textPin.text.toString()

                val q = Volley.newRequestQueue(this)
                val url = "${Global.mainUrl}login.php"
                var stringRequest= object: StringRequest(
                    Request.Method.POST, url, Response.Listener<String>{
                        Log.d("cekparams", it)
                        val obj = JSONObject(it)
                        if(obj.getString("result") == "OK"){
                            val data=obj.getJSONArray("data")
                            val objNrp=data.getJSONObject(0)
                            Global.nrp=objNrp.getString("nrp")

                            Toast.makeText(this, "Berhasil Login", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this, MainActivity::class.java)
                            intent.putExtra(NAMA_MHS, objNrp.getString("nama"))
                            intent.putExtra(TAHUN_MHS, objNrp.getString("angkatan"))
                            startActivity(intent)
                        }
                        else{
                            Toast.makeText(this, "Gagal Login. Cek kembali nrp atau pin", Toast.LENGTH_SHORT).show()
                        }
                    }, Response.ErrorListener {
                        Log.d("cekparams", it.message.toString())
                    })
                {
                    override fun getParams(): MutableMap<String, String> {
                        val params = HashMap<String, String>()
                        params["nrp"] = nrp
                        params["pin"] = pin
                        return params
                    }
                }
                q.add(stringRequest)
            }
            else{
                Toast.makeText(this, "Mohon masukkan NRP atau password", Toast.LENGTH_SHORT).show()
            }
        }

        buttonDaftar.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    fun checkNRPLogin():Boolean{
        if(textNRP.text.toString().isEmpty()){
            textLayoutNrp.error="Mohon masukkan NRP"
            return false
        }
        else{
            textLayoutNrp.error=null
            return true
        }
    }

    fun checkPinLogin():Boolean{
        if(textPin.text.toString().isEmpty()){
            textLayoutPin.error="Mohon masukkan pin"
            return false
        }
        else{
            textLayoutPin.error=null
            return true
        }
    }


}