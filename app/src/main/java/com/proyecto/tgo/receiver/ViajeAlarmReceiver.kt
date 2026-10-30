package com.proyecto.tgo.receiver

import android.Manifest
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.proyecto.tgo.MainActivity
import com.proyecto.tgo.R
import com.proyecto.tgo.util.ViajeScheduler

/** Se ejecuta cuando llega la hora agendada y muestra la notificación del viaje. */
class ViajeAlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val servicioId = intent.getStringExtra(ViajeScheduler.EXTRA_SERVICIO_ID) ?: return
        val descripcion = intent.getStringExtra(ViajeScheduler.EXTRA_DESCRIPCION).orEmpty()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS)
            != PackageManager.PERMISSION_GRANTED
        ) return

        ViajeScheduler.crearCanalNotificaciones(context)

        val abrirApp = PendingIntent.getActivity(
            context,
            servicioId.hashCode(),
            Intent(context, MainActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notificacion = NotificationCompat.Builder(context, ViajeScheduler.CANAL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("Es hora de iniciar el viaje $servicioId")
            .setContentText(descripcion)
            .setStyle(NotificationCompat.BigTextStyle().bigText(descripcion))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_REMINDER)
            .setContentIntent(abrirApp)
            .setAutoCancel(true)
            .build()

        try {
            NotificationManagerCompat.from(context).notify(servicioId.hashCode(), notificacion)
        } catch (_: SecurityException) {
            // Sin permiso de notificaciones: no se muestra nada
        }
    }
}
