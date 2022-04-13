package com.example.recipesapp.ui.profile

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.recipesapp.ui.profile.tabProfile.Favourite.FavouriteFragment
import com.example.recipesapp.ui.profile.tabProfile.Shopping.ShoppingListFragment
import com.example.recipesapp.ui.profile.tabProfile.User.UserFragment


/*
To connect all our fragments with the ViewPager, we need an adapter class
 */
private const val NUM_TABS = 3

class ViewPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int {
        return NUM_TABS
    }

    override fun createFragment(position: Int): Fragment {
        when (position) {
            0 -> return UserFragment()
            1 -> return FavouriteFragment()
        }
        return ShoppingListFragment()
    }
}