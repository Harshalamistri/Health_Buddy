package com.healthapp.reminders

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.healthapp.data.AppDatabaseProvider
import com.healthapp.data.ReminderEntity
import com.healthapp.screens.Reminder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == "android.intent.action.BOOT_COMPLETED") {
            val reminderDao = AppDatabaseProvider.getInstance(context).reminderDao()
            val scheduler = ReminderAlarmScheduler(context)

            CoroutineScope(Dispatchers.IO).launch {
                val reminders = reminderDao.getAllSuspend()
                reminders.forEach { reminder ->
                    if (reminder.active) {
                        scheduler.scheduleReminder(reminder.toModel())
                    }
                }
            }
        }
    }
}

private fun ReminderEntity.toModel() = Reminder(
    id = id,
    medicineName = medicineName,
    dosage = dosage,
    times = timesCsv.split(",").filter { it.isNotBlank() },
    phoneNumber = phoneNumber,
    sendSms = sendSms,
    active = active,
    durationType = durationType,
    startDate = startDate,
    endDate = endDate,
    createdAt = createdAt
)