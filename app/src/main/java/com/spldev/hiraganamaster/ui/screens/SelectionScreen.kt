package com.spldev.hiraganamaster.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
    onPlayKanaPractice: () -> Unit, // Callback para navegar a HiraganaPracticeScreen
    onPlayGuessTheKana: () -> Unit // Callback para navegar a GuessTheKana
) {
    var showLogoutDialog by remember { mutableStateOf(false) } // Estado para mostrar el diálogo de logout
    var selectedKanaOption by remember { mutableStateOf("Hiragana") } // Estado para la opción seleccionada (Hiragana/Katakana)
    var selectedGameMode by remember { mutableStateOf("Dibuja el Kana") } // Estado para la opción seleccionada (Game Mode)

    BackHandler {
        showLogoutDialog = true // Mostrar el diálogo al pulsar "Atrás"
    }

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
            Text(
                text = stringResource(R.string.selection_title),
                style = MaterialTheme.typography.headlineMedium.copy(
                    color = accentColor,
                    fontWeight = FontWeight.Bold
                )
            )

            ElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.elevatedCardColors(containerColor = Color.White),
                elevation = CardDefaults.elevatedCardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally // Centrar contenido horizontalmente dentro de la tarjeta
                ) {

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center, // Centrar horizontalmente los elementos del Row
                        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_hiragana), // Imagen para Hiragana
                            contentDescription = null,
                            modifier = Modifier.size(40.dp)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        RadioButton(
                            selected = selectedKanaOption == "Hiragana",
                            onClick = { selectedKanaOption = "Hiragana" },
                            colors = RadioButtonDefaults.colors(selectedColor = accentColor)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "Hiragana", color = textColor)
                    }

                    Row(
                        verticalAlignment=Alignment.CenterVertically,
                        horizontalArrangement=Arrangement.Center, // Centrar horizontalmente los elementos del Row
                        modifier=Modifier.fillMaxWidth().padding(bottom=8.dp)
                    ) {
                        Image(
                            painter=painterResource(id=R.drawable.ic_katakana), // Imagen para Katakana
                            contentDescription=null,
                            modifier=Modifier.size(40.dp)
                        )
                        Spacer(modifier=Modifier.width(16.dp))
                        RadioButton(
                            selected=selectedKanaOption=="Katakana",
                            onClick={selectedKanaOption="Katakana"},
                            colors=RadioButtonDefaults.colors(selectedColor=accentColor)
                        )
                        Spacer(modifier=Modifier.width(8.dp))
                        Text(text="Katakana",color=textColor)
                    }
                }
            }

            Spacer(modifier=Modifier.height(16.dp))

            ElevatedCard(
                modifier=Modifier.fillMaxWidth(),
                shape=RoundedCornerShape(16.dp),
                colors=CardDefaults.elevatedCardColors(containerColor=Color.White),
                elevation=CardDefaults.elevatedCardElevation(defaultElevation=8.dp)
            ) {
                Column(
                    modifier=Modifier.padding(16.dp),
                    verticalArrangement=Arrangement.spacedBy(16.dp),
                    horizontalAlignment=Alignment.CenterHorizontally
                ) {
                    Text(
                        text="Selecciona el modo de juego",
                        style=MaterialTheme.typography.headlineSmall.copy(
                            color=accentColor,
                            fontWeight=FontWeight.Bold
                        )
                    )

                    Row(
                        verticalAlignment=Alignment.CenterVertically,
                        horizontalArrangement=Arrangement.Center, // Centrar horizontalmente los elementos del Row
                        modifier=Modifier.fillMaxWidth().padding(bottom=8.dp)
                    ) {
                        Image(
                            painter=painterResource(id=R.drawable.ic_draw_kana), // Imagen para Dibuja el Kana
                            contentDescription=null,
                            modifier=Modifier.size(40.dp)
                        )
                        Spacer(modifier=Modifier.width(16.dp))
                        RadioButton(
                            selected=selectedGameMode=="Dibuja el Kana",
                            onClick={selectedGameMode="Dibuja el Kana"},
                            colors=RadioButtonDefaults.colors(selectedColor=accentColor)
                        )
                        Spacer(modifier=Modifier.width(8.dp))
                        Text(text="Dibuja el Kana",color=textColor)
                    }

                    Row(
                        verticalAlignment=Alignment.CenterVertically,
                        horizontalArrangement=Arrangement.Center, // Centrar horizontalmente los elementos del Row
                        modifier=Modifier.fillMaxWidth().padding(bottom=8.dp)
                    ) {
                        Image(
                            painter=painterResource(id=R.drawable.ic_guess_romaji), // Imagen para Adivina el Romaji
                            contentDescription=null,
                            modifier=Modifier.size(40.dp)
                        )
                        Spacer(modifier=Modifier.width(16.dp))
                        RadioButton(
                            selected=selectedGameMode=="Adivina el Romaji",
                            onClick={selectedGameMode="Adivina el Romaji"},
                            colors=RadioButtonDefaults.colors(selectedColor=accentColor)
                        )
                        Spacer(modifier=Modifier.width(8.dp))
                        Text(text="Adivina el Romaji",color=textColor)
                    }
                }
            }

            Spacer(modifier=Modifier.height(16.dp))

            Button(
                onClick={
                    if(selectedGameMode=="Dibuja el Kana"){
                        if(selectedKanaOption=="Hiragana"){
                            hiraganaViewModel.setCharacters("Hiragana")
                        } else{
                            hiraganaViewModel.setCharacters("Katakana")
                        }
                        onPlayKanaPractice()
                    } else{
                        onPlayGuessTheKana()
                    }
                },
                colors=ButtonDefaults.buttonColors(containerColor=accentColor,contentColor=Color.White),
                shape=RoundedCornerShape(8.dp),
                modifier=Modifier.fillMaxWidth()
            ) {
                Text(text="Jugar")
            }
        }
    }

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
