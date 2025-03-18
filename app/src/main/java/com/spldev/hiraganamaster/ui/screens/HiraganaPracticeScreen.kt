package com.spldev.hiraganamaster.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import com.spldev.hiraganamaster.R
import com.spldev.hiraganamaster.viewmodel.HiraganaViewModel

@Composable
fun HiraganaPracticeScreen(
    viewModel: HiraganaViewModel,
    onBack: () -> Unit, // Callback para manejar el logout y navegar a LoginScreen
    onVerifyDrawing: () -> Unit,
    onNextCharacter: () -> Unit,
    onClearCanvas: () -> Unit
) {
    var showBackDialog by remember { mutableStateOf(false) } // Estado para mostrar el diálogo al pulsar "Atrás"

    BackHandler {
        showBackDialog = true // Mostrar el diálogo al pulsar "Atrás"
    }
    // Paleta de colores inspirada en Japón (rojo, blanco, negro y tonos naturales)
    val backgroundColor = Color(0xFFF8E1D4) // Color beige inspirado en papel japonés tradicional
    val accentColor = Color(0xFFB22222) // Rojo japonés (similar al rojo de los torii gates)
    val textColor = Color(0xFF2C2C2C) // Negro suave para texto

    Surface(modifier = Modifier.fillMaxSize(), color = backgroundColor){

        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "${stringResource(R.string.title_write)} ${viewModel.currentCharacter.value.second}",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = accentColor // Rojo japonés como color principal del título
                ),
                modifier = Modifier.padding(bottom = 8.dp, top = 24.dp)
            )

            OutlinedCard(
                modifier = Modifier.fillMaxWidth(),
                border = BorderStroke(1.dp, Color.Black),
                colors = CardDefaults.outlinedCardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Canvas(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                            .background(Color.hsl(40f, 1f, 0.8f))
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
                            viewModel.strokes.forEach { stroke ->
                                val path = Path().apply {
                                    moveTo(stroke.first().x, stroke.first().y)
                                    stroke.drop(1).forEach { point ->
                                        lineTo(point.x, point.y)
                                    }
                                }
                                drawPath(path, color = Color.Black, style = Stroke(width = 15f))
                            }

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

                    Button(
                        onClick = onVerifyDrawing,
                        colors=ButtonDefaults.buttonColors(containerColor=accentColor,contentColor=Color.White),
                        shape=RoundedCornerShape(8.dp),
                        modifier=Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = stringResource(R.string.button_verify),
                            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                        )
                    }

                    Button(
                        onClick = onNextCharacter,
                        colors=ButtonDefaults.buttonColors(containerColor=accentColor,contentColor=Color.White),
                        shape=RoundedCornerShape(8.dp),
                        modifier=Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = stringResource(R.string.button_next),
                            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                        )
                    }

                    Button(
                        onClick = onClearCanvas,
                        colors=ButtonDefaults.buttonColors(containerColor=accentColor,contentColor=Color.White),
                        shape=RoundedCornerShape(8.dp),
                        modifier=Modifier.fillMaxWidth()
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

    // Diálogo para confirmar volver atrás a la pantalla de selección
    if (showBackDialog) {
        AlertDialog(
            onDismissRequest={showBackDialog=false},
            title={Text(stringResource(R.string.back_modal_title),color=textColor)},
            text={Text(stringResource(R.string.back_modal_text),color=textColor)},
            confirmButton={
                Button(onClick={
                    showBackDialog=false
                    onBack()
                },
                    colors=ButtonDefaults.buttonColors(containerColor=accentColor,contentColor=Color.White)) {
                    Text(stringResource(R.string.logout_modal_button_yes))
                }
            },
            dismissButton={
                Button(onClick={showBackDialog=false},
                    colors=ButtonDefaults.buttonColors(containerColor=Color.Gray,contentColor=Color.White)) {
                    Text(stringResource(R.string.logout_modal_button_no))
                }
            }
        )
    }
}
