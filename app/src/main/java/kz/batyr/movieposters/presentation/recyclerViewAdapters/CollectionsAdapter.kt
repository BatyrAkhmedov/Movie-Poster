package kz.batyr.movieposters.presentation.recyclerViewAdapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kz.batyr.movieposters.R
import kz.batyr.movieposters.data.dataBase_entity.CollectionsFilms
import kz.batyr.movieposters.data.dataBase_entity.ViewedFilmsDbEntity
import kz.batyr.movieposters.databinding.RecyclerViewCollectionsBinding

class CollectionsAdapter(val lifecycleScope: CoroutineScope,
                         private val deleteCollection:(String) -> Unit,
                         private val selectCollection: (String) -> Unit ) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var collectionFlow: StateFlow<List<CollectionsFilms>>? = null
    private var filmsFlow: StateFlow<List<ViewedFilmsDbEntity>>? = null

    private var collectionsNames: List<CollectionsFilms> = emptyList()
    private var collectionsFilms: List<ViewedFilmsDbEntity> = emptyList()

    fun setData(
        collection: StateFlow<List<CollectionsFilms>>,
        filmList: StateFlow<List<ViewedFilmsDbEntity>>?
    ) {
        collectionFlow = collection
        filmsFlow = filmList

        combine(collectionFlow?: MutableStateFlow(emptyList()), filmsFlow ?: MutableStateFlow(emptyList())) { collectionNamesList, filmsInCollectionList ->
            Pair(collectionNamesList, filmsInCollectionList)
        }.onEach { (collectionNamesList, filmsInCollectionList) ->
            collectionsNames = collectionNamesList
            collectionsFilms = filmsInCollectionList
            notifyDataSetChanged()
        }.launchIn(lifecycleScope)
    }

    override fun getItemCount(): Int {
        return collectionsNames.size
    }

    override fun getItemViewType(position: Int): Int {
        Log.i("TAG", "position: $position")
        return when {
            position == 0 -> TYPE_LIKED
            position == 1 -> TYPE_FAVORITE
            position >= 2 -> TYPE_CUSTOMER
            else -> Log.i("TAG", "else")
                //throw (Exception("invalid View type"))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
//        return when (viewType) {
//            TYPE_LIKED -> CollectionViewHolder(RecyclerViewCollectionsBinding.inflate(
//                LayoutInflater.from(parent.context),
//                parent,
//                false
//            ))
//            TYPE_FAVORITE -> CollectionViewHolderFavorite()
//            TYPE_CUSTOMER -> CollectionViewHolderCustomer()
//            else -> throw (Exception ("invalid View type"))
//        }
        return CollectionViewHolder(
            RecyclerViewCollectionsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewHolder = holder as CollectionViewHolder
        when (holder.itemViewType) {
            TYPE_LIKED -> {
                holder.binding.icon.setImageResource(R.drawable.collection_liked)
                holder.binding.buttonDelete.visibility = View.GONE
            }
            TYPE_FAVORITE -> {
                holder.binding.icon.setImageResource(R.drawable.collection_favorite)
                holder.binding.buttonDelete.visibility = View.GONE
            }

            TYPE_CUSTOMER -> {
                holder.binding.icon.setImageResource(R.drawable.collection_user)
            }
        }
        holder.binding.title.text = collectionsNames[position].collectionName

        val oneCollectionFilms = collectionsFilms.filter { it.collectionName == collectionsNames[position].collectionName }
        holder.binding.count.text = oneCollectionFilms.size.toString()
        holder.binding.buttonDelete.setOnClickListener {
            deleteCollection(collectionsNames[position].collectionName)
        }
        holder.binding.layout.setOnClickListener {
            selectCollection(collectionsNames[position].collectionName)
        }
    }


    inner class CollectionViewHolder(val binding: RecyclerViewCollectionsBinding) :
        RecyclerView.ViewHolder(binding.root)

    companion object {
        const val TYPE_LIKED = 1
        const val TYPE_FAVORITE = 2
        const val TYPE_CUSTOMER = 3
    }

}