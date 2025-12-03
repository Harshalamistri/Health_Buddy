package com.healthapp.reminders

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.AlarmManagerCompat
import com.healthapp.screens.Reminder
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class ReminderAlarmScheduler(private val context: Context) {
    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    private val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    fun scheduleReminder(reminder: Reminder) {
        ReminderNotificationHelper.createChannel(context)
        reminder.times.forEach { time ->
            val triggerAt = nextTriggerMillis(reminder, time, System.currentTimeMillis())
            if (triggerAt != null) {
                val pendingIntent = buildPendingIntent(reminder, time, PendingIntent.FLAG_UPDATE_CURRENT)
                AlarmManagerCompat.setExactAndAllowWhileIdle(alarmManager, AlarmManager.RTC_WAKEUP, triggerAt, pendingIntent)
            }
        }
    }

    fun cancelReminder(reminder: Reminder) {
        reminder.times.forEach { time ->
            val pi = buildPendingIntent(reminder, time, PendingIntent.FLAG_UPDATE_CURRENT)
            alarmManager.cancel(pi)
            pi.cancel()
        }
    }

    fun scheduleNextOccurrence(reminder: Reminder, time: String, fromMillis: Long) {
        val next = nextTriggerMillis(reminder, time, fromMillis + 60_000L)
        if (next != null) {
            val pendingIntent = buildPendingIntent(reminder, time, PendingIntent.FLAG_UPDATE_CURRENT)
            AlarmManagerCompat.setExactAndAllowWhileIdle(alarmManager, AlarmManager.RTC_WAKEUP, next, pendingIntent)
        }
    }

    private fun nextTriggerMillis(reminder: Reminder, time: String, referenceMillis: Long): Long? {
        val startDate = parseDate(reminder.startDate) ?: Date(referenceMillis)
        val endDate = parseDate(reminder.endDate)
        val cal = Calendar.getInstance()
        cal.timeInMillis = referenceMillis

        // Start from startDate if we haven't reached it yet
        if (cal.time.before(startDate)) {
            cal.time = startDate
        }

        val (hour, minute) = parseTime(time)
        cal.set(Calendar.HOUR_OF_DAY, hour)
        cal.set(Calendar.MINUTE, minute)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)

        if (cal.timeInMillis <= referenceMillis) {
            cal.add(Calendar.DAY_OF_YEAR, 1)
        }

        // Move forward until within allowed window
        while (true) {
            if (endDate != null && cal.time.after(endDate)) return null
            if (!cal.time.before(startDate)) return cal.timeInMillis
            cal.add(Calendar.DAY_OF_YEAR, 1)
        }
    }

    private fun parseTime(time: String): Pair<Int, Int> {
        val parts = time.split(":")
        val hour = parts.getOrNull(0)?.toIntOrNull()?.coerceIn(0, 23) ?: 8
        val minute = parts.getOrNull(1)?.toIntOrNull()?.coerceIn(0, 59) ?: 0
        return hour to minute
    }

    private fun buildPendingIntent(reminder: Reminder, time: String, flags: Int): PendingIntent {
        val requestCode = (reminder.id + time).hashCode()
        val intent = Intent(context, MedicineReminderReceiver::class.java).apply {
            action = "com.healthapp.reminder.ACTION_REMIND"
            putExtra("reminder_id", reminder.id)
            putExtra("medicine_name", reminder.medicineName)
            putExtra("dosage", reminder.dosage)
            putExtra("time", time)
            putExtra("duration_type", reminder.durationType)
            putExtra("start_date", reminder.startDate)
            putExtra("end_date", reminder.endDate)
        }
        val baseFlags = PendingIntent.FLAG_IMMUTABLE
        return PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            baseFlags or flags
        )
    }

    private fun parseDate(date: String?): Date? {
        if (date.isNullOrBlank()) return null
        return runCatching { dateFormatter.parse(date) }.getOrNull()
    }
}
