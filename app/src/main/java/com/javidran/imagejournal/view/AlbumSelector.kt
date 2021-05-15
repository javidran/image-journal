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
/**
 * A simple [Fragment] subclass.
 * Use the [AlbumSelector.newInstance] factory method to
 * create an instance of this fragment.
 */
class AlbumSelector : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_album_selector, container, false)

        val imageView = view.findViewById<View>(R.id.imageCircle)


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
        imageCircle.background = BitmapDrawable(getResources(), bitmap)

        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



    }

    fun populate(model: Model) {
        this.model = model
        icon = resources.getDrawable(model.image) //icon for the view from the
        invalidate()
    }
    fun onDraw(canvas: Canvas) {
        Paint paintText = new Paint()
        paint.setStyle(Paint.Style.STROKE)
        paint.setStrokeWidth(5)


        icon.setBounds(margin,margin,margin + imageSixe, margin + imageSize) //coming from the dimensions
        icon.draw(canvas)

        canvas.drawText(model.title, iamgeSize + margin * 2F, margin * 2F, paintText)// painttext is the paint object initiated with a color
        canvas.drawText(model.subtitle, iamgeSize + margin * 2F, margin * 2F, paintText)

        super.onDraw(canvas)
    }

}