package com.healthapp.reminders;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000V\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J \u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u00062\u0006\u0010\u0010\u001a\u00020\u0011H\u0002J\u000e\u0010\u0012\u001a\u00020\u00132\u0006\u0010\r\u001a\u00020\u000eJ\'\u0010\u0014\u001a\u0004\u0018\u00010\u00152\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u00062\u0006\u0010\u0016\u001a\u00020\u0015H\u0002\u00a2\u0006\u0002\u0010\u0017J\u0014\u0010\u0018\u001a\u0004\u0018\u00010\u00192\b\u0010\u001a\u001a\u0004\u0018\u00010\u0006H\u0002J\u001c\u0010\u001b\u001a\u000e\u0012\u0004\u0012\u00020\u0011\u0012\u0004\u0012\u00020\u00110\u001c2\u0006\u0010\u000f\u001a\u00020\u0006H\u0002J\u001e\u0010\u001d\u001a\u00020\u00132\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u00062\u0006\u0010\u001e\u001a\u00020\u0015J\u000e\u0010\u001f\u001a\u00020\u00132\u0006\u0010\r\u001a\u00020\u000eR\u000e\u0010\u0005\u001a\u00020\u0006X\u0082D\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006 "}, d2 = {"Lcom/healthapp/reminders/ReminderAlarmScheduler;", "", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "TAG", "", "alarmManager", "Landroid/app/AlarmManager;", "dateFormatter", "Ljava/text/SimpleDateFormat;", "buildPendingIntent", "Landroid/app/PendingIntent;", "reminder", "Lcom/healthapp/screens/Reminder;", "time", "flags", "", "cancelReminder", "", "nextTriggerMillis", "", "referenceMillis", "(Lcom/healthapp/screens/Reminder;Ljava/lang/String;J)Ljava/lang/Long;", "parseDate", "Ljava/util/Date;", "date", "parseTime", "Lkotlin/Pair;", "scheduleNextOccurrence", "fromMillis", "scheduleReminder", "app_debug"})
public final class ReminderAlarmScheduler {
    @org.jetbrains.annotations.NotNull
    private final android.content.Context context = null;
    @org.jetbrains.annotations.NotNull
    private final android.app.AlarmManager alarmManager = null;
    @org.jetbrains.annotations.NotNull
    private final java.text.SimpleDateFormat dateFormatter = null;
    @org.jetbrains.annotations.NotNull
    private final java.lang.String TAG = "ReminderAlarmScheduler";
    
    public ReminderAlarmScheduler(@org.jetbrains.annotations.NotNull
    android.content.Context context) {
        super();
    }
    
    public final void scheduleReminder(@org.jetbrains.annotations.NotNull
    com.healthapp.screens.Reminder reminder) {
    }
    
    public final void cancelReminder(@org.jetbrains.annotations.NotNull
    com.healthapp.screens.Reminder reminder) {
    }
    
    public final void scheduleNextOccurrence(@org.jetbrains.annotations.NotNull
    com.healthapp.screens.Reminder reminder, @org.jetbrains.annotations.NotNull
    java.lang.String time, long fromMillis) {
    }
    
    private final java.lang.Long nextTriggerMillis(com.healthapp.screens.Reminder reminder, java.lang.String time, long referenceMillis) {
        return null;
    }
    
    private final kotlin.Pair<java.lang.Integer, java.lang.Integer> parseTime(java.lang.String time) {
        return null;
    }
    
    private final android.app.PendingIntent buildPendingIntent(com.healthapp.screens.Reminder reminder, java.lang.String time, int flags) {
        return null;
    }
    
    private final java.util.Date parseDate(java.lang.String date) {
        return null;
    }
}