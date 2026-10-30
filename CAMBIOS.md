# Cambios realizados en Transfer GO

1. **Menú de cajón**: ícono por sección (`MenuSeccion.icono`), cabecera con logo y nueva sección **Mapa**.
2. **Ícono de la app**: auto + pin de ubicación (paleta verde esmeralda/plata), adaptativo, monocromo (Android 13+) y legacy WebP.
3. **Perfil**: nueva tarjeta "Mi Vehículo" (marca/modelo, patente, color y **capacidad de pasajeros** editable 1–60).
4. **Agendar viaje**: al presionar "Iniciar" se elige *Iniciar ahora* o *Agendar horario* (fecha + hora). Se programa una notificación.
5. **Recursos nativos**: Cámara (foto de perfil), GPS/mapa (osmdroid), Teléfono (marcador), Mapas externos (Navegar), AlarmManager + Notificaciones.
6. Corrección: `formatearFecha` mostraba un día menos por la zona horaria (UTC).
