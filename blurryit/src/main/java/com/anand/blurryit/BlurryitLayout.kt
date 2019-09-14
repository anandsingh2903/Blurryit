package com.anand.blurryit

import android.content.Context
import android.graphics.Bitmap
import android.os.AsyncTask
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import android.widget.ImageView
import java.lang.ref.WeakReference

class BlurryitLayout(context: Context, imageView: ImageView) : AsyncTask<Bitmap, Void, Bitmap>() {
    private val imageViewReference: WeakReference<ImageView> = WeakReference(imageView)
    private val renderScript: RenderScript = RenderScript.create(context)

    private var isShouldRecycleSource = false

    // Decode image in background.
    override fun doInBackground(vararg params: Bitmap): Bitmap {
        val bitmap = params[0]
        return blurBitmap(bitmap)
    }

    // Once complete, see if ImageView is still around and set bitmap.
    override fun onPostExecute(bitmap: Bitmap?) {
        if (bitmap == null || isCancelled) {
            return
        }

        val imageView = imageViewReference.get() ?: return

        imageView.setImageBitmap(bitmap)
    }

    private fun blurBitmap(bitmap: Bitmap): Bitmap {
        //Let's create an empty bitmap with the same size of the bitmap we want to blur
        val outBitmap = Bitmap.createBitmap(
            bitmap.width, bitmap.height,
            Bitmap.Config.ARGB_8888
        )

        //Instantiate a new Renderscript


        //Create an Intrinsic Blur Script using the Renderscript
        val blurScript = ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript))

        //Create the in/out Allocations with the Renderscript and the in/out bitmaps
        val allIn = Allocation.createFromBitmap(renderScript, bitmap)
        val allOut = Allocation.createFromBitmap(renderScript, outBitmap)

        //Set the radius of the blur
        blurScript.setRadius(25f)

        //Perform the Renderscript
        blurScript.setInput(allIn)
        blurScript.forEach(allOut)

        //Copy the final bitmap created by the out Allocation to the outBitmap
        allOut.copyTo(outBitmap)

        // recycle the original bitmap
        // nope, we are using the original bitmap as well :/
        if (isShouldRecycleSource) {
            bitmap.recycle()
        }

        //After finishing everything, we destroy the Renderscript.
        renderScript.destroy()

        return outBitmap
    }
}