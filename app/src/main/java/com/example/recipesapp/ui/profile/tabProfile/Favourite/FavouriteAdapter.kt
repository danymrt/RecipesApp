package com.example.recipesapp.ui.profile.tabProfile.Favourite

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.recipesapp.R
import com.example.recipesapp.data.model.Recipe
import com.squareup.picasso.Picasso

class FavouriteAdapter(val list_favourite : ArrayList<Recipe>) : RecyclerView.Adapter<FavouriteAdapter.FavouriteViewHolder>(){

    private lateinit var mListener: OnItemClickListener
    private lateinit var favListener : OnFavClickListener

    class FavouriteViewHolder(itemView: View, listener: OnItemClickListener, list_favourite2: ArrayList<Recipe>, listener2:OnFavClickListener): RecyclerView.ViewHolder(itemView){
        private var name_recipe: TextView = itemView.findViewById(R.id.name_recipe)
        private var imgview: ImageView = itemView.findViewById(R.id.image)
        private var like: TextView = itemView.findViewById(R.id.likes)
        private var favbtn : Button = itemView.findViewById(R.id.fav_btn)

        fun bind(img: String, likes:String, name:String){
            Picasso.get().load(img).into(imgview)
            name_recipe.text = name
            like.text = likes
        }

        init{
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition )
            }
            //Delete item from recycle
            favbtn.setOnClickListener{
                listener2.onFavClick(adapterPosition)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recipe_favorite, parent, false)
        return FavouriteViewHolder(view, mListener, list_favourite, favListener)
    }

    override fun onBindViewHolder(holder: FavouriteViewHolder, position: Int) {
        var image = list_favourite[position].img
        var name = list_favourite[position].name
        var like = list_favourite[position].likes
        holder.bind(image, like, name)
    }

    override fun getItemCount(): Int {
        return list_favourite.size
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    fun SetOnItemCliclListener(listener: OnItemClickListener){
        mListener = listener
    }

    interface OnFavClickListener{
        fun onFavClick(position: Int)
    }
     fun SetOnFavClickListener(listener2: OnFavClickListener){
         favListener = listener2
     }

}