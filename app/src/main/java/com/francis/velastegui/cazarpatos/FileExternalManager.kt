package com.francis.velastegui.cazarpatos

import android.app.Activity
import android.os.Environment
import java.io.*

class FileExternalManager(val actividad: Activity) : FileHandler {
    private val fileName = "archivo_externo.txt"

    private fun isExternalStorageWritable(): Boolean {
        return Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED
    }

    private fun isExternalStorageReadable(): Boolean {
        return Environment.getExternalStorageState() in
                setOf(Environment.MEDIA_MOUNTED, Environment.MEDIA_MOUNTED_READ_ONLY)
    }

    override fun SaveInformation(datosAGrabar: Pair<String, String>) {
        if (isExternalStorageWritable()) {
            val archivo = File(actividad.getExternalFilesDir(null), fileName)
            archivo.bufferedWriter().use {
                it.write(datosAGrabar.first)
                it.newLine()
                it.write(datosAGrabar.second)
            }
        }
    }

    override fun ReadInformation(): Pair<String, String> {
        return try {
            if (isExternalStorageReadable()) {
                val archivo = File(actividad.getExternalFilesDir(null), fileName)
                val contenido = archivo.bufferedReader().readLines()
                val email = contenido.getOrNull(0) ?: ""
                val clave = contenido.getOrNull(1) ?: ""
                Pair(email, clave)
            } else {
                Pair("", "")
            }
        } catch (e: Exception) {
            Pair("", "")
        }
    }
}