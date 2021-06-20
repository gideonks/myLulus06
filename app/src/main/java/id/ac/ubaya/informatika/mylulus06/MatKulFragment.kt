package id.ac.ubaya.informatika.mylulus06

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MatKulFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MatKulFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    var mataKuliahs: ArrayList<MataKuliah> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        /*mataKuliahs.clear()
        getMatkul()*/
    }

    public fun getMatkul(){
        val queue = Volley.newRequestQueue(activity)
        val url = "${Global.mainUrl}get_matkul.php"
        val stringRequest = object:StringRequest(
            Request.Method.POST, url, Response.Listener {
                Log.d("get_matkul", it)
                val obj = JSONObject(it)
                if(obj.getString("result") == "OK"){
                    val data = obj.getJSONArray("data")
                    for (i in 0 until data.length()){
                        val matkulObj = data.getJSONObject(i)
                        with(matkulObj){
                            if(getString("semester") == "null"){
                                mataKuliahs.add(MataKuliah(
                                    getString("kode"),
                                    getString("nama"),
                                    getInt("sks")
                                ))
                            }else{
                                mataKuliahs.add(MataKuliah(
                                    getString("kode"),
                                    getString("nama"),
                                    getInt("sks"),
                                    getString("semester"),
                                    getInt("tahun_ambil"),
                                    getString("nisbi")
                                ))
                            }

                        }
                    }
                    updateList()
                    Log.d("matakuliahs", mataKuliahs.toString())
                }
            },
            Response.ErrorListener {
                Log.e("get_matkul", it.message.toString())
            }
        )
        {
            override fun getParams(): MutableMap<String, String> {
                val params = HashMap<String, String>()
                params["nrp"] = Global.nrp
                return params
            }
        }
        queue.add(stringRequest)
    }


    override fun onResume() {
        mataKuliahs.clear()
        getMatkul()
        super.onResume()
    }

    fun updateList(){
        val lm = LinearLayoutManager(activity)
        var recyclerView = view?.findViewById<RecyclerView>(R.id.matkulRecView)
        recyclerView?.let {
            it.layoutManager = lm
            it.setHasFixedSize(true)
            it.adapter = MataKuliahAdapter(mataKuliahs)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mat_kul, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MatKulFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MatKulFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}