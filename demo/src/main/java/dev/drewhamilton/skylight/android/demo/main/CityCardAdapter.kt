package dev.drewhamilton.skylight.android.demo.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.drewhamilton.skylight.android.demo.databinding.CityCardBinding

class CityCardAdapter : ListAdapter<CityCardAdapter.Data, CityCardAdapter.ViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(CityCardBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(
        private val binding: CityCardBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Data) = with(binding) {
            titleView.text = data.cityName
            dawnTimeView.text = data.dawn
            sunriseTimeView.text = data.sunrise
            sunsetTimeView.text = data.sunset
            duskTimeView.text = data.dusk
        }
    }

    data class Data(
        val cityName: String,
        val dawn: String,
        val sunrise: String,
        val sunset: String,
        val dusk: String,
    )

    private object DiffCallback : DiffUtil.ItemCallback<Data>() {
        override fun areItemsTheSame(oldItem: Data, newItem: Data): Boolean = oldItem.cityName == newItem.cityName
        override fun areContentsTheSame(oldItem: Data, newItem: Data): Boolean = oldItem == newItem
    }
}
