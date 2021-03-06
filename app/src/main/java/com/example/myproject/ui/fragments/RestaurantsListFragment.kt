package com.example.myproject.ui.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.firstapplication.R
import com.example.myproject.MainActivity
import com.example.myproject.models.RestaurantPic
import com.example.myproject.repository.ApiRepository
import com.example.myproject.ui.adapters.FavouritesAdapter
import com.example.myproject.ui.adapters.RestaurantAdapter
import com.example.myproject.ui.viewmodels.ApiViewModel
import com.example.myproject.ui.viewmodels.ApiViewModelFactory
import com.example.myproject.ui.viewmodels.DaoViewModel
import com.example.myproject.utils.Constants
import com.example.myproject.utils.Constants.Companion.favoritIds
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.fragment_restaurants.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class RestaurantsListFragment: Fragment(), CoroutineScope, TextWatcher {
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + Job()
    private lateinit var restaurantList: RecyclerView
    private lateinit var favouritesAdapter: FavouritesAdapter
    private lateinit var restaurantAdapter: RestaurantAdapter
    private lateinit var restaurantViewModel: ApiViewModel
    private val daoViewModel: DaoViewModel by activityViewModels()
    lateinit var allRestPic: LiveData<List<RestaurantPic>>
    lateinit var citySpinner: Spinner
    lateinit var countrySpinner: Spinner
    lateinit var priceSpinner: Spinner
    lateinit var pageNumber: EditText
    private val repository = ApiRepository()
    val factory = ApiViewModelFactory(repository)
    var restPics: List<RestaurantPic> = listOf()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val navBar: BottomNavigationView? = this.activity?.findViewById(R.id.nav_view)

        val root = inflater.inflate(R.layout.fragment_restaurants, container, false)

        allRestPic = daoViewModel.readAllRestPic

        // observing restaurant pics and saving them into companion for future use
        allRestPic.observe(viewLifecycleOwner, { us ->
            restPics = us
            saveToComp()
        })

        //initializing restaurantViewModel
        restaurantViewModel = ViewModelProvider(requireActivity(), factory).get(ApiViewModel::class.java)

        // setting up recyclerview with adapter
        restaurantAdapter = RestaurantAdapter(daoViewModel, requireContext())
        favouritesAdapter = FavouritesAdapter(requireContext(),daoViewModel)
        restaurantList = root.findViewById(R.id.recyclerView)
        restaurantList.adapter = restaurantAdapter
        restaurantList.layoutManager = LinearLayoutManager(activity)
        restaurantList.setHasFixedSize(true)

        // getting favorite ids
        if(MainActivity.isLoggedIn) {
            val favs = daoViewModel.getUserFavorites(Constants.USER_NAME)

            favs.observe(viewLifecycleOwner, { us ->
                favoritIds = us
                //Log.d("FAVORITIDS", favoritIds.toString())
                FavouritesAdapter.getRestListByid(favoritIds)
                favouritesAdapter.setFav(FavouritesAdapter.restFavs)
            })
        }

        // performing search with filters
        root.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
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

        // setting up spinners default value
        val cityList = mutableListOf("CITY")
        val countryList = mutableListOf("COUNTRY")
        val priceList = listOf("$", "1", "2", "3", "4", "5")

        if (Constants.cities != null) {
            cityList += Constants.cities!!
            countryList += Constants.countries!!
        }

        citySpinner = root.spinner_city
        countrySpinner = root.spinner_country
        priceSpinner = root.spinner_price
        pageNumber = root.page_num

        // setting up spinners adapters
        val citySpinnerAdapter= ArrayAdapter(requireContext(), R.layout.spinner_item, cityList)
        val countrySpinnerAdapter = ArrayAdapter(requireContext(), R.layout.spinner_item, countryList)
        val priceSpinnerAdapter = ArrayAdapter(requireContext(), R.layout.spinner_item, priceList)

        citySpinner.adapter = citySpinnerAdapter
        countrySpinner.adapter = countrySpinnerAdapter
        priceSpinner.adapter = priceSpinnerAdapter

        // adding implemented textchanged listener for the pagenumber edittext
        pageNumber.addTextChangedListener(this)

        citySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val city: String = parent?.getItemAtPosition(position).toString()
                launch {

                    if(city == "CITY") { // if we are at the first item (nothing was selected by user) load all restaurants
                        val restresp = restaurantViewModel.getAllRestaurants()
                        if (restresp.isSuccessful && restresp.body() != null) {
                            restresp.body()?.let { restaurantAdapter.setData(it.restaurants) }
                        } else {
                            restaurantAdapter.setData(Constants.emptyRest)
                        }
                    }
                    else {
                        var price:Int? = null
                        if(priceSpinner.selectedItemPosition != 0) {
                            price = priceSpinner.selectedItemPosition
                        }
                        val restbycityresp = restaurantViewModel.getRestaurantsByAll(price,city,null, pageNumber.text.toString().toInt())
                        if(restbycityresp.isSuccessful && restbycityresp.body() != null) {
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
                    if(country == "COUNTRY") {
                        val restresp = restaurantViewModel.getAllRestaurants()
                        if (restresp.isSuccessful && restresp.body() != null) {
                            restresp.body()?.let { restaurantAdapter.setData(it.restaurants) }
                        } else {
                            restaurantAdapter.setData(Constants.emptyRest)
                        }
                    }
                    else {
                        val restbycountryresp = restaurantViewModel.getRestaurantsByAll(null, null,country, pageNumber.text.toString().toInt())
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
                    var city:String? = null
                    if(citySpinner.selectedItemPosition != 0) {
                        city = citySpinner.selectedItem.toString()
                    }
                    launch {
                        val restbypriceresp = restaurantViewModel.getRestaurantsByAll(price.toInt(),city,null, pageNumber.text.toString().toInt())
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

        // playing with navbar's visibility on scrolledstate
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

    private fun saveToComp() {
        Companion.restPics = restPics
    }

    companion object{
        lateinit var restPics:List<RestaurantPic>
    }

    // pagination
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
    }

    override fun afterTextChanged(s: Editable?) {
        if (s!!.isNotEmpty()) {
            var city:String? = null
            var country:String?= null
            var price:Int? = null

            if(citySpinner.selectedItemPosition != 0) {
                city = citySpinner.selectedItem.toString()
            }
            if (countrySpinner.selectedItemPosition!=0) {
                country = countrySpinner.selectedItem.toString()
            }
            if (priceSpinner.selectedItemPosition != 0) {
                price = priceSpinner.selectedItemPosition.toString().toInt()
            }
            launch {
                val repository = ApiRepository()
                val factory = ApiViewModelFactory(repository)
                restaurantViewModel = ViewModelProvider(requireActivity(), factory).get(ApiViewModel::class.java)

                val restResp = restaurantViewModel.getRestaurantsByAll(price,city,country, s.toString().toInt())
                if (restResp.isSuccessful) {
                    restResp.body()?.restaurants?.let { restaurantAdapter.setData(it) }
                } else {
                    restaurantAdapter.setData(Constants.emptyRest)
                }
            }
        }
    }
}
