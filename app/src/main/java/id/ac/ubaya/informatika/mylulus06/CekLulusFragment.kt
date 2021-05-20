package id.ac.ubaya.informatika.mylulus06

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.fragment_cek_lulus.*
import org.json.JSONObject

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CekLulusFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CekLulusFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

    }

    override fun onResume() {
        val queue = Volley.newRequestQueue(activity)
        val url = "${Global.mainUrl}cek_lulus.php?nrp=${Global.nrp}"
        val stringRequest = StringRequest(
            Request.Method.GET, url, Response.Listener {
                Log.d("cek_lulus", it)
                val obj = JSONObject(it)
                if(obj.getString("status") == "ok"){
                    val ipk = obj.getDouble("ipk")
                    val ipk_min = obj.getDouble("ipk_min")
                    val sks = obj.getInt("sks")
                    val sks_min = obj.getInt("sks_min")
                    val nilai_d = obj.getInt("nilai_d")
                    val nilai_d_max = obj.getInt("nilai_d_max")
                    val kesimpulan = obj.getString("kesimpulan")
                    var sendWA = ""

                    txtIpk.text = "IPK: ${String.format("%.3f", ipk)}/${String.format("%.3f", ipk_min)}"
                    txtTotalSks.text = "Total SKS: $sks/$sks_min"
                    txtTotalD.text = "Total SKS nilai D: $nilai_d/$nilai_d_max"
                    if(kesimpulan == "ok"){
                        sendWA = "Saya dinyatakan lulus dari app myLulus6!"
                        txtKesimpulan.text = "LULUS"
                        txtKesimpulan.setTextColor(Color.parseColor("#03fcb1"))
                        txtIpk.setTextColor(Color.parseColor("#1c1c1c"))
                        txtTotalSks.setTextColor(Color.parseColor("#1c1c1c"))
                        txtTotalD.setTextColor(Color.parseColor("#1c1c1c"))
                    }else{
                        sendWA = "Saya belum bisa lulus menurut app myLulus6."
                        txtKesimpulan.text = "TIDAK LULUS"
                        txtKesimpulan.setTextColor(Color.parseColor("#ff4940"))
                        if(ipk < ipk_min){
                            txtIpk.setTextColor(Color.parseColor("#ff4940"))
                            sendWA += " IPK saya belum mencukupi."
                        }
                        else txtIpk.setTextColor(Color.parseColor("#1c1c1c"))
                        if(sks < sks_min) {
                            txtTotalSks.setTextColor(Color.parseColor("#ff4940"))
                            sendWA += " Total SKS saya belum mencukupi."
                        }
                        else txtTotalSks.setTextColor(Color.parseColor("#1c1c1c"))
                        if(nilai_d > nilai_d_max) {
                            txtTotalD.setTextColor(Color.parseColor("#ff4940"))
                            sendWA += " Masih banyak nilai D yang saya miliki."
                        }
                        else txtTotalD.setTextColor(Color.parseColor("#1c1c1c"))
                    }
                    fabWA.setOnClickListener {
                        val sendIntent = Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(Intent.EXTRA_TEXT, sendWA)
                            type = "text/plain"
                            `package` = "com.whatsapp"
                        }

                        val shareIntent = Intent.createChooser(sendIntent, "Send Cek Kelulusan")
                        startActivity(shareIntent)
                    }
                }

            },
            Response.ErrorListener {
                Log.e("cek_lulus", it.message.toString())
            }
        )
        queue.add(stringRequest)
        super.onResume()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cek_lulus, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CekLulusFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CekLulusFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}