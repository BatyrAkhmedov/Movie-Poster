package kz.batyr.movieposters.presentation.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import kz.batyr.movieposters.databinding.FragmentSearchSettingsCountryBinding
import kz.batyr.movieposters.presentation.MainViewModel
import kz.batyr.movieposters.presentation.recyclerViewAdapters.CountryAndGenresAdapter


class SearchSettingsCountryFragment : Fragment() {
    lateinit var vb: FragmentSearchSettingsCountryBinding
    private val viewModel: MainViewModel by activityViewModels()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        vb = FragmentSearchSettingsCountryBinding.inflate(inflater, container, false)
        return vb.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = CountryAndGenresAdapter ({item -> onCountryClick(item)}, viewModel.searchCountry)
        vb.recyclerView.adapter = adapter
        val listCountryId = viewModel.countriesID.value
        val listCountry = listCountryId.map { it.country }.filter { it != "" }
        adapter.setData(listCountry)
        vb.textInputEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val searchText = vb.textInputEditText.text.toString().uppercase()
                adapter.setData(listCountry.filter { it.uppercase().contains("$searchText") })
            }})

        vb.buttonReturn.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun onCountryClick (item:String) {
        viewModel.searchCountry = item
            requireActivity().onBackPressedDispatcher.onBackPressed()
    }


}