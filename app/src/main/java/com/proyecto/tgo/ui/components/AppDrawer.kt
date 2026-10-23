package com.proyecto.tgo.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.proyecto.tgo.model.MenuSeccion

@Composable
fun AppDrawer(
    seccionSeleccionada: MenuSeccion,
    onSeccionSeleccionada: (MenuSeccion) -> Unit
) {
    ModalDrawerSheet {
        Text(
            text = "Transfer_GO",
            modifier = Modifier.padding(top = 24.dp, start = 16.dp, bottom = 16.dp)
        )
        MenuSeccion.entries.forEach { seccion ->
            NavigationDrawerItem(
                label = { Text(seccion.titulo) },
                selected = seccion == seccionSeleccionada,
                onClick = { onSeccionSeleccionada(seccion) },
                modifier = Modifier.padding(horizontal = 12.dp)
            )
        }
    }
}