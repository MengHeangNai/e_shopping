package kh.edu.rupp.ite.e_shopping.ui.view.categories

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import kh.edu.rupp.ite.e_shopping.R
import kh.edu.rupp.ite.e_shopping.databinding.FragmentClotheViewHolderBinding
import kh.edu.rupp.ite.e_shopping.ui.adapter.recyclerview.ProductsRecyclerAdapter
import kh.edu.rupp.ite.e_shopping.ui.resource.Resource
import kh.edu.rupp.ite.e_shopping.ui.util.Constants
import kh.edu.rupp.ite.e_shopping.ui.view.activity.ShoppingActivity
import kh.edu.rupp.ite.e_shopping.ui.viewmodel.shopping.ShoppingViewModel

class ClothesFragment : Fragment() {

    val TAG = "ChairFragment"
    private lateinit var viewModel: ShoppingViewModel
    private lateinit var binding: FragmentClotheViewHolderBinding
    private lateinit var productsAdapter: ProductsRecyclerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentClotheViewHolderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        productsAdapter = ProductsRecyclerAdapter()
        viewModel = (activity as ShoppingActivity).viewModel

        viewModel.getClothes()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupProductsRecyclerView()
        observeProducts()

        productsAdapter.onItemClick = { product ->
            val bundle = Bundle()
            bundle.putParcelable("product",product)
            bundle.putString("flag", Constants.PRODUCT_FLAG)

            findNavController().navigate(R.id.action_homeFragment_to_productPreviewFragment2,bundle)
        }
    }

    private fun observeProducts() {
        viewModel.clothe.observe(viewLifecycleOwner, Observer { response ->

            when (response) {
                is Resource.Loading -> {
                    showBottomLoading()
                    return@Observer
                }

                is Resource.Success -> {
                    hideBottomLoading()
                    productsAdapter.differ.submitList(response.data)
                    return@Observer
                }

                is Resource.Error -> {
                    hideBottomLoading()
                    Log.e(TAG, response.message.toString())
                    return@Observer
                }
            }
        })
    }

    private fun hideBottomLoading() {
        binding.progressbar2.visibility = View.GONE
    }

    private fun showBottomLoading() {
        binding.progressbar2.visibility = View.VISIBLE
    }

    private fun setupProductsRecyclerView() {
        binding.rvProducts.apply {
            adapter = productsAdapter
            layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
        }
    }
}
