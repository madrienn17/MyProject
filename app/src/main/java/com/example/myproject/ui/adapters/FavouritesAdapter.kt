package com.example.myproject.ui.adapters

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.firstapplication.R
import com.example.myproject.models.Restaurant
import com.example.myproject.repository.ApiRepository
import com.example.myproject.ui.fragments.RestaurantsListFragment
import com.example.myproject.ui.viewmodels.ApiViewModel
import com.example.myproject.ui.viewmodels.ApiViewModelFactory
import com.example.myproject.ui.viewmodels.DaoViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class FavouritesAdapter(private val context: Context, private val daoViewModel: DaoViewModel) : RecyclerView.Adapter<FavouritesAdapter.MyViewHolder>() {
    var favoritesList = emptyList<Restaurant>()

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

        Companion.context = context
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = favoritesList[position]
        holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.cardBackground))

        holder.price.text = "$".repeat(currentItem.price)
        holder.address.text = currentItem.address
        holder.name.text = currentItem.name

        Glide.with(holder.itemView.context)
            .load(currentItem.image_url)
            .apply(RequestOptions().centerCrop())
            .placeholder(R.drawable.ic_launcher_foreground)
            .into(holder.image).view

        for (i in RestaurantsListFragment.restPics) {
            if (currentItem.name == i.restName) {
                val bmp: Bitmap = BitmapFactory.decodeByteArray(i.restPic, 0, i.restPic.size)
                Glide.with(holder.itemView.context)
                        .load(bmp)
                        .apply(RequestOptions().centerCrop())
                        .into(holder.image).view
            }
        }

        holder.itemView.setOnClickListener {
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
                "image" to currentItem.image_url
                )


            Toast.makeText(this.context, "${currentItem.name} clicked", Toast.LENGTH_SHORT).show()

            holder.itemView.findNavController().navigate(R.id.navigation_details, bundle)
        }

        holder.itemView.setOnLongClickListener {
            val alertDialog: AlertDialog = context.let{

                val builder = AlertDialog.Builder(context)
                builder.apply {
                    setTitle("Are you sure you want to delete this item?")
                    setPositiveButton("Yes"
                    ) { _, _ ->
                        daoViewModel.deleteRestaurantDB(currentItem.id)
                        notifyDataSetChanged()
                        Toast.makeText(context, "${currentItem.name} deleted  ", Toast.LENGTH_SHORT).show()
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

    fun setFav(favs:List<Restaurant>) {
        this.favoritesList = favs
        Log.d("SETFAVLIST", favoritesList.toString())
        if (favoritesList.isNotEmpty()) {
            hasFavorites = true
        }
        notifyDataSetChanged()
    }

    companion object: CoroutineScope {
        var restFavs = emptyList<Restaurant>()
        var hasFavorites = false

        lateinit var context: Context
        lateinit var restaurantViewModel: ApiViewModel

        override val coroutineContext: CoroutineContext
            get() =  Dispatchers.Main + Job()

        fun getRestListByid(favIds: List<Long>) {
            var favrests: MutableList<Restaurant> = mutableListOf()
            launch {
                val repository = ApiRepository()
                val factory = ApiViewModelFactory(repository)
                restaurantViewModel =
                    ViewModelProvider(ViewModelStore(), factory).get(ApiViewModel::class.java)
                if (favIds.isNotEmpty()) {
                    for (i in favIds) {
                        val restresp = restaurantViewModel.getRestaurantsById(i)
                        if (restresp.isSuccessful && restresp.body() != null) {
                            val respbody = restresp.body()
                            if (respbody != null) {
                                favrests.add(respbody)
                            } else {
                                Toast.makeText(
                                    context,
                                    "Cannot load favorites because of API!",
                                    Toast.LENGTH_SHORT
                                ).show()
                                break
                            }
                        }
                    }
                } else {
                    favrests = emptyList<Restaurant>().toMutableList()
                }
                restFavs = favrests.toList()
            }
        }
    }
}