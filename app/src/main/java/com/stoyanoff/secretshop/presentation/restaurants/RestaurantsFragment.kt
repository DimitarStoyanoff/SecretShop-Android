package com.stoyanoff.secretshop.presentation.restaurants

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.SearchView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.stoyanoff.secretshop.R
import com.stoyanoff.secretshop.data.model.OpenStatus
import com.stoyanoff.secretshop.data.model.Restaurant
import com.stoyanoff.secretshop.presentation.common.BaseViewFragment
import kotlinx.android.synthetic.main.fragment_restaurants.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.*

/**
 * Created by L on 30/07/2019.
 *  Copyright (c) 2017 Centroida. All rights reserved.
 */
class RestaurantsFragment : BaseViewFragment() {

    private val viewModel : RestaurantsViewModel by viewModel()
    private val restaurantsAdapter : RestaurantsAdapter by inject()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentView = inflater.inflate(R.layout.fragment_restaurants,container,false)

        return fragmentView
    }

    override fun toggleLoading(isVisible: Boolean) {
        if(isVisible) {
            progressBar.visibility = View.VISIBLE
        } else progressBar.visibility = View.GONE
    }

    override fun initUi() {
        initAdapter()
        loadData()
        initSpinners()
        initSearchView()
    }

    private fun loadData() {
        viewModel.loadData()
    }

    private fun initAdapter() {
        restaurantsAdapter.clickListener = {
            viewModel.listItemClicked(it)
        }

        with(recycler_view){
            layoutManager = LinearLayoutManager(activity)
            adapter = restaurantsAdapter
        }
    }

    private fun initSpinners() {
        sort_type_spinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                viewModel.onSortTypeChanged(p2)
            }
        }
    }

    private fun initSearchView() {
        search_view.setOnQueryTextListener(object: androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
               return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                p0?.let {
                    viewModel.onSearchQueryChanged(it)
                }
                return false
            }

        })
    }

    override fun initViewModelStates() {
        handleRestaurantsViewState()
    }

    private fun handleRestaurantsViewState() {
        viewModel.viewState.observe(this, Observer {
            if(it != null) {
                toggleLoading(it.showLoading)

                it.results?.let {restaurants ->
                    restaurantsAdapter.setItems(restaurants)
                    restaurantsAdapter.setSortType(it.selectedSortType)
                }
            }
        })
    }






}