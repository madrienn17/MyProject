package com.example.myproject.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SearchView
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.firstapplication.R
import com.example.myproject.models.Restaurant
import com.example.myproject.repository.ApiRepository
import com.example.myproject.ui.adapters.RestaurantAdapter
import com.example.myproject.ui.viewmodels.ApiViewModel
import com.example.myproject.ui.viewmodels.ApiViewModelFactory
import com.example.myproject.ui.viewmodels.DaoViewModel
import com.example.myproject.ui.viewmodels.SharedViewModel
import com.example.myproject.utils.Constants
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class RestaurantsListFragment: Fragment(), CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + Job()
    private lateinit var restaurantList: RecyclerView
    private lateinit var restaurantAdapter: RestaurantAdapter
    private lateinit var restaurantViewModel: ApiViewModel
    private val daoViewModel: DaoViewModel by activityViewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val navBar: BottomNavigationView? = this.activity?.findViewById(R.id.nav_view)


        val root = inflater.inflate(R.layout.fragment_restaurants, container, false)

        restaurantAdapter = RestaurantAdapter(daoViewModel, requireContext(), sharedViewModel)
        restaurantAdapter.setData(SplashFragment.list)
        restaurantList = root.findViewById(R.id.recyclerView)
        restaurantList.adapter = restaurantAdapter
        restaurantList.layoutManager = LinearLayoutManager(activity)
        restaurantList.setHasFixedSize(true)

        val searchbar = root.findViewById<SearchView>(R.id.searchView)

        searchbar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return if(query.isNullOrEmpty()) {
                    false
                } else {
                    restaurantAdapter.filter.filter(query.toString())
                    true
                }
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return if(newText.isNullOrEmpty()) {
                    false
                } else {
                    restaurantAdapter.filter.filter(newText.toString())
                    true
                }
            }
        })

//        val cityList = mutableListOf("SELECT CITY")
//        val countryList = mutableListOf("SELECT COUNTRY")
//        cityList += Constants.cities
//        countryList += Constants.countries

        val citySpinnerAdapter= ArrayAdapter(requireContext(), R.layout.spinner_item, Constants.cities)
        val countrySpinnerAdapter = ArrayAdapter(requireContext(), R.layout.spinner_item, Constants.countries)

        val citySpinner: Spinner = root.findViewById(R.id.spinner_city)
        val countrySpinner = root.findViewById<Spinner>(R.id.spinner_country)

        citySpinner.adapter = citySpinnerAdapter
        countrySpinner.adapter = countrySpinnerAdapter

        citySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val city: String = parent?.getItemAtPosition(position).toString()
                launch {
                    val repository = ApiRepository()
                    val factory = ApiViewModelFactory(repository)
                    restaurantViewModel = ViewModelProvider(requireActivity(), factory).get(ApiViewModel::class.java)
                    restaurantViewModel.loadRestaurantsByCity(city)
                    lateinit var list: List<Restaurant>
                    restaurantViewModel.restaurantsByCity.observe(requireActivity(), { restaurants ->
                        list = restaurants
                        restaurantAdapter.setData(list)
                    })
                }
            }
        }

        countrySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val country: String = parent?.getItemAtPosition(position).toString()
                launch {
                    val repository = ApiRepository()
                    val factory = ApiViewModelFactory(repository)
                    restaurantViewModel = ViewModelProvider(requireActivity(), factory).get(ApiViewModel::class.java)
                    restaurantViewModel.loadRestaurantsByCountry(country)
                    lateinit var list: List<Restaurant>
                    restaurantViewModel.restaurantsByCountry.observe(requireActivity(), {
                        rest -> list = rest
                        restaurantAdapter.setData(list)
                    })
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        val arrayList = sharedViewModel.getUserFavorites(Constants.USER_ID)
        for (data in arrayList) {
            Log.d("FAV", data.toString())
        }

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

}
