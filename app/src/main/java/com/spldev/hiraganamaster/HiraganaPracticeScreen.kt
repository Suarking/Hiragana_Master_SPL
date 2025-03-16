package com.spldev.hiraganamaster

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.google.mlkit.vision.digitalink.Ink
import androidx.compose.ui.geometry.Offset

@Composable
fun HiraganaPracticeScreen(
    currentCharacter: Pair<String, String>, // (Hiragana, Romaji)
    strokes: MutableList<MutableList<Offset>>, // Lista mutable de trazos acumulados
    currentStroke: MutableList<Offset>, // Trazo actual que se está dibujando
    onVerifyDrawing: (Ink) -> Unit,
    onNextCharacter: () -> Unit,
    onClearCanvas: () -> Unit // Función para limpiar el lienzo manualmente
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Mostrar el texto en romaji
        Text(
            text = "Escribe: ${currentCharacter.second}", // Romaji (ejemplo: "a")
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Lienzo para dibujar los caracteres hiragana/katakana
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragStart = { offset ->
                            currentStroke.clear() // Limpia el trazo actual
                            currentStroke.add(offset) // Inicia un nuevo trazo
                        },
                        onDrag = { change, _ ->
                            currentStroke.add(change.position) // Añadir
                        },
                        onDragEnd = {
                            strokes.add(currentStroke.toMutableList()) // Guarda el trazo actual en la lista de trazos
                            currentStroke.clear() // Resetea el trazo actual
                        }
                    )
                }
        ) {
            // Dibuja todos los trazos acumulados
            strokes.forEach { stroke ->
                val path = Path().apply {
                    moveTo(stroke.first().x, stroke.first().y)
                    stroke.drop(1).forEach { point ->
                        lineTo(point.x, point.y)
                    }
                }
                drawPath(path, color = Color.Black, style = Stroke(width = 5f))
            }

            // Dibuja el trazo actual mientras se está dibujando
            if (currentStroke.isNotEmpty()) {
                val path = Path().apply {
                    moveTo(currentStroke.first().x, currentStroke.first().y)
                    currentStroke.drop(1).forEach { point ->
                        lineTo(point.x, point.y)
                    }
                }
                drawPath(path, color = Color.Gray, style = Stroke(width = 5f))
            }
        }

        // Botón para verificar el dibujo del usuario
        Button(
            onClick = {
                val inkBuilder = Ink.builder()
                strokes.forEach { stroke ->
                    val strokeBuilder = Ink.Stroke.builder()
                    stroke.forEach { point ->
                        strokeBuilder.addPoint(Ink.Point.create(point.x, point.y))
                    }
                    inkBuilder.addStroke(strokeBuilder.build())
                }
                onVerifyDrawing(inkBuilder.build())
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Verificar")
        }

        // Botón para limpiar el lienzo manualmente (opcional)
        Button(
            onClick = onClearCanvas,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Limpiar")
        }
        // Botón para pasar al siguiente carácter (opcional)
        Button(
            onClick = onNextCharacter,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Siguiente")
        }

    }
}
