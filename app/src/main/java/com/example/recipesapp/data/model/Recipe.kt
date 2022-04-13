package com.example.recipesapp.data.model

class Recipe(var name: String, var ID: String) {
    lateinit var ingredients: ArrayList<Map<String, String>>
    lateinit  var instructions: String
    lateinit var img: String
    lateinit var likes: String
    lateinit var feasibility: String
    var fav: String = "0"  //1 if fav, 0 if not

    fun setImg(img: String): String? {
        return img.also { this.img = it }
    }
}