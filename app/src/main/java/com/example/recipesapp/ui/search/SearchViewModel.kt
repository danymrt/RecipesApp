package com.example.recipesapp.ui.search

import android.app.ProgressDialog
import android.os.AsyncTask
import android.util.Log
import android.view.View
import androidx.annotation.NonNull
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.recipesapp.R
import com.example.recipesapp.RecipeAdapter
import com.example.recipesapp.data.api.RetrofitBuilder
import com.example.recipesapp.data.api.SpoonacularApi
import com.example.recipesapp.data.model.ExtendedIngredients
import com.example.recipesapp.data.model.Recipe
import com.example.recipesapp.data.model.Results
import com.example.recipesapp.data.model.Steps
import com.facebook.internal.gatekeeper.AppID
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Dispatcher
import org.json.JSONArray
import org.json.JSONException
import retrofit2.http.Path
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.net.URLConnection
import java.util.*
import kotlin.collections.ArrayList
import kotlin.reflect.typeOf

class SearchViewModel : ViewModel() {

    companion object{
        lateinit var allIngredients: ArrayList<Map<String, String>>
        private var _recipes = MutableLiveData<ArrayList<Recipe>>()
        lateinit var pd: ProgressDialog
        val APP_KEY: String = R.string.api_key_spoonacular.toString()
        var allIngredients_allRecipes = ArrayList<Map<String, ArrayList<Map<String, String>>>>()
        private var recipes_value = ArrayList<Recipe>()
        private val _count = MutableLiveData<Int>().apply {
            value = 0
        }
    }
    var _resultRecipe = MutableLiveData<ArrayList<Steps>>()
    //array of ingredients used in favourite fragment
    var _resultInfoModel = MutableLiveData<ArrayList<ExtendedIngredients>>()
    //array of summaring,time,serving
    var _resultSummaryModel = MutableLiveData<ArrayList<String>>()
    //final array from which we will take the recipe
    var _resultCaloriesModel = MutableLiveData<String>()
    val recipes: LiveData<ArrayList<Recipe>> = _recipes

    private val _position = MutableLiveData<Int>().apply {
        value = 0
    }
    val position: LiveData<Int> = _position
    fun setPosition(pos: Int){
        _position.value = pos
    }

    private val _flag = MutableLiveData<Int>().apply {
        value = 0
    }
    val flag: LiveData<Int> = _flag
    fun setFlag(f: Int){
        _flag.value = f
    }

    val count: LiveData<Int> = _count

    class JsonTask(@NonNull context: FragmentActivity?) : AsyncTask<String, String, JSONArray>(){
        private var mycontext: FragmentActivity? = context

        override fun onPreExecute() {
            super.onPreExecute()
            pd = ProgressDialog(mycontext)
            pd.setMessage("Please wait")
            pd.setCancelable(false)
            pd.show()
            _count.value = _count.value!! +1
        }

