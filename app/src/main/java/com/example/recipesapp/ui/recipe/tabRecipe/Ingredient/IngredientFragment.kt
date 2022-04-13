package com.example.recipesapp.ui.recipe.tabRecipe.Ingredient

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.recipesapp.data.model.Recipe
import com.example.recipesapp.databinding.FragmentIngredientBinding
import com.example.recipesapp.ui.profile.ProfileViewModel
import com.example.recipesapp.ui.search.SearchViewModel

/**
 * Show the ingredients of the recipe in a RecyclerView
 */

class IngredientFragment : Fragment() {
    private var _binding: FragmentIngredientBinding? = null
    private val binding get() = _binding!!
    private val searchViewModel: SearchViewModel by activityViewModels()
    private val profileViewModel: ProfileViewModel by activityViewModels()
    lateinit var recipe: Recipe
    var img_url = "https://spoonacular.com/cdn/ingredients_100x100/"

    class Ingredient_class(var ingredient: String, var img: String) {}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentIngredientBinding.inflate(inflater, container, false)
        val view = binding.root
        var recyclerview: RecyclerView = binding.recyclerViewIngredient
        val ingredientInfo  = ArrayList<Ingredient_class>()
        val flag = searchViewModel.flag.value
        val position = searchViewModel.position.value
        var ingredients = ArrayList<Map<String,String>>()

        //Open the page from the Favourite fragment
        if(flag==1){
            var recipes = profileViewModel.favoriterecipes.value?.get(position!!)
            Log.d("recipes name", recipes!!.name.toString())
            recipe = Recipe(recipes.name!!, recipes.id_recipe!!)
            searchViewModel.getInfoRecipe(recipes.id_recipe).observe(viewLifecycleOwner, Observer{
                ingredients.clear()
                ingredientInfo.clear()
                for((j,elem) in it.withIndex()){
                    Log.d("ingredient", j.toString()+":"+elem.original)
                    var ingredient = elem.original.toString()
                    var img = img_url+elem.image.toString()
                    //recipe.ingredients.add(j, mapOf(ingredient to img))
                    ingredients.add(j, mapOf(ingredient to img))
                }
                recipe.ingredients = ingredients
                Log.d("ingredients size", ingredients.size.toString())

                for(i in 0 until ingredients!!.size){
                    var ingredient_info = Ingredient_class(ingredients[i].keys.elementAt(0), ingredients[i].values.elementAt(0))
                    ingredientInfo.add(ingredient_info)
                }
                Log.d("size ingredientInfo", ingredientInfo.size.toString())

                profileViewModel.shopping.observe(viewLifecycleOwner){ listshop->
                    recyclerview.also {
                        if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                            it.layoutManager = GridLayoutManager(requireContext(), 1)
                        } else {
                            it.layoutManager = GridLayoutManager(requireContext(), 2)
                        }
                        recyclerview.setHasFixedSize(true)
                        var myAdapter = IngredientAdapter(ingredientInfo, listshop, profileViewModel)
                        recyclerview.adapter = myAdapter
                    }
                }
            })

        //Open the page from the Search fragment
        }else{
            recipe = searchViewModel.recipes.value?.get(position!!)!!
            ingredients = recipe?.ingredients

            for(i in 0 until ingredients!!.size){
                var ingredient_info = Ingredient_class(ingredients[i].keys.elementAt(0), ingredients[i].values.elementAt(0))
                ingredientInfo.add(ingredient_info)
            }

            profileViewModel.shopping.observe(viewLifecycleOwner){ listshop->
                recyclerview.also {
                    if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                        it.layoutManager = GridLayoutManager(requireContext(), 1)
                    } else {
                        it.layoutManager = GridLayoutManager(requireContext(), 2)
                    }
                    recyclerview.setHasFixedSize(true)
                    var myAdapter = IngredientAdapter(ingredientInfo, listshop, profileViewModel)
                    recyclerview.adapter = myAdapter
                }
            }

        }
        return view
    }

}