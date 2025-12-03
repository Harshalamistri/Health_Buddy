package com.healthapp.data;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@SuppressWarnings({"unchecked", "deprecation"})
public final class ReminderDao_Impl implements ReminderDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<ReminderEntity> __insertionAdapterOfReminderEntity;

  private final EntityDeletionOrUpdateAdapter<ReminderEntity> __updateAdapterOfReminderEntity;

  private final SharedSQLiteStatement __preparedStmtOfDelete;

  public ReminderDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfReminderEntity = new EntityInsertionAdapter<ReminderEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `reminders` (`id`,`medicineName`,`dosage`,`timesCsv`,`phoneNumber`,`sendSms`,`active`,`durationType`,`startDate`,`endDate`,`createdAt`) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ReminderEntity entity) {
        if (entity.getId() == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.getId());
        }
        if (entity.getMedicineName() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getMedicineName());
        }
        if (entity.getDosage() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getDosage());
        }
        if (entity.getTimesCsv() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getTimesCsv());
        }
        if (entity.getPhoneNumber() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getPhoneNumber());
        }
        final int _tmp = entity.getSendSms() ? 1 : 0;
        statement.bindLong(6, _tmp);
        final int _tmp_1 = entity.getActive() ? 1 : 0;
        statement.bindLong(7, _tmp_1);
        if (entity.getDurationType() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getDurationType());
        }
        if (entity.getStartDate() == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, entity.getStartDate());
        }
        if (entity.getEndDate() == null) {
          statement.bindNull(10);
        } else {
          statement.bindString(10, entity.getEndDate());
        }
        if (entity.getCreatedAt() == null) {
          statement.bindNull(11);
        } else {
          statement.bindString(11, entity.getCreatedAt());
        }
      }
    };
    this.__updateAdapterOfReminderEntity = new EntityDeletionOrUpdateAdapter<ReminderEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `reminders` SET `id` = ?,`medicineName` = ?,`dosage` = ?,`timesCsv` = ?,`phoneNumber` = ?,`sendSms` = ?,`active` = ?,`durationType` = ?,`startDate` = ?,`endDate` = ?,`createdAt` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ReminderEntity entity) {
        if (entity.getId() == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.getId());
        }
        if (entity.getMedicineName() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getMedicineName());
        }
        if (entity.getDosage() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getDosage());
        }
        if (entity.getTimesCsv() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getTimesCsv());
        }
        if (entity.getPhoneNumber() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getPhoneNumber());
        }
        final int _tmp = entity.getSendSms() ? 1 : 0;
        statement.bindLong(6, _tmp);
        final int _tmp_1 = entity.getActive() ? 1 : 0;
        statement.bindLong(7, _tmp_1);
        if (entity.getDurationType() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getDurationType());
        }
        if (entity.getStartDate() == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, entity.getStartDate());
        }
        if (entity.getEndDate() == null) {
          statement.bindNull(10);
        } else {
          statement.bindString(10, entity.getEndDate());
        }
        if (entity.getCreatedAt() == null) {
          statement.bindNull(11);
        } else {
          statement.bindString(11, entity.getCreatedAt());
        }
        if (entity.getId() == null) {
          statement.bindNull(12);
        } else {
          statement.bindString(12, entity.getId());
        }
      }
    };
    this.__preparedStmtOfDelete = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM reminders WHERE id = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insert(final ReminderEntity reminder,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfReminderEntity.insert(reminder);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object update(final ReminderEntity reminder,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfReminderEntity.handle(reminder);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object delete(final String id, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDelete.acquire();
        int _argIndex = 1;
        if (id == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, id);
        }
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDelete.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<ReminderEntity>> getAll() {
    final String _sql = "SELECT * FROM reminders ORDER BY createdAt DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"reminders"}, new Callable<List<ReminderEntity>>() {
      @Override
      @NonNull
      public List<ReminderEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfMedicineName = CursorUtil.getColumnIndexOrThrow(_cursor, "medicineName");
          final int _cursorIndexOfDosage = CursorUtil.getColumnIndexOrThrow(_cursor, "dosage");
          final int _cursorIndexOfTimesCsv = CursorUtil.getColumnIndexOrThrow(_cursor, "timesCsv");
          final int _cursorIndexOfPhoneNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "phoneNumber");
          final int _cursorIndexOfSendSms = CursorUtil.getColumnIndexOrThrow(_cursor, "sendSms");
          final int _cursorIndexOfActive = CursorUtil.getColumnIndexOrThrow(_cursor, "active");
          final int _cursorIndexOfDurationType = CursorUtil.getColumnIndexOrThrow(_cursor, "durationType");
          final int _cursorIndexOfStartDate = CursorUtil.getColumnIndexOrThrow(_cursor, "startDate");
          final int _cursorIndexOfEndDate = CursorUtil.getColumnIndexOrThrow(_cursor, "endDate");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final List<ReminderEntity> _result = new ArrayList<ReminderEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ReminderEntity _item;
            final String _tmpId;
            if (_cursor.isNull(_cursorIndexOfId)) {
              _tmpId = null;
            } else {
              _tmpId = _cursor.getString(_cursorIndexOfId);
            }
            final String _tmpMedicineName;
            if (_cursor.isNull(_cursorIndexOfMedicineName)) {
              _tmpMedicineName = null;
            } else {
              _tmpMedicineName = _cursor.getString(_cursorIndexOfMedicineName);
            }
            final String _tmpDosage;
            if (_cursor.isNull(_cursorIndexOfDosage)) {
              _tmpDosage = null;
            } else {
              _tmpDosage = _cursor.getString(_cursorIndexOfDosage);
            }
            final String _tmpTimesCsv;
            if (_cursor.isNull(_cursorIndexOfTimesCsv)) {
              _tmpTimesCsv = null;
            } else {
              _tmpTimesCsv = _cursor.getString(_cursorIndexOfTimesCsv);
            }
            final String _tmpPhoneNumber;
            if (_cursor.isNull(_cursorIndexOfPhoneNumber)) {
              _tmpPhoneNumber = null;
            } else {
              _tmpPhoneNumber = _cursor.getString(_cursorIndexOfPhoneNumber);
            }
            final boolean _tmpSendSms;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfSendSms);
            _tmpSendSms = _tmp != 0;
            final boolean _tmpActive;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfActive);
            _tmpActive = _tmp_1 != 0;
            final String _tmpDurationType;
            if (_cursor.isNull(_cursorIndexOfDurationType)) {
              _tmpDurationType = null;
            } else {
              _tmpDurationType = _cursor.getString(_cursorIndexOfDurationType);
            }
            final String _tmpStartDate;
            if (_cursor.isNull(_cursorIndexOfStartDate)) {
              _tmpStartDate = null;
            } else {
              _tmpStartDate = _cursor.getString(_cursorIndexOfStartDate);
            }
            final String _tmpEndDate;
            if (_cursor.isNull(_cursorIndexOfEndDate)) {
              _tmpEndDate = null;
            } else {
              _tmpEndDate = _cursor.getString(_cursorIndexOfEndDate);
            }
            final String _tmpCreatedAt;
            if (_cursor.isNull(_cursorIndexOfCreatedAt)) {
              _tmpCreatedAt = null;
            } else {
              _tmpCreatedAt = _cursor.getString(_cursorIndexOfCreatedAt);
            }
            _item = new ReminderEntity(_tmpId,_tmpMedicineName,_tmpDosage,_tmpTimesCsv,_tmpPhoneNumber,_tmpSendSms,_tmpActive,_tmpDurationType,_tmpStartDate,_tmpEndDate,_tmpCreatedAt);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
