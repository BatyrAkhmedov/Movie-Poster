package kz.batyr.movieposters.presentation.recyclerViewAdapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import kz.batyr.movieposters.R
import kz.batyr.movieposters.data.films_data.FilmListPremiers
import kz.batyr.movieposters.databinding.RecyclerViewFilmsBinding
import kz.batyr.movieposters.databinding.RecyclerViewFilmsFinishBinding
import kz.batyr.movieposters.presentation.ActorFragmentDirections
import kz.batyr.movieposters.presentation.MainFragmentDirections


open class ListFilmsAdapter(private val onClick: (Int) -> Unit,
                            private val filmCategory:String): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var data:List<FilmListPremiers.Item> = emptyList()
    private val roundedCorner = RoundedCorners(16)
    open fun setData (data: List<FilmListPremiers.Item>) {
        Log.e("TAG", "setData ${data.toString()}")
        this.data = data
        notifyDataSetChanged()
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_1 -> FilmsViewHolder(
                RecyclerViewFilmsBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )

            VIEW_TYPE_2 -> FilmsContinueViewHolder(
                RecyclerViewFilmsFinishBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )

            else -> throw IllegalArgumentException("Invalid view type")
        }

    }

    override fun getItemViewType(position: Int): Int {
        return when {
            position > 19  -> return VIEW_TYPE_2
            position <= 20 -> return VIEW_TYPE_1
            else -> throw IllegalArgumentException ("Invalid view type")
        }
    }

    override fun getItemCount(): Int {
        return if (data.size<=20) data.size
        else 21

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = data.getOrNull(position)
        when (holder.itemViewType){
            VIEW_TYPE_1 -> {
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
                        if (it.ratingKinopoisk !=null) rate.text = it.ratingKinopoisk.toString()
                        else if (it.rating == null && it.ratingImdb!=null) rate.text = it.ratingImdb.toString()
                        else if (it.ratingImdb==null && it.rating != null) rate.text = it.rating.toString()
                        else Log.e("ERROR", "RATING.isEmpty ListFilmAdapter")
//                        Log.e("ERROR", "${it.ratingKinopoisk}")
//                        Log.e("ERROR", "${it.ratingImdb}")
//                        Log.e("ERROR", "${it.rating}")
//                        Log.e("ERROR", "${it.ratingChange}")
//                        Log.e("ERROR", "${it.ratingVoteCount}")
//                        Log.e("ERROR", "${it.isRatingUp}")
//                        Log.e("ERROR", "${it}")
                    }



                }
                holder.binding.poster.setOnClickListener {
                    item?.let {
                        onClick(item.kinopoiskId ?: item.filmId)
                    }
                }
            }
            VIEW_TYPE_2 -> {
                val viewHolder = holder as FilmsContinueViewHolder
                with(viewHolder) {
                    binding.button.setOnClickListener {
                        val action = if(filmCategory!="BestFilms") MainFragmentDirections.actionMainFragmentToFilmFragment(filmCategory)
                        else ActorFragmentDirections.actionActorFragmentToFilmFragment(filmCategory)
                        itemView.findNavController().navigate(action)
                    }
                }
            }

        }



    }


    class FilmsViewHolder(val binding: RecyclerViewFilmsBinding) : RecyclerView.ViewHolder(binding.root)
    class FilmsContinueViewHolder (val binding: RecyclerViewFilmsFinishBinding) : RecyclerView.ViewHolder(binding.root)

    companion object {
        private const val VIEW_TYPE_1 = 1
        private const val VIEW_TYPE_2 = 2
    }

}