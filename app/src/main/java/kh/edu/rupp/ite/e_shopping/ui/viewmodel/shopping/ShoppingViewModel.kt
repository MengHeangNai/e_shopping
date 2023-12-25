package kh.edu.rupp.ite.e_shopping.ui.viewmodel.shopping

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import kh.edu.rupp.ite.e_shopping.ui.Firebase.FirebaseDb
import kh.edu.rupp.ite.e_shopping.ui.model.*
import kh.edu.rupp.ite.e_shopping.ui.resource.Resource
import kh.edu.rupp.ite.e_shopping.ui.util.Constants.Companion.ACCESSORY_CATEGORY
import kh.edu.rupp.ite.e_shopping.ui.util.Constants.Companion.CLOTHES_CATEGORY
import kh.edu.rupp.ite.e_shopping.ui.util.Constants.Companion.PANTS_CATEGORY
import kh.edu.rupp.ite.e_shopping.ui.util.Constants.Companion.SHIRTS_CATEGORY
import java.util.*

private const val TAG = "ShoppingViewModel"

class ShoppingViewModel(
    private val firebaseDatabase: FirebaseDb
) : ViewModel() {

    val clothes = MutableLiveData<List<Product>>()
    val emptyClothes = MutableLiveData<Boolean>()
    val bestDeals = MutableLiveData<List<Product>>()
    val emptyBestDeals = MutableLiveData<Boolean>()

    val home = MutableLiveData<Resource<List<Product>>>()

    val clothe = MutableLiveData<Resource<List<Product>>>()

    val shirts = MutableLiveData<Resource<List<Product>>>()

    val pants = MutableLiveData<Resource<List<Product>>>()
    
    val accessory = MutableLiveData<Resource<List<Product>>>()
    val mostRequestedAccessories = MutableLiveData<Resource<List<Product>>>()
    
    val addToCart = MutableLiveData<Resource<Boolean>>()

    val addAddress = MutableLiveData<Resource<Address>>()
    val updateAddress = MutableLiveData<Resource<Address>>()
    val deleteAddress = MutableLiveData<Resource<Address>>()

    val profile = MutableLiveData<Resource<User>>()

    val uploadProfileImage = MutableLiveData<Resource<String>>()
    val updateUserInformation = MutableLiveData<Resource<User>>()

    val userOrders = MutableLiveData<Resource<List<Order>>>()

    val passwordReset = MutableLiveData<Resource<String>>()

    val orderAddress = MutableLiveData<Resource<Address>>()
    val orderProducts = MutableLiveData<Resource<List<CartProduct>>>()

    val categories = MutableLiveData<Resource<List<Category>>>()


    val search = MutableLiveData<Resource<List<Product>>>()

    private var homePage: Long = 10
    private var clothesPaging: Long = 5
    private var bestDealsPaging: Long = 5


    private var clothePage: Long = 8
    private var pantsPage: Long = 8
    private var shirtsPage: Long = 8
    private var mostRequestedAccessoryPage: Long = 3
    private var accessoryPage: Long = 8

    init {
        getClothesProducts()
        getBestDealsProduct()
        getHomeProduct()
    }

    private var accessoriesProducts: List<Product>? = null
    fun getAccessories(size: Int = 0) {
        if (accessoriesProducts != null && size == 0) {
            accessory.postValue(Resource.Success(accessoriesProducts))
            return
        }
                Log.d("test", "paging")
                firebaseDatabase.getProductsByCategory(ACCESSORY_CATEGORY, accessoryPage)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            val documents = it.result
                            if (!documents!!.isEmpty) {
                                val productsList = documents.toObjects(Product::class.java)
                                accessory.postValue(Resource.Success(productsList))
                                accessoriesProducts = productsList
                                accessoryPage += 4

                            }
                        } else
                            accessory.postValue(Resource.Error(it.exception.toString()))
                    }

    }

    private var mostRequestedAccessoriesProducts: List<Product>? = null
    fun getMostRequestedAccessories(size: Int = 0) {
        if (mostRequestedAccessoriesProducts != null && size == 0) {
            mostRequestedAccessories.postValue(Resource.Success(mostRequestedAccessoriesProducts))
            return
        }
        mostRequestedAccessories.postValue(Resource.Loading())
        shouldPaging(ACCESSORY_CATEGORY, size) { shouldPaging ->
            if (shouldPaging) {
                clothe.postValue(Resource.Loading())
                firebaseDatabase.getProductsByCategory(
                    ACCESSORY_CATEGORY,
                    mostRequestedAccessoryPage
                )
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            val documents = it.result
                            if (!documents!!.isEmpty) {
                                val productsList = documents.toObjects(Product::class.java)
                                mostRequestedAccessories.postValue(Resource.Success(productsList))
                                mostRequestedAccessoriesProducts = productsList
                                mostRequestedAccessoryPage += 4

                            }
                        } else
                            mostRequestedAccessories.postValue(Resource.Error(it.exception.toString()))
                    }
            } else
                mostRequestedAccessories.postValue(Resource.Error("Cannot paging"))
        }
    }

    private var ClothesProducts: List<Product>? = null
    fun getClothes(size: Int = 0) {
        if (ClothesProducts != null && size == 0) {
            clothe.postValue(Resource.Success(ClothesProducts))
            return
        }
                firebaseDatabase.getProductsByCategory(CLOTHES_CATEGORY, clothePage)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            val documents = it.result
                            if (!documents!!.isEmpty) {
                                val productsList = documents.toObjects(Product::class.java)
                                ClothesProducts = productsList
                                clothe.postValue(Resource.Success(productsList))
                                clothePage += 4

                            }
                        } else
                            clothe.postValue(Resource.Error(it.exception.toString()))
                    }
    }
    
    private var shirtsProducts: List<Product>? = null
    fun getshirts(size: Int = 0) {
        if (shirtsProducts != null && size == 0) {
            shirts.postValue(Resource.Success(shirtsProducts))
            return
        }
                shirts.postValue(Resource.Loading())
                firebaseDatabase.getProductsByCategory(SHIRTS_CATEGORY, shirtsPage)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            val documents = it.result
                            if (!documents!!.isEmpty) {
                                val productsList = documents.toObjects(Product::class.java)
                                shirtsProducts = productsList
                                shirts.postValue(Resource.Success(productsList))
                                shirtsPage += 4

                            }
                        } else
                            shirts.postValue(Resource.Error(it.exception.toString()))
                    }

        }


    private var pantsProduct: List<Product>? = null

    fun  getPants(size: Int = 0) {
        if (pantsProduct != null && size == 0) {
            pants.postValue(Resource.Success(pantsProduct))
            return
        }
        pants.postValue(Resource.Loading())
                firebaseDatabase.getProductsByCategory(PANTS_CATEGORY, pantsPage)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            val documents = it.result
                            if (!documents!!.isEmpty) {
                                val productsList = documents.toObjects(Product::class.java)
                                pants.postValue(Resource.Success(productsList))
                                pantsProduct = productsList
                                pantsPage += 4

                            }
                        } else
                            pants.postValue(Resource.Error(it.exception.toString()))
                    }
    }


    fun getClothesProducts() =
        firebaseDatabase.getClothesProducts(clothesPaging).addOnCompleteListener {
            if (it.isSuccessful) {
                val documents = it.result
                if (!documents!!.isEmpty) {
                    val productsList = documents.toObjects(Product::class.java)
                    clothes.postValue(productsList)
                    clothesPaging += 5
                } else
                    emptyClothes.postValue(true)

            } else
                Log.e(TAG, it.exception.toString())

        }

    fun getBestDealsProduct() =
        firebaseDatabase.getBestDealsProducts(bestDealsPaging).addOnCompleteListener {
            if (it.isSuccessful) {
                val documents = it.result
                if (!documents!!.isEmpty) {
                    val productsList = documents.toObjects(Product::class.java)
                    bestDeals.postValue(productsList)
                    bestDealsPaging += 5
                } else
                    emptyBestDeals.postValue(true)

            } else
                Log.e(TAG, it.exception.toString())
        }

    fun getHomeProduct(size: Int = 0) {
        home.postValue(Resource.Loading())
        shouldPagingHome(size)
        { shouldPaging ->
            if (shouldPaging) {
                home.postValue(Resource.Loading())
                firebaseDatabase.getHomeProducts(homePage)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            val documents = it.result
                            if (!documents!!.isEmpty) {
                                val productsList = documents.toObjects(Product::class.java)
                                home.postValue(Resource.Success(productsList))
                                homePage += 4
                                Log.d("test", "home page $homePage")
                            }
                        } else
                            home.postValue(Resource.Error(it.exception.toString()))
                    }
            } else
                home.postValue(Resource.Error("Cannot paging"))
        }
    }
    
    private fun shouldPaging(category: String, listSize: Int, onSuccess: (Boolean) -> Unit) {
        FirebaseFirestore.getInstance()
            .collection("categories")
            .whereEqualTo("category", category)
            .get()
            .addOnSuccessListener { snapshot ->
                val tempCategory = snapshot.toObjects(Category::class.java)
                val products = tempCategory.getOrNull(0)?.products ?: 0

                if (listSize == products) {
                    onSuccess(false).also { Log.d(TAG, "$category Paging:false") }
                } else {
                    onSuccess(true).also { Log.d(TAG, "$category Paging:true") }
                }
            }
            .addOnFailureListener { exception ->
                // Handle the failure appropriately (e.g., log or propagate the error)
                Log.e(TAG, "Error getting documents: $exception")
                onSuccess(false)  // For simplicity, assuming failure means no paging
            }
    }


    private fun shouldPagingHome(listSize: Int, onSuccess: (Boolean) -> Unit) {
        FirebaseFirestore.getInstance()
            .collection("categories").get().addOnSuccessListener {
                var productsCount = 0
                it.toObjects(Category::class.java).forEach { category ->
                    productsCount += category.products!!.toInt()
                }

                if (listSize == productsCount)
                    onSuccess(false)
                else
                    onSuccess(true)

            }
    }


    private fun checkIfProductAlreadyAdded(
        product: CartProduct,
        onSuccess: (Boolean, String) -> Unit
    ) {
        addToCart.postValue(Resource.Loading())
        firebaseDatabase.getProductInCart(product).addOnCompleteListener {
            if (it.isSuccessful) {
                val documents = it.result!!.documents
                if (documents.isNotEmpty())
                    onSuccess(true, documents[0].id) // true ---> product is already in cart
                else
                    onSuccess(false, "") // false ---> product is not in cart
            } else
                addToCart.postValue(Resource.Error(it.exception.toString()))

        }
    }


    fun addProductToCart(product: CartProduct) =
        checkIfProductAlreadyAdded(product) { isAdded, id ->
            if (isAdded) {
                firebaseDatabase.increaseProductQuantity(id).addOnCompleteListener {
                    if (it.isSuccessful)
                        addToCart.postValue(Resource.Success(true))
                    else
                        addToCart.postValue(Resource.Error(it.exception!!.message))
                }
            } else {
                firebaseDatabase.addProductToCart(product).addOnCompleteListener {
                    if (it.isSuccessful)
                        addToCart.postValue(Resource.Success(true))
                    else
                        addToCart.postValue(Resource.Error(it.exception!!.message))
                }
            }
        }


    fun saveAddress(address: Address) {
        addAddress.postValue(Resource.Loading())
        firebaseDatabase.saveNewAddress(address)?.addOnCompleteListener {
            if (it.isSuccessful)
                addAddress.postValue(Resource.Success(address))
            else
                addAddress.postValue(Resource.Error(it.exception.toString()))
        }
    }

    fun updateAddress(oldAddress: Address, newAddress: Address) {
        updateAddress.postValue(Resource.Loading())
        firebaseDatabase.findAddress(oldAddress).addOnCompleteListener { addressResponse ->
            if (addressResponse.isSuccessful) {
                val documentUid = addressResponse.result!!.documents[0].id
                firebaseDatabase.updateAddress(documentUid, newAddress)?.addOnCompleteListener {
                    if (it.isSuccessful)
                        updateAddress.postValue(Resource.Success(newAddress))
                    else
                        updateAddress.postValue(Resource.Error(it.exception.toString()))

                }

            } else
                updateAddress.postValue(Resource.Error(addressResponse.exception.toString()))

        }
    }

    fun deleteAddress(address: Address) {
        deleteAddress.postValue(Resource.Loading())
        firebaseDatabase.findAddress(address).addOnCompleteListener { addressResponse ->
            if (addressResponse.isSuccessful) {
                val documentUid = addressResponse.result!!.documents[0].id
                firebaseDatabase.deleteAddress(documentUid, address)?.addOnCompleteListener {
                    if (it.isSuccessful)
                        deleteAddress.postValue(Resource.Success(address))
                    else
                        deleteAddress.postValue(Resource.Error(it.exception.toString()))

                }

            } else
                deleteAddress.postValue(Resource.Error(addressResponse.exception.toString()))

        }
    }

    private val user: User? = null
    fun getUser() {
        if (user != null) {
            profile.postValue(Resource.Success(user))
            return
        }

        profile.postValue(Resource.Loading())
        firebaseDatabase.getUser().addSnapshotListener { value, error ->
            if (error != null)
                profile.postValue(Resource.Error(error.message))
            else
                profile.postValue(Resource.Success(value?.toObject(User::class.java)))

        }
    }

    fun uploadProfileImage(image: ByteArray) {
        uploadProfileImage.postValue(Resource.Loading())
        val name = UUID.nameUUIDFromBytes(image).toString()
        firebaseDatabase.uploadUserProfileImage(image, name).addOnCompleteListener {
            if (it.isSuccessful)
                uploadProfileImage.postValue(Resource.Success(name))
            else
                uploadProfileImage.postValue(Resource.Error(it.exception.toString()))
        }
    }

    fun updateInformation(firstName: String, lastName: String, email: String, imageName: String) {
        updateUserInformation.postValue(Resource.Loading())

        firebaseDatabase.getImageUrl(firstName, lastName, email, imageName) { user, exception ->

            if (exception != null)
                updateUserInformation.postValue(Resource.Error(exception))
                    .also { Log.d("test1", "up") }
            else
                user?.let {
                    onUpdateInformation(user).also { Log.d("test1", "down") }
                }
        }
    }

    private fun onUpdateInformation(user: User) {
        firebaseDatabase.updateUserInformation(user).addOnCompleteListener {
            if (it.isSuccessful)
                updateUserInformation.postValue(Resource.Success(user))
            else
                updateUserInformation.postValue(Resource.Error(it.exception.toString()))

        }
    }

    fun getUserOrders() {
        userOrders.postValue(Resource.Loading())
        firebaseDatabase.getUserOrders().addOnCompleteListener {
            if (it.isSuccessful)
                userOrders.postValue(Resource.Success(it.result?.toObjects(Order::class.java)))
            else
                userOrders.postValue(Resource.Error(it.exception.toString()))
        }
    }

    fun resetPassword(email: String) {
        passwordReset.postValue(Resource.Loading())
        firebaseDatabase.resetPassword(email).addOnCompleteListener {
            if (it.isSuccessful)
                passwordReset.postValue(Resource.Success(email))
            else
                passwordReset.postValue(Resource.Error(it.exception.toString()))
        }
    }

    fun getOrderAddressAndProducts(order: Order) {
        orderAddress.postValue(Resource.Loading())
        orderProducts.postValue(Resource.Loading())
        firebaseDatabase.getOrderAddressAndProducts(order, { address, aError ->
            if (aError != null)
                orderAddress.postValue(Resource.Error(aError))
            else
                orderAddress.postValue(Resource.Success(address))
        }, { products, pError ->

            if (pError != null)
                orderProducts.postValue(Resource.Error(pError))
            else
                orderProducts.postValue(Resource.Success(products))

        })
    }

    fun searchProducts(searchQuery: String) {
        search.postValue(Resource.Loading())
        firebaseDatabase.searchProducts(searchQuery).addOnCompleteListener {
            if (it.isSuccessful) {
                val productsList = it.result!!.toObjects(Product::class.java)
                search.postValue(Resource.Success(productsList))

            } else
                search.postValue(Resource.Error(it.exception.toString()))

        }
    }

    private var categoriesSafe: List<Category>? = null
    fun getCategories() {
        if(categoriesSafe != null){
            categories.postValue(Resource.Success(categoriesSafe))
            return
        }
        categories.postValue(Resource.Loading())
        firebaseDatabase.getCategories().addOnCompleteListener {
            if (it.isSuccessful) {
                val categoriesList = it.result!!.toObjects(Category::class.java)
                categoriesSafe = categoriesList
                categories.postValue(Resource.Success(categoriesList))
            } else
                categories.postValue(Resource.Error(it.exception.toString()))
        }


    }

}