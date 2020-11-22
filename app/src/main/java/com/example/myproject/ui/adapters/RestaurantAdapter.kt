package com.example.myproject.ui.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide.with
import com.example.firstapplication.R
import com.example.myproject.models.Restaurant


class RestaurantAdapter (private val restaurantList: List<Restaurant>, private val listener: OnItemClickListener): RecyclerView.Adapter<RestaurantAdapter.RestaurantViewHolder>() {

    inner class RestaurantViewHolder(itemView: View): RecyclerView.ViewHolder(itemView), View.OnClickListener, View.OnLongClickListener {
        val image: ImageView = itemView.findViewById(R.id.imageView)
        val name: TextView = itemView.findViewById(R.id.info_text)
        val rating: TextView = itemView.findViewById(R.id.info_text1)

        init {
            itemView.setOnClickListener(this)
            itemView.setOnLongClickListener(this)
        }

        override fun onClick(v: View?) {
            val position: Int = adapterPosition
            if(position != RecyclerView.NO_POSITION) {
                listener.onItemClick(position)
            }
        }

        override fun onLongClick(v: View?): Boolean {
            val position: Int = adapterPosition
            if(position != RecyclerView.NO_POSITION) {
                listener.onItemLongClick(position)
            }
            return true
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantViewHolder {
       val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_item_row,
           parent, false)
        return RestaurantViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RestaurantViewHolder, position: Int) {
       val currentItem = restaurantList[position]
       // holder.image.setImageURI(currentItem.image_url.toUri())
        Log.d("position",currentItem.id.toString())
        with(holder.itemView)
            .load(currentItem.image_url)
            .into(holder.image)
        holder.rating.text = currentItem.price.toString()
        holder.name.text = currentItem.name
    }

    override fun getItemCount() = restaurantList.size
}
interface OnItemClickListener {
    fun onItemClick(position:Int)
    fun onItemLongClick(position: Int)
}