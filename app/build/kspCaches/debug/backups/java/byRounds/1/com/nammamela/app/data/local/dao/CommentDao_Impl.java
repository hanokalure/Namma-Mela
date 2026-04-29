package com.nammamela.app.data.local.dao;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.nammamela.app.domain.model.Comment;
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
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class CommentDao_Impl implements CommentDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Comment> __insertionAdapterOfComment;

  private final EntityDeletionOrUpdateAdapter<Comment> __deletionAdapterOfComment;

  private final EntityDeletionOrUpdateAdapter<Comment> __updateAdapterOfComment;

  public CommentDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfComment = new EntityInsertionAdapter<Comment>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `comments` (`id`,`userId`,`username`,`userHandle`,`content`,`timestamp`,`likes`,`fires`,`replies`,`imageUrl`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Comment entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getUserId());
        statement.bindString(3, entity.getUsername());
        statement.bindString(4, entity.getUserHandle());
        statement.bindString(5, entity.getContent());
        statement.bindLong(6, entity.getTimestamp());
        statement.bindLong(7, entity.getLikes());
        statement.bindLong(8, entity.getFires());
        statement.bindLong(9, entity.getReplies());
        if (entity.getImageUrl() == null) {
          statement.bindNull(10);
        } else {
          statement.bindString(10, entity.getImageUrl());
        }
      }
    };
    this.__deletionAdapterOfComment = new EntityDeletionOrUpdateAdapter<Comment>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `comments` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Comment entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfComment = new EntityDeletionOrUpdateAdapter<Comment>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `comments` SET `id` = ?,`userId` = ?,`username` = ?,`userHandle` = ?,`content` = ?,`timestamp` = ?,`likes` = ?,`fires` = ?,`replies` = ?,`imageUrl` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Comment entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getUserId());
        statement.bindString(3, entity.getUsername());
        statement.bindString(4, entity.getUserHandle());
        statement.bindString(5, entity.getContent());
        statement.bindLong(6, entity.getTimestamp());
        statement.bindLong(7, entity.getLikes());
        statement.bindLong(8, entity.getFires());
        statement.bindLong(9, entity.getReplies());
        if (entity.getImageUrl() == null) {
          statement.bindNull(10);
        } else {
          statement.bindString(10, entity.getImageUrl());
        }
        statement.bindLong(11, entity.getId());
      }
    };
  }

  @Override
  public Object insertComment(final Comment comment, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfComment.insert(comment);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteComment(final Comment comment, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfComment.handle(comment);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateComment(final Comment comment, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfComment.handle(comment);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<Comment>> getAllComments() {
    final String _sql = "SELECT * FROM comments ORDER BY timestamp DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"comments"}, new Callable<List<Comment>>() {
      @Override
      @NonNull
      public List<Comment> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
          final int _cursorIndexOfUsername = CursorUtil.getColumnIndexOrThrow(_cursor, "username");
          final int _cursorIndexOfUserHandle = CursorUtil.getColumnIndexOrThrow(_cursor, "userHandle");
          final int _cursorIndexOfContent = CursorUtil.getColumnIndexOrThrow(_cursor, "content");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final int _cursorIndexOfLikes = CursorUtil.getColumnIndexOrThrow(_cursor, "likes");
          final int _cursorIndexOfFires = CursorUtil.getColumnIndexOrThrow(_cursor, "fires");
          final int _cursorIndexOfReplies = CursorUtil.getColumnIndexOrThrow(_cursor, "replies");
          final int _cursorIndexOfImageUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "imageUrl");
          final List<Comment> _result = new ArrayList<Comment>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Comment _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final int _tmpUserId;
            _tmpUserId = _cursor.getInt(_cursorIndexOfUserId);
            final String _tmpUsername;
            _tmpUsername = _cursor.getString(_cursorIndexOfUsername);
            final String _tmpUserHandle;
            _tmpUserHandle = _cursor.getString(_cursorIndexOfUserHandle);
            final String _tmpContent;
            _tmpContent = _cursor.getString(_cursorIndexOfContent);
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            final int _tmpLikes;
            _tmpLikes = _cursor.getInt(_cursorIndexOfLikes);
            final int _tmpFires;
            _tmpFires = _cursor.getInt(_cursorIndexOfFires);
            final int _tmpReplies;
            _tmpReplies = _cursor.getInt(_cursorIndexOfReplies);
            final String _tmpImageUrl;
            if (_cursor.isNull(_cursorIndexOfImageUrl)) {
              _tmpImageUrl = null;
            } else {
              _tmpImageUrl = _cursor.getString(_cursorIndexOfImageUrl);
            }
            _item = new Comment(_tmpId,_tmpUserId,_tmpUsername,_tmpUserHandle,_tmpContent,_tmpTimestamp,_tmpLikes,_tmpFires,_tmpReplies,_tmpImageUrl);
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
