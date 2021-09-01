package com.mc.recordatorioparapildoras.kotlin

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*

import com.google.gson.Gson
import com.mc.recordatorioparapildoras.kotlin.Adapter.Remenber
import com.mc.recordatorioparapildoras.kotlin.Constants.Constants
import com.mc.recordatorioparapildoras.kotlin.Notification.Notification


class AddPillActivity : AppCompatActivity() {
    private lateinit var title : TextView
    private lateinit var titulo: EditText
    private lateinit var tiempo:EditText
    private lateinit var submit: Button
    private lateinit var cancel: Button
    private lateinit var spinner: Spinner
    private val opcion = arrayOf("Horas", "Minutos")
    private var horaOminuto: String = "Horas"
    private var time : Long = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pill)

        //variables
        title = findViewById(R.id.title)
        submit = findViewById(R.id.submit)
        titulo = findViewById(R.id.titulo)
        tiempo = findViewById(R.id.tiempo)
        cancel = findViewById(R.id.cancel)

        title.setText(getString(R.string.agrega))

        //declarar el Spinner y su adaptador
        spinner = findViewById(R.id.spinner)
        val adapter : ArrayAdapter<String> = ArrayAdapter(this,
            android.R.layout.simple_spinner_dropdown_item,opcion)
        spinner.adapter = adapter

        //elegir la seleccion del spinner
        spinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                horaOminuto = opcion[p2]
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                horaOminuto = opcion[0]
            }

        }

        submit.setOnClickListener { check() }

        cancel.setOnClickListener { cierro()}

    }

    //volver a MainActivity
    private fun cierro(){
        val intent = Intent(this, MainActivity::class.java)
        // start MainActivity
        startActivity(intent)
        finish()
    }

    //comprobar si los campos estan llenos o vacios, en caso de estar vacios avisar al usuario para completarlos
    private fun check(){
        var title : String = titulo.text.toString()
        var time : String = tiempo.text.toString()

        if(!title.isEmpty() && !time.isEmpty()) {
            var ti: Int = time.toInt()
            guardoValor(title, ti,horaOminuto)
            cierro()
        }else
            Toast.makeText(this,getString(R.string.completa),Toast.LENGTH_SHORT).show()
    }

    //funcion para guardar el valor de una pildora a recordar bajo la clase Remenber
    private fun guardoValor(titulo : String,tiempo : Int, horaOminuto : String){
        var constants = Constants()
        var sharedPreferences : SharedPreferences = getSharedPreferences(constants.SHARED_PREF_NAME, Context.MODE_PRIVATE)

        var n : Int = sharedPreferences.getInt(constants.SAVED,1)
        n++
        var editor : SharedPreferences.Editor = sharedPreferences.edit()
        editor.putInt(constants.SAVED,n)

        var rem = Remenber(n,titulo,tiempo ,horaOminuto)

        if(horaOminuto.equals("Horas"))
            time = (tiempo * constants.Hora).toLong()
        else
            time = (tiempo * constants.Minuto).toLong()

        setAlarma(titulo, n, time)

        var gson = Gson()
        var json : String = gson.toJson(rem)
        editor.putString(constants.PASS+n,json)

        editor.commit()

        Toast.makeText(this,getString(R.string.saved),Toast.LENGTH_SHORT).show()

    }

    //funcion para preparar la alarma, de acuerdo al tiempo que el usuario coloca
    private fun setAlarma(text : String,id : Int, tiempo : Long){
        var manager : AlarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        var pedingIntent : PendingIntent
        var intent : Intent

        intent = Intent(this, Notification::class.java)
        intent.putExtra("texto",text)
        intent.putExtra("ID", ""+id)
        pedingIntent = PendingIntent.getBroadcast(this,id,intent,0)

        manager.setRepeating(AlarmManager.RTC_WAKEUP,System.currentTimeMillis()+tiempo
                        ,tiempo,pedingIntent)

    }

}

