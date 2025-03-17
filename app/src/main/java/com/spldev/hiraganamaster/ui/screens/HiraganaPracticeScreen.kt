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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
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
    onLogout: () -> Unit, // Callback para manejar el logout y navegar a LoginScreen
    onVerifyDrawing: () -> Unit,
    onNextCharacter: () -> Unit,
    onClearCanvas: () -> Unit
) {
    var showLogoutDialog by remember { mutableStateOf(false) } // Estado para mostrar el diálogo de logout

    BackHandler {
        showLogoutDialog = true // Mostrar el diálogo al pulsar "Atrás"
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "${stringResource(R.string.title_write)} ${viewModel.currentCharacter.value.second}",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 8.dp)
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
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.hsl(200f, 1f, 0.8f),
                        contentColor = Color.Black
                    )
                ) {
                    Text(
                        text = stringResource(R.string.button_verify),
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                    )
                }

                Button(
                    onClick = onNextCharacter,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.hsl(200f, 1f, 0.8f),
                        contentColor = Color.Black
                    )
                ) {
                    Text(
                        text = stringResource(R.string.button_next),
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                    )
                }

                Button(
                    onClick = onClearCanvas,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.hsl(200f, 1f, 0.8f),
                        contentColor = Color.Black
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

    // Diálogo de confirmación de logout
    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false }, // Cerrar el diálogo si se cancela o se pulsa fuera del mismo.
            title = { Text("Cerrar sesión") },
            text = { Text("¿Estás seguro que quieres cerrar sesión?") },
            confirmButton = {
                Button(onClick = {
                    FirebaseAuth.getInstance().signOut() // Cerrar sesión en Firebase
                    onLogout() // Navegar a la pantalla de login
                }) {
                    Text("Sí")
                }
            },
            dismissButton = {
                Button(onClick = { showLogoutDialog = false }) {
                    Text("No")
                }
            }
        )
    }
}
