package kh.edu.rupp.ite.e_shopping.ui.Firebase

import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FirebaseDb {
    private val db = Firebase.firestore

    private val userCollection = db.collection("users")

    val userUid = Firebase.auth.currentUser?.uid

    fun createAccount(email: String, password: String) = Firebase.auth.createUserWithEmailAndPassword(email, password)
    fun signIn(email: String, password: String) = Firebase.auth.signInWithEmailAndPassword(email, password)
}