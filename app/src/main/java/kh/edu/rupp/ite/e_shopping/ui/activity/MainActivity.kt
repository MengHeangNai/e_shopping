package kh.edu.rupp.ite.e_shopping.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import kh.edu.rupp.ite.e_shopping.R
import kh.edu.rupp.ite.e_shopping.databinding.ActivityMainBinding
import kh.edu.rupp.ite.e_shopping.ui.fragment.FragmentCart
import kh.edu.rupp.ite.e_shopping.ui.fragment.FragmentHome
import kh.edu.rupp.ite.e_shopping.ui.fragment.FragmentProfile
import kh.edu.rupp.ite.e_shopping.ui.fragment.FragmentSearch


class MainActivity : AppCompatActivity(){
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        showFragment(FragmentHome())

        binding.bottomNav.setOnItemSelectedListener {
            when(it.itemId){
                R.id.mn_home -> showFragment(FragmentHome())

                R.id.mn_search -> showFragment(FragmentSearch())

                R.id.mn_cart -> showFragment(FragmentCart())

                else -> showFragment(FragmentProfile())
            }
            true
        }


    }
  private fun showFragment(fragment: Fragment) {
    supportFragmentManager.beginTransaction()
        .replace(binding.lytFragment.id, fragment)
        .commitNow()
}
}