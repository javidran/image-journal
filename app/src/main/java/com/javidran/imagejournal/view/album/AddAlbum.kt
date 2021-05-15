package com.javidran.imagejournal.view.album

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.javidran.imagejournal.databinding.FragmentAddAlbumBinding
import com.javidran.imagejournal.view.dashboard.AlbumViewModel
import com.javidran.imagejournal.view.dashboard.AlbumViewModelFactory

/**
 * A simple [Fragment] subclass.
 * Use the [AddAlbum.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddAlbum : Fragment() {

    private var _binding: FragmentAddAlbumBinding? = null
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
        _binding = FragmentAddAlbumBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.cancelButton.setOnClickListener {
            Navigation.findNavController(view).popBackStack()
        }

        binding.saveButton.setOnClickListener {
            createAlbum()
        }

        return view
    }


    fun createAlbum() {

        albumViewModel.insertAlbum(binding.albumTitleInput.text.toString(), binding.albumEmojiInput.text.toString())
        view?.let { Navigation.findNavController(it).popBackStack() }
    }
}