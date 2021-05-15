package com.javidran.imagejournal.view

import android.graphics.*
import android.graphics.drawable.BitmapDrawable
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

    private var _binding: FragmentAlbumSelectorBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    var fillPaint = Paint()

    fun initPaints() {

        // fill
        fillPaint.setStyle(Paint.Style.FILL)
        fillPaint.setColor(Color.GRAY)

    }

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
        // get device dimensions
        val displayMetrics = DisplayMetrics()
        activity?.windowManager?.defaultDisplay?.getMetrics(displayMetrics)
        val dwidth = displayMetrics.widthPixels
        val dheight = displayMetrics.heightPixels
        //val rectInf = RectF(0F, ((5*dheight)/6).toFloat(), 0F,0F)
        val rectInf = RectF(3F, 3F,3F,3F)

        val bitmap = Bitmap.createBitmap(dwidth, dheight, Bitmap.Config.ARGB_4444) //width:700 height:1000
        initPaints()
        val canvas = Canvas(bitmap)
        //canvas.drawARGB(255, 100, 250, 250); // canvas background color

        val cornerRadius = 50
        canvas.drawRect(0F, 0F, 5F, 5F, fillPaint);
        canvas.drawRoundRect(rectInf, cornerRadius.toFloat(), cornerRadius.toFloat(), fillPaint)
        // now bitmap holds the updated pixels

        // set bitmap as background to ImageView
        binding.imageRect.background = BitmapDrawable(getResources(), bitmap)
        return view
    }

    fun onDraw (){

    }

}