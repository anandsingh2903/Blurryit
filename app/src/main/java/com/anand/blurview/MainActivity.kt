package com.anand.blurview

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.anand.blurryit.BlurryitLayout


class MainActivity : AppCompatActivity() {

    private var imageViewOverlayOnViewToBeBlurred : ImageView? = null
    private var viewToBeBlurred : LinearLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewToBeBlurred = findViewById(R.id.view_to_be_blurred)
        imageViewOverlayOnViewToBeBlurred = findViewById(R.id.avengers)

        imageViewOverlayOnViewToBeBlurred?.setImageDrawable(ContextCompat.getDrawable(this, android.R.color.transparent))

        val blurryitLayout = BlurryitLayout(this, imageViewOverlayOnViewToBeBlurred!!)

        viewToBeBlurred!!.post {
            val bitmap = Bitmap.createBitmap(viewToBeBlurred!!.width, viewToBeBlurred!!.height, Bitmap.Config.ARGB_8888)
            //val bitmap = viewToBeBlurred!!.drawingCache

            val canvas = Canvas(bitmap)
            val bgDrawable = viewToBeBlurred!!.background
            if (bgDrawable != null)
                bgDrawable.draw(canvas)
            else
                canvas.drawColor(Color.WHITE)
            viewToBeBlurred!!.draw(canvas)

            blurryitLayout.execute(bitmap)
        }
    }
}
