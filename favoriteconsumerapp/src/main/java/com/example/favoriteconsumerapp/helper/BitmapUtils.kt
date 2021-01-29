package com.example.favoriteconsumerapp.helper

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.ByteArrayOutputStream

object BitmapUtils {

    // convert from bitmap to byte array
    fun getBytes(bitmap: Bitmap): ByteArray? {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        return stream.toByteArray()
    }

    // convert from byte array to bitmap
    fun getImage(image: ByteArray): Bitmap? {
        return BitmapFactory.decodeByteArray(image, 0, image.size)
    }

}