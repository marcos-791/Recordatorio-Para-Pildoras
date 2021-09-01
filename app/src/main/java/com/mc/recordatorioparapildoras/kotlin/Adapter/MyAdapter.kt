package com.mc.recordatorioparapildoras.kotlin.Adapter

import android.app.Activity
import android.app.AlarmManager
import android.app.AlertDialog
import android.app.PendingIntent
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.provider.SyncStateContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.mc.recordatorioparapildoras.kotlin.AddPillActivity
import com.mc.recordatorioparapildoras.kotlin.Constants.Constants
import com.mc.recordatorioparapildoras.kotlin.MainActivity
import com.mc.recordatorioparapildoras.kotlin.ModifyActivity
import com.mc.recordatorioparapildoras.kotlin.Notification.Notification
import com.mc.recordatorioparapildoras.kotlin.R


class MyAdapter(val context: Context,private val lista : ArrayList<Remenber>) : RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val itemview = LayoutInflater.from(parent.context).inflate(R.layout.models,
            parent,false)
        return MyViewHolder(itemview)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        //recupera los valores de cada elemento de la lista
        val rembender = lista[position]

        holder.indice.setText(""+(position+1))
        holder.titulo.setText(rembender.nombre)
        holder.numero.setText(""+rembender.tiempo)
        holder.tiempo.setText(rembender.horasOminutos)

        holder.mod.setOnClickListener { modificar(rembender.indice,rembender.nombre,rembender.tiempo,rembender.horasOminutos) }
        holder.borrar.setOnClickListener { mensaje(holder,rembender.indice) }

    }

    override fun getItemCount(): Int {
        return lista.size
    }

    //manda a ModifyActivity
    private fun modificar(indice : Int, nombre : String, tiempo : Int, horasOminutos : String){
        val intent = Intent(context, ModifyActivity::class.java)

        intent.putExtra("indice",indice)
        intent.putExtra("nombre",nombre)
        intent.putExtra("tiempo",tiempo.toString())
        intent.putExtra("horasOminutos",horasOminutos)

        // start ModifyActivity
        context.startActivity(intent)

        Activity().finish()
    }

    //mensaje para comprobar si el usuario realmente desea eliminar el elemento seleccionado
    private fun mensaje(hold : MyViewHolder, n : Int){
        val dialog = AlertDialog.Builder(context)
        dialog.setMessage(context.getString(R.string.desea_borrar))
            .setCancelable(false)
            .setPositiveButton(context.getString(R.string.si)){view, _-> removeItem(hold,n) }
            .setNegativeButton(context.getString(R.string.no)){view, _ -> view.dismiss() }
            .create()

        dialog.show()
    }

    //eliminar el elemento de la lista
    private fun removeItem(hold : MyViewHolder,n : Int){
        var newPos : Int = hold.adapterPosition
        lista.removeAt(newPos)
        notifyItemRemoved(newPos)
        notifyItemRangeChanged(newPos,lista.size)

        var constant = Constants()
        var sharedPreferences : SharedPreferences =
            context.getSharedPreferences(constant.SHARED_PREF_NAME,Context.MODE_PRIVATE)
        sharedPreferences.edit().remove(constant.PASS+n).commit()

        var alarmManager : AlarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        var updateServiceIntent : Intent = Intent(context,Notification::class.java)
        var pendingUpdateIntent : PendingIntent = PendingIntent.getBroadcast(context,n,updateServiceIntent,0)
        //cancel alarm
        alarmManager.cancel(pendingUpdateIntent)

        Activity().finish()
        val intent = Intent(context, MainActivity::class.java)
        // start MainActivity
        context.startActivity(intent)

        Toast.makeText(context,context.getString(R.string.borrado),Toast.LENGTH_SHORT).show()
    }

    //clase ViewHolder con los valores para el layout models
    class MyViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview){
        val indice : TextView = itemview.findViewById(R.id.indice)
        val titulo : TextView = itemview.findViewById(R.id.titulo)
        val numero : TextView = itemview.findViewById(R.id.numero)
        val tiempo : TextView = itemview.findViewById(R.id.tiempo)
        val mod : Button = itemview.findViewById(R.id.mod)
        val borrar : Button = itemview.findViewById(R.id.borrar)
    }

}