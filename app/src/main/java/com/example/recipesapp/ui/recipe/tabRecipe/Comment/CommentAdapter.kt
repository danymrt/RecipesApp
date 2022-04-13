package com.example.recipesapp.ui.recipe.tabRecipe.Comment

import android.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.example.recipesapp.data.model.Comment
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.squareup.picasso.Picasso

class CommentAdapter(options: FirebaseRecyclerOptions<Comment?>?) :
    FirebaseRecyclerAdapter<Comment, CommentAdapter.commentViewHolder>(options!!) {
    override fun onBindViewHolder(holder: commentViewHolder, position: Int, model: Comment) {
        var n = model.user!!.name + " " + model.user!!.familyName
        holder.bind(model.user!!.image!!, n, model.comment, model.dataCreated)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): commentViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(com.example.recipesapp.R.layout.item_comment, parent, false)
        return commentViewHolder(view)
    }

    class commentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var userImg: ImageView = itemView.findViewById(com.example.recipesapp.R.id.user_pic)
        private var name_user: TextView =
            itemView.findViewById(com.example.recipesapp.R.id.name_text)
        private var comment: TextView =
            itemView.findViewById(com.example.recipesapp.R.id.comment_text)
        private var date: TextView = itemView.findViewById(com.example.recipesapp.R.id.date_text)

        fun bind(img_url: String, _name: String, _comment: String, _date: String) {
            Picasso.get().load(img_url).into(userImg)
            name_user.text = _name
            comment.text = _comment
            date.text = _date
        }

    }
}