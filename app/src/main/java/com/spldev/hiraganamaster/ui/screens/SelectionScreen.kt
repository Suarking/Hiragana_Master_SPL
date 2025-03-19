package com.spldev.hiraganamaster.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import com.spldev.hiraganamaster.R
import com.spldev.hiraganamaster.ui.viewmodel.HiraganaViewModel

@Composable
fun SelectionScreen(
    hiraganaViewModel: HiraganaViewModel,
    onLogout: () -> Unit, // Callback para manejar el logout y navegar a LoginScreen
    onPlay: () -> Unit // Callback para navegar a HiraganaPracticeScreen
) {
    var showLogoutDialog by remember { mutableStateOf(false) } // Estado para mostrar el diálogo de logout
    var selectedOption by remember { mutableStateOf("Hiragana") } // Estado para la opción seleccionada

    BackHandler {
        showLogoutDialog = true // Mostrar el diálogo al pulsar "Atrás"
    }

    // Paleta de colores inspirada en Japón (rojo, blanco, negro y tonos naturales)
    val backgroundColor = Color(0xFFF8E1D4) // Color beige inspirado en papel japonés tradicional
    val accentColor = Color(0xFFB22222) // Rojo japonés (similar al rojo de los torii gates)
    val textColor = Color(0xFF2C2C2C) // Negro suave para texto

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = backgroundColor // Fondo inspirado en papel japonés tradicional
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.elevatedCardColors(containerColor = Color.White),
                elevation = CardDefaults.elevatedCardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(R.string.selection_title),
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = accentColor // Rojo japonés como color principal del título
                        ),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 8.dp),
                        thickness = 2.dp,
                        color = accentColor
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.img_cat), // Imagen decorativa (gato japonés)
                            contentDescription = null,
                            modifier = Modifier.size(100.dp).padding(end = 16.dp)
                        )

                        Column {
                            // RadioButton para Hiragana con estilo mejorado
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(bottom = 8.dp)
                            ) {
                                RadioButton(
                                    selected = selectedOption == "Hiragana",
                                    onClick = { selectedOption = "Hiragana" },
                                    colors = RadioButtonDefaults.colors(selectedColor = accentColor)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Hiragana",
                                    style = MaterialTheme.typography.bodyLarge.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = textColor // Texto negro suave para contraste
                                    )
                                )
                            }

                            // RadioButton para Katakana con estilo mejorado
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(bottom=8.dp)
                            ) {
                                RadioButton(
                                    selected=selectedOption=="Katakana",
                                    onClick={selectedOption="Katakana"},
                                    colors=RadioButtonDefaults.colors(selectedColor=accentColor)
                                )
                                Spacer(modifier=Modifier.width(8.dp))
                                Text(
                                    text="Katakana",
                                    style=MaterialTheme.typography.bodyLarge.copy(
                                        fontWeight=FontWeight.Bold,
                                        color=textColor
                                    )
                                )
                            }
                        }
                    }

                    Button(
                        onClick= {
                            if(selectedOption=="Hiragana") {
                                hiraganaViewModel.setCharacters("Hiragana")
                            } else {
                                hiraganaViewModel.setCharacters("Katakana")
                            }
                            onPlay() // Navegar a HiraganaPracticeScreen
                        },
                        colors=ButtonDefaults.buttonColors(containerColor=accentColor, contentColor=Color.White),
                        shape=RoundedCornerShape(8.dp),
                        modifier=Modifier.fillMaxWidth()
                    ) {
                        Text(text=stringResource(R.string.selection_play_button))
                    }
                }
            }
        }
    }

    // Diálogo de confirmación de logout
    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest={showLogoutDialog=false},
            title={Text(stringResource(R.string.logout_modal_title),color=textColor)},
            text={Text(stringResource(R.string.logout_modal_text),color=textColor)},
            confirmButton={
                Button(onClick={
                    FirebaseAuth.getInstance().signOut()
                    onLogout()
                },
                    colors=ButtonDefaults.buttonColors(containerColor=accentColor,contentColor=Color.White)) {
                    Text(stringResource(R.string.logout_modal_button_yes))
                }
            },
            dismissButton={
                Button(onClick={showLogoutDialog=false},
                    colors=ButtonDefaults.buttonColors(containerColor=Color.Gray,contentColor=Color.White)) {
                    Text(stringResource(R.string.logout_modal_button_no))
                }
            }
        )
    }
}
