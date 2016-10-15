package com.jiffyjob.nimblylabs.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;
import android.util.Log;

import com.jiffyjob.nimblylabs.browseCategories.Model.JobModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by NielPC on 10/3/2016.
 */
public class DBHelper extends SQLiteOpenHelper {
    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_JOB, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String jobTable = "CREATE TABLE " + TABLE_NAME + " (id INTEGER PRIMARY KEY, " +
                "JobID text UNIQUE, " +
                "CompanyName text, " +
                "CreatorUserID text, " +
                "DatePosted text, " +
                "JobTitle text, " +
                "IsGenericPhoto INTEGER, " +
                "PhotoDetails text, " +
                "MinAge INTEGER, " +
                "MaxAge INTEGER, " +
                "TotalPax INTEGER, " +
                "ReqMinEduLevel INTEGER, " +
                "Country text, " +
                "State text, " +
                "City text, " +
                "Lat text, " +
                "Lng text, " +
                "StartDateTime text, " +
                "EndDateTime text, " +
                "TotalWorkDays INTEGER, " +
                "SalaryCurrencyCode text, " +
                "Payout INTEGER, " +
                "CurrentlyRecruited INTEGER, " +
                "JobStatus INTEGER, " +
                "Featured INTEGER, " +
                "IsStarred INTEGER, " +
                "Scope text, " +
                "FileName text, " +
                "LastModify text)";
        db.execSQL(jobTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sqlQuery = String.format("DROP TABLE IF EXISTS %s", TABLE_NAME);
        db.execSQL(sqlQuery);
        onCreate(db);
    }

    public long insertJobList(List<JobModel> jobModelList) {
        long result = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        for (JobModel model : jobModelList) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("ID", model.ID);
            contentValues.put("JobID", model.JobID);
            contentValues.put("CompanyName", model.CompanyName);
            contentValues.put("CreatorUserID", model.CreatorUserID);
            contentValues.put("DatePosted", model.DatePosted);
            contentValues.put("JobTitle", model.JobTitle);
            contentValues.put("IsGenericPhoto", model.IsGenericPhoto);
            contentValues.put("PhotoDetails", model.Filename);
            contentValues.put("MinAge", model.MinAge);
            contentValues.put("MaxAge", model.MaxAge);
            contentValues.put("TotalPax", model.TotalPax);
            contentValues.put("ReqMinEduLevel", model.ReqMinEduLevel);
            contentValues.put("Country", model.Country);
            contentValues.put("State", model.State);
            contentValues.put("City", model.City);
            contentValues.put("Lat", model.Lat);
            contentValues.put("Lng", model.Lng);
            contentValues.put("StartDateTime", model.StartDateTime);
            contentValues.put("EndDateTime", model.EndDateTime);
            contentValues.put("TotalWorkDays", model.TotalWorkDays);
            contentValues.put("SalaryCurrencyCode", model.SalaryCurrencyCode);
            contentValues.put("Payout", model.Payout);
            contentValues.put("CurrentlyRecruited", model.CurrentlyRecruited);
            contentValues.put("JobStatus", model.JobStatus);
            contentValues.put("Featured", model.Featured);
            contentValues.put("LastModify", model.LastModify);
            contentValues.put("IsStarred", model.IsStarred);
            contentValues.put("Scope", model.Scope);
            contentValues.put("FileName", model.Filename);
            db.insert(TABLE_NAME, null, contentValues);
            result++;
        }
        db.close();
        return result;
    }

    public int updateJobList(List<JobModel> jobModelList) {
        int result = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        for (JobModel model : jobModelList) {
            ContentValues contentValues = new ContentValues();
/*          contentValues.put("ID", model.ID);
            contentValues.put("JobID", model.JobID);*/
            contentValues.put("CompanyName", model.CompanyName);
            contentValues.put("CreatorUserID", model.CreatorUserID);
            contentValues.put("DatePosted", model.DatePosted);
            contentValues.put("JobTitle", model.JobTitle);
            contentValues.put("IsGenericPhoto", model.IsGenericPhoto);
            contentValues.put("PhotoDetails", model.Filename);
            contentValues.put("MinAge", model.MinAge);
            contentValues.put("MaxAge", model.MaxAge);
            contentValues.put("TotalPax", model.TotalPax);
            contentValues.put("ReqMinEduLevel", model.ReqMinEduLevel);
            contentValues.put("Country", model.Country);
            contentValues.put("State", model.State);
            contentValues.put("City", model.City);
            contentValues.put("Lat", model.Lat);
            contentValues.put("Lng", model.Lng);
            contentValues.put("StartDateTime", model.StartDateTime);
            contentValues.put("EndDateTime", model.EndDateTime);
            contentValues.put("TotalWorkDays", model.TotalWorkDays);
            contentValues.put("SalaryCurrencyCode", model.SalaryCurrencyCode);
            contentValues.put("Payout", model.Payout);
            contentValues.put("CurrentlyRecruited", model.CurrentlyRecruited);
            contentValues.put("JobStatus", model.JobStatus);
            contentValues.put("Featured", model.Featured);
            contentValues.put("LastModify", model.LastModify);
            contentValues.put("IsStarred", model.IsStarred);
            contentValues.put("Scope", model.Scope);
            contentValues.put("FileName", model.Filename);
            String sqlStr = String.format("JobID = '%s'", model.JobID);
            int i = db.update(TABLE_NAME, contentValues, sqlStr, null);
            result += i;
        }
        db.close();
        return result;
    }

    public int updateJob(JobModel model) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
