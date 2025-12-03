package com.healthapp.data;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SuppressWarnings({"unchecked", "deprecation"})
public final class HealthDatabase_Impl extends HealthDatabase {
  private volatile VaccinationDao _vaccinationDao;

  private volatile UploadedFileDao _uploadedFileDao;

  private volatile ReminderDao _reminderDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(2) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `vaccinations` (`id` TEXT NOT NULL, `vaccineName` TEXT NOT NULL, `doseNumber` TEXT NOT NULL, `dateAdministered` TEXT NOT NULL, `nextDueDate` TEXT NOT NULL, `administeredBy` TEXT NOT NULL, `location` TEXT NOT NULL, `batchNumber` TEXT NOT NULL, `notes` TEXT NOT NULL, `createdAt` TEXT NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `uploaded_files` (`id` TEXT NOT NULL, `name` TEXT NOT NULL, `type` TEXT NOT NULL, `uploadDate` TEXT NOT NULL, `size` TEXT NOT NULL, `url` TEXT NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `reminders` (`id` TEXT NOT NULL, `medicineName` TEXT NOT NULL, `dosage` TEXT NOT NULL, `timesCsv` TEXT NOT NULL, `phoneNumber` TEXT NOT NULL, `sendSms` INTEGER NOT NULL, `active` INTEGER NOT NULL, `durationType` TEXT NOT NULL, `startDate` TEXT NOT NULL, `endDate` TEXT NOT NULL, `createdAt` TEXT NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, 'cd7d4d9851c9fa77615b6ad69a875b63')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `vaccinations`");
        db.execSQL("DROP TABLE IF EXISTS `uploaded_files`");
        db.execSQL("DROP TABLE IF EXISTS `reminders`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsVaccinations = new HashMap<String, TableInfo.Column>(10);
        _columnsVaccinations.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsVaccinations.put("vaccineName", new TableInfo.Column("vaccineName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsVaccinations.put("doseNumber", new TableInfo.Column("doseNumber", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsVaccinations.put("dateAdministered", new TableInfo.Column("dateAdministered", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsVaccinations.put("nextDueDate", new TableInfo.Column("nextDueDate", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsVaccinations.put("administeredBy", new TableInfo.Column("administeredBy", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsVaccinations.put("location", new TableInfo.Column("location", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsVaccinations.put("batchNumber", new TableInfo.Column("batchNumber", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsVaccinations.put("notes", new TableInfo.Column("notes", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsVaccinations.put("createdAt", new TableInfo.Column("createdAt", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysVaccinations = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesVaccinations = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoVaccinations = new TableInfo("vaccinations", _columnsVaccinations, _foreignKeysVaccinations, _indicesVaccinations);
        final TableInfo _existingVaccinations = TableInfo.read(db, "vaccinations");
        if (!_infoVaccinations.equals(_existingVaccinations)) {
          return new RoomOpenHelper.ValidationResult(false, "vaccinations(com.healthapp.data.VaccinationEntity).\n"
                  + " Expected:\n" + _infoVaccinations + "\n"
                  + " Found:\n" + _existingVaccinations);
        }
        final HashMap<String, TableInfo.Column> _columnsUploadedFiles = new HashMap<String, TableInfo.Column>(6);
        _columnsUploadedFiles.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUploadedFiles.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUploadedFiles.put("type", new TableInfo.Column("type", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUploadedFiles.put("uploadDate", new TableInfo.Column("uploadDate", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUploadedFiles.put("size", new TableInfo.Column("size", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUploadedFiles.put("url", new TableInfo.Column("url", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysUploadedFiles = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesUploadedFiles = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoUploadedFiles = new TableInfo("uploaded_files", _columnsUploadedFiles, _foreignKeysUploadedFiles, _indicesUploadedFiles);
        final TableInfo _existingUploadedFiles = TableInfo.read(db, "uploaded_files");
        if (!_infoUploadedFiles.equals(_existingUploadedFiles)) {
          return new RoomOpenHelper.ValidationResult(false, "uploaded_files(com.healthapp.data.UploadedFileEntity).\n"
                  + " Expected:\n" + _infoUploadedFiles + "\n"
                  + " Found:\n" + _existingUploadedFiles);
        }
        final HashMap<String, TableInfo.Column> _columnsReminders = new HashMap<String, TableInfo.Column>(11);
        _columnsReminders.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReminders.put("medicineName", new TableInfo.Column("medicineName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReminders.put("dosage", new TableInfo.Column("dosage", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReminders.put("timesCsv", new TableInfo.Column("timesCsv", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReminders.put("phoneNumber", new TableInfo.Column("phoneNumber", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReminders.put("sendSms", new TableInfo.Column("sendSms", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReminders.put("active", new TableInfo.Column("active", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReminders.put("durationType", new TableInfo.Column("durationType", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReminders.put("startDate", new TableInfo.Column("startDate", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReminders.put("endDate", new TableInfo.Column("endDate", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReminders.put("createdAt", new TableInfo.Column("createdAt", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysReminders = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesReminders = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoReminders = new TableInfo("reminders", _columnsReminders, _foreignKeysReminders, _indicesReminders);
        final TableInfo _existingReminders = TableInfo.read(db, "reminders");
        if (!_infoReminders.equals(_existingReminders)) {
          return new RoomOpenHelper.ValidationResult(false, "reminders(com.healthapp.data.ReminderEntity).\n"
                  + " Expected:\n" + _infoReminders + "\n"
                  + " Found:\n" + _existingReminders);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "cd7d4d9851c9fa77615b6ad69a875b63", "0f7aa9174d919285a5d7f3fadda0f6b5");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "vaccinations","uploaded_files","reminders");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `vaccinations`");
      _db.execSQL("DELETE FROM `uploaded_files`");
      _db.execSQL("DELETE FROM `reminders`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(VaccinationDao.class, VaccinationDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(UploadedFileDao.class, UploadedFileDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(ReminderDao.class, ReminderDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public VaccinationDao vaccinationDao() {
    if (_vaccinationDao != null) {
      return _vaccinationDao;
    } else {
      synchronized(this) {
        if(_vaccinationDao == null) {
          _vaccinationDao = new VaccinationDao_Impl(this);
        }
        return _vaccinationDao;
      }
    }
  }

  @Override
  public UploadedFileDao uploadedFileDao() {
    if (_uploadedFileDao != null) {
      return _uploadedFileDao;
    } else {
      synchronized(this) {
        if(_uploadedFileDao == null) {
          _uploadedFileDao = new UploadedFileDao_Impl(this);
        }
        return _uploadedFileDao;
      }
    }
  }

  @Override
  public ReminderDao reminderDao() {
    if (_reminderDao != null) {
      return _reminderDao;
    } else {
      synchronized(this) {
        if(_reminderDao == null) {
          _reminderDao = new ReminderDao_Impl(this);
        }
        return _reminderDao;
      }
    }
  }
}
