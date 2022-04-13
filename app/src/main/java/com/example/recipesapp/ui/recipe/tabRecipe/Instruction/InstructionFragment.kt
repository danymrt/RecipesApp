package com.example.recipesapp.ui.recipe.tabRecipe.Instruction

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recipesapp.MainActivity
import com.example.recipesapp.R
import com.example.recipesapp.databinding.FragmentInstructionBinding
import com.example.recipesapp.ui.profile.ProfileViewModel
import com.example.recipesapp.ui.search.SearchViewModel
import com.squareup.picasso.Picasso
import kotlin.collections.ArrayList

/**
 * Loads the instructions in a Recycler View
 */
class InstructionFragment : Fragment() {
    private var _binding: FragmentInstructionBinding? = null
    private val binding get() = _binding!!
    private lateinit var ID:String
    private val searchViewModel: SearchViewModel by activityViewModels()
    private val profileViewModel: ProfileViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentInstructionBinding.inflate(inflater, container, false)
        val view = binding.root
        val recyclerview = binding.recyclerViewInstructions
        val noinstructions = binding.noinstructions

        val position = searchViewModel.position.value
        val list_instructions  = ArrayList<String>()
        lateinit var myAdapter: InstructionAdapter
        val flag = searchViewModel.flag.value

        //Called by the FavouriteFragment
        if(flag==1){
            val recipes = profileViewModel.favoriterecipes.value?.get(position!!)
            ID = recipes!!.id_recipe.toString()
        }else{ //Called by the SearchFragment
            val recipe = searchViewModel.recipes.value?.get(position!!)
            ID = recipe!!.ID
        }


        searchViewModel.getInstructions(ID).observe(viewLifecycleOwner, Observer {
            list_instructions.clear()
            for (elem in it){
                Log.d("instructions: ", elem.step.toString())
                val instruction = elem.step.toString()
                list_instructions.add(instruction)
            }
            if(list_instructions.size == 0){
                noinstructions!!.text = "No instrunctions found..."
            }else{
                noinstructions!!.text=""
            }
            recyclerview.layoutManager = LinearLayoutManager(requireContext())
            recyclerview.setHasFixedSize(true)
            myAdapter = InstructionAdapter(list_instructions)
            recyclerview.adapter = myAdapter
        })


        return view
    }

}