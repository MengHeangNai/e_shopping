package kh.edu.rupp.ite.e_shopping.ui.view.fragment.applunch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import kh.edu.rupp.ite.e_shopping.R
import kh.edu.rupp.ite.e_shopping.databinding.FragmentSecondScreenBinding

class SecondScreenFragment: Fragment() {
    private lateinit var binding: FragmentSecondScreenBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSecondScreenBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSplashRegister.setOnClickListener {
            try {
                findNavController().navigate(R.id.action_secondScreenFragment_to_registerFragment)
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Navigation error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnSplashSignIn.setOnClickListener {
            try {
                findNavController().navigate(R.id.action_secondScreenFragment_to_loginFragment)
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Navigation error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }

    }
}