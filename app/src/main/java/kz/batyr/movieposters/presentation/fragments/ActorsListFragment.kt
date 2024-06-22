package kz.batyr.movieposters.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import kz.batyr.movieposters.R
import kz.batyr.movieposters.databinding.FragmentActorsListBinding
import kz.batyr.movieposters.presentation.ActorsListFragmentArgs
import kz.batyr.movieposters.presentation.ActorsListFragmentDirections
import kz.batyr.movieposters.presentation.MainViewModel
import kz.batyr.movieposters.presentation.recyclerViewAdapters.ParticipantsAdapter

class ActorsListFragment : Fragment() {
    private lateinit var vb: FragmentActorsListBinding
    private val args: ActorsListFragmentArgs by navArgs()
    private val viewModel: MainViewModel by activityViewModels()
    private val adapter = ParticipantsAdapter{staffId -> onStaffClick(staffId)}


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        vb = FragmentActorsListBinding.inflate(inflater, container, false)
        return vb.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vb.title.text  = args.typeParticipant
        vb.recyclerView.adapter = adapter
        when (args.typeParticipant) {
            getString(R.string.listActors) -> adapter.setData(viewModel.getActorsList)
            getString(R.string.listStaff) -> adapter.setData(viewModel.getStaffList)
        }
    }

    fun onStaffClick (id:Int){
        val action = ActorsListFragmentDirections.actionActorsListFragmentToActorFragment(id)
        findNavController().navigate(action)
    }

}