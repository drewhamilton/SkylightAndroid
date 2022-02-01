package dev.drewhamilton.skylight.android.demo.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.backbase.deferredresources.DeferredColor
import dev.drewhamilton.skylight.android.demo.R
import dev.drewhamilton.skylight.android.demo.databinding.CityCardBinding

class CityCardAdapter : ListAdapter<CityCardAdapter.Data, CityCardAdapter.ViewHolder>(DiffCallback) {

    private val highlightedViewType = 1
    private val normalViewType = 2

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            CityCardBinding.inflate(LayoutInflater.from(parent.context), parent, false).apply {
                if (viewType == highlightedViewType) {
                    val backgroundColor = DeferredColor.Attribute(R.attr.colorPrimaryContainer)
                    root.setCardBackgroundColor(backgroundColor.resolve(root.context))
                }
            }
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun getItemViewType(position: Int): Int {
        return if (getItem(position).isHighlighted) highlightedViewType else normalViewType
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
        val isHighlighted: Boolean,
    )

    private object DiffCallback : DiffUtil.ItemCallback<Data>() {
        override fun areItemsTheSame(oldItem: Data, newItem: Data): Boolean =
            oldItem.cityName == newItem.cityName && oldItem.isHighlighted == newItem.isHighlighted

        override fun areContentsTheSame(oldItem: Data, newItem: Data): Boolean = oldItem == newItem
    }
}
