package kh.edu.rupp.ite.e_shopping.ui.view.fragment.applunch

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import kh.edu.rupp.ite.e_shopping.R
import kh.edu.rupp.ite.e_shopping.databinding.FragmentSplashScreenBinding
import kh.edu.rupp.ite.e_shopping.ui.view.activity.MainActivity
import kh.edu.rupp.ite.e_shopping.ui.view.activity.ShoppingActivity

@SuppressLint("CustomSplashScreen")
class MySplashScreen : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentSplashScreenBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModel = (activity as MainActivity).viewModel
        val isUserSignedIn = viewModel.isUserSignedIn()
        val handler = Handler(Looper.getMainLooper())

        if (isUserSignedIn) {
            val intent = Intent(requireActivity(), ShoppingActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            handler.postDelayed({
                startActivity(intent)
            }, 1500)
        } else {
            handler.postDelayed({
                findNavController().navigate(R.id.action_mySplashScreen_to_firstScreenFragment)
            }, 1500)
        }
    }
}
