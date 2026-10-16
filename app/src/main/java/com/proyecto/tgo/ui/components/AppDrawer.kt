package com.proyecto.tgo.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.proyecto.tgo.model.MenuSeccion

@Composable
fun AppDrawerContent(
    seccionActual: MenuSeccion,
    onSeccionSeleccionada: (MenuSeccion) -> Unit,
    onCloseDrawer: () -> Unit
) {
    ModalDrawerSheet(
        drawerContainerColor = MaterialTheme.colorScheme.background
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            Text(
                text = "TGO - Transfer & Go",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )
        }
        HorizontalDivider()
        Spacer(modifier = Modifier.height(12.dp))

        MenuSeccion.entries.forEach { seccion ->
            NavigationDrawerItem(
                label = { Text(seccion.titulo) },
                selected = seccion == seccionActual,
                onClick = {
                    onSeccionSeleccionada(seccion)
                    onCloseDrawer()
                },
                modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding),
                colors = NavigationDrawerItemDefaults.colors(
                    selectedContainerColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.onPrimary,
                    unselectedTextColor = MaterialTheme.colorScheme.onBackground
                )
            )
        }
    }
}