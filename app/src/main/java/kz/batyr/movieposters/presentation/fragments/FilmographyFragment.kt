package kz.batyr.movieposters.presentation.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.chip.Chip
import kotlinx.coroutines.launch
import kz.batyr.movieposters.R
import kz.batyr.movieposters.data.films_data.FilmListPremiers
import kz.batyr.movieposters.databinding.FragmentFilmographyBinding
import kz.batyr.movieposters.presentation.FilmographyFragmentDirections
import kz.batyr.movieposters.presentation.MainViewModel
import kz.batyr.movieposters.presentation.viewPagerAdapter.FilmographyAdapter


class FilmographyFragment : Fragment() {
    lateinit var vb:FragmentFilmographyBinding
    private val viewModel: MainViewModel by activityViewModels()
    private val mutableListOfProfessionKey = mutableListOf<String>()
    private val viewPagerAdapter = FilmographyAdapter {film->onFilmClick(film)}
    lateinit var map: Map<String, List<FilmListPremiers.Item>>
//    private val _progressBarState = MutableStateFlow<State>(State.Loading)
//    val progressBarState = _progressBarState.asStateFlow()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        vb = FragmentFilmographyBinding.inflate(inflater, container, false)
        vb.vm = viewModel
        vb.lifecycleOwner = viewLifecycleOwner
        return vb.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.progressBarEnable()
        Log.d("TAG", "state ${viewModel.progressBarState.value.isLoading}")
        Log.d("TAG", "visibility ${vb.progressBar.visibility}")
        vb.viewPager.adapter = viewPagerAdapter

        lifecycleScope.launch {
            viewModel.staffInfo.films.distinctBy { it.professionKey}.forEach {
                mutableListOfProfessionKey.add(it.professionKey)
            }
            val filteredListByRating = viewModel.staffInfo.films.sortedByDescending {it.rating?.toDouble()}

            val listOfListFilms:MutableList<List<FilmListPremiers.Item>> = mutableListOf()
            mutableListOfProfessionKey.onEach { profession ->
                val filter = filteredListByRating.filter { it.professionKey == profession }.take(10)
                val list:MutableList<FilmListPremiers.Item> = mutableListOf()
                filter.onEach {
                    val job = viewModel.loadFilmByIdForActorPage(it.filmId)
                    job.join()
                    list.add(viewModel.getFilmByIdForActorPage)
                }
                listOfListFilms.add(list.toList())
            }
            map = mutableListOfProfessionKey.zip(listOfListFilms).toMap()

            Log.d("TAG", "map: ${map!!.keys}, ${map!!.values}")
            viewPagerAdapter.setData(map!!)

            for (item in mutableListOfProfessionKey) {
                val chip = Chip(this@FilmographyFragment.requireContext())
                chip.text = item
                chip.isClickable = true
                chip.setOnClickListener {
                    for (i in 0 until vb.chipGroup.childCount){
                        val child = vb.chipGroup.getChildAt(i)
                        Log.d("TAG", "Child $child, ${child.rootView}, ${child.context} ")
                        if (child is Chip)  {
                            child.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
                            child.setChipBackgroundColorResource(R.color.white)
                        }
                    }
                    (it as Chip).setChipBackgroundColorResource(R.color.blue)
                    it.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                    vb.viewPager.currentItem = vb.chipGroup.indexOfChild(it)
                }
                vb.chipGroup.addView(chip)
            }
            viewModel.progressBarDisable()
            Log.d("TAG", "state ${viewModel.progressBarState.value.isLoading}")
            Log.d("TAG", "visibility ${vb.progressBar.visibility}")

            val firstChip = vb.chipGroup.getChildAt(0) as Chip
            firstChip.setChipBackgroundColorResource(R.color.blue)
            firstChip.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
            vb.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
                override fun onPageSelected(position: Int) {
                    vb.chipGroup.check(position)
                }
            })

        }



        vb.buttonReturn.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }



    }

    private fun onFilmClick(filmId:Int){
        val action =
            FilmographyFragmentDirections.actionFilmographyFragmentToFilmFullInfoFragment(filmId)
        findNavController().navigate(action)
    }

}