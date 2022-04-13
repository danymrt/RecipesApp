package com.example.recipesapp.ui.recipe.tabRecipe.Comment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.recipesapp.data.Recipes
import com.example.recipesapp.data.Users
import com.example.recipesapp.data.api.NotificationApi
import com.example.recipesapp.data.api.RecipeApiService
import com.example.recipesapp.data.api.RetrofitBuilder
import com.example.recipesapp.data.model.Comment
import com.example.recipesapp.data.model.NotificationData
import com.example.recipesapp.data.model.PushNotification
import com.example.recipesapp.data.model.Recipe
import com.example.recipesapp.databinding.FragmentCommentBinding
import com.example.recipesapp.ui.profile.ProfileViewModel
import com.example.recipesapp.ui.search.SearchViewModel
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

private const val BASE_URL = "https://danymrt.pythonanywhere.com"
private const val BASE_URL_NOT = "https://fcm.googleapis.com"

/**
 * Insert a comment in a Firebase Realtime Database and send the notification to all the users
 * with that recipes in the favourite (by using the personal token)
 */

class CommentFragment : Fragment() {
    private var _binding: FragmentCommentBinding? = null
    private val binding get() = _binding!!

    private lateinit var recyclerView_comment: RecyclerView
    private lateinit var post_comment: Button
    private lateinit var editText_comment: EditText
    lateinit var postRef: DatabaseReference
    private lateinit var post_key: String
    lateinit var adapter: CommentAdapter

    private val searchViewModel: SearchViewModel by activityViewModels()
    private val profileViewModel: ProfileViewModel by activityViewModels()

    lateinit var text_comment: String
    lateinit var currentDate:String
    lateinit var model_recipe: Recipe

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCommentBinding.inflate(inflater, container, false)
        val view = binding.root

        val position = searchViewModel.position.value
        val flag = searchViewModel.flag.value
        if(flag==1){
            val model_recipes = profileViewModel.favoriterecipes.value?.get(position!!)
            post_key = model_recipes!!.id_recipe.toString()
            model_recipe = Recipe(model_recipes.name!!, model_recipes.id_recipe!!)
            model_recipe.likes = model_recipes.like.toString()
            model_recipe.img = model_recipes.img.toString()
        }else{
            model_recipe = searchViewModel.recipes.value?.get(position!!)!!
            post_key = model_recipe.ID
        }

        postRef = FirebaseDatabase.getInstance().reference.child("Recipe").child(post_key).child("comments")

        recyclerView_comment = binding.recyclerViewComments
        var linearLayoutManager = LinearLayoutManager(requireContext())
        linearLayoutManager.reverseLayout = true
        linearLayoutManager.stackFromEnd = true
        recyclerView_comment.layoutManager = linearLayoutManager

        post_comment = binding.commentBtn
        editText_comment = binding.comment

        post_comment.setOnClickListener {
            text_comment = editText_comment.text.toString()

            //Put data in the realtime Database
            putDataDBR(model_recipe, text_comment)

            val title = model_recipe.name
            val message = profileViewModel.name.value + " " + profileViewModel.familyName.value + " : "+ text_comment

            var multiple_user: ArrayList<String> = arrayListOf()
            val service = RetrofitBuilder.makeRetrofitService(BASE_URL).create(RecipeApiService::class.java)
            CoroutineScope(Dispatchers.IO).launch {
                val response = service.getRecipesNotification(post_key)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        for(elem in response.body()!!.Recipes){
                            if(elem.id!! != profileViewModel.id.value+post_key) {
                                multiple_user.add(elem.token!!)
                            }
                        }
                        if (title.isNotEmpty() && message.isNotEmpty()) {
                            PushNotification(
                                NotificationData(title, message,post_key,model_recipe.name),
                                multiple_user
                            ).also {
                                sendNotification(it)
                            }
                        }
                        Log.d("FirebaseService", response.message())
                    } else {
                        Log.d("FirebaseService", response.message())
                    }
                }
            }
        }

        return view
    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }

    override fun onStart() {
        super.onStart()
        val option = FirebaseRecyclerOptions.Builder<Comment>()
            .setQuery(postRef,Comment::class.java).build()

        adapter = CommentAdapter(option)
        recyclerView_comment.adapter = adapter
        adapter.startListening()
    }

    private fun putDataDBR(model_recipe: Recipe, text_comment: String){

        editText_comment.setText("")

        if(text_comment.isEmpty()){
            Toast.makeText(requireContext(),"Please Enter the comment", Toast.LENGTH_SHORT )
        }else{
            val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
            currentDate = sdf.format(Date())
        }

        var user = Users(profileViewModel.email.value,profileViewModel.familyName.value, profileViewModel.id.value, profileViewModel.image.value,profileViewModel.name.value,
            profileViewModel.username.value)

        var comment = Comment()
        comment.comment = text_comment
        comment.dataCreated = currentDate
        comment.user = user
        comment.id_recipe= post_key
        comment.name = model_recipe.name

        val key: String = postRef.push().key!!
        comment.id = key
        postRef.child(key).setValue(comment)

    }

    private fun sendNotification(notification: PushNotification){
        try {
            val service = RetrofitBuilder.makeRetrofitService(BASE_URL_NOT).create(NotificationApi::class.java)
            CoroutineScope(Dispatchers.IO).launch {
                val response = service.postNotification(notification)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        Log.d("CommentFragment", response.message())
                    }else{
                        Log.e("CommentFragment", response.errorBody().toString())
                    }
                }
            }

        }catch (e: Exception){
            Log.e("CommentFragment", e.toString() )
        }
    }
}




