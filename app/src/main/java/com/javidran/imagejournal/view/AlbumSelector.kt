package com.javidran.imagejournal.view

import android.graphics.*
import android.graphics.Bitmap.createBitmap
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.javidran.imagejournal.databinding.FragmentAlbumSelectorBinding


/**
 * A simple [Fragment] subclass.
 * Use the [AlbumSelector.newInstance] factory method to
 * create an instance of this fragment.
 */
class AlbumSelector : Fragment() {
    var dwidth = 0
    var dheight = 0
    private var _binding: FragmentAlbumSelectorBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun combineFrameAndImage(imagePath: String, counter: Bitmap): Bitmap? {
        val options = BitmapFactory.Options()
        options.inPreferredConfig = Bitmap.Config.ARGB_8888
        val image = BitmapFactory.decodeFile(imagePath, options)

        val finalImage = createBitmap(image.getWidth(), image.getHeight(), image.getConfig())
        val finalCanvas = Canvas(finalImage)
        finalCanvas.drawBitmap(image, 0F, 0F, null)
        val posFromLeft = 0F
        val posFromTop = 0F
        finalCanvas.drawBitmap(counter, posFromLeft, posFromTop, null)
        return finalImage
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAlbumSelectorBinding.inflate(inflater, container, false)
        val view = binding.root
        // get device dimensions
        val displayMetrics = DisplayMetrics()
        activity?.windowManager?.defaultDisplay?.getMetrics(displayMetrics)
        dwidth = displayMetrics.widthPixels
        dheight = displayMetrics.heightPixels

        var imagePath : String = arguments?.getString("imagePath")!!
        var emptyBM = Bitmap.createBitmap(0, 0, Bitmap.Config.ARGB_8888);
        combineFrameAndImage(imagePath,emptyBM)


        return view
    }


}