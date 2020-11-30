package com.example.myproject.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.firstapplication.R
import com.example.myproject.models.Restaurant
import com.google.android.material.snackbar.Snackbar
import java.util.*


class RestaurantAdapter(val context:Context): RecyclerView.Adapter<RestaurantAdapter.RestaurantViewHolder>(){
    private var restaurantList = Collections.emptyList<Restaurant>()


    var restaurantFilterList : ArrayList<String> = arrayListOf()

    fun initializeFilter() {
        for (i in 1..restaurantList.size) {
            restaurantFilterList[i] = restaurantList[i].name
        }
    }

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
        holder.price.text = currentItem.price.toString()
        holder.address.text = currentItem.address
        holder.name.text = currentItem.name
        holder.favourite.setOnClickListener {
            holder.favourite.setBackgroundResource(R.drawable.star_filled)
            Snackbar.make(
                holder.itemView,
                "Item $position added to favourites",
                Snackbar.LENGTH_SHORT
            ).show()
        }
        holder.favourite.setOnLongClickListener {
            holder.favourite.setBackgroundResource(R.drawable.star)
            Snackbar.make(
                holder.itemView,
                "Item $position removed from favourites",
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
                    "price" to currentItem.price,
                    "lat" to currentItem.lat,
                    "lng" to currentItem.lng,
                    "phone" to currentItem.phone,
                    "reserve_url" to currentItem.reserve_url,
                    "mobile_reserve_url" to currentItem.mobile_reserve_url)

            Toast.makeText(context, "Item $position clicked", Toast.LENGTH_SHORT).show()

            holder.itemView.findNavController().navigate(R.id.navigation_details, bundle)
        }
    }
    override fun getItemCount() = restaurantList.size

    fun setData(restaurants: List<Restaurant>) {
        this.restaurantList = restaurants
        notifyDataSetChanged()
    }
}