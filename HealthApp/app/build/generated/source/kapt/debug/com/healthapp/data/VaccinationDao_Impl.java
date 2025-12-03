package com.healthapp.data;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
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
public final class VaccinationDao_Impl implements VaccinationDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<VaccinationEntity> __insertionAdapterOfVaccinationEntity;

  private final SharedSQLiteStatement __preparedStmtOfDelete;

  public VaccinationDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfVaccinationEntity = new EntityInsertionAdapter<VaccinationEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `vaccinations` (`id`,`vaccineName`,`doseNumber`,`dateAdministered`,`nextDueDate`,`administeredBy`,`location`,`batchNumber`,`notes`,`createdAt`) VALUES (?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final VaccinationEntity entity) {
        if (entity.getId() == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.getId());
        }
        if (entity.getVaccineName() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getVaccineName());
        }
        if (entity.getDoseNumber() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getDoseNumber());
        }
        if (entity.getDateAdministered() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getDateAdministered());
        }
        if (entity.getNextDueDate() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getNextDueDate());
        }
        if (entity.getAdministeredBy() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getAdministeredBy());
        }
        if (entity.getLocation() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getLocation());
        }
        if (entity.getBatchNumber() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getBatchNumber());
        }
        if (entity.getNotes() == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, entity.getNotes());
        }
        if (entity.getCreatedAt() == null) {
          statement.bindNull(10);
        } else {
          statement.bindString(10, entity.getCreatedAt());
        }
      }
    };
    this.__preparedStmtOfDelete = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM vaccinations WHERE id = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insert(final VaccinationEntity vaccination,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfVaccinationEntity.insert(vaccination);
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
  public Flow<List<VaccinationEntity>> getAll() {
    final String _sql = "SELECT * FROM vaccinations ORDER BY createdAt DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"vaccinations"}, new Callable<List<VaccinationEntity>>() {
      @Override
      @NonNull
      public List<VaccinationEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfVaccineName = CursorUtil.getColumnIndexOrThrow(_cursor, "vaccineName");
          final int _cursorIndexOfDoseNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "doseNumber");
          final int _cursorIndexOfDateAdministered = CursorUtil.getColumnIndexOrThrow(_cursor, "dateAdministered");
          final int _cursorIndexOfNextDueDate = CursorUtil.getColumnIndexOrThrow(_cursor, "nextDueDate");
          final int _cursorIndexOfAdministeredBy = CursorUtil.getColumnIndexOrThrow(_cursor, "administeredBy");
          final int _cursorIndexOfLocation = CursorUtil.getColumnIndexOrThrow(_cursor, "location");
          final int _cursorIndexOfBatchNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "batchNumber");
          final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final List<VaccinationEntity> _result = new ArrayList<VaccinationEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final VaccinationEntity _item;
            final String _tmpId;
            if (_cursor.isNull(_cursorIndexOfId)) {
              _tmpId = null;
            } else {
              _tmpId = _cursor.getString(_cursorIndexOfId);
            }
            final String _tmpVaccineName;
            if (_cursor.isNull(_cursorIndexOfVaccineName)) {
              _tmpVaccineName = null;
            } else {
              _tmpVaccineName = _cursor.getString(_cursorIndexOfVaccineName);
            }
            final String _tmpDoseNumber;
            if (_cursor.isNull(_cursorIndexOfDoseNumber)) {
              _tmpDoseNumber = null;
            } else {
              _tmpDoseNumber = _cursor.getString(_cursorIndexOfDoseNumber);
            }
            final String _tmpDateAdministered;
            if (_cursor.isNull(_cursorIndexOfDateAdministered)) {
              _tmpDateAdministered = null;
            } else {
              _tmpDateAdministered = _cursor.getString(_cursorIndexOfDateAdministered);
            }
            final String _tmpNextDueDate;
            if (_cursor.isNull(_cursorIndexOfNextDueDate)) {
              _tmpNextDueDate = null;
            } else {
              _tmpNextDueDate = _cursor.getString(_cursorIndexOfNextDueDate);
            }
            final String _tmpAdministeredBy;
            if (_cursor.isNull(_cursorIndexOfAdministeredBy)) {
              _tmpAdministeredBy = null;
            } else {
              _tmpAdministeredBy = _cursor.getString(_cursorIndexOfAdministeredBy);
            }
            final String _tmpLocation;
            if (_cursor.isNull(_cursorIndexOfLocation)) {
              _tmpLocation = null;
            } else {
              _tmpLocation = _cursor.getString(_cursorIndexOfLocation);
            }
            final String _tmpBatchNumber;
            if (_cursor.isNull(_cursorIndexOfBatchNumber)) {
              _tmpBatchNumber = null;
            } else {
              _tmpBatchNumber = _cursor.getString(_cursorIndexOfBatchNumber);
            }
            final String _tmpNotes;
            if (_cursor.isNull(_cursorIndexOfNotes)) {
              _tmpNotes = null;
            } else {
              _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
            }
            final String _tmpCreatedAt;
            if (_cursor.isNull(_cursorIndexOfCreatedAt)) {
              _tmpCreatedAt = null;
            } else {
              _tmpCreatedAt = _cursor.getString(_cursorIndexOfCreatedAt);
            }
            _item = new VaccinationEntity(_tmpId,_tmpVaccineName,_tmpDoseNumber,_tmpDateAdministered,_tmpNextDueDate,_tmpAdministeredBy,_tmpLocation,_tmpBatchNumber,_tmpNotes,_tmpCreatedAt);
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
