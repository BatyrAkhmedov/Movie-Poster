package kz.batyr.movieposters.presentation.recyclerViewAdapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kz.batyr.movieposters.data.films_data.SeriesInfo
import kz.batyr.movieposters.data.films_data.Staff
import kz.batyr.movieposters.databinding.RecyclerViewSeasonsViewPagerBinding

class AllSeriesInfoAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var data: List<SeriesInfo.Item.Episode> = emptyList()


    fun setData(data: List<SeriesInfo.Item.Episode>) {
        this.data = data
        notifyDataSetChanged()
        Log.d("TAG", "setData $data")
        Log.d("TAG", "setData ${this.data}")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return SeriesInfoViewHolder(
            RecyclerViewSeasonsViewPagerBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = data.getOrNull(position)
        val viewHolder = holder as SeriesInfoViewHolder
        with(viewHolder.binding) {
            item?.let {
                if (it.nameEn==null && it.nameRu!=null) seriaNameRuNameEn.text = "${it.episodeNumber} серия. ${it.nameRu}."
                else if (it.nameRu==null && it.nameEn!=null) seriaNameRuNameEn.text = "${it.episodeNumber} серия. ${it.nameEn}"
                else if (it.nameEn==null && it.nameRu==null) seriaNameRuNameEn.text = "${it.episodeNumber} серия"
                else seriaNameRuNameEn.text = "${it.episodeNumber} серия. ${it.nameRu}. (${it.nameEn})"
                description.text = it.synopsis
                date.text = it.releaseDate
            }

        }

    }

    inner class SeriesInfoViewHolder(val binding: RecyclerViewSeasonsViewPagerBinding) :
        RecyclerView.ViewHolder(binding.root)


}