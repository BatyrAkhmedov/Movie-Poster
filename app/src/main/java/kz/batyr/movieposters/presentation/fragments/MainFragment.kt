package kz.batyr.movieposters.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kz.batyr.movieposters.R
import kz.batyr.movieposters.databinding.FragmentMainBinding
import kz.batyr.movieposters.domain.App
import kz.batyr.movieposters.presentation.MainFragmentDirections
import kz.batyr.movieposters.presentation.MainViewModel
import kz.batyr.movieposters.presentation.recyclerViewAdapters.ListFilmsAdapter
import kotlin.random.Random
import kotlin.random.nextInt


class MainFragment : Fragment() {
    private lateinit var vb: FragmentMainBinding

    private var adapterForPremiers = ListFilmsAdapter ( {item -> onFilmClick(item)}, "Премьеры" )
    private var adapterForPopular = ListFilmsAdapter( {item -> onFilmClick(item)}, "Премьеры")
    private var adapterForTop250 = ListFilmsAdapter( {item -> onFilmClick(item)}, "Премьеры")
    private var adapterForSerials = ListFilmsAdapter( {item -> onFilmClick(item)}, "Премьеры")
    private var adapterForRandomGenre1 = ListFilmsAdapter( {item -> onFilmClick(item)}, "Премьеры")
    private var adapterForRandomGenre2 = ListFilmsAdapter( {item -> onFilmClick(item)}, "Премьеры")
    private val viewModel: MainViewModel by activityViewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val dao = (requireActivity().application as App).db.getDaoViewedFilms()
                return MainViewModel (dao) as T
            }
        }
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        vb = FragmentMainBinding.inflate(inflater, container, false)

        return vb.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vb.recyclerViewPremiers.adapter = adapterForPremiers
        vb.recyclerViewPopular.adapter = adapterForPopular
        vb.recyclerViewTop250.adapter = adapterForTop250
        vb.recyclerViewSerials.adapter = adapterForSerials
        vb.recyclerViewRandom1.adapter = adapterForRandomGenre1
        vb.recyclerViewRandom2.adapter = adapterForRandomGenre2

        vb.recyclerPremiersButton.setOnClickListener {
            val action =
                MainFragmentDirections.actionMainFragmentToFilmFragment(getString(R.string.Premiers))
            findNavController().navigate(action)
        }
        vb.recyclerPopularButton.setOnClickListener {
            val action =
                MainFragmentDirections.actionMainFragmentToFilmFragment(getString(R.string.Popular))
            findNavController().navigate(action)
        }
        vb.recyclerTop250Button.setOnClickListener {
            val action =
                MainFragmentDirections.actionMainFragmentToFilmFragment(getString(R.string.Top_250))
            findNavController().navigate(action)
        }
        vb.recyclerSerialsButton.setOnClickListener {
            val action =
                MainFragmentDirections.actionMainFragmentToFilmFragment(getString(R.string.Serials))
            findNavController().navigate(action)
        }
        vb.recyclerRandom1Button.setOnClickListener {
            val action = MainFragmentDirections.actionMainFragmentToFilmFragment(
                getString(R.string.random1),
                "${viewModel.genresID.value[RANDOM_INT_GENRE_1].genre} ${viewModel.countriesID.value[RANDOM_INT_COUNTRY_1].country} "
            )
            findNavController().navigate(action)
        }
        vb.recyclerRandom2Button.setOnClickListener {
            val action = MainFragmentDirections.actionMainFragmentToFilmFragment(
                getString(R.string.random2),
                "${viewModel.countriesID.value[RANDOM_INT_COUNTRY_2].country} ${viewModel.genresID.value[RANDOM_INT_GENRE_2]}"
            )
            findNavController().navigate(action)
        }


        lifecycleScope.launch  {
            if (viewModel.genresID.value.isEmpty()) viewModel.loadGenresAndCountriesId()
            viewModel.randomGenreAndCountries = mapOf(
                viewModel.genresID.value[RANDOM_INT_GENRE_1].id to viewModel.countriesID.value[RANDOM_INT_COUNTRY_1].id,
                viewModel.genresID.value[RANDOM_INT_GENRE_2].id to viewModel.countriesID.value[RANDOM_INT_COUNTRY_2].id
            )

            vb.recyclerRandom1TextView.text = getDataFromId(
                viewModel.randomGenreAndCountries.keys.first(),
                viewModel.randomGenreAndCountries.values.first()
            )
            vb.recyclerRandom2TextView.text = getDataFromId(
                viewModel.randomGenreAndCountries.keys.last(),
                viewModel.randomGenreAndCountries.values.last()
            )
            with(viewModel){
                loadRandomFilms(
                    randomGenreAndCountries.keys.first(), randomGenreAndCountries.values.first(),
                    randomGenreAndCountries.keys.last(), randomGenreAndCountries.values.last()
                )
            }

            viewModel.getRandom1.onEach {
                adapterForRandomGenre1.setData(it)
            }.launchIn(viewLifecycleOwner.lifecycleScope)

            viewModel.getRandom2.onEach {
                adapterForRandomGenre2.setData(it)
            }.launchIn(viewLifecycleOwner.lifecycleScope)
        }

        viewModel.getSerials.onEach {
            adapterForSerials.setData(it)
        }.launchIn(viewLifecycleOwner.lifecycleScope)
        viewModel.getTop250.onEach {
            adapterForTop250.setData(it)
        }.launchIn(viewLifecycleOwner.lifecycleScope)
        viewModel.getMoviesPremiers.onEach {
            adapterForPremiers.setData(it)
        }.launchIn(viewLifecycleOwner.lifecycleScope)
        viewModel.getTopPopular.onEach {
            adapterForPopular.setData(it)
        }.launchIn(viewLifecycleOwner.lifecycleScope)




    }

    private fun onFilmClick (filmId:Int){
        val action = MainFragmentDirections.actionMainFragmentToFilmFullInfoFragment(filmId)
        findNavController().navigate(action)
    }

    private fun getDataFromId(genreId: Int, countryId: Int): String {
        val genre =
            viewModel.genresID.value.find { it.id == genreId }!!.genre.replaceFirstChar(Char::titlecase)
        val country = viewModel.countriesID.value.find { it.id == countryId }!!.country
        return "$genre $country"
    }

    companion object {
        val RANDOM_INT_GENRE_1 = Random.nextInt(0..14)
        val RANDOM_INT_COUNTRY_1 = Random.nextInt(0..10)
        val RANDOM_INT_GENRE_2 = Random.nextInt(0..14)
        val RANDOM_INT_COUNTRY_2 = Random.nextInt(0..10)
    }

}