package kz.batyr.movieposters.presentation.recyclerViewAdapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kz.batyr.movieposters.data.films_data.Gallery
import kz.batyr.movieposters.databinding.FullscreenImageBinding

class FullscreenPagingAdapter() :
    PagingDataAdapter<Gallery.Item, FullscreenPagingAdapter.FullscreenViewHolder>(
        GalleryPagerDiffUtilCallback()
    )
{



    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FullscreenViewHolder {
        return FullscreenViewHolder(FullscreenImageBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }



    override fun onBindViewHolder(holder: FullscreenViewHolder, position: Int) {

        val item = getItem(position)
        with(holder.binding) {
            item?.let {
                Glide.with(imageViewFullscreen.context)
                    .load(it.imageUrl)
                    .into(imageViewFullscreen)
            }
        }
    }


    class FullscreenViewHolder(val binding:FullscreenImageBinding ) : RecyclerView.ViewHolder(binding.root)
}