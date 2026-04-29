package com.nammamela.app.data.local.dao;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.collection.LongSparseArray;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.room.util.RelationUtil;
import androidx.room.util.StringUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.nammamela.app.domain.model.Booking;
import com.nammamela.app.domain.model.BookingWithPlay;
import com.nammamela.app.domain.model.Play;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
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
public final class BookingDao_Impl implements BookingDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Booking> __insertionAdapterOfBooking;

  public BookingDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfBooking = new EntityInsertionAdapter<Booking>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `bookings` (`id`,`userId`,`playId`,`seats`,`totalPrice`,`timestamp`) VALUES (nullif(?, 0),?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Booking entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getUserId());
        statement.bindLong(3, entity.getPlayId());
        statement.bindString(4, entity.getSeats());
        statement.bindDouble(5, entity.getTotalPrice());
        statement.bindLong(6, entity.getTimestamp());
      }
    };
  }

  @Override
  public Object insertBooking(final Booking booking, final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfBooking.insertAndReturnId(booking);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<Booking>> getBookingsForUser(final int userId) {
    final String _sql = "SELECT * FROM bookings WHERE userId = ? ORDER BY timestamp DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, userId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"bookings"}, new Callable<List<Booking>>() {
      @Override
      @NonNull
      public List<Booking> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
          final int _cursorIndexOfPlayId = CursorUtil.getColumnIndexOrThrow(_cursor, "playId");
          final int _cursorIndexOfSeats = CursorUtil.getColumnIndexOrThrow(_cursor, "seats");
          final int _cursorIndexOfTotalPrice = CursorUtil.getColumnIndexOrThrow(_cursor, "totalPrice");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final List<Booking> _result = new ArrayList<Booking>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Booking _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final int _tmpUserId;
            _tmpUserId = _cursor.getInt(_cursorIndexOfUserId);
            final int _tmpPlayId;
            _tmpPlayId = _cursor.getInt(_cursorIndexOfPlayId);
            final String _tmpSeats;
            _tmpSeats = _cursor.getString(_cursorIndexOfSeats);
            final double _tmpTotalPrice;
            _tmpTotalPrice = _cursor.getDouble(_cursorIndexOfTotalPrice);
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            _item = new Booking(_tmpId,_tmpUserId,_tmpPlayId,_tmpSeats,_tmpTotalPrice,_tmpTimestamp);
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

  @Override
  public Flow<List<BookingWithPlay>> getBookingsWithPlayForUser(final int userId) {
    final String _sql = "SELECT * FROM bookings WHERE userId = ? ORDER BY timestamp DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, userId);
    return CoroutinesRoom.createFlow(__db, true, new String[] {"plays",
        "bookings"}, new Callable<List<BookingWithPlay>>() {
      @Override
      @NonNull
      public List<BookingWithPlay> call() throws Exception {
        __db.beginTransaction();
        try {
          final Cursor _cursor = DBUtil.query(__db, _statement, true, null);
          try {
            final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
            final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
            final int _cursorIndexOfPlayId = CursorUtil.getColumnIndexOrThrow(_cursor, "playId");
            final int _cursorIndexOfSeats = CursorUtil.getColumnIndexOrThrow(_cursor, "seats");
            final int _cursorIndexOfTotalPrice = CursorUtil.getColumnIndexOrThrow(_cursor, "totalPrice");
            final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
            final LongSparseArray<Play> _collectionPlay = new LongSparseArray<Play>();
            while (_cursor.moveToNext()) {
              final long _tmpKey;
              _tmpKey = _cursor.getLong(_cursorIndexOfPlayId);
              _collectionPlay.put(_tmpKey, null);
            }
            _cursor.moveToPosition(-1);
            __fetchRelationshipplaysAscomNammamelaAppDomainModelPlay(_collectionPlay);
            final List<BookingWithPlay> _result = new ArrayList<BookingWithPlay>(_cursor.getCount());
            while (_cursor.moveToNext()) {
              final BookingWithPlay _item;
              final Booking _tmpBooking;
              final int _tmpId;
              _tmpId = _cursor.getInt(_cursorIndexOfId);
              final int _tmpUserId;
              _tmpUserId = _cursor.getInt(_cursorIndexOfUserId);
              final int _tmpPlayId;
              _tmpPlayId = _cursor.getInt(_cursorIndexOfPlayId);
              final String _tmpSeats;
              _tmpSeats = _cursor.getString(_cursorIndexOfSeats);
              final double _tmpTotalPrice;
              _tmpTotalPrice = _cursor.getDouble(_cursorIndexOfTotalPrice);
              final long _tmpTimestamp;
              _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
              _tmpBooking = new Booking(_tmpId,_tmpUserId,_tmpPlayId,_tmpSeats,_tmpTotalPrice,_tmpTimestamp);
              final Play _tmpPlay;
              final long _tmpKey_1;
              _tmpKey_1 = _cursor.getLong(_cursorIndexOfPlayId);
              _tmpPlay = _collectionPlay.get(_tmpKey_1);
              _item = new BookingWithPlay(_tmpBooking,_tmpPlay);
              _result.add(_item);
            }
            __db.setTransactionSuccessful();
            return _result;
          } finally {
            _cursor.close();
          }
        } finally {
          __db.endTransaction();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<BookingWithPlay> getBookingWithPlayById(final int bookingId) {
    final String _sql = "SELECT * FROM bookings WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, bookingId);
    return CoroutinesRoom.createFlow(__db, true, new String[] {"plays",
        "bookings"}, new Callable<BookingWithPlay>() {
      @Override
      @Nullable
      public BookingWithPlay call() throws Exception {
        __db.beginTransaction();
        try {
          final Cursor _cursor = DBUtil.query(__db, _statement, true, null);
          try {
            final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
            final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
            final int _cursorIndexOfPlayId = CursorUtil.getColumnIndexOrThrow(_cursor, "playId");
            final int _cursorIndexOfSeats = CursorUtil.getColumnIndexOrThrow(_cursor, "seats");
            final int _cursorIndexOfTotalPrice = CursorUtil.getColumnIndexOrThrow(_cursor, "totalPrice");
            final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
            final LongSparseArray<Play> _collectionPlay = new LongSparseArray<Play>();
            while (_cursor.moveToNext()) {
              final long _tmpKey;
              _tmpKey = _cursor.getLong(_cursorIndexOfPlayId);
              _collectionPlay.put(_tmpKey, null);
            }
            _cursor.moveToPosition(-1);
            __fetchRelationshipplaysAscomNammamelaAppDomainModelPlay(_collectionPlay);
            final BookingWithPlay _result;
            if (_cursor.moveToFirst()) {
              final Booking _tmpBooking;
              final int _tmpId;
              _tmpId = _cursor.getInt(_cursorIndexOfId);
              final int _tmpUserId;
              _tmpUserId = _cursor.getInt(_cursorIndexOfUserId);
              final int _tmpPlayId;
              _tmpPlayId = _cursor.getInt(_cursorIndexOfPlayId);
              final String _tmpSeats;
              _tmpSeats = _cursor.getString(_cursorIndexOfSeats);
              final double _tmpTotalPrice;
              _tmpTotalPrice = _cursor.getDouble(_cursorIndexOfTotalPrice);
              final long _tmpTimestamp;
              _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
              _tmpBooking = new Booking(_tmpId,_tmpUserId,_tmpPlayId,_tmpSeats,_tmpTotalPrice,_tmpTimestamp);
              final Play _tmpPlay;
              final long _tmpKey_1;
              _tmpKey_1 = _cursor.getLong(_cursorIndexOfPlayId);
              _tmpPlay = _collectionPlay.get(_tmpKey_1);
              _result = new BookingWithPlay(_tmpBooking,_tmpPlay);
            } else {
              _result = null;
            }
            __db.setTransactionSuccessful();
            return _result;
          } finally {
            _cursor.close();
          }
        } finally {
          __db.endTransaction();
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

  private void __fetchRelationshipplaysAscomNammamelaAppDomainModelPlay(
      @NonNull final LongSparseArray<Play> _map) {
    if (_map.isEmpty()) {
      return;
    }
    if (_map.size() > RoomDatabase.MAX_BIND_PARAMETER_CNT) {
      RelationUtil.recursiveFetchLongSparseArray(_map, false, (map) -> {
        __fetchRelationshipplaysAscomNammamelaAppDomainModelPlay(map);
        return Unit.INSTANCE;
      });
      return;
    }
    final StringBuilder _stringBuilder = StringUtil.newStringBuilder();
    _stringBuilder.append("SELECT `id`,`title`,`duration`,`description`,`genre`,`posterUrl`,`rating`,`timestamp`,`isActive` FROM `plays` WHERE `id` IN (");
    final int _inputSize = _map.size();
    StringUtil.appendPlaceholders(_stringBuilder, _inputSize);
    _stringBuilder.append(")");
    final String _sql = _stringBuilder.toString();
    final int _argCount = 0 + _inputSize;
    final RoomSQLiteQuery _stmt = RoomSQLiteQuery.acquire(_sql, _argCount);
    int _argIndex = 1;
    for (int i = 0; i < _map.size(); i++) {
      final long _item = _map.keyAt(i);
      _stmt.bindLong(_argIndex, _item);
      _argIndex++;
    }
    final Cursor _cursor = DBUtil.query(__db, _stmt, false, null);
    try {
      final int _itemKeyIndex = CursorUtil.getColumnIndex(_cursor, "id");
      if (_itemKeyIndex == -1) {
        return;
      }
      final int _cursorIndexOfId = 0;
      final int _cursorIndexOfTitle = 1;
      final int _cursorIndexOfDuration = 2;
      final int _cursorIndexOfDescription = 3;
      final int _cursorIndexOfGenre = 4;
      final int _cursorIndexOfPosterUrl = 5;
      final int _cursorIndexOfRating = 6;
      final int _cursorIndexOfTimestamp = 7;
      final int _cursorIndexOfIsActive = 8;
      while (_cursor.moveToNext()) {
        final long _tmpKey;
        _tmpKey = _cursor.getLong(_itemKeyIndex);
        if (_map.containsKey(_tmpKey)) {
          final Play _item_1;
          final int _tmpId;
          _tmpId = _cursor.getInt(_cursorIndexOfId);
          final String _tmpTitle;
          _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
          final String _tmpDuration;
          _tmpDuration = _cursor.getString(_cursorIndexOfDuration);
          final String _tmpDescription;
          _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
          final String _tmpGenre;
          _tmpGenre = _cursor.getString(_cursorIndexOfGenre);
          final String _tmpPosterUrl;
          if (_cursor.isNull(_cursorIndexOfPosterUrl)) {
            _tmpPosterUrl = null;
          } else {
            _tmpPosterUrl = _cursor.getString(_cursorIndexOfPosterUrl);
          }
          final float _tmpRating;
          _tmpRating = _cursor.getFloat(_cursorIndexOfRating);
          final long _tmpTimestamp;
          _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
          final boolean _tmpIsActive;
          final int _tmp;
          _tmp = _cursor.getInt(_cursorIndexOfIsActive);
          _tmpIsActive = _tmp != 0;
          _item_1 = new Play(_tmpId,_tmpTitle,_tmpDuration,_tmpDescription,_tmpGenre,_tmpPosterUrl,_tmpRating,_tmpTimestamp,_tmpIsActive);
          _map.put(_tmpKey, _item_1);
        }
      }
    } finally {
      _cursor.close();
    }
  }
}
