package com.josephshawcroft.spacexapi.flightlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.ImageLoader
import coil.request.LoadRequest
import com.josephshawcroft.spacexapi.R
import com.josephshawcroft.spacexapi.data.model.LaunchWithRocketInfo
import com.josephshawcroft.spacexapi.databinding.LaunchItemBinding

class LaunchListAdapter(private val imageLoader: ImageLoader) :
    RecyclerView.Adapter<LaunchListAdapter.ViewHolder>() {

    private val launchesList = mutableListOf<LaunchWithRocketInfo>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = LaunchItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = launchesList[position]
        holder.missionText.text = item.launch.missionName
        holder.dateTimeText.text = item.launch.missionDate
        holder.rocketText.text = holder.itemView.context.getString(
            R.string.rocketSlash,
            item.rocket.name,
            item.rocket.type
        )
        holder.daysSinceText.text = "TODO"

        item.launch.missionImageUrl?.let {
            val request = LoadRequest.Builder(holder.itemView.context)
                .data(item.launch.missionImageUrl)
                .target(holder.missionImage)
                .placeholder(R.drawable.ic_rocket)
                .error(R.drawable.ic_rocket)
                .build()

            imageLoader.execute(request)
        } ?: holder.missionImage.setImageResource(R.drawable.ic_rocket)

        val isSuccessIcon = if(item.launch.wasSuccess) {
            R.drawable.ic_tick
        } else {
            R.drawable.ic_cross
        }

        holder.isSuccessIcon.setImageResource(isSuccessIcon)
    }

    override fun getItemCount(): Int = launchesList.count()

    fun updateList(items: LaunchesList) {
        launchesList.clear()
        launchesList.addAll(items)
        notifyDataSetChanged()
    }

    class ViewHolder(binding: LaunchItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val missionImage = binding.missionImage
        val missionText = binding.missionData
        val dateTimeText = binding.dateTimeData
        val rocketText = binding.rocketData
        val daysSinceText = binding.daysSinceData
        val isSuccessIcon = binding.iconIsSuccess
    }
}