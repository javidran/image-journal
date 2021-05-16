package com.javidran.imagejournal.view

import android.graphics.*
import android.graphics.Bitmap.createBitmap
import android.media.MediaScannerConnection
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.javidran.imagejournal.R
import com.javidran.imagejournal.databinding.FragmentAlbumSelectorBinding
import java.io.File
import com.javidran.imagejournal.model.Album
import com.javidran.imagejournal.view.album.EntryViewModel
import com.javidran.imagejournal.view.album.EntryViewModelFactory
import com.javidran.imagejournal.view.dashboard.AlbumViewModel
import com.javidran.imagejournal.view.dashboard.AlbumViewModelFactory
import com.javidran.imagejournal.view.selector.AlbumChooserListAdapter
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*


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

    lateinit var choosenAlbum :Album
    lateinit var bitmapFin: Bitmap

    private val albumViewModel by viewModels<AlbumViewModel> {
        AlbumViewModelFactory(requireContext())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun combineFrameAndImage(imagePath: String): Bitmap {
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
        val posFromTop = 0F//(5*imageHeight)/6F
        finalCanvas.drawBitmap(footerBitmap, posFromLeft, posFromTop, null)

        //Bitmap rotation: https://stackoverflow.com/questions/3647993/android-bitmaps-loaded-from-gallery-are-rotated-in-imageview
        val matrix = Matrix()
        matrix.postRotate(270F)
        finalImage = createBitmap( finalImage,0,0,finalImage.getWidth(), finalImage.getHeight(), matrix,true)

        return finalImage
    }

    fun designFooter(imageWidth: Int,imageHeight: Int): Bitmap {
        //Painter
        val painter = Paint()
        painter.setColor(Color.parseColor("#BDBDBD"))//painter.setColor(Color.RED)
        painter.setStyle(Paint.Style.FILL)
        val bitmap = createBitmap((imageHeight)/6, imageWidth, Bitmap.Config.ARGB_8888)//!!!Ojito
        val canvas = Canvas(bitmap)
        canvas.drawPaint(painter)
        //Writer
        val writer = Paint()
        writer.setColor(Color.BLACK)
        writer.setStyle(Paint.Style.FILL)
        writer.setAntiAlias(true)
        writer.setTextSize(100F)
        val x = 0F
        val y = 0F
        canvas.drawText("Hi", 0F, 0F, writer)
        return bitmap

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAlbumSelectorBinding.inflate(inflater, container, false)
        val view = binding.root
        binding.btnSave.visibility = View.GONE

        binding.albumOptions.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        albumViewModel.albumsLiveData.observe(viewLifecycleOwner, {
            it?.let {
                binding.albumOptions.adapter = AlbumChooserListAdapter(it) { album -> updateChosenAlbum(album) }
            }
        })

        albumViewModel.albumsLiveData.value?.let { list ->
            if (list.isNotEmpty()) {
                updateChosenAlbum(list.first())
            }
        }
        // get device dimensions
        val displayMetrics = DisplayMetrics()
        activity?.windowManager?.defaultDisplay?.getMetrics(displayMetrics)
        dwidth = displayMetrics.widthPixels
        dheight = displayMetrics.heightPixels

        //Image loading
        var imagePath : String = arguments?.getString("imagePath")!!
        bitmapFin = combineFrameAndImage(imagePath)

        binding.btnSave.setOnClickListener {
            onSaveImage()
        }

        binding.imageWithCounter.setImageBitmap(bitmapFin)

        binding.btnRetake.setOnClickListener { retakePhoto(imagePath) }

        return view
    }

    fun onSaveImage() {
        var photoFile = File(
            getOutputDirectory(),
            SimpleDateFormat(
                Camera.FILENAME_FORMAT, Locale.US
            ).format(System.currentTimeMillis()) + ".jpg"
        )

        var out = FileOutputStream(photoFile)
        bitmapFin.compress(Bitmap.CompressFormat.PNG, 100, out)
        out.close()

        var imagePath = photoFile.absolutePath

        val entryViewModel by viewModels<EntryViewModel> {
            EntryViewModelFactory(requireContext(), choosenAlbum)
        }

        entryViewModel.insertEntry(imagePath)

        view?.let {
            Navigation.findNavController(it).navigate(R.id.action_albumSelector_to_albumDashboard)
        }
    }

    private fun getOutputDirectory(): File {
        val mediaDir = activity?.externalMediaDirs?.firstOrNull()?.let {
            File(it, resources.getString(R.string.app_name)).apply { mkdirs() }
        }
        return if (mediaDir != null && mediaDir.exists())
            mediaDir else context?.filesDir!!
    }

    fun updateChosenAlbum(album: Album) {
        binding.btnSave.visibility = View.VISIBLE
        choosenAlbum = album
        binding.albumChooserTitle.text = choosenAlbum.title

        var number = albumViewModel.getNextNumber(album)
        //TODO actualizar filtro con datos del album
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