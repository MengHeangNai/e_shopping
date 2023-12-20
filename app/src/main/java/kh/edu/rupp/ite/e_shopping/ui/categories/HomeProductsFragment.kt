package kh.edu.rupp.ite.e_shopping.ui.categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import kh.edu.rupp.ite.e_shopping.api.model.Product
import kh.edu.rupp.ite.e_shopping.databinding.FragmentHomeProductsViewHolderBinding
import kh.edu.rupp.ite.e_shopping.ui.adapter.recyclerview.AdsRecyclerAdapter
import kh.edu.rupp.ite.e_shopping.ui.adapter.recyclerview.BestDealsRecyclerAdapter
import kh.edu.rupp.ite.e_shopping.ui.adapter.recyclerview.ProductsRecyclerAdapter

class HomeProductsFragment : Fragment() {
    private lateinit var adsRecyclerAdapter: AdsRecyclerAdapter
    private lateinit var bestProductsRecyclerAdapter:ProductsRecyclerAdapter
    private lateinit var binding: FragmentHomeProductsViewHolderBinding
    private lateinit var bestDealsRecyclerAdapter: BestDealsRecyclerAdapter

    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bestDealsRecyclerAdapter = BestDealsRecyclerAdapter()
        adsRecyclerAdapter= AdsRecyclerAdapter()
        bestProductsRecyclerAdapter = ProductsRecyclerAdapter()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeProductsViewHolderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = binding.rvAds
        setupProductsRecyclerView()
        onShowData("Clothes")

        recyclerView = binding.rvBestDeals
        setupBestDealsRecyclerView()
        onShowBestDeal("Best Deals")

        recyclerView = binding.rvChairs
        setUpAllProductsRecyclerView()
        onShowAllProducts()

    }

    private fun setupProductsRecyclerView() {
        adsRecyclerAdapter = AdsRecyclerAdapter()
        recyclerView.apply {
            adapter = adsRecyclerAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,false)
        }
    }

    private fun  setupBestDealsRecyclerView(){
        bestDealsRecyclerAdapter = BestDealsRecyclerAdapter()
        recyclerView.apply {
            adapter = bestDealsRecyclerAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,false)
        }
    }

    private fun setUpAllProductsRecyclerView(){
        bestProductsRecyclerAdapter = ProductsRecyclerAdapter()
        recyclerView.apply {
            adapter = bestProductsRecyclerAdapter
            layoutManager = GridLayoutManager(context, 2)

        }
    }

    private fun onShowAllProducts() {
        val db = FirebaseFirestore.getInstance()
        db.collection("products")
            .get()
            .addOnSuccessListener { result ->
                val productsList = result.toObjects(Product::class.java)
                bestProductsRecyclerAdapter.differ.submitList(productsList)
                bestProductsRecyclerAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "Error fetching data: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun onShowBestDeal(category: String) {
        val db = FirebaseFirestore.getInstance()
        db.collection("products")
            .whereEqualTo("category", category) // Replace "category" with your actual field name
            .get()
            .addOnSuccessListener { result ->
                val productsList = result.toObjects(Product::class.java)
                bestDealsRecyclerAdapter.differ.submitList(productsList)
                bestDealsRecyclerAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "Error fetching data: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun onShowData(category: String) {
        val db = FirebaseFirestore.getInstance()
        db.collection("products")
            .whereEqualTo("category", category) // Replace "category" with your actual field name
            .get()
            .addOnSuccessListener { result ->
                val productsList = result.toObjects(Product::class.java)
                adsRecyclerAdapter.differ.submitList(productsList)
                adsRecyclerAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "Error fetching data: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

}