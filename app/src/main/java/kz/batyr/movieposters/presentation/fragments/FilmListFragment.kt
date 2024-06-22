package kz.batyr.movieposters.presentation.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kz.batyr.movieposters.R
import kz.batyr.movieposters.data.films_data.FilmListPremiers
import kz.batyr.movieposters.databinding.FragmentFilmBinding
import kz.batyr.movieposters.presentation.FilmListFragmentArgs
import kz.batyr.movieposters.presentation.FilmListFragmentDirections
import kz.batyr.movieposters.presentation.MainViewModel
import kz.batyr.movieposters.presentation.recyclerViewAdapters.FullListFilmsAdapter
import kz.batyr.movieposters.presentation.recyclerViewAdapters.FullListFilmsPagedAdapter


class FilmListFragment : Fragment() {
    private val args: FilmListFragmentArgs by navArgs()
    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var vb:FragmentFilmBinding
    private lateinit var listFilms:MutableList<FilmListPremiers.Item>
    private val adapter = FullListFilmsAdapter {item-> onFilmClick(item)}
    private val pagedAdapter = FullListFilmsPagedAdapter{item-> onFilmClick(item)}



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        vb = FragmentFilmBinding.inflate(inflater, container, false)
        return vb.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        vb.buttonReturn.setOnClickListener{
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }


        vb.recyclerView.adapter = pagedAdapter
        when (args.filmCategory) {
            getString(R.string.Premiers) -> {
                vb.recyclerView.adapter = adapter
                adapter.setData(viewModel.getMoviesPremiers.value)
                vb.title.text = args.filmCategory
            }
            getString(R.string.Popular) -> {
                viewModel.pagedPopular.onEach {
                    pagedAdapter.submitData(it)
                }.launchIn(viewLifecycleOwner.lifecycleScope)
                vb.title.text = args.filmCategory
            }
            getString(R.string.Serials) -> {
                viewModel.pagedSerials.onEach {
                    pagedAdapter.submitData(it)
                }.launchIn(viewLifecycleOwner.lifecycleScope)
                vb.title.text = args.filmCategory
            }
            getString(R.string.Top_250) -> {
                viewModel.pagedTop250.onEach {
                    pagedAdapter.submitData(it)
                }.launchIn(viewLifecycleOwner.lifecycleScope)
                vb.title.text = args.filmCategory
            }
            getString(R.string.random1) -> {
                viewModel.loadDataToPagedRandom1(
                    viewModel.randomGenreAndCountries.values.first(),
                    viewModel.randomGenreAndCountries.keys.first()
                )
                viewModel.pagedRandom1.onEach {
                    pagedAdapter.submitData(it)
                }.launchIn(viewLifecycleOwner.lifecycleScope)
                vb.title.text = args.randomFilmName.replaceFirstChar(Char::uppercase)
            }
            getString(R.string.random2) -> {
                viewModel.loadDataToPagedRandom2(
                    viewModel.randomGenreAndCountries.values.last(),
                    viewModel.randomGenreAndCountries.keys.last()
                )
                viewModel.pagedRandom2.onEach {
                    pagedAdapter.submitData(it)
                }.launchIn(viewLifecycleOwner.lifecycleScope)
                vb.title.text = args.randomFilmName.replaceFirstChar(Char::uppercase)
            }
            getString(R.string.best_films_from_actor_page) -> {
                listFilms = viewModel.tenBestFilms.toMutableList()
                vb.recyclerView.adapter = adapter
                lifecycleScope.launch {
                    viewModel.bestFilmsFromActorPage.drop(10).take(10).onEach {
                        val jobLoadFilm = viewModel.loadFilmByIdForActorPage(it.filmId)
                        jobLoadFilm.join()
                        listFilms.add(viewModel.getFilmByIdForActorPage)
                        Log.d("TAG", "${viewModel.getFilmByIdForActorPage}")
                    }

                    adapter.setData(listFilms.toList())
                    Log.d("TAG", "args ${args.filmCategory}${args.randomFilmName}")
                    vb.title.text = args.randomFilmName + " лучшее"
                }
            }
            getString(R.string.similars) -> {
                vb.recyclerView.adapter = adapter
                viewModel.similarsFilm.onEach {
                    adapter.setData(it)
                }.launchIn(lifecycleScope)
                vb.title.text = getString(R.string.similar_rus)
            }
            getString(R.string.interestedFilms) -> {
                vb.recyclerView.adapter = adapter
                viewModel.collectionInterested.onEach {
                    adapter.setData(it)
                }.launchIn(lifecycleScope)
                vb.title.text = getString(R.string.interestedFilms)
            }
            getString(R.string.selectedcollection) -> {
                vb.recyclerView.adapter = adapter
                adapter.setData(viewModel.selectedCollection.value)
                vb.title.text = args.randomFilmName
            }
            getString(R.string.viewed_films) -> {
                vb.recyclerView.adapter = adapter
                viewModel.collectionViewed.onEach {
                    adapter.setData(it)
                }.launchIn(lifecycleScope)
                vb.title.text = getString(R.string.viewed_films)
            }
        }

    }
    private fun onFilmClick (filmId:Int){
            val action = FilmListFragmentDirections.actionFilmFragmentToFilmFullInfoFragment(filmId)
            findNavController().navigate(action)
    }

}