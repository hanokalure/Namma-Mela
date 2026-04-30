package com.nammamela.app.data.local.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.nammamela.app.domain.model.Play;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Long;
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
public final class PlayDao_Impl implements PlayDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Play> __insertionAdapterOfPlay;

  private final EntityDeletionOrUpdateAdapter<Play> __updateAdapterOfPlay;

  private final SharedSQLiteStatement __preparedStmtOfArchiveAllPlays;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAllPlays;

  public PlayDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfPlay = new EntityInsertionAdapter<Play>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `plays` (`id`,`title`,`duration`,`description`,`genre`,`posterUrl`,`rating`,`showTime`,`timestamp`,`isActive`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Play entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getTitle());
        statement.bindString(3, entity.getDuration());
        statement.bindString(4, entity.getDescription());
        statement.bindString(5, entity.getGenre());
        if (entity.getPosterUrl() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getPosterUrl());
        }
        statement.bindDouble(7, entity.getRating());
        statement.bindString(8, entity.getShowTime());
        statement.bindLong(9, entity.getTimestamp());
        final int _tmp = entity.isActive() ? 1 : 0;
        statement.bindLong(10, _tmp);
      }
    };
    this.__updateAdapterOfPlay = new EntityDeletionOrUpdateAdapter<Play>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `plays` SET `id` = ?,`title` = ?,`duration` = ?,`description` = ?,`genre` = ?,`posterUrl` = ?,`rating` = ?,`showTime` = ?,`timestamp` = ?,`isActive` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Play entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getTitle());
        statement.bindString(3, entity.getDuration());
        statement.bindString(4, entity.getDescription());
        statement.bindString(5, entity.getGenre());
        if (entity.getPosterUrl() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getPosterUrl());
        }
        statement.bindDouble(7, entity.getRating());
        statement.bindString(8, entity.getShowTime());
        statement.bindLong(9, entity.getTimestamp());
        final int _tmp = entity.isActive() ? 1 : 0;
        statement.bindLong(10, _tmp);
        statement.bindLong(11, entity.getId());
      }
    };
    this.__preparedStmtOfArchiveAllPlays = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE plays SET isActive = 0";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteAllPlays = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM plays";
        return _query;
      }
    };
  }

  @Override
  public Object insertPlay(final Play play, final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfPlay.insertAndReturnId(play);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updatePlay(final Play play, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfPlay.handle(play);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object archiveAllPlays(final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfArchiveAllPlays.acquire();
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
          __preparedStmtOfArchiveAllPlays.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteAllPlays(final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAllPlays.acquire();
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
          __preparedStmtOfDeleteAllPlays.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<Play>> getAllPlays() {
    final String _sql = "SELECT * FROM plays ORDER BY timestamp DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"plays"}, new Callable<List<Play>>() {
      @Override
      @NonNull
      public List<Play> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfDuration = CursorUtil.getColumnIndexOrThrow(_cursor, "duration");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfGenre = CursorUtil.getColumnIndexOrThrow(_cursor, "genre");
          final int _cursorIndexOfPosterUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "posterUrl");
          final int _cursorIndexOfRating = CursorUtil.getColumnIndexOrThrow(_cursor, "rating");
          final int _cursorIndexOfShowTime = CursorUtil.getColumnIndexOrThrow(_cursor, "showTime");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final int _cursorIndexOfIsActive = CursorUtil.getColumnIndexOrThrow(_cursor, "isActive");
          final List<Play> _result = new ArrayList<Play>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Play _item;
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
            final String _tmpShowTime;
            _tmpShowTime = _cursor.getString(_cursorIndexOfShowTime);
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            final boolean _tmpIsActive;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsActive);
            _tmpIsActive = _tmp != 0;
            _item = new Play(_tmpId,_tmpTitle,_tmpDuration,_tmpDescription,_tmpGenre,_tmpPosterUrl,_tmpRating,_tmpShowTime,_tmpTimestamp,_tmpIsActive);
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
  public Object getAllPlaysOnce(final Continuation<? super List<Play>> $completion) {
    final String _sql = "SELECT * FROM plays";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<Play>>() {
      @Override
      @NonNull
      public List<Play> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfDuration = CursorUtil.getColumnIndexOrThrow(_cursor, "duration");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfGenre = CursorUtil.getColumnIndexOrThrow(_cursor, "genre");
          final int _cursorIndexOfPosterUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "posterUrl");
          final int _cursorIndexOfRating = CursorUtil.getColumnIndexOrThrow(_cursor, "rating");
          final int _cursorIndexOfShowTime = CursorUtil.getColumnIndexOrThrow(_cursor, "showTime");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final int _cursorIndexOfIsActive = CursorUtil.getColumnIndexOrThrow(_cursor, "isActive");
          final List<Play> _result = new ArrayList<Play>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Play _item;
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
            final String _tmpShowTime;
            _tmpShowTime = _cursor.getString(_cursorIndexOfShowTime);
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            final boolean _tmpIsActive;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsActive);
            _tmpIsActive = _tmp != 0;
            _item = new Play(_tmpId,_tmpTitle,_tmpDuration,_tmpDescription,_tmpGenre,_tmpPosterUrl,_tmpRating,_tmpShowTime,_tmpTimestamp,_tmpIsActive);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<Play> getPlayById(final int id) {
    final String _sql = "SELECT * FROM plays WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"plays"}, new Callable<Play>() {
      @Override
      @Nullable
      public Play call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfDuration = CursorUtil.getColumnIndexOrThrow(_cursor, "duration");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfGenre = CursorUtil.getColumnIndexOrThrow(_cursor, "genre");
          final int _cursorIndexOfPosterUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "posterUrl");
          final int _cursorIndexOfRating = CursorUtil.getColumnIndexOrThrow(_cursor, "rating");
          final int _cursorIndexOfShowTime = CursorUtil.getColumnIndexOrThrow(_cursor, "showTime");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final int _cursorIndexOfIsActive = CursorUtil.getColumnIndexOrThrow(_cursor, "isActive");
          final Play _result;
          if (_cursor.moveToFirst()) {
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
            final String _tmpShowTime;
            _tmpShowTime = _cursor.getString(_cursorIndexOfShowTime);
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            final boolean _tmpIsActive;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsActive);
            _tmpIsActive = _tmp != 0;
            _result = new Play(_tmpId,_tmpTitle,_tmpDuration,_tmpDescription,_tmpGenre,_tmpPosterUrl,_tmpRating,_tmpShowTime,_tmpTimestamp,_tmpIsActive);
          } else {
            _result = null;
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
  public Object getPlayByIdOnce(final int id, final Continuation<? super Play> $completion) {
    final String _sql = "SELECT * FROM plays WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Play>() {
      @Override
      @Nullable
      public Play call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfDuration = CursorUtil.getColumnIndexOrThrow(_cursor, "duration");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfGenre = CursorUtil.getColumnIndexOrThrow(_cursor, "genre");
          final int _cursorIndexOfPosterUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "posterUrl");
          final int _cursorIndexOfRating = CursorUtil.getColumnIndexOrThrow(_cursor, "rating");
          final int _cursorIndexOfShowTime = CursorUtil.getColumnIndexOrThrow(_cursor, "showTime");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final int _cursorIndexOfIsActive = CursorUtil.getColumnIndexOrThrow(_cursor, "isActive");
          final Play _result;
          if (_cursor.moveToFirst()) {
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
            final String _tmpShowTime;
            _tmpShowTime = _cursor.getString(_cursorIndexOfShowTime);
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            final boolean _tmpIsActive;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsActive);
            _tmpIsActive = _tmp != 0;
            _result = new Play(_tmpId,_tmpTitle,_tmpDuration,_tmpDescription,_tmpGenre,_tmpPosterUrl,_tmpRating,_tmpShowTime,_tmpTimestamp,_tmpIsActive);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<Play> getActivePlay() {
    final String _sql = "SELECT * FROM plays WHERE isActive = 1 ORDER BY timestamp DESC LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"plays"}, new Callable<Play>() {
      @Override
      @Nullable
      public Play call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfDuration = CursorUtil.getColumnIndexOrThrow(_cursor, "duration");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfGenre = CursorUtil.getColumnIndexOrThrow(_cursor, "genre");
          final int _cursorIndexOfPosterUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "posterUrl");
          final int _cursorIndexOfRating = CursorUtil.getColumnIndexOrThrow(_cursor, "rating");
          final int _cursorIndexOfShowTime = CursorUtil.getColumnIndexOrThrow(_cursor, "showTime");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final int _cursorIndexOfIsActive = CursorUtil.getColumnIndexOrThrow(_cursor, "isActive");
          final Play _result;
          if (_cursor.moveToFirst()) {
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
            final String _tmpShowTime;
            _tmpShowTime = _cursor.getString(_cursorIndexOfShowTime);
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            final boolean _tmpIsActive;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsActive);
            _tmpIsActive = _tmp != 0;
            _result = new Play(_tmpId,_tmpTitle,_tmpDuration,_tmpDescription,_tmpGenre,_tmpPosterUrl,_tmpRating,_tmpShowTime,_tmpTimestamp,_tmpIsActive);
          } else {
            _result = null;
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
