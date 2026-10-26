package com.proyecto.tgo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.proyecto.tgo.ui.screens.LoginScreen
import com.proyecto.tgo.ui.screens.MainScreen
import com.proyecto.tgo.ui.theme.TransferGOTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TransferGOTheme {
                // Contenedor principal para el fondo de la app
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Variable de estado que controla qué pantalla se ve
                    var sesionIniciada by remember { mutableStateOf(false) }

                    if (sesionIniciada) {
                        // Si ya inició sesión, mostramos la app normal
                        MainScreen()
                    } else {
                        // Si no, mostramos el login
                        LoginScreen(
                            onLoginSuccess = { sesionIniciada = true }
                        )
                    }
                }
            }
        }
    }
}