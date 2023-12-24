package kh.edu.rupp.ite.e_shopping.ui.view.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import kh.edu.rupp.ite.e_shopping.databinding.FragmentLanguageBinding


class LanguageFragment : Fragment() {

    private lateinit var binding: FragmentLanguageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLanguageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onCloseImageClick()
    }

    private fun onCloseImageClick() {
        binding.imgCloseLanguage.setOnClickListener {
            findNavController().navigateUp()
        }
    }



}