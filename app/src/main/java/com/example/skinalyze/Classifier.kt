package com.example.skinalyze

import android.content.res.AssetManager
import android.graphics.Bitmap
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel

class Classifier(
    private val assetManager: AssetManager,
    modelPath: String,
    private val inputSize: Int
) {
    private var interpreter: Interpreter
    private val imgData: ByteBuffer
    private val intValues = IntArray(inputSize * inputSize)

    init {
        interpreter = Interpreter(loadModelFile(assetManager, modelPath))
        imgData = ByteBuffer.allocateDirect(4 * 1 * inputSize * inputSize * 3)
        imgData.order(ByteOrder.nativeOrder())
    }

    /**
     * Load the TensorFlow Lite model file from assets.
     */
    private fun loadModelFile(assetManager: AssetManager, modelPath: String): MappedByteBuffer {
        val fileDescriptor = assetManager.openFd(modelPath)
        val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
        val fileChannel = inputStream.channel
        val startOffset = fileDescriptor.startOffset
        val declaredLength = fileDescriptor.declaredLength
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
    }

    /**
     * Classify an image.
     */
    fun classify(bitmap: Bitmap): Array<FloatArray> {
        convertBitmapToByteBuffer(bitmap)
        val output = Array(1) { FloatArray(5) }
        interpreter.run(imgData, output)
        return output
    }

    /**
     * Convert a bitmap to a ByteBuffer.
     */
    private fun convertBitmapToByteBuffer(bitmap: Bitmap) {
        imgData.rewind()
        bitmap.getPixels(intValues, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)
        var pixel = 0
        for (i in 0 until inputSize) {
            for (j in 0 until inputSize) {
                val value = intValues[pixel++]
                imgData.putFloat((value shr 16 and 0xFF) / 255.0f)
                imgData.putFloat((value shr 8 and 0xFF) / 255.0f)
                imgData.putFloat((value and 0xFF) / 255.0f)
            }
        }
    }
}