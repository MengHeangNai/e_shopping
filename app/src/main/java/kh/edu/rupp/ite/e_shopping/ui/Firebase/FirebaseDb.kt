package kh.edu.rupp.ite.e_shopping.ui.Firebase

import Constants.Companion.ADDRESS_COLLECTION
import Constants.Companion.BEST_DEALS
import Constants.Companion.CART_COLLECTION
import Constants.Companion.CATEGORIES_COLLECTION
import Constants.Companion.CATEGORY
import Constants.Companion.CLOTHES
import Constants.Companion.CUPBOARD_CATEGORY
import Constants.Companion.ORDERS
import Constants.Companion.PRODUCTS_COLLECTION
import Constants.Companion.STORES_COLLECTION
import Constants.Companion.USERS_COLLECTION
import User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage


class FirebaseDb {
    private val usersCollectionRef = Firebase.firestore.collection(USERS_COLLECTION)
    private val productsCollection = Firebase.firestore.collection(PRODUCTS_COLLECTION)
    private val categoriesCollection = Firebase.firestore.collection(CATEGORIES_COLLECTION)
    private val storesCollection = Firebase.firestore.collection(STORES_COLLECTION)
    private val firebaseStorage = Firebase.storage.reference

    val userUid = FirebaseAuth.getInstance().currentUser?.uid

    private val userCartCollection = userUid?.let {
        Firebase.firestore.collection(USERS_COLLECTION).document(it).collection(CART_COLLECTION)
    }
    private val userAddressesCollection = userUid?.let {
        Firebase.firestore.collection(USERS_COLLECTION).document(it).collection(ADDRESS_COLLECTION)

    }



    private val firebaseAuth = Firebase.auth


    fun getProductsByCategory(category: String,page:Long) =
        productsCollection.whereEqualTo(CATEGORY,category).limit(page).get()


    fun getMostRequestedProducts(category: String,page:Long) =
        productsCollection.whereEqualTo(CATEGORY, category)
            .orderBy(ORDERS, Query.Direction.DESCENDING).limit(page).get()


    fun createNewUser(
        email: String, password: String
    ) = firebaseAuth.createUserWithEmailAndPassword(email, password)

    fun saveUserInformation(
        userUid: String,
        user: User
    ) = usersCollectionRef.document(userUid).set(user)

    fun loginUser(
        email: String,
        password: String
    ) = firebaseAuth.signInWithEmailAndPassword(email, password)

    fun getClothesProducts(pagingPage: Long) =
        productsCollection.whereEqualTo(CATEGORY, CLOTHES).limit(pagingPage).get()

    fun getBestDealsProducts(pagingPage: Long) =
        productsCollection.whereEqualTo(CATEGORY, BEST_DEALS).limit(pagingPage).get()

    fun getHomeProducts(pagingPage: Long) =
        productsCollection.limit(pagingPage).get()

    //add order by orders
    fun getMostOrderedCupboard(pagingPage: Long) =
        productsCollection.whereEqualTo(CATEGORY, CUPBOARD_CATEGORY).limit(pagingPage)
            .orderBy(ORDERS, Query.Direction.DESCENDING).limit(pagingPage).get()

}

