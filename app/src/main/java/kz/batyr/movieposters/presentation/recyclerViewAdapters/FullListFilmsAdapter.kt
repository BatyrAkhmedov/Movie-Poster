package kz.batyr.movieposters.presentation.recyclerViewAdapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import kz.batyr.movieposters.data.films_data.FilmListPremiers
import kz.batyr.movieposters.databinding.RecyclerViewFilmsBinding

class FullListFilmsAdapter (val onClick: (Int) -> Unit) : ListFilmsAdapter(onClick, "null") {
    private var data:List<FilmListPremiers.Item> = emptyList()
    private val roundedCorner = RoundedCorners(16)
    override fun setData (data: List<FilmListPremiers.Item>) {
        this.data = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return FilmsViewHolder(
            RecyclerViewFilmsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ))
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = data.getOrNull(position)
        val viewHolder = holder as FilmsViewHolder
        with(viewHolder.binding) {
            if (item?.nameRu!=null) filmName.text = item.nameRu
            else if (item?.nameRu==null && item?.nameEn != null) {filmName.text = item.nameEn
            }
            else filmName.text = item?.nameOriginal

            genre.text = item?.genres?.first()?.genre

            item?.let {
                Glide.with(poster.context)
                    .load(it.posterUrlPreview)
                    .transform(roundedCorner)
                    .into(poster)
            }
            if (item?.ratingKinopoisk!=null) rate.text = item.ratingKinopoisk.toString()
            else if (item?.ratingKinopoisk == null && item?.rating == null && item?.ratingImdb!=null) rate.text = item?.ratingImdb.toString()
            else if (item?.ratingKinopoisk == null && item?.ratingImdb==null && item?.rating != null) rate.text = item?.rating.toString()
        }
        viewHolder.binding.poster.setOnClickListener {
            item?.let {
                onClick(item.kinopoiskId ?: item.filmId)
            }
        }

    }


    }







