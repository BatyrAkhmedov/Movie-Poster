package kz.batyr.movieposters.presentation.recyclerViewAdapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import kz.batyr.movieposters.data.films_data.FilmListPremiers
import kz.batyr.movieposters.databinding.RecyclerViewFilmsBinding
import kz.batyr.movieposters.presentation.recyclerViewAdapters.ListFilmsAdapter.FilmsViewHolder

class FullListFilmsPagedAdapter(val onClick:(Int)->Unit): PagingDataAdapter<FilmListPremiers.Item, FilmsViewHolder> (
    PagerDiffUtilCallback()
){
    private val roundedCorner = RoundedCorners(16)
    override fun onBindViewHolder(holder: FilmsViewHolder, position: Int) {
        val item = getItem(position)
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
        poster.setOnClickListener {
            item?.let {
                onClick(it.kinopoiskId ?: it.filmId)
            }
        }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FilmsViewHolder {
        return FilmsViewHolder(
            RecyclerViewFilmsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    class PagerDiffUtilCallback : DiffUtil.ItemCallback<FilmListPremiers.Item>() {
        override fun areItemsTheSame(
            oldItem: FilmListPremiers.Item,
            newItem: FilmListPremiers.Item
        ): Boolean = oldItem.filmId == newItem.filmId


        override fun areContentsTheSame(
            oldItem: FilmListPremiers.Item,
            newItem: FilmListPremiers.Item
        ): Boolean = oldItem == newItem

    }
}