package kz.batyr.movieposters.presentation.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kz.batyr.movieposters.R
import kz.batyr.movieposters.databinding.FragmentFullscreenBinding
import kz.batyr.movieposters.presentation.FullscreenFragmentArgs
import kz.batyr.movieposters.presentation.MainViewModel
import kz.batyr.movieposters.presentation.recyclerViewAdapters.FullscreenPagingAdapter


class FullscreenFragment : Fragment() {
    private val args: FullscreenFragmentArgs by navArgs()
    private val viewModel: MainViewModel by activityViewModels()
    lateinit var vb: FragmentFullscreenBinding
    private val pagingAdapter = FullscreenPagingAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        vb = FragmentFullscreenBinding.inflate(inflater, container, false)
        return vb.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vb.viewPager.adapter = pagingAdapter
        Log.d("TAG", "arg: ${args.typeGalleryFlow}")
        Log.d("TAG", "FullscreenFragment")




        when (args.typeGalleryFlow) {
            "POSTER" -> {
                viewModel.pagedGalleryPoster.onEach {
                    pagingAdapter.submitData(it)
                }.launchIn(lifecycleScope)
                Log.d("TAG", "Poster set")
            }
            "STILL" -> {
                viewModel.pagedGalleryStill.onEach {
                    pagingAdapter.submitData(it)
                }.launchIn(lifecycleScope)
                Log.d("TAG", "Still set")
            }
            "SHOOTING" -> {
                viewModel.pagedGalleryShooting.onEach {
                    pagingAdapter.submitData(it)
                }.launchIn(lifecycleScope)
                Log.d("TAG", "Shooting set")
            }
            getString(R.string.singleimagefullscreenfragment) -> {
                Glide.with(vb.singleImage.context)
                    .load(args.url)
                    .into(vb.singleImage)
            }
        }
        kotlin.runCatching {
            vb.viewPager.setCurrentItem(args.position, false)
        }

    }

}