package com.devcraft.freesignalsfortrading.notificationManager

import android.app.PendingIntent
import android.content.Context
import android.media.RingtoneManager
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.devcraft.freesignalsfortrading.MainActivity
import com.devcraft.freesignalsfortrading.R
import com.devcraft.freesignalsfortrading.app.Constants
import java.util.*
import kotlin.collections.ArrayList

class WorkService(val context: Context, val params: WorkerParameters) : Worker(context, params) {

    private var preferences = context.getSharedPreferences("ForNotify", Context.MODE_PRIVATE)
    private val vm: Fetch = Fetch()
    private lateinit var listContent: ArrayList<String>

    override fun doWork(): Result {
        showNotification()
        return Result.success()
    }

    private fun showNotification() {
        val list = vm.loadData(Constants.q)
        Thread.sleep(5000)
        val e = list.value!!
        for (value in e) {
            if (preferences
                    .contains(
                        value.symbol
                    )
            ) {


                val arrData = preferences
                    .getString(value.symbol, "")

                if (!arrData?.isEmpty()!!) {
                    listContent =
                        arrData.split(";") as ArrayList<String>
                }
                if (listContent[0].toDouble() < value.close) {
                    val intent = MainActivity
                        .newIntent(context)

                    val pendingIntent = PendingIntent
                        .getActivity(context, 0, intent, 0)

                    val notification = NotificationCompat
                        .Builder(context, "NOTIFICATION_CHANNEL")
                        .setTicker("Notify")
                        .setSmallIcon(R.drawable.ic_notifications)
                        .setContentTitle(
                            "Value " + value.symbol.replaceRange(
                                3,
                                3,
                                "/"
                            ) + " was changed"
                        )
                        .setContentText(listContent[2])
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(false)


                    val sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
                    notification.setSound(sound)
                    val vibrate = longArrayOf(0, 100, 200, 300)
                    notification.setVibrate(vibrate)

                    val notificationManager = NotificationManagerCompat.from(context)
                    notificationManager
                        .notify(
                            Date()
                                .time.toInt(), notification.build()
                        )
                }
            }
        }
    }

}