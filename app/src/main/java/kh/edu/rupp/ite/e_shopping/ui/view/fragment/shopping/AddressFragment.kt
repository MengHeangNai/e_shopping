package kh.edu.rupp.ite.e_shopping.ui.view.fragment.shopping

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomnavigation.BottomNavigationView
import kh.edu.rupp.ite.e_shopping.R
import kh.edu.rupp.ite.e_shopping.api.model.Address
import kh.edu.rupp.ite.e_shopping.databinding.FragmentAddressBinding
import kh.edu.rupp.ite.e_shopping.ui.resource.Resource
import kh.edu.rupp.ite.e_shopping.ui.view.activity.ShoppingActivity
import kh.edu.rupp.ite.e_shopping.ui.viewmodel.shopping.ShoppingViewModel


class AddressFragment : Fragment() {
    val args by navArgs<AddressFragmentArgs>()
    val TAG = "AddressFragment"
    private lateinit var binding: FragmentAddressBinding
    private lateinit var viewModel: ShoppingViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = (activity as ShoppingActivity).viewModel

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddressBinding.inflate(inflater)
        val bottomNavigation = activity?.findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigation?.visibility = View.INVISIBLE
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val address = args.address
        if (address == null) {
            onSaveClick()
            observeAddAddress()
            onImgCloseClick()
        } else {
            setInformation(address)
            updateAddress(address)
            observeUpdateAddress()
            observeDeleteAddress()
            onImgCloseClick()
        }
    }

    private fun observeDeleteAddress() {
        viewModel.deleteAddress.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Loading -> {
                    showLoading()
                    return@Observer
                }

                is Resource.Success -> {
                    hideLoading()
                    findNavController().navigateUp()
                    viewModel.deleteAddress.postValue(null)
                    return@Observer
                }

                is Resource.Error -> {
                    hideLoading()
                    Log.e(TAG, response.message.toString())
                    Toast.makeText(activity, "Error occurred", Toast.LENGTH_SHORT).show()
                    return@Observer
                }
            }
        })    }

    private fun observeUpdateAddress() {
        viewModel.updateAddress.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Loading -> {
                    showLoading()
                    return@Observer
                }

                is Resource.Success -> {
                    hideLoading()
                    findNavController().navigateUp()
                    viewModel.updateAddress.postValue(null)
                    return@Observer
                }

                is Resource.Error -> {
                    hideLoading()
                    Log.e(TAG, response.message.toString())
                    Toast.makeText(activity, "Error occurred", Toast.LENGTH_SHORT).show()
                    return@Observer
                }
            }
        })
    }

    private fun updateAddress(oldAddress: Address) {
        binding.save.setOnClickListener {
            binding.apply {
                val title = location.text.toString()
                val fullName = fullName.text.toString()
                val street = street.text.toString()
                val phone = phone.text.toString()
                val city = city.text.toString()
                val state = state.text.toString()

                val newAddress = Address(title, fullName, street, phone, city, state)
                viewModel.updateAddress(oldAddress,newAddress)
            }
        }
    }

    private fun setInformation(address: Address) {
        binding.apply {
            location.setText(address.addressTitle)
            fullName.setText(address.fullName)
            phone.setText(address.phone)
            city.setText(address.city)
            state.setText(address.state)
            street.setText(address.street)

            save.text = resources.getText(R.string.g_update)
        }
    }

    private fun observeAddAddress() {
        viewModel.addAddress.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Loading -> {
                    showLoading()
                    return@Observer
                }

                is Resource.Success -> {
                    hideLoading()
                    findNavController().navigateUp()
                    viewModel.addAddress.postValue(null)
                    return@Observer
                }

                is Resource.Error -> {
                    hideLoading()
                    Log.e(TAG, response.message.toString())
                    Toast.makeText(activity, "Error occurred", Toast.LENGTH_SHORT).show()
                    return@Observer
                }
            }
        })
    }

    private fun hideLoading() {
        binding.apply {
            save.visibility = View.VISIBLE

        }
    }

    private fun showLoading() {
        binding.apply {
            save.visibility = View.INVISIBLE
        }
    }

    private fun onSaveClick() {
        binding.apply {
            save.setOnClickListener {
                val title = location.text.toString()
                val fullName = fullName.text.toString()
                val street = street.text.toString()
                val phone = phone.text.toString()
                val city = city.text.toString()
                val state = state.text.toString()

                if (title.isEmpty() || fullName.isEmpty() || street.isEmpty() ||
                    phone.isEmpty() || city.isEmpty() || state.isEmpty()
                ) {
                    Toast.makeText(
                        activity,
                        "Make sure you filled all requirements",
                        Toast.LENGTH_LONG
                    ).show()
                    return@setOnClickListener
                }

                val address = Address(title, fullName, street, phone, city, state)
                viewModel.saveAddress(address)
            }
        }
    }

    private fun onImgCloseClick() {
        binding.closeIcon.setOnClickListener {
            findNavController().navigateUp()
        }
    }

}