package com.example.myproject.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.firstapplication.R
import com.example.myproject.models.Favorite
import com.example.myproject.models.Restaurant
import com.example.myproject.ui.viewmodels.DaoViewModel
import com.example.myproject.ui.viewmodels.SharedViewModel
import com.example.myproject.utils.Constants
import com.google.android.material.snackbar.Snackbar
import java.util.*


class RestaurantAdapter(val daoViewModel: DaoViewModel, val context:Context, val viewModel:SharedViewModel): RecyclerView.Adapter<RestaurantAdapter.RestaurantViewHolder>(), Filterable{
    private var restaurantList = Collections.emptyList<Restaurant>()
    var searchableList: MutableList<Restaurant> = mutableListOf()
    private var favorites: List<Favorite> = listOf()
    private lateinit var fav : Favorite

    inner class RestaurantViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.image)
        val name: TextView = itemView.findViewById(R.id.name)
        val address: TextView = itemView.findViewById(R.id.address)
        val price: TextView = itemView.findViewById(R.id.price)
        val favourite: ImageButton = itemView.findViewById(R.id.star)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantViewHolder {
       val itemView = LayoutInflater.from(parent.context).inflate(
           R.layout.list_item_row,
           parent, false
       )

        return RestaurantViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RestaurantViewHolder, position: Int) {
        val currentItem = restaurantList[position]

        Glide.with(holder.itemView.context)
                .load(currentItem.image_url)
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(holder.image).view
        holder.price.text = currentItem.price.times('$')
        holder.address.text = currentItem.address
        holder.name.text = currentItem.name

        if (isFavoriteForCurr(currentItem)) {
            holder.favourite.setBackgroundResource(R.drawable.star_filled)
        }

        holder.favourite.setOnClickListener {
//            Log.d("SHARED",viewModel.favorited.toString())
//            val favL = daoViewModel.readAllData
//            Log.d("FAVDATA", favL.value.toString())

            holder.favourite.setBackgroundResource(R.drawable.star_filled)
            fav = Favorite(currentItem.id,Constants.USER_NAME)
            daoViewModel.addRestaurantDB(fav)
            //notifyDataSetChanged()
            if(!(viewModel.getUserFavorites(Constants.USER_NAME).contains(currentItem)) ){
                        viewModel.addFav(Constants.USER_NAME, currentItem)
                    }

            Snackbar.make(
                holder.itemView,
                "${currentItem.name} added to favourites",
                Snackbar.LENGTH_SHORT
            ).show()
        }


        holder.favourite.setOnLongClickListener {
//            val favL = daoViewModel.readAllData
//            Log.d("FAVDATA", favL.value.toString())
            fav = Favorite(currentItem.id,Constants.USER_NAME)
            daoViewModel.deleteRestaurantDB(fav)
            holder.favourite.setBackgroundResource(R.drawable.star)
            Snackbar.make(
                holder.itemView,
                "${currentItem.name} removed from favourites",
                Snackbar.LENGTH_SHORT
            ).show()
            true
        }
        holder.itemView.setOnClickListener{
            val bundle = bundleOf(
                    "name" to currentItem.name,
                    "address" to currentItem.address,
                    "city" to currentItem.city,
                    "state" to currentItem.state,
                    "area" to currentItem.area,
                    "postal_code" to currentItem.postal_code,
                    "country" to currentItem.country,
                    "price" to currentItem.price.times('$'),
                    "lat" to currentItem.lat,
                    "lng" to currentItem.lng,
                    "phone" to currentItem.phone,
                    "reserve_url" to currentItem.reserve_url,
                    "mobile_reserve_url" to currentItem.mobile_reserve_url)

            Toast.makeText(context, "Item ${currentItem.name} clicked", Toast.LENGTH_SHORT).show()

            holder.itemView.findNavController().navigate(R.id.navigation_details, bundle)
        }
    }
    override fun getItemCount() = searchableList.size

    fun setData(restaurants: List<Restaurant>) {
        this.restaurantList = restaurants
        this.searchableList = restaurants.toMutableList()
        notifyDataSetChanged()
    }

    fun setFav(favorites: List<Favorite>) {
        this.favorites = favorites
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            private val filterResults = FilterResults()
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                searchableList.clear()
                if(constraint.isNullOrBlank()) {
                    searchableList.addAll(restaurantList)
                } else {
                    val filterPattern = constraint.toString().toLowerCase(Locale.ROOT).trim { it <= ' '}
                    for(item in 0..restaurantList.size) {
                        if(restaurantList[item].name.toLowerCase(Locale.ROOT).contains(filterPattern)){
                            searchableList.add(restaurantList[item])
                        }
                    }
                }
                return filterResults.also {
                    it.values = searchableList
                }
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                if(searchableList.isNullOrEmpty()) {
                    setData(restaurantList)
                }
                else {
                    setData(searchableList)
                }
                notifyDataSetChanged()
            }
        }
    }

    fun isFavoriteForCurr(rest:Restaurant) : Boolean{
        for (i in favorites) {
            if (i.restId == rest.id && i.userName == Constants.USER_NAME) {
                return true
            }
        }
        return false
    }
}

private fun Int.times(s: Char): String {
    val result = StringBuffer("")
    for (i in 1..this) {
       result.append(s)
    }
    return result.toString()
}
