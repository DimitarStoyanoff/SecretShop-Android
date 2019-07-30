package com.stoyanoff.secretshop.presentation.restaurants

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.stoyanoff.secretshop.R
import com.stoyanoff.secretshop.presentation.common.BaseViewFragment
import kotlinx.android.synthetic.main.fragment_restaurants.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel

/**
 * Created by L on 30/07/2019.
 *  Copyright (c) 2017 Centroida. All rights reserved.
 */
class RestaurantsFragment : BaseViewFragment() {

    private val viewModel : RestaurantsViewModel by viewModel()
    private val adapter : RestaurantsAdapter by inject()

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
    }

    private fun loadData() {
        viewModel.loadData()
    }

    private fun initAdapter() {
        adapter.clickListener = {
            viewModel.listItemClicked(it)
        }

        with(recycler_view){
            layoutManager = LinearLayoutManager(activity)
            adapter = adapter
        }
    }

    override fun initViewModelStates() {
        handleRestaurantsViewState()
    }

    private fun handleRestaurantsViewState() {
        viewModel.viewState.observe(this, Observer {
            if(it != null) {
                toggleLoading(it.showLoading)

                it.results?.let {albums ->
                    adapter.setItems(albums)
                }
            }
        })
    }
}