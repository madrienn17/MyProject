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
import com.example.myproject.models.Favorite
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
    private var favorites: List<Favorite> = listOf()
    private lateinit var restaurantAdapter: RestaurantAdapter
    private lateinit var restaurantViewModel: ApiViewModel
    private val daoViewModel: DaoViewModel by activityViewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()
    var page: Int = 1

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val navBar: BottomNavigationView? = this.activity?.findViewById(R.id.nav_view)


        val root = inflater.inflate(R.layout.fragment_restaurants, container, false)

        restaurantAdapter = RestaurantAdapter(daoViewModel, requireContext(), sharedViewModel)
        restaurantList = root.findViewById(R.id.recyclerView)
        val allFav = daoViewModel.readAllData
        allFav.observe(viewLifecycleOwner, { us ->
            favorites = us
            restaurantAdapter.setFav(favorites)
        })
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

        val cityList = mutableListOf("CITY")
        val countryList = mutableListOf("COUNTRY")
        val priceList = listOf<String>("$", "1", "2", "3", "4", "5")

        if (Constants.cities != null) {
            cityList += Constants.cities!!
            countryList += Constants.countries!!
        }

        val citySpinnerAdapter= ArrayAdapter(requireContext(), R.layout.spinner_item, cityList)
        val countrySpinnerAdapter = ArrayAdapter(requireContext(), R.layout.spinner_item, countryList)
        val priceSpinnerAdapter = ArrayAdapter(requireContext(), R.layout.spinner_item, priceList)

        val citySpinner: Spinner = root.findViewById(R.id.spinner_city)
        val countrySpinner = root.findViewById<Spinner>(R.id.spinner_country)
        val priceSpinner = root.findViewById<Spinner>(R.id.spinner_price)

        citySpinner.adapter = citySpinnerAdapter
        countrySpinner.adapter = countrySpinnerAdapter
        priceSpinner.adapter = priceSpinnerAdapter

        citySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val city: String = parent?.getItemAtPosition(position).toString()
                launch {
                    val repository = ApiRepository()
                    val factory = ApiViewModelFactory(repository)
                    if(city == "CITY") {
                        restaurantViewModel = ViewModelProvider(requireActivity(), factory).get(ApiViewModel::class.java)
                        val restbycityresp = restaurantViewModel.getRestaurantsByCity("Westminster", page)
                        if (restbycityresp.isSuccessful) {
                            restbycityresp.body()?.let { restaurantAdapter.setData(it.restaurants) }
                        } else {
                            restaurantAdapter.setData(Constants.emptyRest)
                        }
                    }
                    else {
                        restaurantViewModel = ViewModelProvider(requireActivity(), factory).get(ApiViewModel::class.java)
                        val restbycityresp = restaurantViewModel.getRestaurantsByCity(city, page)
                        if(restbycityresp.isSuccessful) {
                            restbycityresp.body()?.restaurants?.let { restaurantAdapter.setData(it) }
                        }
                        else {
                            restaurantAdapter.setData(Constants.emptyRest)
                        }
                    }
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
                    if(country == "COUNTRY") {
                        restaurantViewModel = ViewModelProvider(requireActivity(), factory).get(ApiViewModel::class.java)
                        val restbycityresp = restaurantViewModel.getRestaurantsByCity("Westminster", page)
                        if (restbycityresp.isSuccessful) {
                            restbycityresp.body()?.let { restaurantAdapter.setData(it.restaurants) }
                        } else {
                            restaurantAdapter.setData(Constants.emptyRest)
                        }
                    }
                    else {
                        restaurantViewModel = ViewModelProvider(requireActivity(), factory).get(ApiViewModel::class.java)
                        val restbycountryresp = restaurantViewModel.getRestaurantsByCountry(country, page)
                        if(restbycountryresp.isSuccessful) {
                            restbycountryresp.body()?.restaurants?.let { restaurantAdapter.setData(it) }
                        }
                        else {
                            restaurantAdapter.setData(Constants.emptyRest)
                        }
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        priceSpinner.onItemSelectedListener= object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val price: String = parent?.getItemAtPosition(position).toString()
                if (price != "$") {
                    launch {
                        val repository = ApiRepository()
                        val factory = ApiViewModelFactory(repository)
                        restaurantViewModel = ViewModelProvider(requireActivity(), factory).get(ApiViewModel::class.java)

                        val restbypriceresp = restaurantViewModel.getRestaurantsByPrice(price.toInt())
                        if (restbypriceresp.isSuccessful) {
                            restbypriceresp.body()?.restaurants?.let { restaurantAdapter.setData(it) }
                        } else {
                            restaurantAdapter.setData(Constants.emptyRest)
                        }
                    }
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        val arrayList = sharedViewModel.getUserFavorites(Constants.USER_NAME)
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
//                if (!recyclerView.canScrollVertically(1) && newState==RecyclerView.SCROLL_STATE_IDLE) {
//                we are at hte bottom
//                }
                super.onScrollStateChanged(recyclerView, newState)
            }
        })

        return root
    }

}
