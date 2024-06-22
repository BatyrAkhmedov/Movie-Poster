package kz.batyr.movieposters.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.launch
import kz.batyr.movieposters.databinding.FragmentGalleryBinding
import kz.batyr.movieposters.presentation.GalleryFragmentArgs
import kz.batyr.movieposters.presentation.MainViewModel
import kz.batyr.movieposters.presentation.viewPagerAdapter.TypeGalleryAdapter


class GalleryFragment : Fragment() {
    private val viewModel: MainViewModel by activityViewModels()
    private val args: GalleryFragmentArgs by navArgs()
    private lateinit var vb: FragmentGalleryBinding
    private val viewPagerAdapter = TypeGalleryAdapter(this)
    private var tabNames = listOf("Кадры", "Со Съемок", "Постеры")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        vb = FragmentGalleryBinding.inflate(inflater, container, false)
        return vb.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vb.viewPager.adapter = viewPagerAdapter
        lifecycleScope.launch {
            viewModel.loadTypeGallery(args.filmId)
            viewPagerAdapter.setData(viewModel.pagedGalleryStill, viewModel.pagedGalleryShooting, viewModel.pagedGalleryPoster)
        }
        TabLayoutMediator(vb.tabLayout, vb.viewPager)  {tab, position ->
            tab.text = tabNames[position]
        }.attach()


    }




}