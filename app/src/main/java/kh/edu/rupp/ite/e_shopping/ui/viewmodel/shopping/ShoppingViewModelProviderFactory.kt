package com.example.kleine.viewmodel.shopping

import ShoppingViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kh.edu.rupp.ite.e_shopping.ui.Firebase.FirebaseDb

class ShoppingViewModelProviderFactory(
    val db: FirebaseDb
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ShoppingViewModel(db) as T
    }
}