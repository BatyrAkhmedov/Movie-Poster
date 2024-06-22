package kz.batyr.movieposters.presentation.recyclerViewAdapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import jp.wasabeef.glide.transformations.RoundedCornersTransformation
import kz.batyr.movieposters.data.films_data.Staff
import kz.batyr.movieposters.databinding.RecyclerViewParticipantBinding

class ParticipantsAdapter(val onClick: (Int) -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var data: List<Staff.StaffItem> = emptyList()
    private val roundedCorner = RoundedCorners(40)
    private val requestOptions = RequestOptions
        .bitmapTransform(roundedCorner)
        .transform(RoundedCornersTransformation(40,0))

    fun setData(data: List<Staff.StaffItem>) {
        this.data = data
        notifyDataSetChanged()
        Log.d("TAG", "setData $data")
        Log.d("TAG", "setData ${this.data}")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ParticipantsViewHolder(
            RecyclerViewParticipantBinding.inflate(
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
        val viewHolder = holder as ParticipantsViewHolder
        with(viewHolder.binding) {
            item?.let {
                Glide.with(photo.context)
                    .load(it.posterUrl)
                    .transform(roundedCorner)
                    .apply(requestOptions)
                    .into(photo)
                photo.background
            }
            participantName.text = item?.nameRu
            if (item?.nameRu == "") participantName.text = item.nameEn
            if (item?.professionKey == "ACTOR") role.text = item.description
            else role.text = item?.professionText
        }
        holder.binding.photo.setOnClickListener {
            item?.let { onClick(it.staffId)}
        }
        holder.itemView.setOnClickListener {
            item?.let { onClick(it.staffId) }
        }

    }

    inner class ParticipantsViewHolder(val binding: RecyclerViewParticipantBinding) :
        RecyclerView.ViewHolder(binding.root)


}







