package kh.edu.rupp.ite.e_shopping.ui.Firebase

import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.Query.Direction
import com.google.firebase.firestore.Transaction
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.ktx.storage
import kh.edu.rupp.ite.e_shopping.api.model.Address
import kh.edu.rupp.ite.e_shopping.api.model.CartProduct
import kh.edu.rupp.ite.e_shopping.api.model.Order
import kh.edu.rupp.ite.e_shopping.api.model.User
import kh.edu.rupp.ite.e_shopping.ui.util.Constants.Companion.ADDRESS_COLLECTION
import kh.edu.rupp.ite.e_shopping.ui.util.Constants.Companion.BEST_DEALS
import kh.edu.rupp.ite.e_shopping.ui.util.Constants.Companion.CART_COLLECTION
import kh.edu.rupp.ite.e_shopping.ui.util.Constants.Companion.CATEGORIES_COLLECTION
import kh.edu.rupp.ite.e_shopping.ui.util.Constants.Companion.CATEGORY
import kh.edu.rupp.ite.e_shopping.ui.util.Constants.Companion.CLOTHES
import kh.edu.rupp.ite.e_shopping.ui.util.Constants.Companion.COLOR
import kh.edu.rupp.ite.e_shopping.ui.util.Constants.Companion.CUPBOARD_CATEGORY
import kh.edu.rupp.ite.e_shopping.ui.util.Constants.Companion.ID
import kh.edu.rupp.ite.e_shopping.ui.util.Constants.Companion.ORDERS
import kh.edu.rupp.ite.e_shopping.ui.util.Constants.Companion.PRICE
import kh.edu.rupp.ite.e_shopping.ui.util.Constants.Companion.PRODUCTS_COLLECTION
import kh.edu.rupp.ite.e_shopping.ui.util.Constants.Companion.QUANTITY
import kh.edu.rupp.ite.e_shopping.ui.util.Constants.Companion.SIZE
import kh.edu.rupp.ite.e_shopping.ui.util.Constants.Companion.STORES_COLLECTION
import kh.edu.rupp.ite.e_shopping.ui.util.Constants.Companion.TITLE
import kh.edu.rupp.ite.e_shopping.ui.util.Constants.Companion.USERS_COLLECTION


class FirebaseDb {
    private val usersCollectionRef = Firebase.firestore.collection(USERS_COLLECTION)
    private val productsCollection = Firebase.firestore.collection(PRODUCTS_COLLECTION)
    private val categoriesCollection = Firebase.firestore.collection(CATEGORIES_COLLECTION)
    private val storesCollection = Firebase.firestore.collection(STORES_COLLECTION)
    private val firebaseStorage = Firebase.storage.reference

    val userUid = FirebaseAuth.getInstance().currentUser?.uid
    private val firebaseAuth = Firebase.auth

    private val userCartCollection = userUid?.let {
        Firebase.firestore.collection(USERS_COLLECTION).document(it).collection(CART_COLLECTION)
    }

    private val userAddressesCollection = userUid?.let {
        Firebase.firestore.collection(USERS_COLLECTION).document(it).collection(ADDRESS_COLLECTION)
    }

    fun getUser() = usersCollectionRef
        .document(FirebaseAuth.getInstance().currentUser!!.uid)

    fun getProductsByCategory(category: String,page:Long) =
        productsCollection.whereEqualTo(CATEGORY,category).limit(page).get()


    fun getMostRequestedProducts(category: String,page:Long) =
        productsCollection.whereEqualTo(CATEGORY, category)
            .orderBy(ORDERS, Direction.DESCENDING).limit(page).get()


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
            .orderBy(ORDERS, Direction.DESCENDING).limit(pagingPage).get()

    fun getCupboards(pagingPage: Long) =
        productsCollection.whereEqualTo(CATEGORY, CUPBOARD_CATEGORY).limit(pagingPage)
            .limit(pagingPage).get()

    fun addProductToCart(product: CartProduct) = userCartCollection?.document()!!.set(product)

    fun getProductInCart(product: CartProduct) = userCartCollection!!
        .whereEqualTo(ID, product.id)
        .whereEqualTo(COLOR, product.color)
        .whereEqualTo(SIZE, product.size).get()

    fun increaseProductQuantity(documentId: String): Task<Transaction> {
        val document = userCartCollection!!.document(documentId)
        return Firebase.firestore.runTransaction { transaction ->
            val productBefore = transaction.get(document)
            var quantity = productBefore.getLong(QUANTITY)
            quantity = quantity!! + 1
            transaction.update(document, QUANTITY, quantity)
        }

    }

    fun getItemsInCart() = userCartCollection!!

    fun decreaseProductQuantity(documentId: String): Task<Transaction> {
        val document = userCartCollection!!.document(documentId)
        return Firebase.firestore.runTransaction { transaction ->
            val productBefore = transaction.get(document)
            var quantity = productBefore.getLong(QUANTITY)
            quantity = if (quantity!!.toInt() == 1)
                1
            else
                quantity - 1
            transaction.update(document, QUANTITY, quantity)

        }

    }

    fun deleteProductFromCart(documentId: String) =
        userCartCollection!!.document(documentId).delete()


