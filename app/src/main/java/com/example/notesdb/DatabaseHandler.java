package com.example.notesdb;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "spinnerExample";
    private static final String TABLE_NAME = "labels";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "label";
    private static final String COLUMN_TEXT = "text";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Category table create query
        String CREATE_ITEM_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_NAME + " TEXT NOT NULL )";
        db.execSQL(CREATE_ITEM_TABLE);

        String alterTable = "ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + COLUMN_TEXT + " TEXT";
        // Change TEXT to the desired type
        db.execSQL(alterTable);

      // Log.d("NUMBER_OF_COLUMNS", String.valueOf(getColumnCount(CREATE_ITEM_TABLE)));
    }

    public int getColumnCount(String query) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        int columnCount = 0;

        try {
            cursor = db.rawQuery(query, null);
            columnCount = cursor.getColumnCount();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return columnCount;
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed

        if (newVersion > oldVersion) {

            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            // Create tables again
            onCreate(db);

        }

    }

    /**
     * Inserting new lable into lables table
     * */
    public void insert(String label, String text){
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction(); // Start the transaction
        try {
                ContentValues contentValues = new ContentValues();
                contentValues.put("label", label); // First element is label
                contentValues.put("text", text); // Second element is text
                db.insert("labels", null, contentValues);

            db.setTransactionSuccessful(); // Mark the transaction as successful
        } catch (Exception e) {
            e.printStackTrace(); // Handle any exceptions
        } finally {
            db.endTransaction(); // End the transaction
            db.close(); // Close the database connection
        }
    }


    // Method to delete a row by ID
    public boolean deleteRow(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        // Define the where clause and arguments
        String whereClause = "id = ?";
        String[] whereArgs = { String.valueOf(id) };

        //Execute the delete operation
        int rowsDeleted = db.delete(TABLE_NAME, whereClause, whereArgs);

        Log.d("rowsDeleted", String.valueOf(rowsDeleted));

        db.close(); // Close the database connection
        // Return true if a row was deleted, false otherwise
        return rowsDeleted > 0;
    }

    /**
     * Getting all labels
     * returns list of labels
     * */
    public List<String> getAllLabels(){
        List<String> list_l = new ArrayList<String>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);//selectQuery,selectedArguments

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                list_l.add(cursor.getString(1));//adding 2nd column data
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables and commentaries
        return list_l;
    }
    public List<String> getAllComentaris(){

        List<String> list_c = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_NAME;


        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);//selectQuery,selectedArguments
        // looping through all rows and adding to list
        if (cursor != null && cursor.moveToFirst()) {
            do {
                list_c.add(cursor.getString(2));//adding 3rd column data
                Log.d("CursorDataComentari", "Row Data: " + cursor.getString(2));
            } while (cursor.moveToNext());
        }
        // closing connection
        if (cursor != null){
            cursor.close();
        }

        db.close();
        // returning lables and commentaries
        return list_c;
    }

// metodo para el metodo de logCursorData
    public Cursor getAllData() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM labels", null);
    }

}



