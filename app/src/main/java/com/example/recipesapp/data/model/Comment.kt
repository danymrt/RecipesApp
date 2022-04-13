package com.example.recipesapp.data.model

import com.example.recipesapp.data.Users

data class Comment(
    var id: String,
    var comment: String,
    var dataCreated: String,
    var user: Users?,
    var id_recipe: String,
    var name: String
) {
    constructor() : this("","","",null,"","")
}

