package com.proyecto.tgo.util

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.proyecto.tgo.receiver.ViajeAlarmReceiver
import java.time.LocalDateTime
import java.time.ZoneId

/**
 * Recurso nativo: AlarmManager + notificaciones.
 * Programa un recordatorio para la hora a la que el conductor agendó iniciar el viaje.
 */
object ViajeScheduler {
    const val CANAL_ID = "viajes_agendados"
    const val EXTRA_SERVICIO_ID = "servicio_id"
    const val EXTRA_DESCRIPCION = "descripcion"

    fun crearCanalNotificaciones(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val canal = NotificationChannel(
                CANAL_ID,
                "Viajes agendados",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Recordatorios para iniciar los viajes que agendaste"
                enableVibration(true)
            }
            context.getSystemService(NotificationManager::class.java).createNotificationChannel(canal)
        }
    }

    private fun pendingIntent(context: Context, servicioId: String, descripcion: String): PendingIntent {
        val intent = Intent(context, ViajeAlarmReceiver::class.java).apply {
            putExtra(EXTRA_SERVICIO_ID, servicioId)
            putExtra(EXTRA_DESCRIPCION, descripcion)
        }
        return PendingIntent.getBroadcast(
            context,
            servicioId.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    /** Agenda el recordatorio. Devuelve false si el horario ya pasó. */
    fun agendar(
        context: Context,
        servicioId: String,
        descripcion: String,
        momento: LocalDateTime
    ): Boolean {
        val millis = momento.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
        if (millis <= System.currentTimeMillis()) return false

        crearCanalNotificaciones(context)
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            millis,
            pendingIntent(context, servicioId, descripcion)
        )
        return true
    }

    fun cancelar(context: Context, servicioId: String) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(pendingIntent(context, servicioId, ""))
    }
}