        override fun doInBackground(vararg params: String?): JSONArray?{
            var connection: URLConnection? = null
            var reader: BufferedReader? = null
            try {
                val url = URL(params[0])
                connection = url.openConnection() as HttpURLConnection
                connection.connect()
                val stream = connection.inputStream
                reader = BufferedReader(InputStreamReader(stream))
                val stringBuffer = StringBuffer()
                var line: String?
                while (reader.readLine().also { line = it } != null) {
                    stringBuffer.append(
                        """
                            $line
                            
                            """.trimIndent()
                    )
                }
                return JSONArray(stringBuffer.toString())
            } catch (e: MalformedURLException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            } catch (e: JSONException) {
                e.printStackTrace()
            } finally {
                if (connection != null) {
                    (connection as HttpURLConnection).disconnect()
                }
                try {
                    reader?.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
            return null
        }


        override fun onPostExecute(response: JSONArray) {
            super.onPostExecute(response)

            if (pd.isShowing) {
                pd.dismiss()
            }
            if (response != null) {
                try {
                    for (i in 0 until response.length()) {
                        val recipe_object = response.getJSONObject(i)
                        val ID = recipe_object.getInt("id").toString()
                        val name = recipe_object.getString("title")
                        val img = recipe_object.getString("image")
                        val missedIngredients = recipe_object.getJSONArray("missedIngredients")
                        val usedIngredients = recipe_object.getJSONArray("usedIngredients")
                        val likes = recipe_object.getInt("likes").toString()
                        var array_missed_ingredients = ArrayList<Map<String, String>>()
                        var array_used_ingredients = ArrayList<Map<String, String>>()
                        val recipe = Recipe(name, ID)
                        recipe.likes = likes
                        recipe.setImg(img)
                        for (j in 0 until missedIngredients.length()) {
                            val ingredientObject = missedIngredients.getJSONObject(j)
                            val ingredient = ingredientObject.getString("original")
                            val img_ingredient = ingredientObject.getString("image")
                            array_missed_ingredients.add(j, mapOf(ingredient to img_ingredient))
                        }
                        for (j in 0 until usedIngredients.length()) {
                            val ingredientObject = usedIngredients.getJSONObject(j)
                            val ingredient = ingredientObject.getString("original")
                            val img_ingredient = ingredientObject.getString("image")
                            array_used_ingredients.add(j, mapOf(ingredient to img_ingredient))
                        }
                        var len_missed = (array_missed_ingredients.size).toDouble()
                        var len_used = array_used_ingredients.size
                        var feasibility = ((len_used / (len_missed+len_used)) * 100).toInt()
                        recipe.feasibility = feasibility.toString()
                        array_missed_ingredients.addAll(array_used_ingredients)
                        allIngredients = array_missed_ingredients
                        recipe.ingredients = allIngredients
                        val hashedMap: Map<String, ArrayList<Map<String, String>>> = hashMapOf(
                            recipe.name to recipe.ingredients
                        )
                        allIngredients_allRecipes.add(i, hashedMap)   //only for show ingredients in recycle
                        recipes_value.add(recipe)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                _recipes.postValue(recipes_value)
            }
        }
    }

    fun getInstructions(ID: String?) : LiveData<ArrayList<Steps>>{
        val url = "https://api.spoonacular.com/"
        val service = RetrofitBuilder.makeRetrofitService(url).create(SpoonacularApi::class.java)
        CoroutineScope(Dispatchers.IO).launch {
            val response = service.getDetails(ID,true, APP_KEY)
            withContext(Dispatchers.Main){
                if(response!!.isSuccessful()){
                    if(response.body()!!.size > 0) {
                        _resultRecipe.value = response.body()!![0]?.steps
                    }else{
                        val null_steps = ArrayList<Steps>()
                        _resultRecipe.value = null_steps
                    }
                }else{
                    Log.d("InstructionFragment", response.message())
                }
            }
        }
        val resultRecipe : LiveData<ArrayList<Steps>> = _resultRecipe
        return resultRecipe
    }

    fun getInfoRecipe(ID: String?) : LiveData<ArrayList<ExtendedIngredients>> {
        val url = "https://api.spoonacular.com/"
        val service = RetrofitBuilder.makeRetrofitService(url).create(SpoonacularApi::class.java)
        CoroutineScope(Dispatchers.IO).launch {
            val response = service.getInfoRecipe(ID,false, APP_KEY)
            withContext(Dispatchers.Main){
                if(response!!.isSuccessful()){
                    _resultInfoModel.value = response.body()?.extendedIngredients
                }else{
                    Log.d("InfoRecipe", response.message())
                }
            }
        }
        val resultInfoModel : LiveData<ArrayList<ExtendedIngredients>> = _resultInfoModel
        return resultInfoModel
    }

    fun getSummaryRecipe(ID: String?) : LiveData<ArrayList<String>> {
        val url = "https://api.spoonacular.com/"
        val list_summary = ArrayList<String>()
        val service = RetrofitBuilder.makeRetrofitService(url).create(SpoonacularApi::class.java)
        CoroutineScope(Dispatchers.IO).launch {
            val response = service.getInfoRecipe(ID,false, APP_KEY)
            withContext(Dispatchers.Main){
                if(response!!.isSuccessful()){
                    var summary = response.body()?.summary
                    var time = response.body()?.readyInMinutes.toString()
                    var servings = response.body()?.servings.toString()
                    list_summary.add(summary!!)
                    list_summary.add(time)
                    list_summary.add(servings)
                    _resultSummaryModel.value = list_summary
                }else{
                    Log.d("SummaryRecipe", response.message())
                }
            }
        }
        val resultSummaryModel : LiveData<ArrayList<String>> = _resultSummaryModel
        return resultSummaryModel
    }

    fun getCaloriesRecipe(ID: String?) : LiveData<String>{
        val url = "https://api.spoonacular.com/"
        val service = RetrofitBuilder.makeRetrofitService(url).create(SpoonacularApi::class.java)
        CoroutineScope(Dispatchers.IO).launch {
            val response = service.getInfoCalories(ID, APP_KEY)
            withContext(Dispatchers.Main){
                if(response!!.isSuccessful()){
                    _resultCaloriesModel.value = response.body()?.calories!!
                }else{
                    Log.d("CaloriesRecipe", response.message())
                }
            }
        }
        val resultCaloriesModel : LiveData<String> = _resultCaloriesModel
        return resultCaloriesModel
    }

    fun restartRecipe(){
        _recipes.value = arrayListOf()
    }

}
