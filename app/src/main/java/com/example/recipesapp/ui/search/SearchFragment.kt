package com.example.recipesapp.ui.search

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.recipesapp.R
import com.example.recipesapp.RecipeAdapter
import com.example.recipesapp.data.model.Recipe
import com.example.recipesapp.databinding.FragmentSearchBinding
import com.example.recipesapp.ui.FirebaseService

/**
 * Search in the editText the ingredients and get all the recipes in a recyclerView
 * by making a call to the SpoonacularApi
 */

class SearchFragment : Fragment(){

    private lateinit var searchViewModel: SearchViewModel
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    var btnHit: Button? = null
    var search: EditText? = null

    companion object {
        val APP_KEY2 = R.string.api_key_spoonacular
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        searchViewModel = ViewModelProvider(this).get(SearchViewModel::class.java)

        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val imageBase: ImageView = binding.logoSearch
        search = binding.search
        btnHit = binding.btnHit
        var recyclerview: RecyclerView = binding.recyclerView
        lateinit var myAdapter : RecipeAdapter

        imageBase.visibility = View.VISIBLE

        searchViewModel.recipes.observe(viewLifecycleOwner, Observer { recipes ->
            recyclerview.also {
                if(resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT){
                    it.layoutManager = GridLayoutManager(requireContext(),1)
                }else{
                    it.layoutManager = GridLayoutManager(requireContext(),2)
                }
                it.setHasFixedSize(true)
                myAdapter = RecipeAdapter(recipes)
                it.adapter = myAdapter
                myAdapter.SetOnItemCliclListener(object : RecipeAdapter.OnItemClickListener{
                    override fun onItemClick(position: Int) {
                        findNavController().navigate(R.id.action_navigation_search_to_recipeFragment, Bundle().apply {
                            putInt("position",position)
                            putInt("flag", 0)
                        })
                    }
                })
            }
        })

        var path_request = "https://api.spoonacular.com/recipes/findByIngredients?apiKey=$APP_KEY2&ingredients="

        val activity: FragmentActivity? = activity
        btnHit!!.setOnClickListener {
            var count_search = searchViewModel.count.value
            if(count_search!! > 0){
                myAdapter.clear()
            }
            val ingredients = search!!.text.toString()
            if(ingredients.length !=0) {
                imageBase.visibility = View.GONE
                val list_ingredients = clear_string(ingredients)
                for (elem in list_ingredients) {
                    if (elem != list_ingredients.get(list_ingredients.size - 1)) {
                        path_request = path_request + elem + ","
                    }else{
                        path_request = path_request + elem + "&number=10"
                    }
                }
                hideKeyboard()
                SearchViewModel.JsonTask(activity).execute(path_request)
                path_request = "https://api.spoonacular.com/recipes/findByIngredients?apiKey=$APP_KEY2&ingredients="
            }else{
                Toast.makeText(getActivity(), "Please insert ingredients", Toast.LENGTH_LONG).show()
            }
            
        }
        return root
    }

    fun clear_string(text: String): Array<String> {
        var pos = 0
        for( (i,x) in text.withIndex()){
            if (x.isDigit()) {
                pos = i
                break
            }
        }
        var new_str = text.subSequence(pos,text.length).toString()
        new_str= new_str.replace(",", " ")
        new_str = new_str.replace("\\s+".toRegex(), " ")
        if(new_str[0]== ' '){
            new_str = new_str.subSequence(1,new_str.length).toString()}
        val list_ingredients = new_str.split("\\s".toRegex()).toTypedArray()
        return list_ingredients
    }

    fun Fragment.hideKeyboard() {
        view?.let { activity?.hideKeyboard(it) }
    }

    fun Context.hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    override fun onStart() {
        super.onStart()
        if(FirebaseService.NOTIFICATION_ID){
            FirebaseService.NOTIFICATION_ID=false
            findNavController().navigate(R.id.action_navigation_search_to_navigation_profile)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}