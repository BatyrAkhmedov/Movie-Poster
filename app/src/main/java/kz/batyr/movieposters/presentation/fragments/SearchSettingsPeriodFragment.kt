package kz.batyr.movieposters.presentation.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import kz.batyr.movieposters.data.SearchingYear
import kz.batyr.movieposters.databinding.FragmentSearchSettingsPeriodBinding
import kz.batyr.movieposters.presentation.MainViewModel
import kz.batyr.movieposters.presentation.recyclerViewAdapters.YearFromToAdapter
import java.util.Calendar


class SearchSettingsPeriodFragment : Fragment() {
    lateinit var vb: FragmentSearchSettingsPeriodBinding
    private val viewModel: MainViewModel by activityViewModels()
    private val yearNow = Calendar.getInstance().get(Calendar.YEAR)
    private val listYears = (1998..yearNow).toList().map { it.toString() }
    private val listYearsTo = listYears.map { SearchingYear(it, false) }
    private val listYearsFrom = listYears.map { SearchingYear(it, false) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        vb = FragmentSearchSettingsPeriodBinding.inflate(inflater, container, false)
        return vb.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapterFrom =
            YearFromToAdapter({ item-> onClickFrom(item)}, viewModel.searchYearFrom, requireContext())
        val adapterTo =
            YearFromToAdapter({ item-> onClickTo(item)}, viewModel.searchYearTo, requireContext())
        val layoutManagerFrom = GridLayoutManager(requireContext(), 4, GridLayoutManager.HORIZONTAL, false)
        val layoutManagerTo = GridLayoutManager(requireContext(), 4, GridLayoutManager.HORIZONTAL, false)
        vb.recyclerViewFrom.adapter = adapterFrom
        vb.recyclerViewFrom.layoutManager = layoutManagerFrom
        vb.recyclerViewTo.adapter = adapterTo
        vb.recyclerViewTo.layoutManager = layoutManagerTo

        adapterFrom.setData(listYearsFrom)
        adapterTo.setData(listYearsTo)


        vb.buttonStartFrom.setOnClickListener {
            layoutManagerFrom.scrollToPosition(layoutManagerFrom.findFirstVisibleItemPosition()-1)
        }
        vb.buttonEndFrom.setOnClickListener {
            layoutManagerFrom.scrollToPosition(layoutManagerFrom.findLastVisibleItemPosition()+1)
        }
        vb.buttonStartTo.setOnClickListener {
            layoutManagerTo.scrollToPosition(layoutManagerFrom.findFirstVisibleItemPosition()-1)
        }
        vb.buttonEndTo.setOnClickListener {
            layoutManagerTo.scrollToPosition(layoutManagerFrom.findLastVisibleItemPosition()+1)
        }



        vb.buttonChoose.setOnClickListener {
            with(viewModel) {
                if (pressedYearFrom != null) searchYearFrom = pressedYearFrom!!
                if (pressedYearTo != null) searchYearTo = pressedYearTo!!
            }
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        vb.buttonReturn.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }


    private fun onClickFrom (item:String) {
        viewModel.pressedYearFrom = item
        Log.d("TAG", "onClick: $item")
    }
    private fun onClickTo (item:String) {
        viewModel.pressedYearTo = item
        Log.d("TAG", "onClick: $item")
//        requireActivity().onBackPressedDispatcher.onBackPressed()
    }

}