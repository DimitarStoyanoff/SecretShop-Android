package com.stoyanoff.secretshop.presentation.restaurants

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.stoyanoff.secretshop.R
import com.stoyanoff.secretshop.data.model.Restaurant

/**
 * Created by L on 30/07/2019.
 *  Copyright (c) 2017 Centroida. All rights reserved.
 */
class RestaurantsAdapter : RecyclerView.Adapter<RestaurantsAdapter.RestaurantViewHolder>() {

    lateinit var clickListener: ((Restaurant) -> Unit)
    private var restaurants = mutableListOf<Restaurant>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.restaurant_list_item,parent,false)
        return RestaurantViewHolder(view)
    }

    override fun getItemCount(): Int {
        return restaurants.size
    }

    override fun onBindViewHolder(holder: RestaurantViewHolder, position: Int) {
        holder.bindRestaurantItem(restaurants[position])
    }

    fun setItems(items: MutableList<Restaurant>) {
        restaurants = items
        notifyDataSetChanged()
    }

    fun addItem(item : Restaurant) {
        restaurants.add(item)
        notifyItemInserted(restaurants.indexOf(item))
    }

    inner class RestaurantViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {

        val restaurantName = itemView.findViewById<TextView>(R.id.name)
        val sortType = itemView.findViewById<TextView>(R.id.sort_type)
        val sortSortValue = itemView.findViewById<TextView>(R.id.sort_value)
        val favoriteButton = itemView.findViewById<ImageView>(R.id.favorite_button)
        var restaurant : Restaurant? = null


        fun bindRestaurantItem(restaurant: Restaurant) {
            this.restaurant = restaurant
            restaurantName.text = restaurant.name

            if(restaurant.isFavorite) {
                favoriteButton.setImageResource(android.R.drawable.btn_star_big_on)
            } else {
                favoriteButton.setImageResource(android.R.drawable.btn_star_big_off)
            }

            favoriteButton.setOnClickListener {
                restaurant.let {
                    clickListener.invoke(restaurant)
                }
            }
        }
    }
}