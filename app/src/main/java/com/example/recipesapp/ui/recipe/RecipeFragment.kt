package com.example.recipesapp.ui.recipe

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.viewpager2.widget.ViewPager2
import com.example.recipesapp.MainActivity
import com.example.recipesapp.R
import com.example.recipesapp.data.Recipes
import com.example.recipesapp.data.api.RecipeApiService
import com.example.recipesapp.data.api.RetrofitBuilder
import com.example.recipesapp.databinding.FragmentRecipeBinding
import com.example.recipesapp.ui.profile.ProfileViewModel
import com.example.recipesapp.ui.search.SearchViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.messaging.FirebaseMessaging
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Main Page for the Recipe where we take all the informations and
 * we manage the favourite button by adding the recipe in our database
 */

class RecipeFragment: Fragment() {

    private var _binding: FragmentRecipeBinding? = null
    private val binding get() = _binding!!
    private val searchViewModel: SearchViewModel by activityViewModels()
    private val profileViewModel: ProfileViewModel by activityViewModels()
    val sharedPref = MainActivity.applicationContextLogin()
    val BASE_URL = "https://danymrt.pythonanywhere.com"

    private lateinit var viewPager : ViewPager2 // creating object of ViewPager
    private lateinit var tabLayout : TabLayout  // creating object of TabLayout
    private lateinit var imagerecipe : ImageView
    private lateinit var titlerecipe : TextView
    private lateinit var summary: TextView
    private lateinit var serving: TextView
    private lateinit var timing : TextView
    private lateinit var calories : TextView
    private lateinit var favbtn : Button
    private var recipe = null

    val toolbarArray = arrayOf(
        "Ingredient",
        "Instruction",
        "Blog"
    )
    val tabIcons = intArrayOf(
        R.drawable.ic_baseline_food_bank_24,
        R.drawable.ic_baseline_menu_book_24,
        R.drawable.ic_baseline_insert_comment_24
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRecipeBinding.inflate(inflater, container, false)
        val view = binding.root

        //Get elements
        tabLayout = binding.tabLayoutRecipe
        viewPager = binding.viewPagerRecipe
        imagerecipe = binding.image
        titlerecipe = binding.titlerecipe
        summary = binding.summary
        summary.movementMethod = ScrollingMovementMethod()
        serving = binding.serving
        timing = binding.time
        calories = binding.calories
        favbtn = binding.favourite as Button

        // Initializing the ViewPagerAdapter
        val adapter = RecipePagerAdapter(requireActivity())
        viewPager.adapter = adapter

        // add fragment to the list
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = toolbarArray[position]
            tab.setIcon(tabIcons[position])

        }.attach()

        val position= requireArguments().getInt("position")
        val flag = requireArguments().getInt("flag")
        val img : String
        val title : String
        val id_recipe : String
        val like : String
        var fav = "0"
        Log.d("position1", position.toString())
        searchViewModel.setFlag(flag)
        searchViewModel.setPosition(position)
        if(flag==1){ //0 from FavouriteFragment
            val recipe = profileViewModel.favoriterecipes.value?.get(position)
            img = recipe?.img!!
            title = recipe.name!!
            id_recipe = recipe.id_recipe!!
            like = recipe.like.toString()
            fav = "1"
            favbtn.setBackgroundResource(R.drawable.ic_baseline_favorite_dark)
        }else{  //0 from FavouriteFragment
            val recipe = searchViewModel.recipes.value?.get(position)
            img = recipe?.img!!
            title = recipe.name
            id_recipe = recipe.ID
            like = recipe.likes
            if(recipe.fav == "1"){
                favbtn.setBackgroundResource(R.drawable.ic_baseline_favorite_dark)
                fav="1"
            }else{
                fav ="0"
            }
        }

        Picasso.get().load(img).into(imagerecipe)
        titlerecipe.text = title
        searchViewModel.getSummaryRecipe(id_recipe).observe(viewLifecycleOwner, Observer{
            val styledText: CharSequence = HtmlCompat.fromHtml(it[0], HtmlCompat.FROM_HTML_MODE_LEGACY)
            summary.text = styledText
            timing.text = it[1]
            serving.text = it[2]
        })

        searchViewModel.getCaloriesRecipe(id_recipe).observe(viewLifecycleOwner,Observer{
            calories.text = it
        })

        favbtn.setOnClickListener {
            val id_user = sharedPref?.getString("id", "")
            val id = id_user + id_recipe
            FirebaseMessaging.getInstance().token.addOnSuccessListener {
                val fav_recipe = Recipes(id, id_recipe, id_user, title, img, like, it)
                val service = RetrofitBuilder.makeRetrofitService(BASE_URL).create(RecipeApiService::class.java)
                CoroutineScope(Dispatchers.IO).launch {
                    if (fav == "0") {
                        val response = service.addRecipe(fav_recipe)
                        withContext(Dispatchers.Main) {
                            if (response.isSuccessful) {
                                Log.d("ok", response.message())
                                if(flag==0){
                                    val recipe = searchViewModel.recipes.value?.get(position)
                                    recipe!!.fav = "1"
                                }
                                fav = "1"
                                favbtn.setBackgroundResource(R.drawable.ic_baseline_favorite_dark)
                                Log.d("pieno2", id_recipe)
                            } else {
                                Log.d("DBRecipe1", response.message())
                            }
                        }
                    } else {
                        val response = service.deleteRecipe(id)
                        Log.d("DBRecipe2", id)
                        withContext(Dispatchers.Main) {
                            if (response.isSuccessful) {
                                if(flag==0){
                                    val recipe = searchViewModel.recipes.value?.get(position)
                                    recipe!!.fav = "0"
                                }
                                fav = "0"
                                favbtn.setBackgroundResource(R.drawable.ic_baseline_favorite_shadow_24)
                                Log.d("vuoto2", id_recipe)
                            }else{
                                Log.d("DBRecipe2", response.message())
                            }
                        }
                    }
                }
            }
        }
        return view
    }

}

