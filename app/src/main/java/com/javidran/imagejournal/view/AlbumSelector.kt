package com.javidran.imagejournal.view

import android.graphics.Canvas
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.javidran.imagejournal.R
import android.graphics.Color
import android.graphics.Bitmap
import android.graphics.Paint
import android.graphics.drawable.BitmapDrawable
import android.util.DisplayMetrics
import com.javidran.imagejournal.databinding.FragmentAlbumSelectorBinding
import com.javidran.imagejournal.databinding.FragmentCameraBinding

/**
 * A simple [Fragment] subclass.
 * Use the [AlbumSelector.newInstance] factory method to
 * create an instance of this fragment.
 */
class AlbumSelector : Fragment() {

    private var _binding: FragmentAlbumSelectorBinding? = null
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
        _binding = FragmentAlbumSelectorBinding.inflate(inflater, container, false)
        val view = binding.root

        val bitmap = Bitmap.createBitmap(700, 1000, Bitmap.Config.ARGB_4444)
        val canvas = Canvas(bitmap)
        // canvas background color
        canvas.drawARGB(255, 78, 168, 186);

        var paint = Paint()
        paint.setColor(Color.parseColor("#FFFFFF"))
        paint.setStrokeWidth(30F)
        paint.setStyle(Paint.Style.STROKE)
        paint.setAntiAlias(true)
        paint.setDither(true)

        // get device dimensions
        val displayMetrics = DisplayMetrics()
        WindowManager.defaultDisplay.getMetrics(displayMetrics)
        // circle center
        System.out.println("Width : "+displayMetrics.widthPixels)
        var center_x = (displayMetrics.widthPixels/2).toFloat()
        var center_y = (displayMetrics.heightPixels/2).toFloat()
        var radius = 300F

        // draw circle
        canvas.drawCircle(center_x, center_y, radius, paint)
        // now bitmap holds the updated pixels

        // set bitmap as background to ImageView
        binding.imageCircle.background = BitmapDrawable(getResources(), bitmap)

        return view
    }

}