/*          contentValues.put("ID", model.ID);
            contentValues.put("JobID", model.JobID);*/
        contentValues.put("CompanyName", model.CompanyName);
        contentValues.put("CreatorUserID", model.CreatorUserID);
        contentValues.put("DatePosted", model.DatePosted);
        contentValues.put("JobTitle", model.JobTitle);
        contentValues.put("IsGenericPhoto", model.IsGenericPhoto);
        contentValues.put("PhotoDetails", model.Filename);
        contentValues.put("MinAge", model.MinAge);
        contentValues.put("MaxAge", model.MaxAge);
        contentValues.put("TotalPax", model.TotalPax);
        contentValues.put("ReqMinEduLevel", model.ReqMinEduLevel);
        contentValues.put("Country", model.Country);
        contentValues.put("State", model.State);
        contentValues.put("City", model.City);
        contentValues.put("Lat", model.Lat);
        contentValues.put("Lng", model.Lng);
        contentValues.put("StartDateTime", model.StartDateTime);
        contentValues.put("EndDateTime", model.EndDateTime);
        contentValues.put("TotalWorkDays", model.TotalWorkDays);
        contentValues.put("SalaryCurrencyCode", model.SalaryCurrencyCode);
        contentValues.put("Payout", model.Payout);
        contentValues.put("CurrentlyRecruited", model.CurrentlyRecruited);
        contentValues.put("JobStatus", model.JobStatus);
        contentValues.put("Featured", model.Featured);
        contentValues.put("LastModify", model.LastModify);
        contentValues.put("IsStarred", model.IsStarred);
        contentValues.put("Scope", model.Scope);
        contentValues.put("FileName", model.Filename);
        String sqlStr = String.format("JobID = '%s'", model.JobID);
        int i = db.update(TABLE_NAME, contentValues, sqlStr, null);
        db.close();
        return i;
    }

    public boolean isTableExists() {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = null;
        try {
            String queryStr = String.format("SELECT * FROM %s", TABLE_NAME);
            cursor = db.rawQuery(queryStr, null);
            if (cursor.getColumnCount() > 0) {
                cursor.close();
                db.close();
                return true;
            }
        } catch (Exception e) {
            Log.e(DBHelper.class.getSimpleName(), "Error:" + e.getMessage());
        }
        if (cursor != null) {
            cursor.close();
        }
        db.close();
        return false;
    }

    public JobModel getJobByJobID(int jobID) {
        JobModel model = null;
        SQLiteDatabase db = this.getReadableDatabase();
        String query = String.format("SELECT * FROM %s WHERE jobID = %s", TABLE_NAME, jobID);
        Cursor res = db.rawQuery(query, null);
        while (!res.isAfterLast()) {
            model = getModel(res);
            res.moveToNext();
        }
        res.close();
        return model;
    }

    public List<JobModel> getAllJobs() {
        List<JobModel> jobModelList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String queryStr = String.format("SELECT * FROM %s", TABLE_NAME);
        Cursor cursor = db.rawQuery(queryStr, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            jobModelList.add(getModel(cursor));
            cursor.moveToNext();
        }
        db.close();
        cursor.close();
        return jobModelList;
    }

    public int numberOfRows() {
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, TABLE_NAME);
        db.close();
        return numRows;
    }

    public int clearTable() {
        SQLiteDatabase db = getWritableDatabase();
        int count = db.delete(TABLE_NAME, null, null);
        db.close();
        return count;
    }

    public void dropTable() {
        SQLiteDatabase db = getWritableDatabase();
        String sqlQuery = String.format("DROP TABLE IF EXISTS %s", TABLE_NAME);
        db.execSQL(sqlQuery);
        db.close();
    }

    private JobModel getModel(@NonNull Cursor cursor) {
        JobModel model = new JobModel();
        model.JobID = cursor.getString(cursor.getColumnIndex("JobID"));
        model.CompanyName = cursor.getString(cursor.getColumnIndex("CompanyName"));
        model.CreatorUserID = cursor.getString(cursor.getColumnIndex("CreatorUserID"));
        model.DatePosted = cursor.getString(cursor.getColumnIndex("DatePosted"));
        model.JobTitle = cursor.getString(cursor.getColumnIndex("JobTitle"));
        model.IsGenericPhoto = cursor.getInt(cursor.getColumnIndex("IsGenericPhoto"));
        model.MinAge = cursor.getInt(cursor.getColumnIndex("MinAge"));
        model.MaxAge = cursor.getInt(cursor.getColumnIndex("MaxAge"));
        model.TotalPax = cursor.getInt(cursor.getColumnIndex("TotalPax"));
        model.ReqMinEduLevel = cursor.getInt(cursor.getColumnIndex("ReqMinEduLevel"));
        model.Country = cursor.getString(cursor.getColumnIndex("Country"));
        model.State = cursor.getString(cursor.getColumnIndex("State"));
        model.City = cursor.getString(cursor.getColumnIndex("City"));
        model.Lat = Double.parseDouble(cursor.getString(cursor.getColumnIndex("Lat")));
        model.Lng = Double.parseDouble(cursor.getString(cursor.getColumnIndex("Lng")));
        model.StartDateTime = cursor.getString(cursor.getColumnIndex("StartDateTime"));
        model.EndDateTime = cursor.getString(cursor.getColumnIndex("EndDateTime"));
        model.TotalWorkDays = cursor.getInt(cursor.getColumnIndex("TotalWorkDays"));
        model.SalaryCurrencyCode = cursor.getString(cursor.getColumnIndex("SalaryCurrencyCode"));
        model.Payout = cursor.getInt(cursor.getColumnIndex("Payout"));
        model.CurrentlyRecruited = cursor.getInt(cursor.getColumnIndex("CurrentlyRecruited"));
        model.JobStatus = cursor.getInt(cursor.getColumnIndex("JobStatus"));
        model.Featured = cursor.getInt(cursor.getColumnIndex("Featured"));
        model.Filename = cursor.getString(cursor.getColumnIndex("FileName"));
        int isStarred = cursor.getInt(cursor.getColumnIndex("IsStarred"));
        if (isStarred == 1) {
            model.IsStarred = true;
        } else {
            model.IsStarred = false;
        }
        model.Scope = cursor.getString(cursor.getColumnIndex("Scope"));
        model.LastModify = cursor.getString(cursor.getColumnIndex("LastModify"));
        return model;
    }

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_JOB = "JobsDB.db";
    public static final String TABLE_NAME = "Jobs";
}
