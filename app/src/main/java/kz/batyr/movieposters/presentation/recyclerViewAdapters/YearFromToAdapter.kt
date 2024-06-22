package kz.batyr.movieposters.presentation.recyclerViewAdapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import kz.batyr.movieposters.R
import kz.batyr.movieposters.data.SearchingYear
import kz.batyr.movieposters.databinding.RecyclerViewYearFromToBinding

class YearFromToAdapter(
    private val onClick: (String) -> Unit,
    private val lastYear: String,
    private val context: Context
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var data: List<SearchingYear> = emptyList()
    private var selectedItemPosition = -1
    private var beforeLaunchPosition = -1
    private var beforeLaunchActivatedItem = lastYear


    fun setData(data: List<SearchingYear>) {
        this.data = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return YearViewHolder(
            RecyclerViewYearFromToBinding.inflate(
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
        val viewHolder = holder as YearViewHolder
        val textView = viewHolder.binding.year



        item?.let {
            textView.text = it.text
            if (it.text == beforeLaunchActivatedItem) {
                it.isSelected = true
               selectedItemPosition = viewHolder.bindingAdapterPosition

            }
            //else Log.d("TAG", "else")
            Log.d("TAG", "POsITION: $selectedItemPosition")
            // закраска зажатого и незажатых элементов
            if (it.isSelected) checked(textView)
            else unchecked(textView)
        }

        viewHolder.itemView.setOnClickListener {
            // Если мы нажимали ранее на кнопку, у нас сохранена его позиция
            // находим элемент по позиции, и у элемента изменяем isSelected = false
            // потверждаем изменение элемента в recyclerView
            Log.d("TAG", "POSiTION: $selectedItemPosition")
            if (selectedItemPosition != -1) {
                data[selectedItemPosition].isSelected = false
                beforeLaunchActivatedItem = ""
                notifyItemChanged(selectedItemPosition)
            }




            item?.let {
                onClick(it.text)
                it.isSelected = true
                selectedItemPosition = viewHolder.bindingAdapterPosition
                notifyItemChanged(viewHolder.bindingAdapterPosition)
            }
            Log.d("TAG", "POSItION: $selectedItemPosition")
        }





    }

    private fun checked(view: TextView) {
        Log.d("TAG", "Закрасил ${view.text}")
        view.background =
            ContextCompat.getDrawable(context, R.drawable.settings_year_background_checked)
        view.setTextColor(ContextCompat.getColor(context, R.color.white))
    }

    private fun unchecked(view: TextView) {
        Log.d("TAG", "Забелил ${view.text}")
        view.background =
            ContextCompat.getDrawable(context, R.drawable.settings_year_background_unchecked)
        view.setTextColor(ContextCompat.getColor(context, R.color.black))
    }


    inner class YearViewHolder(val binding: RecyclerViewYearFromToBinding) :
        RecyclerView.ViewHolder(binding.root)
}