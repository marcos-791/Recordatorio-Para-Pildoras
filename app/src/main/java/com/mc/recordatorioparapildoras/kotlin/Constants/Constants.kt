package com.mc.recordatorioparapildoras.kotlin.Constants

import android.app.Application

class Constants : Application() {
    val SHARED_PREF_NAME = "database"
    val SAVED = "saved"
    val PASS = "Pass "

    val Segundo = 1000
    val Minuto = Segundo * 60
    val Hora = Minuto * 60
}