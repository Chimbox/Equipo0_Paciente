package felix.alfonso.equipo0_paciente

import android.R
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import felix.alfonso.equipo0_paciente.dominio.Solicitud


class NotificationService : FirebaseMessagingService() {

    fun obtenerToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                return@OnCompleteListener
            }

            val token = task.result

            Log.d("TOKENFIREBASE", token)
        })
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        if (remoteMessage.data.isNotEmpty()) {
            var data = remoteMessage.data
            var gson: Gson = GsonBuilder().create()

            println("SOLICITUD: " + gson.toJson(data))
            var solicitud=gson.fromJson(gson.toJson(data), Solicitud::class.java)

            MainActivity.lstSolicitudes.add(solicitud)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                showNotification(solicitud.body)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun showNotification(body:String) {
        val intent = Intent(applicationContext, MainActivity::class.java)
        val CHANNEL_ID = "MYCHANNEL"
        val notificationChannel =
            NotificationChannel(CHANNEL_ID, "name", NotificationManager.IMPORTANCE_HIGH)
        val pendingIntent = PendingIntent.getActivity(applicationContext, 1, intent, 0)
        val notification: Notification = Notification.Builder(applicationContext, CHANNEL_ID)
            .setContentText(body)
            .setContentTitle("Revisión de expediente")
            .setContentIntent(pendingIntent)
            .addAction(R.drawable.sym_action_chat, "Nueva solicitud", pendingIntent)
            .setChannelId(CHANNEL_ID)
            .setSmallIcon(R.drawable.sym_action_chat)
            .build()

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(notificationChannel)
        notificationManager.notify(1, notification)
    }

}