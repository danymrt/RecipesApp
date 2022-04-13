package com.example.recipesapp.ui.recipe

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.recipesapp.ui.recipe.tabRecipe.Comment.CommentFragment
import com.example.recipesapp.ui.recipe.tabRecipe.Ingredient.IngredientFragment
import com.example.recipesapp.ui.recipe.tabRecipe.Instruction.InstructionFragment


/*
To connect all our fragments with the ViewPager, we need an adapter class
 */
private const val NUM_TABS = 3

class RecipePagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int {
        return NUM_TABS
    }

    override fun createFragment(position: Int): Fragment {
        when (position) {
            0 -> return IngredientFragment()
            1 -> return InstructionFragment()
        }
        return CommentFragment()
    }
}