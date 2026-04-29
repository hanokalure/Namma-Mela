package com.nammamela.app.data.local.database;

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
import com.nammamela.app.data.local.dao.ActorDao;
import com.nammamela.app.data.local.dao.ActorDao_Impl;
import com.nammamela.app.data.local.dao.BookingDao;
import com.nammamela.app.data.local.dao.BookingDao_Impl;
import com.nammamela.app.data.local.dao.CommentDao;
import com.nammamela.app.data.local.dao.CommentDao_Impl;
import com.nammamela.app.data.local.dao.PlayDao;
import com.nammamela.app.data.local.dao.PlayDao_Impl;
import com.nammamela.app.data.local.dao.SeatDao;
import com.nammamela.app.data.local.dao.SeatDao_Impl;
import com.nammamela.app.data.local.dao.UserDao;
import com.nammamela.app.data.local.dao.UserDao_Impl;
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
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class AppDatabase_Impl extends AppDatabase {
  private volatile PlayDao _playDao;

  private volatile ActorDao _actorDao;

  private volatile SeatDao _seatDao;

  private volatile CommentDao _commentDao;

  private volatile BookingDao _bookingDao;

  private volatile UserDao _userDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(2) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `plays` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `title` TEXT NOT NULL, `duration` TEXT NOT NULL, `description` TEXT NOT NULL, `genre` TEXT NOT NULL, `posterUrl` TEXT, `rating` REAL NOT NULL, `timestamp` INTEGER NOT NULL, `isActive` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `actors` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `playId` INTEGER NOT NULL, `name` TEXT NOT NULL, `role` TEXT NOT NULL, `imageUrl` TEXT, `category` TEXT NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `seats` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `playId` INTEGER NOT NULL, `row` TEXT NOT NULL, `column` INTEGER NOT NULL, `status` TEXT NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `comments` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `userId` INTEGER NOT NULL, `username` TEXT NOT NULL, `userHandle` TEXT NOT NULL, `content` TEXT NOT NULL, `timestamp` INTEGER NOT NULL, `likes` INTEGER NOT NULL, `fires` INTEGER NOT NULL, `replies` INTEGER NOT NULL, `imageUrl` TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `bookings` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `userId` INTEGER NOT NULL, `playId` INTEGER NOT NULL, `seats` TEXT NOT NULL, `totalPrice` REAL NOT NULL, `timestamp` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `users` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT NOT NULL, `handle` TEXT NOT NULL, `email` TEXT NOT NULL, `password` TEXT NOT NULL, `role` TEXT NOT NULL, `imageUrl` TEXT, `companyName` TEXT, `location` TEXT, `phone` TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, 'bd48c939dbb85ec5c8f9abf45ce5a152')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `plays`");
        db.execSQL("DROP TABLE IF EXISTS `actors`");
        db.execSQL("DROP TABLE IF EXISTS `seats`");
        db.execSQL("DROP TABLE IF EXISTS `comments`");
        db.execSQL("DROP TABLE IF EXISTS `bookings`");
        db.execSQL("DROP TABLE IF EXISTS `users`");
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
        final HashMap<String, TableInfo.Column> _columnsPlays = new HashMap<String, TableInfo.Column>(9);
        _columnsPlays.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPlays.put("title", new TableInfo.Column("title", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPlays.put("duration", new TableInfo.Column("duration", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPlays.put("description", new TableInfo.Column("description", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPlays.put("genre", new TableInfo.Column("genre", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPlays.put("posterUrl", new TableInfo.Column("posterUrl", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPlays.put("rating", new TableInfo.Column("rating", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPlays.put("timestamp", new TableInfo.Column("timestamp", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPlays.put("isActive", new TableInfo.Column("isActive", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysPlays = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesPlays = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoPlays = new TableInfo("plays", _columnsPlays, _foreignKeysPlays, _indicesPlays);
        final TableInfo _existingPlays = TableInfo.read(db, "plays");
        if (!_infoPlays.equals(_existingPlays)) {
          return new RoomOpenHelper.ValidationResult(false, "plays(com.nammamela.app.domain.model.Play).\n"
                  + " Expected:\n" + _infoPlays + "\n"
                  + " Found:\n" + _existingPlays);
        }
        final HashMap<String, TableInfo.Column> _columnsActors = new HashMap<String, TableInfo.Column>(6);
        _columnsActors.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsActors.put("playId", new TableInfo.Column("playId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsActors.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsActors.put("role", new TableInfo.Column("role", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsActors.put("imageUrl", new TableInfo.Column("imageUrl", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsActors.put("category", new TableInfo.Column("category", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysActors = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesActors = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoActors = new TableInfo("actors", _columnsActors, _foreignKeysActors, _indicesActors);
        final TableInfo _existingActors = TableInfo.read(db, "actors");
        if (!_infoActors.equals(_existingActors)) {
          return new RoomOpenHelper.ValidationResult(false, "actors(com.nammamela.app.domain.model.Actor).\n"
                  + " Expected:\n" + _infoActors + "\n"
                  + " Found:\n" + _existingActors);
        }
        final HashMap<String, TableInfo.Column> _columnsSeats = new HashMap<String, TableInfo.Column>(5);
        _columnsSeats.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSeats.put("playId", new TableInfo.Column("playId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSeats.put("row", new TableInfo.Column("row", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSeats.put("column", new TableInfo.Column("column", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSeats.put("status", new TableInfo.Column("status", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysSeats = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesSeats = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoSeats = new TableInfo("seats", _columnsSeats, _foreignKeysSeats, _indicesSeats);
        final TableInfo _existingSeats = TableInfo.read(db, "seats");
        if (!_infoSeats.equals(_existingSeats)) {
          return new RoomOpenHelper.ValidationResult(false, "seats(com.nammamela.app.domain.model.Seat).\n"
                  + " Expected:\n" + _infoSeats + "\n"
                  + " Found:\n" + _existingSeats);
        }
        final HashMap<String, TableInfo.Column> _columnsComments = new HashMap<String, TableInfo.Column>(10);
        _columnsComments.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsComments.put("userId", new TableInfo.Column("userId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsComments.put("username", new TableInfo.Column("username", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsComments.put("userHandle", new TableInfo.Column("userHandle", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsComments.put("content", new TableInfo.Column("content", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsComments.put("timestamp", new TableInfo.Column("timestamp", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsComments.put("likes", new TableInfo.Column("likes", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsComments.put("fires", new TableInfo.Column("fires", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsComments.put("replies", new TableInfo.Column("replies", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsComments.put("imageUrl", new TableInfo.Column("imageUrl", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysComments = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesComments = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoComments = new TableInfo("comments", _columnsComments, _foreignKeysComments, _indicesComments);
        final TableInfo _existingComments = TableInfo.read(db, "comments");
        if (!_infoComments.equals(_existingComments)) {
          return new RoomOpenHelper.ValidationResult(false, "comments(com.nammamela.app.domain.model.Comment).\n"
                  + " Expected:\n" + _infoComments + "\n"
                  + " Found:\n" + _existingComments);
        }
        final HashMap<String, TableInfo.Column> _columnsBookings = new HashMap<String, TableInfo.Column>(6);
        _columnsBookings.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBookings.put("userId", new TableInfo.Column("userId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBookings.put("playId", new TableInfo.Column("playId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBookings.put("seats", new TableInfo.Column("seats", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBookings.put("totalPrice", new TableInfo.Column("totalPrice", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBookings.put("timestamp", new TableInfo.Column("timestamp", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysBookings = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesBookings = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoBookings = new TableInfo("bookings", _columnsBookings, _foreignKeysBookings, _indicesBookings);
        final TableInfo _existingBookings = TableInfo.read(db, "bookings");
        if (!_infoBookings.equals(_existingBookings)) {
          return new RoomOpenHelper.ValidationResult(false, "bookings(com.nammamela.app.domain.model.Booking).\n"
                  + " Expected:\n" + _infoBookings + "\n"
                  + " Found:\n" + _existingBookings);
        }
        final HashMap<String, TableInfo.Column> _columnsUsers = new HashMap<String, TableInfo.Column>(10);
        _columnsUsers.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("handle", new TableInfo.Column("handle", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("email", new TableInfo.Column("email", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("password", new TableInfo.Column("password", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("role", new TableInfo.Column("role", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("imageUrl", new TableInfo.Column("imageUrl", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("companyName", new TableInfo.Column("companyName", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("location", new TableInfo.Column("location", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("phone", new TableInfo.Column("phone", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysUsers = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesUsers = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoUsers = new TableInfo("users", _columnsUsers, _foreignKeysUsers, _indicesUsers);
        final TableInfo _existingUsers = TableInfo.read(db, "users");
        if (!_infoUsers.equals(_existingUsers)) {
          return new RoomOpenHelper.ValidationResult(false, "users(com.nammamela.app.domain.model.User).\n"
                  + " Expected:\n" + _infoUsers + "\n"
                  + " Found:\n" + _existingUsers);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "bd48c939dbb85ec5c8f9abf45ce5a152", "8f45726e5ddc183fdabfbc5e972d3264");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "plays","actors","seats","comments","bookings","users");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `plays`");
      _db.execSQL("DELETE FROM `actors`");
      _db.execSQL("DELETE FROM `seats`");
      _db.execSQL("DELETE FROM `comments`");
      _db.execSQL("DELETE FROM `bookings`");
      _db.execSQL("DELETE FROM `users`");
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
    _typeConvertersMap.put(PlayDao.class, PlayDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(ActorDao.class, ActorDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(SeatDao.class, SeatDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(CommentDao.class, CommentDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(BookingDao.class, BookingDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(UserDao.class, UserDao_Impl.getRequiredConverters());
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
  public PlayDao getPlayDao() {
    if (_playDao != null) {
      return _playDao;
    } else {
      synchronized(this) {
        if(_playDao == null) {
          _playDao = new PlayDao_Impl(this);
        }
        return _playDao;
      }
    }
  }

  @Override
  public ActorDao getActorDao() {
    if (_actorDao != null) {
      return _actorDao;
    } else {
      synchronized(this) {
        if(_actorDao == null) {
          _actorDao = new ActorDao_Impl(this);
        }
        return _actorDao;
      }
    }
  }

  @Override
  public SeatDao getSeatDao() {
    if (_seatDao != null) {
      return _seatDao;
    } else {
      synchronized(this) {
        if(_seatDao == null) {
          _seatDao = new SeatDao_Impl(this);
        }
        return _seatDao;
      }
    }
  }

  @Override
  public CommentDao getCommentDao() {
    if (_commentDao != null) {
      return _commentDao;
    } else {
      synchronized(this) {
        if(_commentDao == null) {
          _commentDao = new CommentDao_Impl(this);
        }
        return _commentDao;
      }
    }
  }

  @Override
  public BookingDao getBookingDao() {
    if (_bookingDao != null) {
      return _bookingDao;
    } else {
      synchronized(this) {
        if(_bookingDao == null) {
          _bookingDao = new BookingDao_Impl(this);
        }
        return _bookingDao;
      }
    }
  }

  @Override
  public UserDao getUserDao() {
    if (_userDao != null) {
      return _userDao;
    } else {
      synchronized(this) {
        if(_userDao == null) {
          _userDao = new UserDao_Impl(this);
        }
        return _userDao;
      }
    }
  }
}
