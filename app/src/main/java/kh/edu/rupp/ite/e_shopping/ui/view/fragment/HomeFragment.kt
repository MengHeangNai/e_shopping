package kh.edu.rupp.ite.e_shopping.ui.view.fragment

import HomeViewpagerAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import kh.edu.rupp.ite.e_shopping.R
import kh.edu.rupp.ite.e_shopping.databinding.FragmentHomeBinding
import kh.edu.rupp.ite.e_shopping.ui.view.activity.ShoppingActivity
import kh.edu.rupp.ite.e_shopping.ui.view.categories.*
import kh.edu.rupp.ite.e_shopping.ui.viewmodel.shopping.ShoppingViewModel

class HomeFragment : Fragment() {
    val TAG = "HomeFragment"
    private lateinit var viewModel: ShoppingViewModel
    private lateinit var binding: FragmentHomeBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = (activity as ShoppingActivity).viewModel

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.frameScan.setOnClickListener{
            val snackBar = requireActivity().findViewById<CoordinatorLayout>(R.id.snackBar_coordinator)
            Snackbar.make(snackBar,resources.getText(R.string.coming_soon), Snackbar.LENGTH_SHORT).show()
        }
        binding.fragmeMicrohpone.setOnClickListener {
            val snackBar = requireActivity().findViewById<CoordinatorLayout>(R.id.snackBar_coordinator)
            Snackbar.make(snackBar,resources.getText(R.string.coming_soon),Snackbar.LENGTH_SHORT).show()
        }

        val categoriesFragments = arrayListOf<Fragment>(
            HomeProductsFragment(),
           ClothesFragment(),
            PantsFragment(),
            ShirtsFragment(),
            AccessoryFragment(),
        )

        binding.viewpagerHome.isUserInputEnabled = false

        val viewPager2Adapter =
            HomeViewpagerAdapter(categoriesFragments, childFragmentManager, lifecycle)
        binding.viewpagerHome.adapter = viewPager2Adapter
        TabLayoutMediator(binding.tabLayout,binding.viewpagerHome){tab,position->

            when(position){
                0 -> tab.text = "Home"
                1-> tab.text = "Clothes"
                2-> tab.text = "Pants"
                3-> tab.text = "Shirts"
                4-> tab.text = "Accessory"
            }

        }.attach()

//        binding.tvSearch.setOnClickListener {
//            findNavController().navigate(R.id.action_home_to_search) }

    }

//    override fun onResume() {
//        super.onResume()
//
//        val bottomNavigation =
//            requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation)
//        bottomNavigation.visibility = View.VISIBLE
//    }

    override fun onDestroy() {
        super.onDestroy()
    }

}