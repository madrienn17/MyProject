package com.example.myproject.ui.adapters

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat.getColor
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.firstapplication.R
import com.example.myproject.MainActivity
import com.example.myproject.models.Favorite
import com.example.myproject.models.Restaurant
import com.example.myproject.ui.fragments.RestaurantsListFragment
import com.example.myproject.ui.viewmodels.DaoViewModel
import com.example.myproject.utils.Constants
import com.google.android.material.snackbar.Snackbar
import java.util.*


class RestaurantAdapter(private val daoViewModel: DaoViewModel, val context:Context): RecyclerView.Adapter<RestaurantAdapter.RestaurantViewHolder>(), Filterable{
    private var restaurantList = Collections.emptyList<Restaurant>()
    var searchableList: MutableList<Restaurant> = mutableListOf()
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

        holder.itemView.setBackgroundColor(getColor(context,R.color.cardBackground))

        Glide.with(holder.itemView.context)
                .load(currentItem.image_url)
                .placeholder(R.drawable.ic_launcher_foreground)
                .apply(RequestOptions().centerCrop())
                .into(holder.image).view

        // if the restaurant has picture saved in the db, display that picture
        for (i in RestaurantsListFragment.restPics) {
            if (currentItem.name == i.restName) {
                val bmp: Bitmap = BitmapFactory.decodeByteArray(i.restPic, 0, i.restPic.size)
                Glide.with(holder.itemView.context)
                        .load(bmp)
                        .apply(RequestOptions().centerCrop())
                        .into(holder.image).view
            }
        }

        holder.price.text = "$".repeat(currentItem.price) // for displaying $ instead of numbers
        holder.address.text = currentItem.address
        holder.name.text = currentItem.name

        if (isFavoriteForCurr(currentItem)) {
            holder.favourite.setBackgroundResource(R.drawable.star_filled)
        }
        else {
            holder.favourite.setBackgroundResource(R.drawable.star)
        }

        holder.favourite.setOnClickListener {
            if (MainActivity.isLoggedIn) {
                holder.favourite.setBackgroundResource(R.drawable.star_filled)
                fav = Favorite(currentItem.id, Constants.USER_NAME)
                daoViewModel.addRestaurantDB(fav)
                Snackbar.make(
                    holder.itemView,
                    "${currentItem.name} added to favourites",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
            else {
                Toast.makeText(context,"You can't add favorites! Please sign in!", Toast.LENGTH_LONG).show()
            }
        }

        holder.favourite.setOnLongClickListener {
            if(isFavoriteForCurr(currentItem)) {
                daoViewModel.deleteRestaurantDB(currentItem.id)
                holder.favourite.setBackgroundResource(R.drawable.star)
                Snackbar.make(
                    holder.itemView,
                    "${currentItem.name} removed from favourites",
                    Snackbar.LENGTH_SHORT
                ).show()
                notifyDataSetChanged()
            }
            true
        }

        // when clicking on an item, bundling it's details and navigating to details fragment
        holder.itemView.setOnClickListener{
            val bundle = bundleOf(
                    "name" to currentItem.name,
                    "address" to currentItem.address,
                    "city" to currentItem.city,
                    "state" to currentItem.state,
                    "area" to currentItem.area,
                    "postal_code" to currentItem.postal_code,
                    "country" to currentItem.country,
                    "price" to "$".repeat(currentItem.price),
                    "lat" to currentItem.lat,
                    "lng" to currentItem.lng,
                    "phone" to currentItem.phone,
                    "reserve_url" to currentItem.reserve_url,
                    "mobile_reserve_url" to currentItem.mobile_reserve_url,
                    "image" to currentItem.image_url)

            Toast.makeText(context, "Item ${currentItem.name} clicked", Toast.LENGTH_SHORT).show()

            holder.itemView.findNavController().navigate(R.id.navigation_details, bundle)
        }
    }

    override fun getItemCount() = searchableList.size

    // setting the data that will be exposed, saving a copy of it in the searcheble list for filtering results
    fun setData(restaurants: List<Restaurant>) {
        this.restaurantList = restaurants
        this.searchableList = restaurants.toMutableList()
        notifyDataSetChanged()
    }

    // overriding the filterable inteface's methods for performing search by name
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

    /**
     * @param rest a single restaurant object
     * @return boolean `true` if the rest is favorite for the user
     * @return boolean `false` if the rest is NOT favorite for the user
     * */
    private fun isFavoriteForCurr(rest:Restaurant) : Boolean{
        for (i in FavouritesAdapter.restFavs) {
            if (i.name == rest.name && i.id == rest.id) {
                return true
            }
        }
        return false
    }

}
