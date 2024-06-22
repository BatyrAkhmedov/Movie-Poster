package kz.batyr.movieposters.presentation.viewPagerAdapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import androidx.paging.PagingData
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kz.batyr.movieposters.R
import kz.batyr.movieposters.data.films_data.Gallery
import kz.batyr.movieposters.presentation.recyclerViewAdapters.GalleryPagingAdapter
import kz.batyr.movieposters.presentation.fragments.GalleryFragment
import kz.batyr.movieposters.presentation.GalleryFragmentDirections

class TypeGalleryAdapter(private val context: GalleryFragment) :
    RecyclerView.Adapter<TypeGalleryAdapter.TypeGalleryViewHolder>() {

    private var items: List<String> = listOf("STILL", "SHOOTING", "POSTER")
    lateinit var listFlow: List<Flow<PagingData<Gallery.Item>>>

    fun setData(
        stillFlow: Flow<PagingData<Gallery.Item>>,
        shootingFlow: Flow<PagingData<Gallery.Item>>,
        posterFlow: Flow<PagingData<Gallery.Item>>,
    ) {
        listFlow = listOf(stillFlow, shootingFlow, posterFlow)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TypeGalleryViewHolder {
        val view = LayoutInflater.from(context.requireContext())
            .inflate(R.layout.gallery_viewpager_item, parent, false)
        return TypeGalleryViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: TypeGalleryViewHolder, position: Int) {
        val item = listFlow[position]
        holder.bind(item, items[position])
    }


    inner class TypeGalleryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(flow: Flow<PagingData<Gallery.Item>>, typeFlow: String) {
            val adapter = GalleryPagingAdapter { item -> onImageClick(item, typeFlow)}
            val recyclerView =
                itemView.findViewById<RecyclerView>(R.id.recyclerView_gallery_viewPager_item)
            val textView = itemView.findViewById<TextView>(R.id.imageNotFound)
            recyclerView.adapter = adapter
            val scope = CoroutineScope(Dispatchers.Main)
            flow.onEach {
                adapter.submitData(it)
            }.launchIn(scope)
            scope.launch {
                delay(2000)
                when {
                    (adapter.itemCount == 0) -> {
                        textView.visibility = View.VISIBLE
                    }
                    adapter.itemCount != 0 -> {
                        textView.visibility = View.GONE
                    }
                }
            }
        }
    }
    private fun onImageClick (position: Int, typeFlow:String){
        val action = GalleryFragmentDirections.actionGalleryFragmentToFullscreenFragment(typeFlow, url = null, position =  position, )
        Log.d("TAG", "position $position")
        context.findNavController().navigate(action)
    }

}

