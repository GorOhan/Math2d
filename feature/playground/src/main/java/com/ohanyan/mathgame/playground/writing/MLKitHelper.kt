package com.ohanyan.mathgame.playground.writing

import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.common.model.RemoteModelManager
import com.google.mlkit.vision.digitalink.DigitalInkRecognition
import com.google.mlkit.vision.digitalink.DigitalInkRecognitionModel
import com.google.mlkit.vision.digitalink.DigitalInkRecognitionModelIdentifier
import com.google.mlkit.vision.digitalink.DigitalInkRecognizer
import com.google.mlkit.vision.digitalink.DigitalInkRecognizerOptions
import com.google.mlkit.vision.digitalink.Ink
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
            .addOnFailureListener { e: Exception ->
                println("MLKitHelper ERROR DOWNLOADED")
            }

        recognizer =
            DigitalInkRecognition.getClient(
                DigitalInkRecognizerOptions.builder(model).build()
            )
    }

    fun recognizeDrawing(
        strokes: List<Ink.Stroke>,
        onResult: (String) -> Unit
    ) {
        val ink = Ink.builder().apply { strokes.forEach { addStroke(it) } }.build()
        recognizer.recognize(ink)
            .addOnSuccessListener { result ->
                val recognizedText = result.candidates.firstOrNull()?.text ?: "Unrecognized"
                if (recognizedText == "o") {
                    onResult("0")
                } else {
                    onResult(recognizedText)
                }
            }
            .addOnFailureListener { e ->
                onResult("Failed: ${e.message}")
            }
    }

}