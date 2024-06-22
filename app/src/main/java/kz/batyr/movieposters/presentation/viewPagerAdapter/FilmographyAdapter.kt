package kz.batyr.movieposters.presentation.viewPagerAdapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kz.batyr.movieposters.data.films_data.FilmListPremiers
import kz.batyr.movieposters.databinding.FilmographyViewpagerItemBinding
import kz.batyr.movieposters.presentation.recyclerViewAdapters.FilmographyFilmsAdapter

class FilmographyAdapter(val onClick: (Int) -> Unit) :
    RecyclerView.Adapter<FilmographyAdapter.FilmographyViewHolder>() {
    private var data: Map <String, List<FilmListPremiers.Item>> = emptyMap()

    fun setData(data: Map <String, List<FilmListPremiers.Item>>) {
        this.data = data
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilmographyViewHolder {
        return FilmographyViewHolder(
            FilmographyViewpagerItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return data.keys.size
    }

    override fun onBindViewHolder(holder: FilmographyViewHolder, position: Int) {
        val dataMap = data.entries.elementAt(position)
        holder.bind(dataMap)
    }


    inner class FilmographyViewHolder(private val binding: FilmographyViewpagerItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Map.Entry<String, List<FilmListPremiers.Item>>) {
            val adapter = FilmographyFilmsAdapter(onClick)
            binding.recyclerView.adapter = adapter
            adapter.setData(data.value)
        }
    }
}