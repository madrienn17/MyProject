package com.example.myproject.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.firstapplication.R
import com.example.myproject.ui.adapters.FavouritesAdapter
import com.example.myproject.ui.viewmodels.SharedViewModel
import com.example.myproject.utils.Constants

class FavouritesFragment : Fragment() {
    private lateinit var favorites: RecyclerView
    private lateinit var adapter: FavouritesAdapter
    // private val daoViewModel: DaoViewModel by activityViewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_favourites_list, container, false)

        adapter = FavouritesAdapter(requireContext(),sharedViewModel)
        adapter.setFav(Constants.USER_ID)
        favorites = root.findViewById(R.id.fav_list)
        favorites.adapter = adapter
        favorites.layoutManager = LinearLayoutManager(activity)
        favorites.setHasFixedSize(true)

        return root
    }

}