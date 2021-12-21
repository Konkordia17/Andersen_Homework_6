package com.example.andersen_homework_6

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.andersen_homework_6.databinding.FragmentDetailInfoBinding

class DetailInfoFragment : Fragment(R.layout.fragment_detail_info) {
    private val args: DetailInfoFragmentArgs by navArgs()
    private lateinit var binding: FragmentDetailInfoBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailInfoBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.contactName.setText(args.user.name)
        binding.contactSurname.setText(args.user.surName)
        binding.contactPhone.setText(args.user.phone)
        Glide.with(view)
            .load(args.user.picture)
            .into(binding.contactImage)

        binding.changeContactButton.setOnClickListener {
            navigateBackToList()
        }
    }

    private fun navigateBackToList() {
        val name = binding.contactName.text.toString()
        val surname = binding.contactSurname.text.toString()
        val phone = binding.contactPhone.text.toString()
        val position = args.position
        val avatar = args.user.picture
        val user = User(name = name, surname, phone, avatar)
        val info = Pair(user, position)
        val savedStateHandle = findNavController().previousBackStackEntry?.savedStateHandle
        savedStateHandle?.set(ListFragment.RESULT_FROM_DETAIL_FRAGMENT, info)
        findNavController().navigateUp()
    }
}