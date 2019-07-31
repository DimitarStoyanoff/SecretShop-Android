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
    private var selectedSortType = SortTypes.BEST_MATCH

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

    fun setSortType(type : SortTypes) {
        selectedSortType = type
        notifyDataSetChanged()
    }

    inner class RestaurantViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {

        val restaurantName = itemView.findViewById<TextView>(R.id.name)
        val sortType = itemView.findViewById<TextView>(R.id.sort_type)
        val sortValue = itemView.findViewById<TextView>(R.id.sort_value)
        val openStatus = itemView.findViewById<TextView>(R.id.open_status)
        val favoriteButton = itemView.findViewById<ImageView>(R.id.favorite_button)
        var restaurant : Restaurant? = null


        fun bindRestaurantItem(restaurant: Restaurant) {
            this.restaurant = restaurant
            restaurantName.text = restaurant.name
            openStatus.text = restaurant.status?.status
            sortType.text = selectedSortType.name

            //TODO use formatters instead
            when(selectedSortType){
                SortTypes.BEST_MATCH -> sortValue.text = restaurant.sortingValues?.bestMatch.toString()
                SortTypes.NEWEST ->sortValue.text = restaurant.sortingValues?.newest.toString()
                SortTypes.RATING_AVERAGE -> sortValue.text = restaurant.sortingValues?.ratingAverage.toString()
                SortTypes.DISTANCE -> sortValue.text = restaurant.sortingValues?.distance.toString()
                SortTypes.POPULARITY -> sortValue.text = restaurant.sortingValues?.popularity.toString()
                SortTypes.AVERAGE_PRODUCT_PRICE -> sortValue.text = restaurant.sortingValues?.averageProductPrice.toString()
                SortTypes.DELIVERY_COSTS -> sortValue.text = restaurant.sortingValues?.deliveryCosts.toString()
                SortTypes.MIN_COST -> sortValue.text = restaurant.sortingValues?.minCost.toString()
            }

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