package id.ac.ubaya.informatika.mylulus06

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_ambil_mat_kul.*
import kotlinx.android.synthetic.main.matkul_card_layout.view.*
import kotlinx.android.synthetic.main.matkul_taken_card_layout.view.*

class MataKuliahAdapter(val matakuliahs: ArrayList<MataKuliah>):RecyclerView.Adapter<MataKuliahAdapter.MataKuliahViewHolder>() {
    class MataKuliahViewHolder(val view: View):RecyclerView.ViewHolder(view){

    }

    val NORMAL_TYPE = 0
    val TAKEN_TYPE = 1
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MataKuliahViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        if(viewType == NORMAL_TYPE){
            val view = inflater.inflate(R.layout.matkul_card_layout, parent, false)
            return MataKuliahViewHolder(view)
        }else{
            val view = inflater.inflate(R.layout.matkul_taken_card_layout, parent, false)
            return MataKuliahViewHolder(view)
        }

    }

    override fun onBindViewHolder(holder: MataKuliahViewHolder, position: Int) {
        val matakuliah = matakuliahs[position]
        if(getItemViewType(position) == NORMAL_TYPE){
            with(holder.view){
                txtKode.text = matakuliah.kode
                txtNama.text = matakuliah.nama
                txtSks.text = "${matakuliah.sks} sks"
                btnAmbil.setOnClickListener {
                    val i = Intent(context, AmbilMatKulActivity::class.java).apply {
                        putExtra(Global.KODE_MATKUL, matakuliah.kode)
                        putExtra(Global.NAMA_MATKUL, matakuliah.nama)
                        putExtra(Global.SKS_MATKUL, matakuliah.sks)
                    }
                    context.startActivity(i)
                }
            }
        }else{
            with(holder.view){
                txtKodeTaken.text = matakuliah.kode
                txtNamaTaken.text = matakuliah.nama
                txtSksTaken.text = "${matakuliah.sks} sks"
                txtSemester.text = "(${matakuliah.smst} ${matakuliah.tahun}/${matakuliah.tahun+1})"
                txtNisbi.text = matakuliah.nisbi
                btnUbah.setOnClickListener {
                    val i = Intent(context, UbahMatKulActivity::class.java).apply {
                        putExtra(Global.KODE_MATKUL, matakuliah.kode)
                        putExtra(Global.NAMA_MATKUL, matakuliah.nama)
                        putExtra(Global.SKS_MATKUL, matakuliah.sks)
                        putExtra(Global.SMST_MATKUL, matakuliah.smst)
                        putExtra(Global.THN_MATKUL, matakuliah.tahun)
                        putExtra(Global.NISBI_MATKUL, matakuliah.nisbi)
                    }
                    context.startActivity(i)
                }
                btnDelete.setOnClickListener {
                    AlertDialog.Builder(context).apply {
                        setMessage("Batal ambil mata kuliah " + matakuliah.nama + "?")
                        setPositiveButton("Batal", DialogInterface.OnClickListener { _, _ ->
                            val queue = Volley.newRequestQueue(context)
                            val url = "${Global.mainUrl}batal_matkul.php"
                            val stringRequest = object: StringRequest(
                                Request.Method.POST,
                                url,
                                Response.Listener {
                                    Log.d("batal_matkul", it)
                                },
                                Response.ErrorListener {
                                    Log.e("batal_matkul", it.toString())
                                }
                            )
                            {
                                override fun getParams(): MutableMap<String, String> {
                                    val params = HashMap<String, String>()
                                    params["nrp"]  = Global.nrp
                                    params["kode"] = matakuliah.kode

                                    return params
                                }
                            }
                            queue.add(stringRequest)
                            context.startActivity(Intent(context, MainActivity::class.java))
                            Toast.makeText(context, "Mata Kuliah ${matakuliah.nama} batal diambil.", Toast.LENGTH_SHORT).show()
                        })
                        setNegativeButton("Cancel", null)
                        create().show()
                    }
                }
            }
        }

    }

    override fun getItemViewType(position: Int): Int {
        val matakuliah = matakuliahs[position]
        if(matakuliah.tahun <= -1){
            return NORMAL_TYPE
        }else{
            return TAKEN_TYPE
        }
    }

    override fun getItemCount() = matakuliahs.size
}