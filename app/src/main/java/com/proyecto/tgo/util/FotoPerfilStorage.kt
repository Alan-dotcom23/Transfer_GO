package com.proyecto.tgo.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.File

/** Guarda y recupera la foto de perfil en el almacenamiento interno de la app. */
object FotoPerfilStorage {
    private const val NOMBRE_ARCHIVO = "foto_perfil.jpg"

    fun guardar(context: Context, bitmap: Bitmap) {
        runCatching {
            File(context.filesDir, NOMBRE_ARCHIVO).outputStream().use {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, it)
            }
        }
    }

    fun cargar(context: Context): Bitmap? = runCatching {
        val archivo = File(context.filesDir, NOMBRE_ARCHIVO)
        if (archivo.exists()) BitmapFactory.decodeFile(archivo.absolutePath) else null
    }.getOrNull()
}
