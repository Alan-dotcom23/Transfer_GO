package com.proyecto.tgo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.proyecto.tgo.ui.screens.MainScreen
import com.proyecto.tgo.ui.theme.TransferGOTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TransferGOTheme {
                MainScreen()
            }
        }
    }
}