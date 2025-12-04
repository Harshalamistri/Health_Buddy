package com.healthapp.reminders

import android.Manifest
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.app.TaskStackBuilder
import com.healthapp.MainActivity
import com.healthapp.R
import com.healthapp.screens.Reminder
import java.text.SimpleDateFormat
import java.util.Locale

class MedicineReminderReceiver : BroadcastReceiver() {
    private val TAG = "MedicineReminderReceiver"

    override fun onReceive(context: Context, intent: Intent) {
        val reminderId = intent.getStringExtra("reminder_id") ?: return
        val medicineName = intent.getStringExtra("medicine_name") ?: "Medicine reminder"
        val dosage = intent.getStringExtra("dosage").orEmpty()
        val time = intent.getStringExtra("time") ?: ""
        val durationType = intent.getStringExtra("duration_type") ?: "everyday"
        val startDate = intent.getStringExtra("start_date") ?: ""
        val endDate = intent.getStringExtra("end_date") ?: ""

        Log.d(TAG, "Received reminder: $reminderId - $medicineName at $time")

        ReminderNotificationHelper.createChannel(context)

        val title = "Time for your medicine"
        val content = if (dosage.isBlank()) medicineName else "$medicineName - $dosage"

        if (canShowNotification(context)) {
            val launchIntent = Intent(context, MainActivity::class.java)
            val contentIntent = TaskStackBuilder.create(context).run {
                addNextIntentWithParentStack(launchIntent)
                getPendingIntent(
                    reminderId.hashCode(),
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
            }

            val notification = NotificationCompat.Builder(context, ReminderNotificationHelper.CHANNEL_ID)
                .setSmallIcon(R.drawable.appicon)
                .setContentTitle(title)
                .setContentText(content)
                .setStyle(NotificationCompat.BigTextStyle().bigText(content))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setContentIntent(contentIntent)
                .build()

            NotificationManagerCompat.from(context).notify((reminderId + time).hashCode(), notification)
            Log.d(TAG, "Notification shown for reminder: $reminderId")
        } else {
            Log.d(TAG, "Notification not shown for reminder: $reminderId - permission not granted")
        }

        // Schedule next occurrence for this reminder/time if still within its window
        val scheduler = ReminderAlarmScheduler(context)
        scheduler.scheduleNextOccurrence(
            reminder = Reminder(
                id = reminderId,
                medicineName = medicineName,
                dosage = dosage,
                times = listOf(time),
                phoneNumber = "",
                sendSms = false,
                active = true,
                durationType = durationType,
                startDate = startDate,
                endDate = endDate,
                createdAt = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(java.util.Date())
            ),
            time = time,
            fromMillis = System.currentTimeMillis()
        )
    }

    private fun canShowNotification(context: Context): Boolean {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
            ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
    }
}
