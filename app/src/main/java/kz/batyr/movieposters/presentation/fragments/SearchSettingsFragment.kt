package kz.batyr.movieposters.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.button.MaterialButton
import kz.batyr.movieposters.R
import kz.batyr.movieposters.databinding.FragmentSearchSettingsBinding
import kz.batyr.movieposters.presentation.MainViewModel
import kz.batyr.movieposters.presentation.SearchSettingsFragmentDirections


class SearchSettingsFragment : Fragment() {
    lateinit var vb:FragmentSearchSettingsBinding
    private val viewModel: MainViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        vb= FragmentSearchSettingsBinding.inflate(inflater, container, false)
        return vb.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        vb.country.text = viewModel.searchCountry
        vb.genre.text = viewModel.searchGenre
        vb.rangeSlider.values = listOf(viewModel.searchSliderMin.toFloat(), viewModel.searchSliderMax.toFloat())
        valueForRating(viewModel.searchSliderMin, viewModel.searchSliderMax)
        valuesForYears()
        checkToggleFilmType()
        checkToggleFilmSort()

        vb.rangeSlider.addOnChangeListener { rangeSlider, _, _ ->
            val minValue = rangeSlider.values[0].toInt()
            val maxValue = rangeSlider.values[1].toInt()
            viewModel.searchSliderMin = minValue
            viewModel.searchSliderMax = maxValue
            valueForRating(minValue, maxValue)
        }
        vb.toggleGroup.addOnButtonCheckedListener { _, checkedId, isChecked ->
            val checkedButton: MaterialButton = vb.root.findViewById(checkedId)
            if (isChecked) {
                when (checkedButton.text.toString()) {
                    "Все" -> viewModel.searchTypeFilm = "ALL"
                    "Фильмы" -> viewModel.searchTypeFilm = "FILM"
                    "Сериалы" -> viewModel.searchTypeFilm = "TV_SERIES"
                }
            }
        }
        vb.toggleGroupSort.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (isChecked) {
                val checkedButton: MaterialButton = vb.root.findViewById(checkedId)
                when (checkedButton.text.toString()) {
                    "Дата" -> viewModel.searchSortFilm = "YEAR"
                    "Популярность" -> viewModel.searchSortFilm = "NUM_VOTE"
                    "Рейтинг" -> viewModel.searchSortFilm = "RATING"
                }
            }
        }

        vb.country.setOnClickListener {
            val action =
                SearchSettingsFragmentDirections.actionSearchSettingsFragmentToSearchSettingsCountryFragment()
            findNavController().navigate(action)
        }
        vb.genre.setOnClickListener {
            val action =
                SearchSettingsFragmentDirections.actionSearchSettingsFragmentToSearchSettingsGenreFragment()
            findNavController().navigate(action)
        }
        vb.year.setOnClickListener {
            val action =
                SearchSettingsFragmentDirections.actionSearchSettingsFragmentToSearchSettingsPeriodFragment()
            findNavController().navigate(action)
        }

        vb.buttonReturn.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun valueForRating (minValue:Int, maxValue:Int){
        if (minValue == 1 && maxValue == 10) vb.rate.text = "любой"
        else if (minValue == maxValue) vb.rate.text = "только $maxValue"
        else vb.rate.text = "с $minValue до $maxValue"
    }
    private fun valuesForYears () {
        if (viewModel.searchYearFrom != "Не выбрано" && viewModel.searchYearTo != "Не выбрано")
            vb.year.text = "с ${viewModel.searchYearFrom} до ${viewModel.searchYearTo}"
        else if (viewModel.searchYearFrom != "Не выбрано")
            vb.year.text = "с ${viewModel.searchYearFrom}"
        else if (viewModel.searchYearTo != "Не выбрано")
            vb.year.text = "до ${viewModel.searchYearTo}"
    }

    private fun checkToggleFilmType (){
        val button1:MaterialButton = vb.root.findViewById(R.id.button_toggle_1)
        val button2:MaterialButton = vb.root.findViewById(R.id.button_toggle_2)
        val button3:MaterialButton = vb.root.findViewById(R.id.button_toggle_3)
        when (viewModel.searchTypeFilm) {
            "ALL" -> vb.toggleGroup.check(button1.id)
            "FILM" -> vb.toggleGroup.check(button2.id)
            "TV_SERIES" -> vb.toggleGroup.check(button3.id)
        }
    }
    private fun checkToggleFilmSort (){
        val button1:MaterialButton = vb.root.findViewById(R.id.button_toggle_sort_1)
        val button2:MaterialButton = vb.root.findViewById(R.id.button_toggle_sort_2)
        val button3:MaterialButton = vb.root.findViewById(R.id.button_toggle_sort_3)
        when (viewModel.searchSortFilm) {
            "YEAR" -> vb.toggleGroupSort.check(button1.id)
            "NUM_VOTE" -> vb.toggleGroupSort.check(button2.id)
            "RATING" -> vb.toggleGroupSort.check(button3.id)
        }
    }

}