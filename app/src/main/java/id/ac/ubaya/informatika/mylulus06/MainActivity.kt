package id.ac.ubaya.informatika.mylulus06

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.drawer_layout.*

class MainActivity : AppCompatActivity() {
    val fragments: ArrayList<Fragment> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.drawer_layout)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "MyLulus06"
        supportActionBar?.setDisplayHomeAsUpEnabled(false)

        var drawerToggle =
                ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.app_name, R.string.app_name)

        drawerToggle.isDrawerIndicatorEnabled = true
        drawerToggle.syncState()

        navView.setNavigationItemSelectedListener {
            viewPager.currentItem = when(it.itemId){
                R.id.itemMatkul -> 0
                R.id.itemCekLul -> 1
                R.id.itemProfil -> 2
                else -> 0
            }
            val sendIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_EMAIL, arrayOf("mylulus@unit.ubaya.ac.id"))
                putExtra(Intent.EXTRA_SUBJECT, "Laporan Permasalahan Aplikasi")
                putExtra(Intent.EXTRA_TEXT, "Terdapat kesalahan pada data saya")
                type = "message/rfc822"
            }
            when(it.itemId){
                R.id.itemHubungi -> startActivity(Intent.createChooser(sendIntent, "Send Email"))
                R.id.itemKeluar -> logout()
            }
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }

        with(fragments){
            add(MatKulFragment())
            add(CekLulusFragment())
            add(ProfilFragment())
        }
        viewPager.adapter = FragmentAdapter(this, fragments)
        viewPager.registerOnPageChangeCallback(object:ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                botNav.selectedItemId = botNav.menu.getItem(position).itemId
            }
        })
        botNav.setOnNavigationItemSelectedListener {
            viewPager.currentItem = when(it.itemId){
                R.id.itemMatKul -> 0
                R.id.itemCekLulus -> 1
                R.id.itemProfil -> 2
                else -> 0
            }
            true
        }
    }

    fun logout(){
        Global.nrp=""
        val intent = Intent(this, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
        finish()
    }

    override fun onBackPressed() {

    }
}