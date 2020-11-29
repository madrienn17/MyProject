package com.example.myproject.ui.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.firstapplication.R
import com.example.myproject.api.RestaurantApiRepository
import com.example.myproject.data.RestaurantViewModel
import com.example.myproject.models.Restaurant
import com.example.myproject.ui.adapters.OnItemClickListener
import com.example.myproject.ui.adapters.RestaurantAdapter
import com.example.myproject.ui.viewmodels.ApiViewModelFactory
import com.example.myproject.ui.viewmodels.RestaurantsListViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class RestaurantsListFragment: Fragment(), OnItemClickListener, CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + Job()

    private val listViewModel: RestaurantViewModel by activityViewModels()
    private lateinit var restaurantViewModel: RestaurantsListViewModel
    private lateinit var restaurantList: RecyclerView
    private lateinit var restaurantAdapter: RestaurantAdapter


    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val navBar: BottomNavigationView? = this.activity?.findViewById(R.id.nav_view)


        val root = inflater.inflate(R.layout.fragment_restaurants, container, false)

        //val restaurantSearch: SearchView = root.findViewById(R.id.restaurant_search)

//        restaurantSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
//            override fun onQueryTextSubmit(query: String?): Boolean {
//                return false
//            }
//
//            override fun onQueryTextChange(newText: String?): Boolean {
//                restaurantAdapter.filter.filter(newText)
//                return false
//            }
//        })
//
//      restaurantSearch.findViewById<ImageView>(R.id.restaurant_search_icon)

       // restaurantAdapter = RestaurantAdapter(listViewModel.getAllRestaurants(), this)
        restaurantAdapter = RestaurantAdapter()
        restaurantList = root.findViewById(R.id.recyclerView)
        restaurantList.adapter = restaurantAdapter
        restaurantList.layoutManager = LinearLayoutManager(activity)
        //restaurantList.setHasFixedSize(true)



        restaurantList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0 || dy < 0 && navBar!!.isShown) {
                    navBar?.visibility = View.GONE
                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    navBar?.visibility = View.VISIBLE
                }
                super.onScrollStateChanged(recyclerView, newState)
            }
        })

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        launch {
            val repository = RestaurantApiRepository()
            val factory = ApiViewModelFactory(repository)
            restaurantViewModel = ViewModelProvider(requireActivity(), factory).get(RestaurantsListViewModel::class.java)

            restaurantViewModel.loadRestaurants("London")
            lateinit var list: List<Restaurant>

            restaurantViewModel.restaurants.observe(requireActivity(), { restaurants ->
                list = restaurants
                restaurantAdapter.setData(list)
                Log.d("APIDATA", restaurants.toString())
            })
            super.onViewCreated(view, savedInstanceState)
        }

    }

    override fun onItemClick(position: Int) {
        Toast.makeText(activity, "Item $position clicked", Toast.LENGTH_SHORT).show()
        val bundle = bundleOf("position" to position)
        requireView().findNavController().navigate(R.id.navigation_details, bundle)
    }

    override fun onItemLongClick(position: Int) {
        val alertDialog: AlertDialog? = activity.let {

            val builder = AlertDialog.Builder(it)
            builder.apply {
                setTitle("Are you sure you want to delete this item?")
                setPositiveButton("Yes"
                ) { _, _ ->
                    listViewModel.removeRestaurant(position)
                    restaurantAdapter.notifyDataSetChanged()
                    Toast.makeText(activity, "Item $position Deleted", Toast.LENGTH_SHORT).show()
                }
                setNegativeButton("No"
                ) { _, _ ->
                    Toast.makeText(activity, "Delete cancelled", Toast.LENGTH_SHORT).show()
                }
            }
            builder.create()
        }
        alertDialog?.show()
    }

}
