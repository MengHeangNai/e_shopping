package kh.edu.rupp.ite.e_shopping.ui.viewmodel.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kh.edu.rupp.ite.e_shopping.ui.Firebase.FirebaseDb

class ViewModelProviderFactory(
    private val firebaseDb: FirebaseDb
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return LoginViewModel(firebaseDb) as T
    }
}
