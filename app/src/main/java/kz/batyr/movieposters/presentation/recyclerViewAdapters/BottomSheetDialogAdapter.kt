package kz.batyr.movieposters.presentation.recyclerViewAdapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kz.batyr.movieposters.R
import kz.batyr.movieposters.data.dataBase_entity.CollectionsFilms
import kz.batyr.movieposters.data.dataBase_entity.ViewedFilmsDbEntity
import kz.batyr.movieposters.databinding.RecyclerViewBottomSheetBinding

class BottomSheetDialogAdapter(
    private val deleteFilm:(String) -> Unit,
    private val addFilm :(String) -> Unit,
    private val lifecycleScope:CoroutineScope,
    private val filmId:Int
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var allFilms:List<ViewedFilmsDbEntity> = emptyList()
    var collections:List<CollectionsFilms> = emptyList()
    fun setData(data: StateFlow<List<CollectionsFilms>>, allFilmsData:StateFlow<List<ViewedFilmsDbEntity>>){
        data.onEach {
            collections = it.filter {  it.collectionName!="Просмотрено" }
            notifyDataSetChanged()
        }.launchIn(lifecycleScope)
        allFilmsData.onEach {
            allFilms = it
            notifyDataSetChanged()
        }.launchIn(lifecycleScope)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return BottomSheetDialogVH(RecyclerViewBottomSheetBinding.inflate(LayoutInflater.from(parent.context),
        parent, false))
    }

    override fun getItemCount(): Int {
        return collections.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder as BottomSheetDialogVH
        val collection = collections[position].collectionName
        holder.binding.title.text = collection
        holder.binding.countAtCollection.text =
            allFilms.filter { it.collectionName == collection }.size.toString()
        Log.e("TAG", "find  ${allFilms.find {it.collectionName == collection}}")
        Log.e("TAG", "find  ${collection}")
        Log.e("TAG", "find  ${filmId}}")
        if (allFilms.find { it.collectionName == collection && it.id==filmId}!=null)
            holder.binding.stateCheck.isSelected = true
        holder.itemView.setOnClickListener {
            it.isSelected = !it.isSelected
            if(it.isSelected) addFilm(collection)
            else deleteFilm (collection)
        }
    }

    inner class BottomSheetDialogVH(val binding: RecyclerViewBottomSheetBinding):RecyclerView.ViewHolder (binding.root)

}