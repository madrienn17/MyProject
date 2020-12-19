package com.example.myproject.ui.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.firstapplication.R
import com.example.myproject.MainActivity
import com.example.myproject.ui.adapters.FavouritesAdapter
import com.example.myproject.ui.viewmodels.DaoViewModel
import com.example.myproject.utils.Constants
import kotlinx.android.synthetic.main.fragment_favourites_list.view.*

class FavouritesFragment : Fragment() {
    private lateinit var favorites: RecyclerView
    private lateinit var adapter: FavouritesAdapter
    private val daoViewModel: DaoViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_favourites_list, container, false)

        adapter = FavouritesAdapter(requireContext(),daoViewModel)
        favorites = root.fav_list
        favorites.adapter = adapter
        favorites.layoutManager = LinearLayoutManager(activity)
        favorites.setHasFixedSize(true)

        if(MainActivity.isLoggedIn) {
            val favs = daoViewModel.getUserFavorites(Constants.USER_NAME)

            favs.observe(viewLifecycleOwner, { us ->
                Constants.favoritIds = us
                FavouritesAdapter.getRestListByid(Constants.favoritIds)
                adapter.setFav(FavouritesAdapter.restFavs)
            })
        }

        setHasOptionsMenu(true)
        return root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.delete_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_delete) {
            deleteAllUser()
            adapter.notifyDataSetChanged()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun deleteAllUser() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes") { _, _ ->
            daoViewModel.deleteAllFavs()
            Toast.makeText(
                    requireContext(),
                    "Successfully removed every favorite!",
                    Toast.LENGTH_SHORT
            ).show()
        }
        builder.setNegativeButton("No") { _, _ ->

        }
        builder.setTitle("Delete everything?")
        builder.setMessage("Are you sure you want to delete every favorited restaurant?")
        builder.create().show()
    }
}