package kz.batyr.movieposters.presentation.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kz.batyr.movieposters.databinding.FragmentSearchBinding
import kz.batyr.movieposters.presentation.MainViewModel
import kz.batyr.movieposters.presentation.SearchFragmentDirections
import kz.batyr.movieposters.presentation.recyclerViewAdapters.FilmographyFilmsAdapter

class SearchFragment : Fragment() {
    lateinit var vb: FragmentSearchBinding
    private val viewModel: MainViewModel by activityViewModels()
    private var searchJob: Job? = null
    private val scope = CoroutineScope(Dispatchers.Default)
    private val delayOfSearch = 700L
    private val adapter = FilmographyFilmsAdapter { item -> onClick(item) }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        vb = FragmentSearchBinding.inflate(inflater, container, false)
        vb.recyclerView.adapter = adapter
        return vb.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        vb.textInputEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                searchJob?.cancel()
                if (vb.textInputEditText.text.toString() != "") {
                    searchJob = scope.launch {
                        delay(delayOfSearch)
                        val genre = if (viewModel.searchGenre != "Не выбрано")
                            arrayOf(viewModel.genresID.value.find { it.genre == viewModel.searchGenre }!!.id)
                        else null
                        val country = if (viewModel.searchCountry != "Не выбрано")
                            arrayOf(viewModel.countriesID.value.find { it.country == viewModel.searchCountry }!!.id)
                        else null
                        val yearFrom = if (viewModel.searchYearFrom != "Не выбрано")
                            viewModel.searchYearFrom.toInt()
                        else null
                        val yearTo = if (viewModel.searchYearTo != "Не выбрано")
                            viewModel.searchYearTo.toInt()
                        else null
                        with(viewModel) {
                            val keyWord = vb.textInputEditText.text.toString()
                            lifecycleScope.launch {
                                searchFilm(
                                    country,
                                    genre,
                                    searchSortFilm,
                                    searchTypeFilm,
                                    searchSliderMin,
                                    searchSliderMax,
                                    1,
                                    yearFrom = yearFrom ?: 1900,
                                    yearTo = yearTo ?: 3000,
                                    keyWord
                                )
                                Log.d("TAG", "${viewModel.searchFilms.value}")
                                adapter.setData(viewModel.searchFilms.value)
                                Log.d("TAG", "${viewModel.searchFilms.value}")
                            }
                        }
                    }
                }
            }
        })







            vb.textInputLayout.setEndIconOnClickListener {
                findNavController().navigate(SearchFragmentDirections.actionSearchFragmentToSearchSettingsFragment())
            }
        }
                private fun onClick(id: Int) {
                    val action =
                        SearchFragmentDirections.actionSearchFragmentToFilmFullInfoFragment(id)
                    findNavController().navigate(action)
                }
    }