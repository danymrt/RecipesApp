package com.example.recipesapp.ui.profile

import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.recipesapp.MainActivity
import com.example.recipesapp.data.ListItems
import com.example.recipesapp.data.Recipes
import com.example.recipesapp.data.ShoppingItem
import com.example.recipesapp.data.Users
import com.example.recipesapp.data.api.RecipeApiService
import com.example.recipesapp.data.api.RetrofitBuilder
import com.example.recipesapp.data.api.UserApiService
import com.example.recipesapp.data.model.Recipe
import com.example.recipesapp.data.model.Steps
import com.example.recipesapp.ui.main.SearchActivity
import com.example.recipesapp.ui.profile.tabProfile.User.UserFragment
import com.example.recipesapp.ui.search.SearchViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProfileViewModel : ViewModel() {
    val BASE_URL = "https://danymrt.pythonanywhere.com"
    var pref_user = MainActivity.applicationContextLogin()
    var editor = pref_user!!.edit()


    private val _modifyButton = MutableLiveData<Boolean>().apply {
        value = false
    }
    val modifyButton: LiveData<Boolean> = _modifyButton

    fun updateModifyButton(input: Boolean){
        _modifyButton.value = input
    }

    private val _enableButton = MutableLiveData<Boolean>().apply {
        value = true
    }
    val enableButton: LiveData<Boolean> = _enableButton

    fun setButton(input: Boolean){
        _enableButton.value = input
    }

    private val _username = MutableLiveData<String>().apply {
        value = pref_user!!.getString("username", "")
    }
    val username: LiveData<String> = _username

    private val _name = MutableLiveData<String>().apply {
        value = pref_user!!.getString("name", "")
    }
    val name: LiveData<String> = _name

    private val _familyName = MutableLiveData<String>().apply {
        value = pref_user!!.getString("familyname", "")
    }
    val familyName: LiveData<String> = _familyName

    private val _id = MutableLiveData<String>().apply {
        value = pref_user!!.getString("id", "")
    }
    val id: LiveData<String> = _id

    private val _email = MutableLiveData<String>().apply {
        value = pref_user!!.getString("email", "")
    }
    val email: LiveData<String> = _email

    private val _image = MutableLiveData<String>().apply {
        value = pref_user!!.getString("url", "")
    }
    val image: LiveData<String> = _image

    fun update(usernameEdit: String, nameEdit: String, familyNameEdit: String, emailEdit: String) {
        _username.value = usernameEdit
        _name.value = nameEdit
        _familyName.value = familyNameEdit

        //Change the shared preference
        editor.putString("username",_username.value)
        editor.putString("email",_email.value)
        editor.putString("name",_name.value)
        editor.putString("familyname",_familyName.value)
        editor.commit()

        var user = Users(_email.value, familyNameEdit,_id.value, _image.value, nameEdit, usernameEdit)
        val BASE_URL = "https://danymrt.pythonanywhere.com"
        val service = RetrofitBuilder.makeRetrofitService(BASE_URL).create(UserApiService::class.java)
        CoroutineScope(Dispatchers.IO).launch {
            val response = service.modifyUser(_id.value!!, user)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    Log.d("DBUser", response.message())
                }else{
                    Log.d("DBUser", response.message())
                }
            }
        }

    }

    private val _shopping = MutableLiveData<ArrayList<ShoppingItem>>().apply {
        val service = RetrofitBuilder.makeRetrofitService(BASE_URL).create(UserApiService::class.java)
        CoroutineScope(Dispatchers.IO).launch {
            val response = service.getShoppingbyId(_id.value!!)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    value = response.body()!!
                    Log.d("POSTITEM", response.body().toString())
                }else{
                    Log.d("POSTITEM_ERROR", response.message())
                }
            }
        }
    }
    val shopping: LiveData<ArrayList<ShoppingItem>> = _shopping

    fun delete_elemnt_shopping(id: String, list_elem: ListItems, pos : Int){
        val service = RetrofitBuilder.makeRetrofitService(BASE_URL).create(UserApiService::class.java)
        CoroutineScope(Dispatchers.IO).launch {
            val response = service.deleteItem(id,list_elem)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    Log.d("POSTITEM", response.body().toString())
                }else{
                    Log.d("POSTITEM_ERROR", response.message())
                }
            }
        }
        _shopping.value!!.removeAt(pos)
    }

    fun add_element_shopping(id: String,pos:Int, elem: ShoppingItem){
        val service = RetrofitBuilder.makeRetrofitService(BASE_URL).create(UserApiService::class.java)
        CoroutineScope(Dispatchers.IO).launch {
            val response = service.addItem(id,elem)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    Log.d("POSTITEM", response.body().toString())
                }else{
                    Log.d("POSTITEM_ERROR", response.message())
                }
            }
        }
        _shopping.value!!.add(pos,elem)
    }

    private var _favouriterecipes = MutableLiveData<ArrayList<Recipes>>()
    val favoriterecipes: LiveData<ArrayList<Recipes>> = _favouriterecipes

    fun getFavouriteRecipe() : LiveData<ArrayList<Recipes>> {
        val service = RetrofitBuilder.makeRetrofitService(BASE_URL).create(RecipeApiService::class.java)
        CoroutineScope(Dispatchers.IO).launch {
            val response = service.getFavouriteRecipe(_id.value.toString())
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    _favouriterecipes.value = response.body()?.Recipes
                } else {
                    Log.d("DBRecipeFavourite", response.message())
                }
            }
        }

        val favouriterecipes : LiveData<ArrayList<Recipes>> = _favouriterecipes
        return favouriterecipes
    }

    fun delete_element_fav(id: String, pos : Int){
        val service = RetrofitBuilder.makeRetrofitService(BASE_URL).create(RecipeApiService::class.java)
        CoroutineScope(Dispatchers.IO).launch {
            val response = service.deleteRecipe(id)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    Log.d("POSTITEM", response.body().toString())
                }else{
                    Log.d("POSTITEM_ERROR", response.message())
                }
            }
        }
        _favouriterecipes.value!!.removeAt(pos)
    }

}