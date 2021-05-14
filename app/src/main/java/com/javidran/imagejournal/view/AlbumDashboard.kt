package com.javidran.imagejournal.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.javidran.imagejournal.R

/**
 * A simple [Fragment] subclass.
 * Use the [AlbumDashboard.newInstance] factory method to
 * create an instance of this fragment.
 */
class AlbumDashboard : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_album_dashboard, container, false)
    }
}