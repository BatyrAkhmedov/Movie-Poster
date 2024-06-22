package kz.batyr.movieposters.presentation.recyclerViewAdapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import jp.wasabeef.glide.transformations.RoundedCornersTransformation
import kz.batyr.movieposters.data.films_data.Gallery
import kz.batyr.movieposters.databinding.RecyclerViewGalleryBinding

class GalleryAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var data: List<Gallery.Item> = emptyList()
    private val roundedCorner = RoundedCorners(40)
    private val requestOptions = RequestOptions
        .bitmapTransform(roundedCorner)
        .transform(RoundedCornersTransformation(40,0))

    fun setData(data: List<Gallery.Item>) {
        this.data = data
        notifyDataSetChanged()
        Log.d("TAG", "setData $data")
        Log.d("TAG", "setData ${this.data}")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return GalleryViewHolder(
            RecyclerViewGalleryBinding.inflate(
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
        val viewHolder = holder as GalleryViewHolder
        with(viewHolder.binding) {
            item?.let {
                com.bumptech.glide.Glide.with(imageView.context)
                    .load(it.previewUrl)
                    .transform(roundedCorner)
                    .apply(requestOptions)
                    .into(imageView)
            }
        }

    }

    inner class GalleryViewHolder(val binding: RecyclerViewGalleryBinding) :
        RecyclerView.ViewHolder(binding.root)


}