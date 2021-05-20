package id.ac.ubaya.informatika.mylulus06

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    val fragments: ArrayList<Fragment> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
}