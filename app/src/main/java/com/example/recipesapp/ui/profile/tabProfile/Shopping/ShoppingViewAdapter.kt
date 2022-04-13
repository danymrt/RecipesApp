package com.example.recipesapp.ui.profile.tabProfile.Shopping

import android.content.Context
import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.recipesapp.data.ShoppingItem
import com.squareup.picasso.Picasso


class ShoppingViewAdapter(recyclerDataArrayList: ArrayList<ShoppingItem>, mcontext: Context) :
    RecyclerView.Adapter<ShoppingViewAdapter.RecyclerViewHolder>() {
    private val itemsDataArrayList: ArrayList<ShoppingItem> = recyclerDataArrayList
    private val mcontext: Context = mcontext
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        // Inflate Layout
        val view = LayoutInflater.from(parent.context).inflate(com.example.recipesapp.R.layout.item_shopping, parent, false)
        return RecyclerViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        val recyclerData: ShoppingItem = itemsDataArrayList[position]
        holder.itemName.text = recyclerData.name
        Picasso.get().load(recyclerData.image).into(holder.itemImage)
    }

    override fun getItemCount(): Int {
        return itemsDataArrayList.size
    }

    inner class RecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal val itemName: TextView = itemView.findViewById(com.example.recipesapp.R.id.idItemName)
        internal val itemImage: ImageView? = itemView.findViewById(com.example.recipesapp.R.id.image_ingredient)
    }
}