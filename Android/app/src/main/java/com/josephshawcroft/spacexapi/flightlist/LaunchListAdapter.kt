package com.josephshawcroft.spacexapi.flightlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.josephshawcroft.spacexapi.data.model.LaunchWithRocketInfo
import com.josephshawcroft.spacexapi.databinding.LaunchItemBinding

class LaunchListAdapter : RecyclerView.Adapter<LaunchListAdapter.ViewHolder>() {

    private val launchesList = mutableListOf<LaunchWithRocketInfo>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = LaunchItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

    }

    override fun getItemCount(): Int = launchesList.count()

    fun updateList(items: LaunchesList) {
        launchesList.clear()
        launchesList.addAll(items)
    }

    class ViewHolder(binding: LaunchItemBinding) : RecyclerView.ViewHolder(binding.root) {

    }
}