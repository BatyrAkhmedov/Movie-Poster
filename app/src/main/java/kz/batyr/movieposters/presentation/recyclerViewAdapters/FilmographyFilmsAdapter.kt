package kz.batyr.movieposters.presentation.recyclerViewAdapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import kz.batyr.movieposters.data.films_data.FilmListPremiers
import kz.batyr.movieposters.databinding.RecyclerViewFilmographyItemBinding

class FilmographyFilmsAdapter(val onClick: (Int) -> Unit) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var data: List<FilmListPremiers.Item> = emptyList()
    private val roundedCorner = RoundedCorners(16)
    fun setData(data: List<FilmListPremiers.Item>) {
        this.data = data
        notifyDataSetChanged()
        Log.d("TAG", "1")
        Log.d("TAG", "$data")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return FilmographyFilmsViewHolder(
            RecyclerViewFilmographyItemBinding.inflate(
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
        Log.d("TAG", "2")
        val item = data.getOrNull(position)
        val viewHolder = holder as FilmographyFilmsViewHolder
        with(viewHolder.binding) {
            if (item?.nameRu != null) filmName.text = item.nameRu
            else if (item?.nameRu == null && item?.nameEn != null) {
                filmName.text = item.nameEn
            } else filmName.text = item?.nameOriginal
            item?.let { yearGenres.text = "${it.year}, ${it.genres?.firstOrNull()?.genre}" }
            item?.let {
                Glide.with(poster.context)
                    .load(it.posterUrlPreview)
                    .transform(roundedCorner)
                    .into(poster)
            }
            Log.d("TAG", "uuuuuuuuuuu")

            if (item?.ratingKinopoisk != null) rate.text = item.ratingKinopoisk.toString()
            else if (item?.ratingKinopoisk == null && item?.rating == null && item?.ratingImdb != null) rate.text =
                item?.ratingImdb.toString()
            else if (item?.ratingKinopoisk == null && item?.ratingImdb == null && item?.rating != null) rate.text =
                item?.rating.toString()
        }
        viewHolder.binding.poster.setOnClickListener {
            item?.let {
                onClick(item.kinopoiskId ?: item.filmId)
            }
        }
    }

    class FilmographyFilmsViewHolder(val binding: RecyclerViewFilmographyItemBinding) :
        RecyclerView.ViewHolder(binding.root)


}
