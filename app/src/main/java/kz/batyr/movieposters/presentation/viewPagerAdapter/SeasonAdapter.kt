package kz.batyr.movieposters.presentation.viewPagerAdapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kz.batyr.movieposters.R
import kz.batyr.movieposters.data.films_data.SeriesInfo
import kz.batyr.movieposters.presentation.recyclerViewAdapters.AllSeriesInfoAdapter
import kz.batyr.movieposters.presentation.fragments.SeasonFragment

class SeasonAdapter (private val context: SeasonFragment) :RecyclerView.Adapter<SeasonAdapter.SeasonViewHolder>(){
    private var items:List<SeriesInfo.Item> = emptyList()

    fun setData (itemList:List<SeriesInfo.Item>) {
        items = itemList
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SeasonViewHolder {
        val view = LayoutInflater.from(context.requireContext()).inflate(R.layout.season_item_page, parent, false)
        return SeasonViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: SeasonViewHolder, position: Int) {
        val item  = items[position].episodes
        holder.bind(item, items[position])
    }



    inner class SeasonViewHolder (itemView: View):RecyclerView.ViewHolder(itemView) {
        fun bind (data: List<SeriesInfo.Item.Episode>, position: SeriesInfo.Item){
            val adapter = AllSeriesInfoAdapter()
            val textView = itemView.findViewById<TextView>(R.id.textViewSeason_itemPage)
            val recyclerView = itemView.findViewById<RecyclerView>(R.id.recyclerViewSeason_itemPage)
            textView.text = "${position.number} сезон, ${data.size} серий"
            recyclerView.adapter = adapter
            adapter.setData(data)
        }
    }


}
