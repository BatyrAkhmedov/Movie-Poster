package kz.batyr.movieposters.presentation.recyclerViewAdapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import kz.batyr.movieposters.R
import kz.batyr.movieposters.databinding.RecyclerViewCountrysAndGenresBinding

class CountryAndGenresAdapter(
    private val onClick: (String) -> Unit,
    private val lastGenre: String
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var data: List<String> = emptyList()
    private var lastLayout: View? = null


    fun setData(data: List<String>) {
        this.data = data
        notifyDataSetChanged()
        Log.d("TAG", "setData $data")
        Log.d("TAG", "setData ${this.data}")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return CountryAndGenreViewHolder(
            RecyclerViewCountrysAndGenresBinding.inflate(
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
        val viewHolder = holder as CountryAndGenreViewHolder
        val context = viewHolder.itemView.context


        item?.let {currentItem ->
            viewHolder.binding.countryOrGenre.text = currentItem
            if (currentItem==lastGenre) viewHolder.binding.layout.setBackgroundColor( ContextCompat.getColor(context, R.color.gray30))
            else viewHolder.binding.layout.setBackgroundColor( ContextCompat.getColor(context, R.color.trans))
            viewHolder.itemView.setOnClickListener { onClick (currentItem) }
        }


    }

    inner class CountryAndGenreViewHolder(val binding: RecyclerViewCountrysAndGenresBinding) :
        RecyclerView.ViewHolder(binding.root)

}