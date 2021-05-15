package com.javidran.imagejournal.view.dashboard

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.javidran.imagejournal.R
import com.javidran.imagejournal.databinding.FragmentAlbumDashboardBinding
import com.javidran.imagejournal.model.Album

/**
 * A simple [Fragment] subclass.
 * Use the [AlbumDashboard.newInstance] factory method to
 * create an instance of this fragment.
 */
class AlbumDashboard : Fragment() {

    private var _binding: FragmentAlbumDashboardBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val albumViewModel by viewModels<AlbumViewModel> {
        AlbumViewModelFactory(requireContext())
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentAlbumDashboardBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
//        binding.recyclerView.adapter = albumViewModel.albumsLiveData.value?.let { CustomAdapter(it) }

        albumViewModel.albumsLiveData.observe(viewLifecycleOwner, {
            it?.let {
                binding.recyclerView.adapter = AlbumListAdapter(it)
            }
        })

        binding.addAlbumButton.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_albumDashboard_to_addAlbum)
        }

        return view
    }



    private fun adapterOnClick(album: Album) {
        // TODO
    }

}