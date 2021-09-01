package com.mc.recordatorioparapildoras.kotlin

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

class ModifyActivity : AppCompatActivity() {
    private var time : Int = 0
    private var n : Int = 0
    private lateinit var title : TextView
    private lateinit var titulo : EditText
    private lateinit var tiempo : EditText
    private val opcion = arrayOf("Horas", "Minutos")
    private lateinit var spinner : Spinner
    private lateinit var submit: Button
    private lateinit var cancel: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pill)

        //variables
        title = findViewById(R.id.title)
        titulo = findViewById(R.id.titulo)
        tiempo = findViewById(R.id.tiempo)
        spinner = findViewById(R.id.spinner)
        submit = findViewById(R.id.submit)
        cancel = findViewById(R.id.cancel)

        //declarar el Spinner y su adaptador
        val adapter : ArrayAdapter<String> = ArrayAdapter(this,
            android.R.layout.simple_spinner_dropdown_item,opcion)
        spinner.adapter = adapter

        title.setText(getString(R.string.modifica))

        //los valores que provienen del Adapter para ver y modificar
        n = getIntent().getIntExtra("indice",0)
        var nombre = getIntent().getStringExtra("nombre")
        var tiemp = getIntent().getStringExtra("tiempo").toString()
        time = tiemp.toInt()
        var des = getIntent().getStringExtra("horasOminutos")

        titulo.setText(nombre)
        tiempo.setText(tiemp)

        //elegir la seleccion del spinner
        spinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if (p0 != null) {
                    des = p0.getItemAtPosition(p2).toString()
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                if (p0 != null)
                    des = p0.getItemAtPosition(0).toString()
            }

        }

        submit.setOnClickListener { submit(nombre.toString(),time,des.toString(),n) }
        cancel.setOnClickListener { cancel() }

    }

    //volver a MainActivity
    private fun cancel(){
        val intent = Intent(this, MainActivity::class.java)
        // start MainActivity
        startActivity(intent)
        finish()
    }

    //confirmar los cambios en la pildora seleccionada
    private fun submit(nomb : String, time : Int, des : String, n : Int){
        var remenber = Remenber(n,nomb,time,des)

        var constant = Constants()
        var sharedPreferences : SharedPreferences = getSharedPreferences(constant.SHARED_PREF_NAME
                ,Context.MODE_PRIVATE)
        var editor : SharedPreferences.Editor = sharedPreferences.edit()

        var gson = Gson()
        var json : String = gson.toJson(remenber)
        editor.putString(constant.PASS+n, json)

        editor.commit()

    }

}
