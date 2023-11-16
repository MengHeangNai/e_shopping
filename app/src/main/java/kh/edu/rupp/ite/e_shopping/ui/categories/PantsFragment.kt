package kh.edu.rupp.ite.e_shopping.ui.categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import kh.edu.rupp.ite.e_shopping.api.model.Product
import kh.edu.rupp.ite.e_shopping.databinding.FragmentClotheViewHolderBinding
import kh.edu.rupp.ite.e_shopping.ui.adapter.recyclerview.ClothesAdapter

class PantsFragment : Fragment(){
    private lateinit var recyclerView: RecyclerView
    private lateinit var binding: FragmentClotheViewHolderBinding
    private lateinit var clotheeAdapter: ClothesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentClotheViewHolderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = binding.rvProducts
        setupProductsRecyclerView()
        onShowData("Pants")
    }

    private fun setupProductsRecyclerView() {
        clotheeAdapter = ClothesAdapter()
        recyclerView.apply {
            adapter = clotheeAdapter
            layoutManager = GridLayoutManager(context, 2)
        }
    }

    private fun onShowData(category: String) {
        val db = FirebaseFirestore.getInstance()
        db.collection("products")
            .whereEqualTo("category", category) // Replace "category" with your actual field name
            .get()
            .addOnSuccessListener { result ->
                val productsList = result.toObjects(Product::class.java)
                clotheeAdapter.differ.submitList(productsList)
                clotheeAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "Error fetching data: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

}