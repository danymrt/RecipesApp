package com.example.recipesapp.ui.profile

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.NewInstanceFactory.Companion.instance
import androidx.viewpager2.widget.ViewPager2
import com.example.recipesapp.MainActivity
import com.example.recipesapp.R
import com.example.recipesapp.R.drawable.ic_baseline_person_24
import com.example.recipesapp.databinding.FragmentProfileBinding
import com.example.recipesapp.ui.search.SearchViewModel
import com.facebook.login.LoginManager
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.squareup.picasso.Picasso


class MainProfileFragment : Fragment() {

    private val profileViewModel: ProfileViewModel by activityViewModels()
    private val searchViewModel: SearchViewModel by activityViewModels()
    private var _binding: FragmentProfileBinding? = null

    private lateinit var viewPager : ViewPager2 // creating object of ViewPager
    private lateinit var tabLayout : TabLayout  // creating object of TabLayout

    val toolbarArray = arrayOf(
        "User",
        "Favourite",
        "Shopping List"
    )
     val tabIcons = intArrayOf(
        ic_baseline_person_24,
        R.drawable.ic_baseline_favorite_24,
        R.drawable.ic_baseline_shopping_basket_24
    )

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        //Set the image
        var pref_user = MainActivity.applicationContextLogin()
        if(pref_user!!.getString("url","").toString()=="null"){
            Picasso.get().load(R.drawable.blank_profile_picture).into(binding.imageProfile)
        }else{
            Picasso.get().load(pref_user!!.getString("url","")).into(binding.imageProfile)
        }

        profileViewModel.name.observe(viewLifecycleOwner){
            binding.nameUser.text = it
        }

        profileViewModel.familyName.observe(viewLifecycleOwner){
            binding.familyNameUser.text = it
        }

        //Get elements
        tabLayout = binding.tabLayout
        viewPager  = binding.viewPager

        // Initializing the ViewPagerAdapter
        val adapter = ViewPagerAdapter(requireActivity())
        viewPager.adapter = adapter

        // add fragment to the list
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = toolbarArray[position]
            tab.setIcon(tabIcons[position])

        }.attach()

        var pref = MainActivity.applicationContextKey()
        val logout_buttom = binding.buttonLogout
        logout_buttom.setOnClickListener {
            // Re-Do login and set the preference to false
            pref!!.edit().putBoolean(MainActivity.LOGIN_KEY, false).apply()
            pref?.edit()?.putString("id", "")?.apply()
            //LogoutFacebook
            LoginManager.getInstance().logOut()
            startActivity(Intent(it.context, MainActivity::class.java))
            requireActivity().finish()
            searchViewModel.restartRecipe()
            Toast.makeText(context, "Succefully signed out", Toast.LENGTH_LONG).show()
        }

        var modify_button = binding.buttonModify
        modify_button.setOnClickListener {
            profileViewModel.updateModifyButton(true)
        }

        //Enable the button for modifying the user profile
        profileViewModel.enableButton.observe(viewLifecycleOwner, Observer {
            modify_button.isEnabled = it
        })

        return root

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}