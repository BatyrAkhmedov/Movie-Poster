package kz.batyr.movieposters.presentation.recyclerViewAdapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import jp.wasabeef.glide.transformations.RoundedCornersTransformation
import kz.batyr.movieposters.data.films_data.Gallery
import kz.batyr.movieposters.databinding.GridImageBinding

class GalleryPagingAdapter (val onClick:(Int)->Unit) : PagingDataAdapter<Gallery.Item, GalleryPagingAdapter.GalleryPagingViewHolder>(
    GalleryPagerDiffUtilCallback()
) {
    private val roundedCorner = RoundedCorners(40)
    private val requestOptions = RequestOptions
        .bitmapTransform(roundedCorner)
        .transform(RoundedCornersTransformation(40,0))

    override fun onBindViewHolder(holder: GalleryPagingViewHolder, position: Int) {
        val item = getItem(position)
        val viewHolder = holder as GalleryPagingViewHolder
        with(viewHolder.binding) {
            item?.let {
                Glide.with(imageView.context)
                    .load(it.imageUrl)
                    .transform(roundedCorner)
                    .optionalFitCenter()
                    .apply(requestOptions)
                    .into(imageView)
                holder.itemView.setOnClickListener {
                    Log.d("TAG", "position GPA $position")
                    onClick(position)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryPagingViewHolder {
        return GalleryPagingViewHolder(
            GridImageBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }





    inner class GalleryPagingViewHolder(val binding:GridImageBinding) :
        RecyclerView.ViewHolder(binding.root)
}
class GalleryPagerDiffUtilCallback : DiffUtil.ItemCallback<Gallery.Item>() {
    override fun areItemsTheSame(
        oldItem: Gallery.Item,
        newItem: Gallery.Item
    ): Boolean = oldItem.imageUrl == newItem.imageUrl


    override fun areContentsTheSame(
        oldItem: Gallery.Item,
        newItem: Gallery.Item
    ): Boolean = oldItem == newItem

}