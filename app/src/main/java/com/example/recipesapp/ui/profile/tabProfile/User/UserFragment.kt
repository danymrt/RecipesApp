package com.example.recipesapp.ui.profile.tabProfile.User

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.recipesapp.MainActivity
import com.example.recipesapp.MainActivity.Companion.LOGIN_KEY
import com.example.recipesapp.databinding.FragmentUserBinding
import com.example.recipesapp.ui.main.SearchActivity
import com.example.recipesapp.ui.profile.MainProfileFragment
import com.example.recipesapp.ui.profile.ProfileViewModel
import com.google.android.material.shape.ShapeAppearanceModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class UserFragment : Fragment() {

    private val profileViewModel: ProfileViewModel by activityViewModels()
    private var _binding: FragmentUserBinding? = null
    private val binding get() = _binding!!

    lateinit var usernameEdit : String
    lateinit var nameEdit : String
    lateinit var familyNameEdit : String
    lateinit var emailEdit : String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentUserBinding.inflate(inflater, container, false)
        val view = binding.root

        /**
         * Take the User's data from the ProfileViewModel and
         * if the pencil button is clicked, we can modify the fields and update the data
         */

        profileViewModel.username.observe(viewLifecycleOwner){
            binding.userName.text = it
            usernameEdit = it
        }

        profileViewModel.name.observe(viewLifecycleOwner){
            binding.firstName.text = it
            nameEdit = it
        }

        profileViewModel.familyName.observe(viewLifecycleOwner){
            binding.familyName.text = it
            familyNameEdit = it
        }

        profileViewModel.email.observe(viewLifecycleOwner){
            binding.email.text = it
            emailEdit = it
        }

        profileViewModel.modifyButton.observe(viewLifecycleOwner){
            if(it == true){
                binding.editName.visibility = View.VISIBLE
                binding.firstName.visibility = View.INVISIBLE

                binding.editFamilyName.visibility = View.VISIBLE
                binding.familyName.visibility = View.INVISIBLE

                binding.editUsername.visibility = View.VISIBLE
                binding.userName.visibility = View.INVISIBLE

                binding.buttonSave.visibility = View.VISIBLE
            }
        }

        val save_buttom = binding.buttonSave
        save_buttom.setOnClickListener{
            //Variable to check if at least one variable is changed, and in that case the
            var check = false

            //If one values is different set the variable and update the database
            if(binding.editUsername.text.toString() != "") {
                usernameEdit = binding.editUsername.text.toString()
                check = true
            }
            if(binding.editName.text.toString() != ""){
                nameEdit = binding.editName.text.toString()
                check = true
            }
            if(binding.editFamilyName.text.toString() != ""){
                familyNameEdit = binding.editFamilyName.text.toString()
                check = true
            }

            if(check){
                profileViewModel.update(usernameEdit,nameEdit,familyNameEdit,emailEdit)
                check = false
            }

            //Change the visibility

            binding.editName.visibility = View.INVISIBLE
            binding.firstName.visibility = View.VISIBLE

            binding.editFamilyName.visibility = View.INVISIBLE
            binding.familyName.visibility = View.VISIBLE

            binding.editUsername.visibility = View.INVISIBLE
            binding.userName.visibility = View.VISIBLE

            binding.buttonSave.visibility = View.INVISIBLE

            profileViewModel.updateModifyButton(false)
        }


        return view
    }

    override fun onResume() {
        super.onResume()
        //Enable the button for modifing the profile
        profileViewModel.setButton(true)
    }

}
