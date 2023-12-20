
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import kh.edu.rupp.ite.e_shopping.api.model.Product
import kh.edu.rupp.ite.e_shopping.ui.Firebase.FirebaseDb
import kh.edu.rupp.ite.e_shopping.ui.adapter.recyclerview.BestDealsRecyclerAdapter


private const val TAG = "ShoppingViewModel"

class ShoppingViewModel(
    private val firebaseDatabase: FirebaseDb
) : ViewModel() {

    val clothes = MutableLiveData<List<Product>>()
    val emptyClothes = MutableLiveData<Boolean>()
    val bestDeals = MutableLiveData<List<Product>>()
    val emptyBestDeals = MutableLiveData<Boolean>()
    val home = MutableLiveData<Resource<List<Product>>>()


    private var homePage: Long = 10
    private var clothesPaging: Long = 5
    private var bestDealsPaging: Long = 5
    var adsRecyclerAdapter: BestDealsRecyclerAdapter? = null


    init {
        getClothesProducts()
        getBestDealsProduct()
        getHomeProduct()
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

                            }
                        } else
                            home.postValue(Resource.Error(it.exception.toString()))
                    }
            } else
                home.postValue(Resource.Error("Cannot paging"))
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
}