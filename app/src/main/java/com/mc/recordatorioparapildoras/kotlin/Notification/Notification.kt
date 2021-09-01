package com.mc.recordatorioparapildoras.kotlin.Notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

import com.mc.recordatorioparapildoras.kotlin.R

import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build


class Notification : BroadcastReceiver(){
    //clase para enviar notificaciones, totalmente lista
    //solo hay que invocarla y enviar los parámetros
    //para usarlo en otra App, añadir esto al Manifest: <receiver android:name=".Notification.Notification"/>
    var mensaje : String = ""
    var id : Int = 0

    override fun onReceive(context: Context, intent: Intent) {
        var titulo : String = context.getString(R.string.recordador)

        var dontforg : String = context.getString(R.string.dontforget)+" "
        mensaje = dontforg + intent.getStringExtra("texto").toString()
        var iD : String = intent.getStringExtra("ID").toString()
        //var value : Int = iD.toInt()
        id  = iD.toInt()

        var notificationManager : NotificationManager
        var notificationChannel : NotificationChannel
        var builder : Notification.Builder

        notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val pendingIntent = PendingIntent.getActivity(context,0,intent,PendingIntent.FLAG_UPDATE_CURRENT)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel = NotificationChannel(iD,mensaje,NotificationManager.IMPORTANCE_HIGH)
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.GREEN
            notificationChannel.enableVibration(false)
            notificationManager.createNotificationChannel(notificationChannel)

            builder = Notification.Builder(context,iD)
                .setContentTitle(titulo)
                .setContentText(mensaje)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(context.resources,R.mipmap.ic_launcher))
                .setContentIntent(pendingIntent)
        }else{
            builder = Notification.Builder(context)
                .setContentTitle(titulo)
                .setContentText(mensaje)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(context.resources,R.mipmap.ic_launcher))
                .setContentIntent(pendingIntent)
        }

        notificationManager.notify(id,builder.build())

    }


}