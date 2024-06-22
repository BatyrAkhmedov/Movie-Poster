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
import com.bumptech.glide.Glide
import kotlinx.coroutines.launch
import kz.batyr.movieposters.R
import kz.batyr.movieposters.data.films_data.FilmListPremiers
import kz.batyr.movieposters.databinding.FragmentActorBinding
import kz.batyr.movieposters.presentation.ActorFragmentArgs
import kz.batyr.movieposters.presentation.ActorFragmentDirections
import kz.batyr.movieposters.presentation.MainViewModel
import kz.batyr.movieposters.presentation.recyclerViewAdapters.ListFilmsAdapter


class ActorFragment : Fragment() {
    private lateinit var vb:FragmentActorBinding
    private val args: ActorFragmentArgs by navArgs()
    private val viewModel: MainViewModel by activityViewModels()

    private val adapter = ListFilmsAdapter ({item -> onFilmClick(item)}, "BestFilms")


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        vb = FragmentActorBinding.inflate(inflater, container, false)
        return vb.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vb.recyclerView.adapter = adapter
        val listFilms = mutableListOf<FilmListPremiers.Item>()
        lifecycleScope.launch {
            val jobLoadStaff = viewModel.loadStaffInfo(args.staffId)
            jobLoadStaff.join()
            with (viewModel.staffInfo) {
                vb.nameRu.text = nameRu
                vb.nameEn.text = nameEn
                vb.profession.text = profession
                Glide.with(vb.photo.context)
                    .load(posterUrl)
                    .into(vb.photo)
            }
            val uniqueFilms = viewModel.staffInfo.films.distinctBy { it.filmId }.sortedBy { it.rating?.toDouble()}.reversed()
            viewModel.bestFilmsFromActorPage = uniqueFilms

            uniqueFilms.take(10).onEach {
                val jobLoadFilm = viewModel.loadFilmByIdForActorPage (it.filmId)
                jobLoadFilm.join()
                Log.d("TAG", "add ${viewModel.getFilmById}")
                listFilms.add(viewModel.getFilmByIdForActorPage)
            }
            viewModel.tenBestFilms = listFilms.toList()
            adapter.setData(listFilms.toList())
            vb.filmsCount.text = correctEnding(uniqueFilms.size)



        }
        vb.photo.setOnClickListener {
            val action = ActorFragmentDirections.actionActorFragmentToFullscreenFragment(
                typeGalleryFlow = getString(R.string.singleimagefullscreenfragment),
                url = viewModel.staffInfo.posterUrl
            )
            findNavController().navigate(action)
        }
        vb.buttonBest.setOnClickListener {
            val name = if (viewModel.staffInfo.nameRu!=null) viewModel.staffInfo.nameRu!!
            else viewModel.staffInfo.nameEn
            val action = ActorFragmentDirections.actionActorFragmentToFilmFragment(
                getString(R.string.best_films_from_actor_page), name
            )
            findNavController().navigate(action)
        }
        vb.buttonReturn.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
        vb.buttonFilmography.setOnClickListener {
            val action = ActorFragmentDirections.actionActorFragmentToFilmographyFragment()
            findNavController().navigate(action)
        }
    }
    private fun onFilmClick (filmId: Int){
        val action = ActorFragmentDirections.actionActorFragmentToFilmFullInfoFragment(filmId)
        findNavController().navigate(action)
    }

    private fun correctEnding(count:Int): String {
        return when (count) {
            1 -> "$count фильм"
            in 2..4 -> "$count фильма"
            else -> "$count фильмов"
        }
    }



}