package kz.batyr.movieposters.presentation.fragments

import android.os.Bundle
import android.text.InputType
import android.text.Spannable
import android.text.SpannableString
import android.text.style.TypefaceSpan
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.launch
import kz.batyr.movieposters.R
import kz.batyr.movieposters.data.dataBase_entity.CollectionsFilms
import kz.batyr.movieposters.databinding.FragmentUserBinding
import kz.batyr.movieposters.domain.App
import kz.batyr.movieposters.presentation.recyclerViewAdapters.CollectionsAdapter
import kz.batyr.movieposters.presentation.recyclerViewAdapters.ListFilmsAdapter
import kz.batyr.movieposters.entity.DaoViewedFilms
import kz.batyr.movieposters.presentation.MainViewModel
import kz.batyr.movieposters.presentation.UserFragmentDirections


class UserFragment : Fragment() {
    private lateinit var vb: FragmentUserBinding
    private lateinit var daoFilms: DaoViewedFilms
    private val adapterForCollection = CollectionsAdapter(lifecycleScope, {collName -> deleteCollection(collName)}, {collName-> selectCollection(collName)})
    private val adapterForInterested = ListFilmsAdapter ({item -> onClickFilm(item)}, "Вам было интересно")
    private val adapterForViewed = ListFilmsAdapter ({item -> onClickFilm(item)}, "Просмотрено")
    private val viewModel: MainViewModel by activityViewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val dao = (requireActivity().application as App).db.getDaoViewedFilms()
                return MainViewModel(dao) as T
            }
        }
    }
    private var nameNewCollection: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        vb = FragmentUserBinding.inflate(inflater, container, false)
        return vb.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        daoFilms = (requireActivity().application as App).db.getDaoViewedFilms()
        vb.recyclerViewCollection.adapter = adapterForCollection
        vb.recyclerViewInterested.adapter = adapterForInterested
        vb.recyclerViewViewed.adapter = adapterForViewed
        adapterForCollection.setData(viewModel.collectionFilmsDB, viewModel.viewedFilmsDB)
        lifecycleScope.launch {
            val job = viewModel.getStandardCollections()
            job.join()
            adapterForInterested.setData(viewModel.collectionInterested.value.reversed())
            updateFilmsCount()
            adapterForViewed.setData(viewModel.collectionViewed.value.reversed())
        }



        //listeners
        vb.tvViewed.setOnClickListener {
            Log.d("TAG", "collectionValue ${viewModel.collectionInterested.value}")
        }



        vb.buttonClear.setOnClickListener {
            lifecycleScope.launch {
               viewModel.deleteInterestedFilms()
                adapterForInterested.setData(viewModel.collectionInterested.value)
                updateFilmsCount()
            }
            vb.buttonClear.visibility = View.GONE
            vb.layoutInterested.isEnabled = false
        }

        vb.layoutCreateCollection.setOnClickListener {
            openDialogWindow()
        }

        vb.layoutInterested.setOnClickListener {
            val action =
                UserFragmentDirections.actionUserFragmentToFilmFragment(getString(R.string.interestedFilms))
            findNavController().navigate(action)
        }
        vb.layoutViewed.setOnClickListener {
            val action =
                UserFragmentDirections.actionUserFragmentToFilmFragment(getString(R.string.viewed_films))
            findNavController().navigate(action)
        }

        // settings state
        when (vb.tvInterestedCount.text){
            "0"-> {
                vb.buttonClear.visibility = View.GONE
                vb.layoutInterested.isEnabled = false
            }
            else -> vb.buttonClear.visibility = View.VISIBLE
        }

    }

    //fun
    private fun updateFilmsCount () {
        vb.tvInterestedCount.text = viewModel.collectionInterested.value.size.toString()
        vb.tvViewedCount.text = viewModel.collectionViewed.value.size.toString()
    }
    private fun openDialogWindow() {
        val inputLayout = EditText(requireContext())
        inputLayout.inputType = InputType.TYPE_CLASS_TEXT
        val dialog = AlertDialog.Builder(requireContext())
            .setView(inputLayout)
            .setCancelable(false)
            .setTitle("Введите название коллекции")
            .setPositiveButton("Сохранить") { dialog, which ->
                if (inputLayout.text.toString().isNotBlank()) {
                    nameNewCollection = inputLayout.text.toString()
                    viewModel.viewModelScope.launch {
                        daoFilms.insertCollection(CollectionsFilms(collectionName = nameNewCollection!!))
                    }
                    Toast.makeText(
                        requireContext(),
                        "Вы создали коллекцию $nameNewCollection",
                        Toast.LENGTH_SHORT
                    ).show()
                    dialog.dismiss()
                } else Toast.makeText(
                    requireContext(),
                    "Напишите название коллекции",
                    Toast.LENGTH_SHORT
                ).show()
            }
            .setNeutralButton("Отмена") { dialog, which ->
                dialog.dismiss()
            }
            .create()

        dialog.setOnShowListener {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE)?.let { button ->
                button.setBackgroundColor(resources.getColor(R.color.blue, requireContext().theme))
                button.setTextColor(resources.getColor(R.color.white, requireContext().theme))
                button.gravity = Gravity.CENTER
                val layoutParamsPositive = button.layoutParams as LinearLayout.LayoutParams
                layoutParamsPositive.marginEnd = 20
                button.layoutParams = layoutParamsPositive
            }
            dialog.getButton(AlertDialog.BUTTON_NEUTRAL)?.let { button ->
                button.setBackgroundColor(resources.getColor(R.color.blue, requireContext().theme))
                button.setTextColor(resources.getColor(R.color.white, requireContext().theme))
                button.gravity = Gravity.CENTER
                val layoutParamsNegative = button.layoutParams as LinearLayout.LayoutParams
                layoutParamsNegative.marginStart = 20
                button.layoutParams = layoutParamsNegative
            }

            inputLayout.gravity = Gravity.CENTER
            inputLayout.setBackgroundResource(R.color.trans)
        }
        val title = SpannableString("Введите название коллекции")
        title.setSpan(
            TypefaceSpan(resources.getFont(R.font.graphik_regular)),
            0,
            title.length,
            Spannable.SPAN_INCLUSIVE_INCLUSIVE
        )
        dialog.setTitle(title)
        dialog.show()
    }
    private fun onClickFilm (item:Int) {
        val action = UserFragmentDirections.actionUserFragmentToFilmFullInfoFragment(item)
        findNavController().navigate(action)
    }

    private fun selectCollection(collectionName:String){
        viewModel.getSelectedCollections(collectionName)
        val action = UserFragmentDirections.actionUserFragmentToFilmFragment(
            getString(R.string.selectedcollection),
            collectionName
        )
        findNavController().navigate(action)
    }
    private fun deleteCollection(collectionName: String){
        viewModel.deleteCollectionAndAllInfo(collectionName)
    }
}