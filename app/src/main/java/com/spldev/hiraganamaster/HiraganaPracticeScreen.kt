package com.spldev.hiraganamaster

import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.spldev.hiraganamaster.viewmodel.HiraganaViewModel

@Composable
fun HiraganaPracticeScreen(
    viewModel: HiraganaViewModel,
    context: Context,
    onVerifyDrawing: () -> Unit, // Función para verificar el dibujo
    onNextCharacter: () -> Unit, // Función para cambiar al siguiente carácter
    onClearCanvas: () -> Unit // Función para limpiar el lienzo
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Mostrar el texto en romaji
        Text(
            text = "${stringResource(R.string.title_write)} ${viewModel.currentCharacter.value.second}",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // OutlinedCard que contiene el Canvas y los botones
        OutlinedCard(
            modifier = Modifier.fillMaxWidth(),
            border = BorderStroke(1.dp, Color.Black),
            colors = CardDefaults.outlinedCardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Lienzo para dibujar los caracteres hiragana/katakana
                Canvas(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .background(Color.hsl(40f,1f, 0.8f ))
                        .pointerInput(Unit) {
                            detectDragGestures(
                                onDragStart = { offset ->
                                    viewModel.currentStroke.clear()
                                    viewModel.currentStroke.add(offset)
                                },
                                onDrag = { change, _ ->
                                    viewModel.currentStroke.add(change.position)
                                },
                                onDragEnd = {
                                    viewModel.strokes.add(viewModel.currentStroke.toMutableList())
                                    viewModel.currentStroke.clear()
                                }
                            )
                        },
                    onDraw = {
                        // Dibujar todos los trazos acumulados
                        viewModel.strokes.forEach { stroke ->
                            val path = Path().apply {
                                moveTo(stroke.first().x, stroke.first().y)
                                stroke.drop(1).forEach { point ->
                                    lineTo(point.x, point.y)
                                }
                            }
                            drawPath(path, color = Color.Black, style = Stroke(width = 15f))
                        }

                        // Dibujar el trazo actual mientras se está dibujando
                        if (viewModel.currentStroke.isNotEmpty()) {
                            val path = Path().apply {
                                moveTo(viewModel.currentStroke.first().x, viewModel.currentStroke.first().y)
                                viewModel.currentStroke.drop(1).forEach { point ->
                                    lineTo(point.x, point.y)
                                }
                            }
                            drawPath(path, color = Color.Gray, style = Stroke(width = 25f))
                        }
                    }
                )

                // Botón para verificar el dibujo del usuario
                Button(
                    onClick = onVerifyDrawing,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.hsl(200f, 1f, 0.8f), // Color de fondo del botón
                        contentColor = Color.Black // Color del texto dentro del botón
                    )
                ) {
                    Text(
                        text = stringResource(R.string.button_verify),
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                    )
                }

                // Botón para cambiar al siguiente carácter (opcional)
                Button(
                    onClick = onNextCharacter,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.hsl(200f, 1f, 0.8f), // Color de fondo del botón
                        contentColor = Color.Black // Color del texto dentro del botón
                    )
                ) {
                    Text(
                        text = stringResource(R.string.button_next),
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                    )
                }

                // Botón para limpiar el lienzo manualmente (opcional)
                Button(
                    onClick = onClearCanvas,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.hsl(200f, 1f, 0.8f), // Color de fondo del botón
                        contentColor = Color.Black // Color del texto dentro del botón
                    )
                ) {
                    Text(
                        text = stringResource(R.string.button_clear),
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                    )
                }

            }
        }
    }
}
