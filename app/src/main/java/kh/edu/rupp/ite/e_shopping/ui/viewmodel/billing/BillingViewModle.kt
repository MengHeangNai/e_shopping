package  kh.edu.rupp.ite.e_shopping.ui.viewmodel.billing
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kh.edu.rupp.ite.e_shopping.ui.model.Address
import kh.edu.rupp.ite.e_shopping.ui.model.CartProduct

import kh.edu.rupp.ite.e_shopping.ui.model.Order
import kh.edu.rupp.ite.e_shopping.ui.Firebase.FirebaseDb
import kh.edu.rupp.ite.e_shopping.ui.resource.Resource
import kh.edu.rupp.ite.e_shopping.ui.util.Constants.Companion.ORDER_PLACED_STATE
import java.util.*
import kotlin.random.Random


class BillingViewModel : ViewModel() {
    val placeOrder = MutableLiveData<Resource<Order>>()
    val firebaseDatabase = FirebaseDb()
    val addresses = MutableLiveData<Resource<List<Address>>>()

    init {
        getShippingAddresses()
    }

    private fun getShippingAddresses() {
        addresses.postValue(Resource.Loading())
        firebaseDatabase.getAddresses()?.addSnapshotListener { value, error ->
            if (error != null) {
                addresses.postValue(Resource.Error(error.toString()))
                return@addSnapshotListener
            }
            if (!value!!.isEmpty) {
                val addressesList = value.toObjects(Address::class.java)
                addresses.postValue(Resource.Success(addressesList))
            }
        }
    }

    fun placeOrder(products:List<CartProduct>, address: Address, price:String){
        placeOrder.postValue(Resource.Loading())
        val id = Random.nextInt(9999999)
        val date = Calendar.getInstance().time
        val order = Order(id.toString(),date,price,ORDER_PLACED_STATE)

        firebaseDatabase.placeOrder(products, address, order).addOnCompleteListener {
            if(it.isSuccessful)
                placeOrder.postValue(Resource.Success(order))
            else
                placeOrder.postValue(Resource.Error(it.exception.toString()))
        }
    }
}
