package kz.batyr.movieposters.presentation.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import kz.batyr.movieposters.databinding.FragmentSearchSettingsGenreBinding
import kz.batyr.movieposters.presentation.MainViewModel
import kz.batyr.movieposters.presentation.recyclerViewAdapters.CountryAndGenresAdapter


class SearchSettingsGenreFragment : Fragment() {
    lateinit var vb: FragmentSearchSettingsGenreBinding
    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        vb = FragmentSearchSettingsGenreBinding.inflate(inflater, container, false)
        return vb.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = CountryAndGenresAdapter ({item -> onGenreClick(item)}, viewModel.searchGenre)
        vb.recyclerView.adapter = adapter
        val listGenreId = viewModel.genresID.value
        val listGenre = listGenreId.map { it.genre }.filter { it != "" }
        adapter.setData(listGenre)
        vb.textInputEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val searchText = vb.textInputEditText.text.toString().uppercase()
                adapter.setData(listGenre.filter { it.uppercase().contains("$searchText") })
            }})


        vb.buttonReturn.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun onGenreClick (item:String) {
        viewModel.searchGenre = item
        requireActivity().onBackPressedDispatcher.onBackPressed()
    }
}
