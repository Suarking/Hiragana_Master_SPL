package com.spldev.hiraganamaster.common.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.spldev.hiraganamaster.R

@Composable
fun LogoHeader() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.image_logo), // Reemplaza con el ID de tu logotipo
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth() // Logotipo ocupa todo el ancho de la pantalla
                .height(150.dp) // Ajusta la altura según lo necesario
        )
    }
}
