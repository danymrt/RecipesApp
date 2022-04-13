package com.example.recipesapp.ui.recipe.tabRecipe.Ingredient

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.recipesapp.MainActivity
import com.example.recipesapp.R
import com.example.recipesapp.data.ShoppingItem
import com.example.recipesapp.ui.profile.ProfileViewModel
import com.squareup.picasso.Picasso

class IngredientAdapter(
    val ingredientList: ArrayList<IngredientFragment.Ingredient_class>,
    val shopping_list: ArrayList<ShoppingItem>?,
    val profileViewModel: ProfileViewModel
) : RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder>() {

    class IngredientViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var ingredientImg: ImageView = itemView.findViewById(R.id.ingredient_pic)
        private var name_ingredient: TextView = itemView.findViewById(R.id.ingredient)
        var btn_add: ImageButton = itemView.findViewById(R.id.button_add_shopping)
        var btn_shopping : ImageButton = itemView.findViewById(R.id.added)

        fun bind(img_url: String, ingredient: String) {
            Picasso.get().load(img_url).into(ingredientImg)
            name_ingredient.text = ingredient
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_ingredient, parent, false)
        return IngredientViewHolder(view)
    }

    override fun onBindViewHolder(holder: IngredientViewHolder, position: Int) {
        var image = ingredientList[position].img
        var name = ingredientList[position].ingredient
        var pref_user = MainActivity.applicationContextLogin()

        var id = pref_user!!.getString("id", "")
        holder.bind(image, name)

        for(item in shopping_list!!){
            if(item.name == name){
                holder.btn_add.visibility = View.INVISIBLE
                holder.btn_shopping.visibility = View.VISIBLE
            }
        }

        //When the add button is clicked we add the element in the User ShoppingList
        holder.btn_add.setOnClickListener {
            var item = ShoppingItem(image,name)
            profileViewModel.add_element_shopping(profileViewModel.id.value!!,shopping_list.size,item)
            holder.btn_add.visibility = View.INVISIBLE
            holder.btn_shopping.visibility = View.VISIBLE

        }
        
    }

    override fun getItemCount(): Int {
        return ingredientList.size
    }

}