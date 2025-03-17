package com.spldev.hiraganamaster.viewmodel


import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.geometry.Offset
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.common.model.RemoteModelManager
import com.google.mlkit.vision.digitalink.DigitalInkRecognition
import com.google.mlkit.vision.digitalink.DigitalInkRecognitionModel
import com.google.mlkit.vision.digitalink.DigitalInkRecognitionModelIdentifier
import com.google.mlkit.vision.digitalink.DigitalInkRecognizer
import com.google.mlkit.vision.digitalink.DigitalInkRecognizerOptions
import com.google.mlkit.vision.digitalink.Ink
import com.spldev.hiraganamaster.datasource.HiraganaData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HiraganaViewModel @Inject constructor() : ViewModel() {
    private lateinit var recognizer: DigitalInkRecognizer

    // Estado para el carácter actual (hiragana y romaji)
    val currentCharacter = mutableStateOf(HiraganaData.getallHiraganas()[(0 until 20).random()])

    // Lista de hiraganas y sus correspondientes romaji
    private val hiraganaList = HiraganaData.getallHiraganas()

    // Estado para los trazos acumulados (lista de listas de puntos)
    val strokes = mutableStateListOf<MutableList<Offset>>()

    // Estado para el trazo actual (lista de puntos)
    val currentStroke = mutableStateListOf<Offset>()

    fun setupDigitalInkRecognition(context: Context) {
        val modelIdentifier = DigitalInkRecognitionModelIdentifier.fromLanguageTag("ja-JP")
            ?: throw IllegalArgumentException("Modelo no encontrado para el idioma especificado")

        val model = DigitalInkRecognitionModel.builder(modelIdentifier).build()
        val remoteModelManager = RemoteModelManager.getInstance()

        // Verificar si el modelo ya está descargado
        remoteModelManager.isModelDownloaded(model)
            .addOnSuccessListener { isDownloaded ->
                if (!isDownloaded) {
                    // Descargar el modelo si no está disponible
                    remoteModelManager.download(
                        model,
                        DownloadConditions.Builder().requireWifi().build()
                    ).addOnSuccessListener {
                        Toast.makeText(context, "Modelo descargado exitosamente", Toast.LENGTH_SHORT).show()
                        recognizer = DigitalInkRecognition.getClient(
                            DigitalInkRecognizerOptions.builder(model).build()
                        )
                    }.addOnFailureListener { e ->
                        Toast.makeText(context, "Error al descargar el modelo: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    // Si ya está descargado, inicializar el reconocedor directamente
                    recognizer = DigitalInkRecognition.getClient(
                        DigitalInkRecognizerOptions.builder(model).build()
                    )
                }
            }.addOnFailureListener { e ->
                Toast.makeText(context, "Error al verificar el modelo: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    fun verifyDrawing(context: Context, ink: Ink) {
        if (!::recognizer.isInitialized) {
            Toast.makeText(context, "El modelo aún no está listo. Intenta nuevamente.", Toast.LENGTH_SHORT).show()
            return
        }

        recognizer.recognize(ink)
            .addOnSuccessListener { result ->
                val recognizedText = result.candidates.firstOrNull()?.text ?: ""
                if (recognizedText == currentCharacter.value.first) {
                    Toast.makeText(context, "¡Correcto!", Toast.LENGTH_SHORT).show()
                    changeCharacter()
                    clearCanvas()
                } else {
                    Toast.makeText(context, "Intenta nuevamente.", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "Error al reconocer dibujo: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    fun changeCharacter() {
        currentCharacter.value = hiraganaList.random()
        clearCanvas()
    }

    fun clearCanvas() {
        strokes.clear()
        currentStroke.clear()
    }
}
