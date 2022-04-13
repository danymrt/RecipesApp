package com.example.recipesapp.ui.profile.tabProfile.Shopping
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.SimpleCallback
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.recipesapp.data.ListItems
import com.example.recipesapp.data.ShoppingItem
import com.example.recipesapp.databinding.FragmentShoppinglistBinding
import com.example.recipesapp.ui.profile.ProfileViewModel
import com.example.recipesapp.ui.recipe.tabRecipe.Ingredient.IngredientAdapter
import com.google.android.material.snackbar.Snackbar



class ShoppingListFragment: Fragment() {
    private val profileViewModel: ProfileViewModel by activityViewModels()
    private var _binding: FragmentShoppinglistBinding? = null
    private val binding get() = _binding!!
    private var items: RecyclerView? = null
    private var recyclerDataArrayList: ArrayList<ShoppingItem>? = null
    private var recyclerViewAdapter: ShoppingViewAdapter? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentShoppinglistBinding.inflate(inflater, container, false)
        val view = binding.root

        items = binding.idRVItem
        val text = binding.emptyList

        /**
         * Take the shopping list and show it in a RecyclerView
         * and we can remove an item by swiping our item to right direction
         */
        profileViewModel.shopping.observe(viewLifecycleOwner){
            recyclerDataArrayList = it

            if(it.isEmpty()){
                text.visibility = View.VISIBLE
            }

            // initializing our adapter class with our array list and context.
            recyclerViewAdapter = ShoppingViewAdapter(recyclerDataArrayList!!, requireContext())

            // below line is to set layout manager for our recycler view.
            val manager = LinearLayoutManager(requireContext())

            // setting layout manager for our recycler view.
            items!!.layoutManager = manager
            items!!.adapter = recyclerViewAdapter

            val dividerItemDecoration = DividerItemDecoration(requireContext(), manager.orientation)
            items!!.addItemDecoration(dividerItemDecoration)

            ItemTouchHelper(object : SimpleCallback(0, ItemTouchHelper.LEFT) {
                override fun onMove(recyclerView: RecyclerView, ViewHolder: ViewHolder, target: ViewHolder): Boolean {
                    // this method is called
                    // when the item is moved.
                    return false
                }

                override fun onSwiped(viewHolder: ViewHolder, direction: Int) {
                    //We swipe our item to right direction and get the item at a particular position.
                    var deletedItem : ShoppingItem = it!![viewHolder.adapterPosition]
                    var elem : ArrayList<String> = ArrayList()
                    elem.add(deletedItem.name!!)
                    val l = ListItems(elem)

                    // Get the position of the item
                    var position = viewHolder.adapterPosition

                    // Remove item
                    profileViewModel.delete_elemnt_shopping(profileViewModel.id.value!!,l, position)

                    //Notify item is removed from adapter
                    recyclerViewAdapter!!.notifyItemRemoved(position)
                    //Check if list is empty
                    if(it.isEmpty()){
                        text.visibility = View.VISIBLE
                    }

                    // Display our snackbar with action.
                    Snackbar.make(items!!, deletedItem.name!!, Snackbar.LENGTH_LONG).setAction("Undo") {
                        // Add our item to array list with a position.
                        profileViewModel.add_element_shopping(profileViewModel.id.value!!,position, deletedItem)
                        // Notify item is added to adapter class.
                        recyclerViewAdapter!!.notifyItemInserted(position)
                        text.visibility = View.INVISIBLE
                    }.show()
                }
                // Adding this to the recycler view.
            }).attachToRecyclerView(items)
        }


        return view
    }

    override fun onResume() {
        super.onResume()
        //Disable the button for modifying the profile
        profileViewModel.setButton(false)
    }
}

