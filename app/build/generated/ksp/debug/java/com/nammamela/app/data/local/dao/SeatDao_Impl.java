package com.nammamela.app.data.local.dao;

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
import com.nammamela.app.domain.model.Seat;
import com.nammamela.app.domain.model.SeatStatus;
import java.lang.Class;
import java.lang.Exception;
import java.lang.IllegalArgumentException;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class SeatDao_Impl implements SeatDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Seat> __insertionAdapterOfSeat;

  private final EntityDeletionOrUpdateAdapter<Seat> __updateAdapterOfSeat;

  private final SharedSQLiteStatement __preparedStmtOfDeleteSeatsForPlay;

  public SeatDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfSeat = new EntityInsertionAdapter<Seat>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `seats` (`id`,`playId`,`row`,`column`,`status`) VALUES (nullif(?, 0),?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Seat entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getPlayId());
        statement.bindString(3, entity.getRow());
        statement.bindLong(4, entity.getColumn());
        statement.bindString(5, __SeatStatus_enumToString(entity.getStatus()));
      }
    };
    this.__updateAdapterOfSeat = new EntityDeletionOrUpdateAdapter<Seat>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `seats` SET `id` = ?,`playId` = ?,`row` = ?,`column` = ?,`status` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Seat entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getPlayId());
        statement.bindString(3, entity.getRow());
        statement.bindLong(4, entity.getColumn());
        statement.bindString(5, __SeatStatus_enumToString(entity.getStatus()));
        statement.bindLong(6, entity.getId());
      }
    };
    this.__preparedStmtOfDeleteSeatsForPlay = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM seats WHERE playId = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insertSeats(final List<Seat> seats, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfSeat.insert(seats);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateSeat(final Seat seat, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfSeat.handle(seat);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateSeats(final List<Seat> seats, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfSeat.handleMultiple(seats);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteSeatsForPlay(final int playId, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteSeatsForPlay.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, playId);
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
          __preparedStmtOfDeleteSeatsForPlay.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<Seat>> getSeatsForPlay(final int playId) {
    final String _sql = "SELECT * FROM seats WHERE playId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, playId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"seats"}, new Callable<List<Seat>>() {
      @Override
      @NonNull
      public List<Seat> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfPlayId = CursorUtil.getColumnIndexOrThrow(_cursor, "playId");
          final int _cursorIndexOfRow = CursorUtil.getColumnIndexOrThrow(_cursor, "row");
          final int _cursorIndexOfColumn = CursorUtil.getColumnIndexOrThrow(_cursor, "column");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final List<Seat> _result = new ArrayList<Seat>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Seat _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final int _tmpPlayId;
            _tmpPlayId = _cursor.getInt(_cursorIndexOfPlayId);
            final String _tmpRow;
            _tmpRow = _cursor.getString(_cursorIndexOfRow);
            final int _tmpColumn;
            _tmpColumn = _cursor.getInt(_cursorIndexOfColumn);
            final SeatStatus _tmpStatus;
            _tmpStatus = __SeatStatus_stringToEnum(_cursor.getString(_cursorIndexOfStatus));
            _item = new Seat(_tmpId,_tmpPlayId,_tmpRow,_tmpColumn,_tmpStatus);
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

  private String __SeatStatus_enumToString(@NonNull final SeatStatus _value) {
    switch (_value) {
      case AVAILABLE: return "AVAILABLE";
      case BOOKED: return "BOOKED";
      case SELECTED: return "SELECTED";
      default: throw new IllegalArgumentException("Can't convert enum to string, unknown enum value: " + _value);
    }
  }

  private SeatStatus __SeatStatus_stringToEnum(@NonNull final String _value) {
    switch (_value) {
      case "AVAILABLE": return SeatStatus.AVAILABLE;
      case "BOOKED": return SeatStatus.BOOKED;
      case "SELECTED": return SeatStatus.SELECTED;
      default: throw new IllegalArgumentException("Can't convert value to enum, unknown value: " + _value);
    }
  }
}
