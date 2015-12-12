package com.jiffyjob.nimblylabs.commonUtilities.sqliteTool;

import android.database.sqlite.SQLiteDatabase;

import java.util.HashMap;
import java.util.List;

/**
 * Created by NimblyLabs on 8/10/2015.
 */
public interface IsqliteUtil {
    boolean openOrCreateDB(String path, SQLiteDatabase.CursorFactory factory);

    boolean insertToDB(String tableName, HashMap<String, String> hashMap);

    List<HashMap<String, String>> selectFromDB(String sqlQuery, String[] selectionArgs);
}
