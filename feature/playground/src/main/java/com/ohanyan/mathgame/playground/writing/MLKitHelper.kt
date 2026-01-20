package com.ohanyan.mathgame.playground.writing

import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.common.model.RemoteModelManager
import com.google.mlkit.vision.digitalink.recognition.DigitalInkRecognition
import com.google.mlkit.vision.digitalink.recognition.DigitalInkRecognitionModel
import com.google.mlkit.vision.digitalink.recognition.DigitalInkRecognitionModelIdentifier
import com.google.mlkit.vision.digitalink.recognition.DigitalInkRecognizer
import com.google.mlkit.vision.digitalink.recognition.DigitalInkRecognizerOptions
import com.google.mlkit.vision.digitalink.recognition.Ink
import javax.inject.Inject

class MLKitHelper @Inject constructor() {

    private var recognizer: DigitalInkRecognizer

    init {
        val modelIdentifier: DigitalInkRecognitionModelIdentifier =
            DigitalInkRecognitionModelIdentifier.fromLanguageTag("en-US")!!

        val model: DigitalInkRecognitionModel =
            DigitalInkRecognitionModel.builder(modelIdentifier).build()


        val remoteModelManager = RemoteModelManager.getInstance()
        remoteModelManager.download(model, DownloadConditions.Builder().build())
            .addOnSuccessListener {
                println("MLKitHelper MODEL DOWNLOADED")
            }
            .addOnFailureListener { _: Exception ->
                println("MLKitHelper ERROR DOWNLOADED")
            }

        recognizer =
            DigitalInkRecognition.getClient(
                DigitalInkRecognizerOptions.builder(model).build()
            )
    }

    fun recognizeDrawing(
        hardMode: Boolean,
        strokes: Ink.Stroke,
        inputNumber: String,
        onResult: (Boolean) -> Unit
    ) {
        val ink = Ink.builder().apply { addStroke(strokes) }.build()
        recognizer.recognize(ink)
            .addOnSuccessListener { result ->

                val isCorrect = if (hardMode) result.candidates.firstOrNull()?.text == inputNumber
                else result.candidates.find { it.text == inputNumber } != null
                onResult(isCorrect)
            }
            .addOnFailureListener { e ->
                onResult(false)
            }
    }

}