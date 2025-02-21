package com.ohanyan.mathgame.playground.count

import android.content.Context
import android.content.res.AssetFileDescriptor
import android.graphics.Bitmap
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel

class DigitRecognizer(context: Context) {
    private val interpreter: Interpreter

    init {
        interpreter = Interpreter(loadModelFile(context))
    }

    private fun loadModelFile(context: Context): MappedByteBuffer {
        val fileDescriptor: AssetFileDescriptor = context.assets.openFd("mnist.tflite")
        val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
        val fileChannel = inputStream.channel
        val startOffset = fileDescriptor.startOffset
        val declaredLength = fileDescriptor.declaredLength
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
    }


    fun recognize(bitmap: Bitmap): Int {
        val inputImageWidth = interpreter.getInputTensor(0).shape()[1]
        val inputImageHeight = interpreter.getInputTensor(0).shape()[2]
        val resizedBitmap = Bitmap.createScaledBitmap(bitmap, inputImageWidth, inputImageHeight, true)

        val inputBuffer = convertBitmapToByteBuffer(resizedBitmap)
        val outputBuffer = Array(1) { FloatArray(10) } // Assuming 10 digits (0-9)

        interpreter.run(inputBuffer, outputBuffer)

        return getOutputDigit(outputBuffer[0])
    }
    private fun convertBitmapToByteBuffer(bitmap: Bitmap): ByteBuffer {
        val inputImageWidth = interpreter.getInputTensor(0).shape()[1]
        val inputImageHeight = interpreter.getInputTensor(0).shape()[2]
        val shape = interpreter.getInputTensor(0).shape()
        val inputImageDepth = if (shape.size > 3) shape[3] else 1

        val byteBuffer = ByteBuffer.allocateDirect(4 * inputImageWidth * inputImageHeight * inputImageDepth)
        byteBuffer.order(ByteOrder.nativeOrder())

        val pixels = IntArray(inputImageWidth * inputImageHeight)
        bitmap.getPixels(pixels, 0, inputImageWidth, 0, 0, inputImageWidth, inputImageHeight)

        if (inputImageDepth == 1) { //Grayscale
            for (pixelValue in pixels) {
                val r = (pixelValue shr 16 and 0xFF) / 255.0f
                val g = (pixelValue shr 8 and 0xFF) / 255.0f
                val b = (pixelValue and 0xFF) / 255.0f

                val grayValue = (r + g + b) / 3.0f
                byteBuffer.putFloat(grayValue)
            }
        } else { //RGB
            for (pixelValue in pixels) {
                val r = (pixelValue shr 16 and 0xFF) / 255.0f
                val g = (pixelValue shr 8 and 0xFF) / 255.0f
                val b = (pixelValue and 0xFF) / 255.0f

                byteBuffer.putFloat(r)
                byteBuffer.putFloat(g)
                byteBuffer.putFloat(b)
            }
        }
        return byteBuffer
    }

    private fun getOutputDigit(output: FloatArray): Int {
        var maxIndex = 0
        var maxValue = output[0]
        for (i in 1 until output.size) {
            if (output[i] > maxValue) {
                maxValue = output[i]
                maxIndex = i
            }
        }
        return maxIndex
    }
}

