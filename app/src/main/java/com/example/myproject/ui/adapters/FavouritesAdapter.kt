package com.example.myproject.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.firstapplication.R
import com.example.myproject.models.Restaurant
import com.example.myproject.ui.viewmodels.SharedViewModel

class FavouritesAdapter(private val context: Context, private val sharedViewModel: SharedViewModel) : RecyclerView.Adapter<FavouritesAdapter.MyViewHolder>() {
    private var favoritesList = emptyList<Restaurant>()

    inner class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.image)
        val name: TextView = itemView.findViewById(R.id.name)
        val address: TextView = itemView.findViewById(R.id.address)
        val price: TextView = itemView.findViewById(R.id.price)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.favourites_item_row,
            parent, false
        )
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = favoritesList[position]

        Glide.with(holder.itemView.context)
            .load(currentItem.image_url)
            .placeholder(R.drawable.ic_launcher_foreground)
            .into(holder.image).view
        holder.price.text = currentItem.price.toString()
        holder.address.text = currentItem.address
        holder.name.text = currentItem.name


        holder.itemView.setOnClickListener {
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

            Toast.makeText(this.context, "Item $position clicked", Toast.LENGTH_SHORT).show()

            holder.itemView.findNavController().navigate(R.id.navigation_details, bundle)
        }

        holder.itemView.setOnLongClickListener {
            val alertDialog: AlertDialog = context.let{

                val builder = AlertDialog.Builder(context)
                builder.apply {
                    setTitle("Are you sure you want to delete this item?")
                    setPositiveButton("Yes"
                    ) { _, _ ->
                        sharedViewModel.removeFavorite(currentItem)
                        notifyDataSetChanged()
                        Toast.makeText(context, "Item $position Deleted", Toast.LENGTH_SHORT).show()
                    }
                    setNegativeButton("No"
                    ) { _, _ ->
                        Toast.makeText(context, "Delete cancelled", Toast.LENGTH_SHORT).show()
                    }
                }
                builder.create()
            }
            alertDialog.show()
            alertDialog.isShowing
        }
    }
    override fun getItemCount() = favoritesList.size

    fun setFav(userName:String) {
        this.favoritesList = sharedViewModel.getUserFavorites(userName)
        notifyDataSetChanged()
    }
}