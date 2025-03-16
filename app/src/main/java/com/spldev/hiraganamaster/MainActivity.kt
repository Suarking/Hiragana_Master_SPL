package com.spldev.hiraganamaster

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.compose.ui.geometry.Offset
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.common.model.RemoteModelManager
import com.google.mlkit.vision.digitalink.DigitalInkRecognition
import com.google.mlkit.vision.digitalink.DigitalInkRecognitionModel
import com.google.mlkit.vision.digitalink.DigitalInkRecognitionModelIdentifier
import com.google.mlkit.vision.digitalink.DigitalInkRecognizer
import com.google.mlkit.vision.digitalink.DigitalInkRecognizerOptions
import com.google.mlkit.vision.digitalink.Ink

class MainActivity : ComponentActivity() {

    private lateinit var recognizer: DigitalInkRecognizer

    // Lista de hiraganas y sus correspondientes romaji
    private val hiraganaList = listOf(
        Pair("あ", "a"),
        Pair("い", "i"),
        Pair("う", "u"),
        Pair("え", "e"),
        Pair("お", "o"),
        Pair("か", "ka"),
        Pair("き", "ki"),
        Pair("く", "ku"),
        Pair("け", "ke"),
        Pair("こ", "ko")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializar el reconocedor y descargar el modelo si es necesario
        setupDigitalInkRecognition()



        setContent {
            // Estado para los trazos acumulados (lista de listas de puntos)
            var strokes = remember { mutableStateListOf<MutableList<Offset>>() }

            // Estado para el trazo actual (lista de puntos)
            var currentStroke = remember { mutableStateListOf<Offset>() }
            var currentCharacter by remember { mutableStateOf(hiraganaList.random()) }

            HiraganaPracticeScreen(
                currentCharacter = currentCharacter,
                strokes = strokes,
                currentStroke = currentStroke,
                onVerifyDrawing = { ink ->
                    verifyDrawing(ink, currentCharacter.first) { isCorrect ->
                        if (isCorrect) {
                            Toast.makeText(this, "¡Correcto!", Toast.LENGTH_SHORT).show()
                            currentCharacter =
                                hiraganaList.random() // Cambiar a un nuevo carácter aleatorio
                            strokes.clear() // Limpiar los trazos acumulados manualmente
                            currentStroke.clear() // Limpiar el trazo

                        } else {
                            Toast.makeText(this, "Intenta nuevamente.", Toast.LENGTH_SHORT).show()
                        }
                    }
                },
                onNextCharacter = {
                    currentCharacter =
                        hiraganaList.random() // Cambiar al siguiente carácter aleatorio
                    strokes.clear() // Limpiar los trazos acumulados manualmente
                    currentStroke.clear() // Limpiar el trazo
                },
                onClearCanvas =  {
                    strokes.clear() // Limpiar los trazos acumulados manualmente
                    currentStroke.clear() // Limpiar el trazo
                }

            )
        }
    }

    private fun setupDigitalInkRecognition() {
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
                        Toast.makeText(this, "Modelo descargado exitosamente", Toast.LENGTH_SHORT).show()
                        recognizer = DigitalInkRecognition.getClient(
                            DigitalInkRecognizerOptions.builder(model).build()
                        )
                    }.addOnFailureListener { e ->
                        Toast.makeText(this, "Error al descargar el modelo: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    // Si ya está descargado, inicializar el reconocedor directamente
                    recognizer = DigitalInkRecognition.getClient(
                        DigitalInkRecognizerOptions.builder(model).build()
                    )
                }
            }.addOnFailureListener { e ->
                Toast.makeText(this, "Error al verificar el modelo: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun verifyDrawing(ink: Ink, expectedCharacter: String, onResult: (Boolean) -> Unit) {
        if (!::recognizer.isInitialized) {
            Toast.makeText(this, "El modelo aún no está listo. Intenta nuevamente.", Toast.LENGTH_SHORT).show()
            onResult(false)
            return
        }

        recognizer.recognize(ink)
            .addOnSuccessListener { result ->
                val recognizedText = result.candidates.firstOrNull()?.text ?: ""
                onResult(recognizedText == expectedCharacter)
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error al reconocer dibujo: ${e.message}", Toast.LENGTH_SHORT).show()
                onResult(false)
            }
    }
}
