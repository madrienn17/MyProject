package com.example.myproject.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.firstapplication.R
import com.example.myproject.models.Restaurant
import com.google.android.material.snackbar.Snackbar
import java.util.*


class RestaurantAdapter(var listener :OnItemClickListener): RecyclerView.Adapter<RestaurantAdapter.RestaurantViewHolder>(){
    private var restaurantList = Collections.emptyList<Restaurant>()


    var restaurantFilterList : ArrayList<String> = arrayListOf()

    fun initializeFilter() {
        for (i in 1..restaurantList.size) {
            restaurantFilterList[i] = restaurantList[i].name
        }
    }

    inner class RestaurantViewHolder(itemView: View): RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val image: ImageView = itemView.findViewById(R.id.imageView)
        val name: TextView = itemView.findViewById(R.id.name)
        val address: TextView = itemView.findViewById(R.id.address)
        val price: TextView = itemView.findViewById(R.id.price)
        val favourite: ImageButton = itemView.findViewById(R.id.star)


        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position: Int = adapterPosition
            if(position != RecyclerView.NO_POSITION) {
                listener.onItemClick(position)
            }
        }
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
    }
    override fun getItemCount() = restaurantList.size

    fun setData(restaurants: List<Restaurant>) {
        this.restaurantList = restaurants
        notifyDataSetChanged()
    }
}
interface OnItemClickListener {
    fun onItemClick(position: Int)
}