    fun searchProducts(searchQuery: String) = productsCollection
        .orderBy("title")
        .startAt(searchQuery)
        .endAt("\u03A9+$searchQuery")
        .limit(5)
        .get()

    fun getCategories() = categoriesCollection.orderBy("rank").get()

    fun getProductFromCartProduct(cartProduct: CartProduct) =
        productsCollection.whereEqualTo(ID, cartProduct.id)
            .whereEqualTo(TITLE, cartProduct.name)
            .whereEqualTo(PRICE, cartProduct.price).get()

    fun saveNewAddress(address: Address) = userAddressesCollection?.add(address)

    fun getAddresses() = userAddressesCollection

    fun findAddress(address: Address) = userAddressesCollection!!
        .whereEqualTo("addressTitle", address.addressTitle)
        .whereEqualTo("fullName", address.fullName).get()

    fun updateAddress(documentUid: String, address: Address) =
        userAddressesCollection?.document(documentUid)?.set(address)

    fun deleteAddress(documentUid: String, address: Address) =
        userAddressesCollection?.document(documentUid)?.delete()

    private fun deleteCartItems() {
        userCartCollection?.get()?.addOnSuccessListener {
            Firebase.firestore.runBatch { batch ->
                it.documents.forEach {
                    val document = userCartCollection.document(it.id)
                    batch.delete(document)
                }
            }
        }
    }

    fun uploadUserProfileImage(image: ByteArray, imageName: String): UploadTask {
        val imageRef = firebaseStorage.child("profileImages")
            .child(firebaseAuth.currentUser!!.uid)
            .child(imageName)

        return imageRef.putBytes(image)

    }

    fun getImageUrl(
        firstName: String,
        lastName: String,
        email: String,
        imageName: String,
        onResult: (User?, String?) -> Unit,
    ) {
        if (imageName.isNotEmpty())
            firebaseStorage.child("profileImages")
                .child(firebaseAuth.currentUser!!.uid)
                .child(imageName).downloadUrl.addOnCompleteListener {
                    if (it.isSuccessful) {
                        val imageUrl = it.result.toString()
                        val user = User(firstName, lastName, email, imageUrl)
                        onResult(user, null)
                    } else
                        onResult(null, it.exception.toString())

                } else {
            val user = User(firstName, lastName, email, "")
            onResult(user, null)
        }
    }

    fun updateUserInformation(user: User) =
        Firebase.firestore.runTransaction { transaction ->
            val userPath = usersCollectionRef.document(Firebase.auth.currentUser!!.uid)
            if (user.imagePath.isNotEmpty()) {
                transaction.set(userPath, user)
            } else {
                val imagePath = transaction.get(userPath)["imagePath"] as String
                user.imagePath = imagePath
                transaction.set(userPath, user)
            }

        }


    fun getUserOrders() = usersCollectionRef
        .document(FirebaseAuth.getInstance().currentUser!!.uid)
        .collection(ORDERS)
        .orderBy("date", Query.Direction.DESCENDING)
        .get()

    fun resetPassword(email: String) = firebaseAuth.sendPasswordResetEmail(email)

    fun getOrderAddressAndProducts(
        order: Order,
        address: (Address?, String?) -> Unit,
        products: (List<CartProduct>?, String?) -> Unit
    ) {
        usersCollectionRef
            .document(Firebase.auth.currentUser!!.uid).collection(ORDERS)
            .whereEqualTo("id", order.id)
            .get().addOnCompleteListener {
                if (it.isSuccessful) {
                    val id = it.result?.documents?.get(0)?.id
                    usersCollectionRef.document(Firebase.auth.currentUser!!.uid)
                        .collection(ORDERS).document(id!!).collection(ADDRESS_COLLECTION).get()
                        .addOnCompleteListener { it2 ->
                            if (it2.isSuccessful) {
                                val address2 = it2.result?.toObjects(Address::class.java)
                                Log.d("test", address2!!.size.toString())
                                address(address2?.get(0), null)
                            } else
                                address(null, it2.exception.toString())
                        }

                    usersCollectionRef.document(Firebase.auth.currentUser!!.uid)
                        .collection(ORDERS).document(id).collection(PRODUCTS_COLLECTION).get()
                        .addOnCompleteListener { it2 ->
                            if (it2.isSuccessful) {
                                val products2 = it2.result?.toObjects(CartProduct::class.java)
                                Log.d("test", products2!!.size.toString())
                                products(products2, null)
                            } else
                                products(null, it2.exception.toString())
                        }


                } else {
                    address(null, it.exception.toString())
                    products(null, it.exception.toString())
                }
            }
    }


    fun checkUserByEmail(email: String, onResult: (String?, Boolean?) -> Unit) {
        usersCollectionRef.whereEqualTo("email", email).get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val user = it.result.toObjects(User::class.java)
                    if (user.isEmpty())
                        onResult(null, false)
                    else
                        onResult(null, true)
                } else
                    onResult(it.exception.toString(), null)
            }
    }
    fun signInWithGoogle(credential: AuthCredential) =
        FirebaseAuth.getInstance().signInWithCredential(credential)
    fun logout() = Firebase.auth.signOut()
}

