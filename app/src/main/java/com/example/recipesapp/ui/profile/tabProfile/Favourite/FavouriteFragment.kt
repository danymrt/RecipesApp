package com.example.recipesapp.ui.profile.tabProfile.Favourite

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recipesapp.MainActivity
import com.example.recipesapp.R
import com.example.recipesapp.RecipeAdapter
import com.example.recipesapp.data.api.RecipeApiService
import com.example.recipesapp.data.api.RetrofitBuilder
import com.example.recipesapp.data.model.Recipe
import com.example.recipesapp.databinding.FragmentFavouriteBinding
import com.example.recipesapp.ui.main.SearchActivity
import com.example.recipesapp.ui.profile.MainProfileFragment
import com.example.recipesapp.ui.profile.ProfileViewModel
import com.example.recipesapp.ui.recipe.tabRecipe.Ingredient.IngredientAdapter
import com.example.recipesapp.ui.search.SearchViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.http.POST

class FavouriteFragment : Fragment() {
    private var _binding: FragmentFavouriteBinding? = null
    private val binding get() = _binding!!
    private val profileViewModel: ProfileViewModel by activityViewModels()
    val sharedPref = MainActivity.applicationContextLogin()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavouriteBinding.inflate(inflater, container, false)
        val view = binding.root
        var recyclerView = binding.recyclerViewFavourite
        val list_favourite  = ArrayList<Recipe>()
        var list_ingredient = ArrayList<Map<String,String>>()

        /**
         * Get the list of the favourite recipe from the ViewModel and create on it the RecyclerView.
         * When the item is clicked, we moved in the RecipeFragment for showing the recipe
         * and we can remove the recipe from the favourite by tapping on the orange heart
         */
        profileViewModel.getFavouriteRecipe().observe(viewLifecycleOwner, Observer{
            list_favourite.clear()
            list_ingredient.clear()
            for((i,elem) in it.withIndex()){
                var recipe = Recipe(elem.name!!, elem.id_recipe!!)
                recipe.likes = elem.like!! + " "
                recipe.img = elem.img!!
                list_favourite.add(recipe)
            }
            recyclerView.setHasFixedSize(true)
            val myAdapter = FavouriteAdapter(list_favourite)
            if(resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT){
                recyclerView.layoutManager = GridLayoutManager(requireContext(),1)
            }else{
                recyclerView.layoutManager = GridLayoutManager(requireContext(),2)
            }
            recyclerView.adapter = myAdapter

            myAdapter.SetOnItemCliclListener(object : FavouriteAdapter.OnItemClickListener{
                override fun onItemClick(position : Int) {
                    findNavController().navigate(R.id.action_navigation_profile_to_recipeFragment, Bundle().apply {
                        putInt("position",position)
                        putInt("flag",1)
                    })
                }
            })

            myAdapter.SetOnFavClickListener(object  : FavouriteAdapter.OnFavClickListener{
                override fun onFavClick(position: Int) {
                    val id_user = sharedPref?.getString("id","")
                    var recipe_item = list_favourite[position]
                    val id_recipe = recipe_item.ID
                    val id = id_user+ id_recipe
                    profileViewModel.delete_element_fav(id, position)
                    list_favourite.removeAt(position)
                    myAdapter.notifyItemRemoved(position)
                }
            })
        })


        return view
    }


    override fun onResume() {
        super.onResume()
        //Disable the button for modifying the profile
        profileViewModel.setButton(false)
    }
}