package com.healthapp.data;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\'\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0003\u001a\u00020\u0004H&J\b\u0010\u0005\u001a\u00020\u0006H&J\b\u0010\u0007\u001a\u00020\bH&\u00a8\u0006\t"}, d2 = {"Lcom/healthapp/data/HealthDatabase;", "Landroidx/room/RoomDatabase;", "()V", "reminderDao", "Lcom/healthapp/data/ReminderDao;", "uploadedFileDao", "Lcom/healthapp/data/UploadedFileDao;", "vaccinationDao", "Lcom/healthapp/data/VaccinationDao;", "app_debug"})
@androidx.room.Database(entities = {com.healthapp.data.VaccinationEntity.class, com.healthapp.data.UploadedFileEntity.class, com.healthapp.data.ReminderEntity.class}, version = 2, exportSchema = false)
public abstract class HealthDatabase extends androidx.room.RoomDatabase {
    
    public HealthDatabase() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull
    public abstract com.healthapp.data.VaccinationDao vaccinationDao();
    
    @org.jetbrains.annotations.NotNull
    public abstract com.healthapp.data.UploadedFileDao uploadedFileDao();
    
    @org.jetbrains.annotations.NotNull
    public abstract com.healthapp.data.ReminderDao reminderDao();
}