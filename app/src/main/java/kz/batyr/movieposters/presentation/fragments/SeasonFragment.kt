package kz.batyr.movieposters.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.google.android.material.tabs.TabLayoutMediator
import kz.batyr.movieposters.databinding.FragmentSeasonBinding
import kz.batyr.movieposters.presentation.MainViewModel
import kz.batyr.movieposters.presentation.SeasonFragmentArgs
import kz.batyr.movieposters.presentation.viewPagerAdapter.SeasonAdapter

class SeasonFragment : Fragment() {
    private val viewModel: MainViewModel by activityViewModels()
    private val args: SeasonFragmentArgs by navArgs()
    private lateinit var vb: FragmentSeasonBinding
    private val adapter = SeasonAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        vb = FragmentSeasonBinding.inflate(layoutInflater, container, false)
        return vb.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vb.viewPager.adapter = adapter

        vb.textFilmName.text = args.filmName

        adapter.setData(viewModel.seriesInfo.value)

        TabLayoutMediator(vb.tabLayout, vb.viewPager){ tab, position ->
            tab.text = "${position+1}"

        }.attach()


    }

}