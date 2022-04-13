package com.example.recipesapp

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.recipesapp.data.model.Recipe
import com.squareup.picasso.Picasso
import com.example.recipesapp.data.Recipes
import com.example.recipesapp.data.api.RecipeApiService
import com.example.recipesapp.data.api.RetrofitBuilder
import com.example.recipesapp.ui.FirebaseService
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class RecipeAdapter(val recipeList: ArrayList<Recipe>) : RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder>() {
    private lateinit var mListener: OnItemClickListener

    // Describes an item view and its place within the RecyclerView
    class RecipeViewHolder(itemView: View , listener: OnItemClickListener, recipeList2: ArrayList<Recipe>) : RecyclerView.ViewHolder(itemView) {
        private var recipeImgView: ImageView = itemView.findViewById(R.id.image)
        private var name_recipe: TextView = itemView.findViewById(R.id.name_recipe)
        private var num_likes : TextView = itemView.findViewById(R.id.likes)
        private var feasibility : TextView = itemView.findViewById(R.id.missed_ingredients)
        private var favbtn : Button = itemView.findViewById(R.id.fav_btn)
        var recipe : CardView = itemView.findViewById(R.id.recipe)
        val sharedPref = MainActivity.applicationContextLogin()
        var recipelist = recipeList2

        val BASE_URL = "https://danymrt.pythonanywhere.com"

        fun bind(img_url: String, name: String, likes: String, feasiblity: String, position: Int) {
            Picasso.get().load(img_url).into(recipeImgView)
            name_recipe.text = name
            num_likes.text = likes + " "
            val percentage = feasiblity+"% "
            feasibility.text = percentage
            //Load the recipe that we have already insert in the fav
            if(recipelist[position].fav == "1"){
                favbtn.setBackgroundResource(R.drawable.ic_baseline_favorite_orange)
                Log.d("pieno0", recipelist[position].ID)
            }

            //Insert or remove the Recipe from the favourite
            val id_user = sharedPref?.getString("id","")
            val recipe_current_item = recipelist[position]
            val service = RetrofitBuilder.makeRetrofitService(BASE_URL).create(RecipeApiService::class.java)
            CoroutineScope(Dispatchers.IO).launch {
                val response = service.getRecipes()
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        val list_recipes = response.body()?.Recipes
                        for (elem in list_recipes!!){
                            if(elem.id_recipe == recipe_current_item.ID && elem.id_user == id_user){
                                recipe_current_item.fav = "1"
                                favbtn.setBackgroundResource(R.drawable.ic_baseline_favorite_orange)
                                Log.d("pieno1", elem.id.toString())
                                break
                            }else{
                                recipe_current_item.fav = "0"
                                favbtn.setBackgroundResource(R.drawable.ic_baseline_favorite_shadow_24)
                                Log.d("vuoto1", recipe_current_item.ID)
                            }
                        }
                    }else{
                        Log.d("DBRecipe3", response.message())
                    }
                }
            }

        }


        init {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
            favbtn.setOnClickListener{
                val id_user = sharedPref?.getString("id","")
                Log.d("id_user", id_user.toString())
                val position_item = adapterPosition
                var recipe_item = recipeList2[position_item]
                val id_recipe = recipe_item.ID
                val id = id_user+ id_recipe
                Log.d("id_recipe", id_recipe)
                var fav = recipeList2[position_item].fav
                FirebaseMessaging.getInstance().token.addOnSuccessListener {
                    val fav_recipe = Recipes(id,id_recipe,id_user,recipe_item.name ,recipe_item.img, recipe_item.likes, it)
                    val service = RetrofitBuilder.makeRetrofitService(BASE_URL).create(RecipeApiService::class.java)
                    CoroutineScope(Dispatchers.IO).launch {
                        if(fav=="0") {
                            val response = service.addRecipe(fav_recipe)
                            withContext(Dispatchers.Main) {
                                if (response.isSuccessful) {
                                    Log.d("ok", response.message())
                                    recipeList2[position_item].fav = "1"
                                    favbtn.setBackgroundResource(R.drawable.ic_baseline_favorite_orange)
                                    Log.d("pieno2", id_recipe)
                                } else {
                                    Log.d("DBRecipe1", response.message())
                                }
                            }
                        }else{      //fav==1
                            val response = service.deleteRecipe(id)
                            Log.d("DBRecipe2", id)
                            withContext(Dispatchers.Main) {
                                if (response.isSuccessful) {
                                    recipeList2[position_item].fav = "0"
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
        }


    }

    // Returns a new ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recipe_item, parent, false)
        return RecipeViewHolder(view, mListener, recipeList)
    }

    // Returns size of data list
    override fun getItemCount(): Int {
        return recipeList.size
    }

    // Displays data at a certain position
    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        holder.bind(recipeList[position].img, recipeList[position].name, recipeList[position].likes, recipeList[position].feasibility, position)
    }

    fun clear() {
        val size: Int = recipeList.size
        recipeList.clear()
        notifyItemRangeRemoved(0, size)
    }


    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }


    fun SetOnItemCliclListener(listener: OnItemClickListener){
        mListener = listener
    }


}