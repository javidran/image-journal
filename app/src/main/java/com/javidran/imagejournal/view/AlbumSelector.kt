package com.javidran.imagejournal.view

import android.graphics.*
import android.graphics.Bitmap.createBitmap
import android.media.MediaScannerConnection
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.javidran.imagejournal.R
import com.javidran.imagejournal.databinding.FragmentAlbumSelectorBinding
import java.io.File
import com.javidran.imagejournal.model.Album
import com.javidran.imagejournal.view.dashboard.AlbumListAdapter
import com.javidran.imagejournal.view.dashboard.AlbumViewModel
import com.javidran.imagejournal.view.dashboard.AlbumViewModelFactory
import com.javidran.imagejournal.view.selector.AlbumChooserListAdapter


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

    private val albumViewModel by viewModels<AlbumViewModel> {
        AlbumViewModelFactory(requireContext())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun combineFrameAndImage(imagePath: String): Bitmap? {
        val options = BitmapFactory.Options()
        options.inPreferredConfig = Bitmap.Config.ARGB_8888
        val image = BitmapFactory.decodeFile(imagePath, options)
        val imageWidth = image.getWidth()//!!!Ojillo
        val imageHeight = image.getHeight()//!!!Ojillo
        //Creación footer
        val footerBitmap = designFooter(imageWidth,imageHeight)


        var finalImage = createBitmap(image.getWidth(), image.getHeight(), image.getConfig())
        val finalCanvas = Canvas(finalImage)
        finalCanvas.drawBitmap(image, 0F, 0F, null)

        val posFromLeft = 0F
        val posFromTop = (5*imageHeight)/6F
        finalCanvas.drawBitmap(footerBitmap, posFromLeft, posFromTop, null)

        //Bitmap rotation: https://stackoverflow.com/questions/3647993/android-bitmaps-loaded-from-gallery-are-rotated-in-imageview
        val matrix = Matrix()
        matrix.postRotate(270F)
        finalImage = createBitmap( finalImage,0,0,finalImage.getWidth(), finalImage.getHeight(), matrix,true)

        return finalImage
    }

    fun designFooter(imageWidth: Int,imageHeight: Int): Bitmap {
        //Paint creation
        val painter = Paint()
        painter.setColor(Color.RED) //Color.parseColor("#BDBDBD")
        //painter.setAntiAlias(true);
        painter.setStyle(Paint.Style.FILL)
        /*
        1.Create a bitmap of the correct size using Bitmap.createBitmap()
        2.Create a canvas instance pointing that this bitmap using Canvas(Bitmap) constructor
        3.Draw to the canvas
        4.Use the bitmap
        */
        val bitmap = createBitmap(imageWidth, (imageHeight)/6, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        //canvas.drawRect(0F, 0F, (imageHeight)/6F, (imageHeight)/6F, painter)    // fill
        canvas.drawPaint(painter)
        return bitmap

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAlbumSelectorBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.albumOptions.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        albumViewModel.albumsLiveData.observe(viewLifecycleOwner, {
            it?.let {
                binding.albumOptions.adapter = AlbumChooserListAdapter(it) { album -> adapterOnClick(album) }
            }
        })

        // get device dimensions
        val displayMetrics = DisplayMetrics()
        activity?.windowManager?.defaultDisplay?.getMetrics(displayMetrics)
        dwidth = displayMetrics.widthPixels
        dheight = displayMetrics.heightPixels

        //Image loading
        var imagePath : String = arguments?.getString("imagePath")!!
        var bitmapFin = combineFrameAndImage(imagePath)


        binding.imageWithCounter.setImageBitmap(bitmapFin)

        binding.btnRetake.setOnClickListener { retakePhoto(imagePath) }

        return view
    }

    private fun adapterOnClick(album: Album) {
        val bundle = bundleOf("album_title" to album.title)
        view?.let { Navigation.findNavController(it).navigate(R.id.action_albumDashboard_to_viewAlbum, bundle) }
    }
    private fun retakePhoto(imagePath: String) {
        //deleting rejected image before going back to camera fragment
        val rejectedImage = File(imagePath)
        rejectedImage.delete()
        MediaScannerConnection.scanFile(context, arrayOf(rejectedImage.toString()),
            arrayOf(rejectedImage.getName()), null)

        //navigating back to camera fragment
        view?.let { Navigation.findNavController(it).popBackStack() }
    }


}