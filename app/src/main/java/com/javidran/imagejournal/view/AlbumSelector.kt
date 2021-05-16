package com.javidran.imagejournal.view

import android.graphics.*
import android.graphics.Bitmap.createBitmap
import android.media.MediaScannerConnection
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.javidran.imagejournal.R
import com.javidran.imagejournal.databinding.FragmentAlbumSelectorBinding
import com.javidran.imagejournal.model.Album
import com.javidran.imagejournal.view.album.EntryViewModel
import com.javidran.imagejournal.view.album.EntryViewModelFactory
import com.javidran.imagejournal.view.dashboard.AlbumViewModel
import com.javidran.imagejournal.view.dashboard.AlbumViewModelFactory
import com.javidran.imagejournal.view.selector.AlbumChooserListAdapter
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
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

    lateinit var originalImagePath: String
    lateinit var choosenAlbum :Album
    lateinit var bitmapFin: Bitmap

    private val albumViewModel by viewModels<AlbumViewModel> {
        AlbumViewModelFactory(requireContext())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun combineFrameAndImage(imagePath: String, number: Int?): Bitmap {
        val options = BitmapFactory.Options()
        options.inPreferredConfig = Bitmap.Config.ARGB_8888
        var image = BitmapFactory.decodeFile(imagePath, options)

        //Bitmap rotation: https://stackoverflow.com/questions/3647993/android-bitmaps-loaded-from-gallery-are-rotated-in-imageview
        val matrix = Matrix()
        matrix.postRotate(270F)
        image = createBitmap( image,0,0,image.getWidth(), image.getHeight(), matrix,true)

        val imageWidth = image.getWidth()//!!!Ojillo
        val imageHeight = image.getHeight()//!!!Ojillo

        //Creación footer
        val footerBitmap = designFooter(imageWidth,imageHeight,number)


        var finalImage = createBitmap(image.getWidth(), image.getHeight(), image.getConfig())
        val finalCanvas = Canvas(finalImage)
        finalCanvas.drawBitmap(image, 0F, 0F, null)

        val posFromLeft = 0F
        val posFromTop = (5*imageHeight)/6F
        finalCanvas.drawBitmap(footerBitmap, posFromLeft, posFromTop, null)

        return finalImage
    }

    fun designFooter(imageWidth: Int,imageHeight: Int, number: Int?): Bitmap {
        //Painter

        val painter = Paint()
        painter.setColor(Color.parseColor("#B200FFFF"))//painter.setColor(Color.RED)
        painter.setStyle(Paint.Style.FILL)
        val bitmap = createBitmap(imageWidth, (imageHeight)/6, Bitmap.Config.ARGB_8888)//!!!Ojito
        val canvas = Canvas(bitmap)
        canvas.drawPaint(painter)

        //Writer
        val writerLeft = Paint()
        writerLeft.setColor(Color.BLACK)
        writerLeft.setStyle(Paint.Style.FILL)
        writerLeft.setAntiAlias(true)
        writerLeft.setTextAlign(Paint.Align.LEFT)
        writerLeft.setTextSize(3*bitmap.height.toFloat()/5F)

        val writerRight = Paint()
        writerRight.setColor(Color.BLACK)
        writerRight.setStyle(Paint.Style.FILL)
        writerRight.setAntiAlias(true)
        writerRight.setTextAlign(Paint.Align.RIGHT)
        writerRight.setTextSize(3*bitmap.height.toFloat()/5F)
        var textNumber = "#"
        if(number != null) {
            textNumber = textNumber + number.toString()
        }
        canvas.drawText(textNumber, 20F, (5*bitmap.height.toFloat())/6, writerLeft)

        val format = SimpleDateFormat("dd/MM/yy")
        val dateString = format.format(Date())

        canvas.drawText(dateString, bitmap.width.toFloat()-20F, (5*bitmap.height.toFloat())/6, writerRight)

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

        // get device dimensions
        val displayMetrics = DisplayMetrics()
        activity?.windowManager?.defaultDisplay?.getMetrics(displayMetrics)
        dwidth = displayMetrics.widthPixels
        dheight = displayMetrics.heightPixels

        //Image loading
        originalImagePath = arguments?.getString("imagePath")!!

        bitmapFin = combineFrameAndImage(originalImagePath, null)
        binding.imageWithCounter.setImageBitmap(bitmapFin)

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

        binding.btnSave.setOnClickListener {
            onSaveImage()
        }

        binding.btnRetake.setOnClickListener { retakePhoto(originalImagePath) }

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

        bitmapFin = combineFrameAndImage(originalImagePath, number)
        binding.imageWithCounter.setImageBitmap(bitmapFin)
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