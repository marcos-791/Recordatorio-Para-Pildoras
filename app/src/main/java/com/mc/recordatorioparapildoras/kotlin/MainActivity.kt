package com.mc.recordatorioparapildoras.kotlin

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.ColorStateList
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson
import com.mc.recordatorioparapildoras.kotlin.Adapter.MyAdapter
import com.mc.recordatorioparapildoras.kotlin.Adapter.Remenber
import com.mc.recordatorioparapildoras.kotlin.Constants.Constants
import com.mc.recordatorioparapildoras.kotlin.Notification.Notification


class MainActivity : AppCompatActivity() {
    //intervalo de tiempo de dos segundos
    private val Interval = 2000;

    private var firstClick = 0L;
    private lateinit var fab : FloatingActionButton
    private lateinit var message : TextView
    private lateinit var recyclerView : RecyclerView
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var ModelArrayList : ArrayList<Remenber>

    private lateinit var adapt : MyAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //variables de la UI
        fab = findViewById(R.id.fab);
        recyclerView = findViewById(R.id.my_recycler_view)
        message = findViewById(R.id.message)

        //variables del RecyclerView
        linearLayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.setHasFixedSize(true)

        //cambia el color del ícono del botón fab a blanco
        fab.imageTintList= ColorStateList.valueOf(Color.rgb(255, 255, 255))

        //funcion que llena el Array
        getData()
        //declarar la variable adaptadora e ingresarla al RecyclerView
        adapt = MyAdapter(this,ModelArrayList)
        recyclerView.adapter = adapt

        //Comprobar si está vacío el adaptador. En caso de estarlo, mostrar el cartel
        //de que no hay píldoras nuevas
        message.isVisible = adapt.itemCount==0

        //manda a la función addPill que lleva a la actividad
        fab.setOnClickListener{addPill()}

    }

    //recupera los datos y es la función que llena la lista de la App
    private fun getData() {
        val sharedPreferences : SharedPreferences
        var n : Int
        val constante = Constants()

        sharedPreferences = getSharedPreferences(constante.SHARED_PREF_NAME, Context.MODE_PRIVATE)
        n = sharedPreferences.getInt(constante.SAVED, 0)
        ModelArrayList = arrayListOf()

        var i=0
        while (n>=0&&i<5){
            val response : String = sharedPreferences.getString(constante.PASS+n , "").toString()

            if (!response.equals("")) {
                val obj: Remenber = Gson().fromJson(response, Remenber::class.java)
                ModelArrayList.add(obj)
                i++
            }
            n -= 1
        }

    }

    //inicia la Actividad AddPillActivity
    private fun addPill(){
        val intent = Intent(this, AddPillActivity::class.java)
        // start AddPillActivity
        startActivity(intent)
        finish()
    }

    override fun onBackPressed() {
        //simplemente para salir de la App haciendo "back" dos veces seguidas
        if(firstClick+ Interval > System.currentTimeMillis()){
            super.onBackPressed()
            return
        }
        else{
            Toast.makeText(this,getString(R.string.press_again),Toast.LENGTH_SHORT).show()
        }
        firstClick = System.currentTimeMillis()
    }

}

