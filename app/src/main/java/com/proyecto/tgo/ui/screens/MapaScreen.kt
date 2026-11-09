package com.proyecto.tgo.ui.screens

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay

@Composable
fun MapaScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    // 1. Configuración de osmdroid usando SharedPreferences moderno
    LaunchedEffect(Unit) {
        Configuration.getInstance().load(
            context,
            context.getSharedPreferences("osmdroid_prefs", Context.MODE_PRIVATE)
        )
        Configuration.getInstance().userAgentValue = context.packageName
    }

    var tienePermisoUbicacion by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        tienePermisoUbicacion = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
    }

    LaunchedEffect(Unit) {
        if (!tienePermisoUbicacion) {
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    // Declaramos el MapView en un remember para poder inyectarle los eventos de ciclo de vida
    val mapView = remember { MapView(context) }

    // 2. Manejo estricto del ciclo de vida para evitar fugas de memoria o drenar batería
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> mapView.onResume()
                Lifecycle.Event.ON_PAUSE -> mapView.onPause()
                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            mapView.onDetach() // Limpia los recursos de Osmdroid al destruir la vista
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = {
                mapView.apply {
                    setTileSource(TileSourceFactory.MAPNIK)
                    setMultiTouchControls(true)

                    // 3. Coordenadas reales: Aeropuerto El Tepual (Lat: -41.4388, Lon: -73.0940)
                    val elTepual = GeoPoint(-41.4388, -73.0940)
                    controller.setZoom(14.0)
                    controller.setCenter(elTepual)

                    val startMarker = Marker(this).apply {
                        position = elTepual
                        title = "Aeropuerto El Tepual"
                        snippet = "Punto de recogida"
                        setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                    }
                    overlays.add(startMarker)

                    if (tienePermisoUbicacion) {
                        val locationOverlay = MyLocationNewOverlay(GpsMyLocationProvider(context), this)
                        locationOverlay.enableMyLocation()
                        overlays.add(locationOverlay)
                    }
                }
            }
        )

        if (!tienePermisoUbicacion) {
            Text(
                text = "GPS desactivado o sin permisos",
                modifier = Modifier.align(Alignment.TopCenter)
            )
        }
    }
}