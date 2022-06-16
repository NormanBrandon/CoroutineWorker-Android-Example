package com.nprmanbrandons11.myapplication

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.widget.ImageView
import androidx.core.app.NotificationCompat
import androidx.work.*
import com.bumptech.glide.Glide
import kotlinx.coroutines.runBlocking
import java.lang.Exception

class DocumentWorker(val context:Context,params: WorkerParameters):CoroutineWorker(context,params) {
    companion object{
        const val KEY_ID_NOTIFICATION = "KEY_ID_NOTIFICATION"
        const val KEY_USUARIO = "KEY_USUARIO"

    }
    override suspend fun doWork(): Result {

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val idNotification:Int = inputData.getInt(KEY_ID_NOTIFICATION,0
        )
        return try{
            createChannelAndNotification(notificationInfo = NotificationInfo("DownLoading photo","Your image is downloading"),id = idNotification,notificationManager = notificationManager,isLoading = true)

            val photoRequest = InsertDocumentRequest()
            photoRequest.base64 = "photo"
            val result = DroidApp.getRetrofitUpload().getPhoto()
            runBlocking { Thread.sleep(5000) }

            if(result.isSuccessful){
                notificationManager.cancel("Hello",idNotification)
                createChannelAndNotification(notificationInfo = NotificationInfo("Success","Download successful"),id = idNotification,notificationManager = notificationManager,isLoading = false)

                val data = Data.Builder().putString("WORKER_RESULT",result.body()?.message?:"").build()
                Result.success(data)
            }
            else{
                notificationManager.cancel("Hello",idNotification)
                createChannelAndNotification(notificationInfo = NotificationInfo("Error","An error ocurred downloading"),id = idNotification,notificationManager = notificationManager,isLoading = false)

                Result.failure()
            }
        }
        catch (e:Exception){
            e.printStackTrace()
            Result.failure()
        }

    }

    private fun createChannelAndNotification(namePhoto:String ="Photo.jpg", notificationInfo:NotificationInfo, id:Int, notificationManager: NotificationManager, isLoading:Boolean){
        val notification = buildNotification(notificationInfo.title,notificationInfo.message,R.drawable.ic_launcher_background,isLoading)
         if(Build.VERSION.SDK_INT >= 26){
            notificationManager.createNotificationChannel(
                NotificationChannel(
                    "CHANNEL_ID",
                    "CHANNEL_NAME",
                    NotificationManager.IMPORTANCE_HIGH
                )
            )
         }
        notificationManager.notify(namePhoto,id,notification)
    }

    private fun buildNotification(title: String,message: String,icon:Int,isLoading: Boolean):Notification{
        val notification = NotificationCompat.Builder(context,"CHANNEL_ID")
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(icon)
            .setStyle(
                NotificationCompat.BigTextStyle().bigText(message)
            )
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
        if(isLoading){
            notification.apply {
                setAutoCancel(false)
                setProgress(100,0,true)
                setOngoing(true)
                priority = NotificationCompat.PRIORITY_MAX
            }
        }
        return notification.build()
    }
    private data class NotificationInfo(
        val title:String,
        val message:String
    )

}