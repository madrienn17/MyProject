package com.example.myproject.data;

import android.database.Cursor;
import androidx.lifecycle.LiveData;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteQuery;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.example.myproject.models.Favorite;
import java.lang.Exception;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import kotlin.Unit;
import kotlin.coroutines.Continuation;

@SuppressWarnings({"unchecked", "deprecation"})
public final class RestaurantDao_Impl implements RestaurantDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Favorite> __insertionAdapterOfFavorite;

  private final EntityDeletionOrUpdateAdapter<Favorite> __deletionAdapterOfFavorite;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAllDao;

  public RestaurantDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfFavorite = new EntityInsertionAdapter<Favorite>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR IGNORE INTO `favorites` (`restId`,`userId`,`id`) VALUES (?,?,nullif(?, 0))";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Favorite value) {
        stmt.bindLong(1, value.getRestId());
        stmt.bindLong(2, value.getUserId());
        stmt.bindLong(3, value.getId());
      }
    };
    this.__deletionAdapterOfFavorite = new EntityDeletionOrUpdateAdapter<Favorite>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `favorites` WHERE `id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Favorite value) {
        stmt.bindLong(1, value.getId());
      }
    };
    this.__preparedStmtOfDeleteAllDao = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "Delete FROM favorites";
        return _query;
      }
    };
  }

  @Override
  public Object insert(final Favorite favorite, final Continuation<? super Unit> p1) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfFavorite.insert(favorite);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, p1);
  }

  @Override
  public Object deleteRestaurantDao(final Favorite favorite, final Continuation<? super Unit> p1) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfFavorite.handle(favorite);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, p1);
  }

  @Override
  public Object deleteAllDao(final Continuation<? super Unit> p0) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAllDao.acquire();
        __db.beginTransaction();
        try {
          _stmt.executeUpdateDelete();
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
          __preparedStmtOfDeleteAllDao.release(_stmt);
        }
      }
    }, p0);
  }

  @Override
  public LiveData<List<Favorite>> selectAllRestaurants() {
    final String _sql = "SELECT * FROM favorites ORDER BY id ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return __db.getInvalidationTracker().createLiveData(new String[]{"favorites"}, false, new Callable<List<Favorite>>() {
      @Override
      public List<Favorite> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfRestId = CursorUtil.getColumnIndexOrThrow(_cursor, "restId");
          final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final List<Favorite> _result = new ArrayList<Favorite>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final Favorite _item;
            final long _tmpRestId;
            _tmpRestId = _cursor.getLong(_cursorIndexOfRestId);
            final long _tmpUserId;
            _tmpUserId = _cursor.getLong(_cursorIndexOfUserId);
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            _item = new Favorite(_tmpRestId,_tmpUserId,_tmpId);
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
  public int vacuumDb(final SupportSQLiteQuery supportSQLiteQuery) {
    final SupportSQLiteQuery _internalQuery = supportSQLiteQuery;
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _internalQuery, false, null);
    try {
      final int _result;
      if(_cursor.moveToFirst()) {
        _result = _cursor.getInt(0);
      } else {
        _result = 0;
      }
      return _result;
    } finally {
      _cursor.close();
    }
  }
}
