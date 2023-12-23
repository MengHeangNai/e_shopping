package kh.edu.rupp.ite.e_shopping.ui.view.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import kh.edu.rupp.ite.e_shopping.R
import kh.edu.rupp.ite.e_shopping.ui.Firebase.FirebaseDb
import kh.edu.rupp.ite.e_shopping.ui.viewmodel.auth.LoginViewModel
import kh.edu.rupp.ite.e_shopping.ui.viewmodel.auth.ViewModelProviderFactory


class MainActivity : AppCompatActivity() {

    val viewModel by lazy {
        val firebaseDb = FirebaseDb()
        val viewModelFactory = ViewModelProviderFactory(firebaseDb)
        ViewModelProvider(this, viewModelFactory)[LoginViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lunch)

        supportActionBar?.hide()
    }
}
