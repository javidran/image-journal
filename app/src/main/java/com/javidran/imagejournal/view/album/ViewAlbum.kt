package com.javidran.imagejournal.view.album

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.compose.navArgument
import com.javidran.imagejournal.R
import com.javidran.imagejournal.databinding.FragmentAddAlbumBinding
import com.javidran.imagejournal.databinding.FragmentViewAlbumBinding
import com.javidran.imagejournal.model.DataSource

/**
 * A simple [Fragment] subclass.
 * Use the [ViewAlbum.newInstance] factory method to
 * create an instance of this fragment.
 */
class ViewAlbum : Fragment() {

    private var _binding: FragmentViewAlbumBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentViewAlbumBinding.inflate(inflater, container, false)
        val view = binding.root

        val albumTitle = arguments?.getString("album_title")!!
        binding.viewAlbumTitle.text = albumTitle

        val album = DataSource.getDataSource(requireContext()).getAlbumForTitle(albumTitle)



        return view
    }
}