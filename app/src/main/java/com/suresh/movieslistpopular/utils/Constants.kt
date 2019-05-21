package com.suresh.movieslistpopular.utils

import android.content.Context
import android.graphics.Bitmap
import android.net.ConnectivityManager
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import android.widget.Toast


class Constants {

    companion object {

        //Bundle keys
        const val movieData:String = "movieData"



        fun isNetConnected(ctx: Context): Boolean {
            val cm = ctx.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

            return (cm.activeNetworkInfo != null
                    && cm.activeNetworkInfo.isAvailable
                    && cm.activeNetworkInfo.isConnected)
        }


        fun toast(context: Context, message: String) {
            Toast.makeText(context, "" + message, Toast.LENGTH_SHORT).show()
        }


        fun blurRenderScript(context: Context, smallBitmap: Bitmap, radius: Int): Bitmap {
            var smallBitmap = smallBitmap
            try {
                smallBitmap = RGB565toARGB888(smallBitmap)
            } catch (e: Exception) {
                e.printStackTrace()
            }

            val bitmap = Bitmap.createBitmap(
                    smallBitmap.width, smallBitmap.height,
                    Bitmap.Config.ARGB_8888)

            val renderScript = RenderScript.create(context)

            val blurInput = Allocation.createFromBitmap(renderScript, smallBitmap)
            val blurOutput = Allocation.createFromBitmap(renderScript, bitmap)

            val blur = ScriptIntrinsicBlur.create(renderScript,
                    Element.U8_4(renderScript))
            blur.setInput(blurInput)
            blur.setRadius(radius.toFloat()) // radius must be 0 < r <= 25
            blur.forEach(blurOutput)

            blurOutput.copyTo(bitmap)
            renderScript.destroy()

            return bitmap

        }


        @Throws(Exception::class)
        private fun RGB565toARGB888(img: Bitmap): Bitmap {
            val numPixels = img.width * img.height
            val pixels = IntArray(numPixels)

            //Get JPEG pixels.  Each int is the color values for one pixel.
            img.getPixels(pixels, 0, img.width, 0, 0, img.width, img.height)

            //Create a Bitmap of the appropriate format.
            val result = Bitmap.createBitmap(img.width, img.height, Bitmap.Config.ARGB_8888)

            //Set RGB pixels.
            result.setPixels(pixels, 0, result.width, 0, 0, result.width, result.height)
            return result
        }


    }

}