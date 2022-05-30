package com.example.memorygame;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DatabaseHelperClass extends SQLiteOpenHelper {

    //Database version
    private static final int DATABASE_VERSION = 1;
    //Database name
    private static final String DATABASE_NAME = "memoryGame_database";
    //Datavase table name
    private static final String TABLE_NAME = "name";
    //Table columns:
    public static final String ID = "id";
    public static final String BEST_SCORE = "best_score";
    public static final String STEPS = "steps";
    public static final String COLUMN_WORK = "work";

    private SQLiteDatabase sqLiteDatabase;

    //Create table query
    private static final String CREATE_TABLE = "create table " + TABLE_NAME +
            "(" +ID+ " INTEGER PRIMARY KEY AUTOINCREMENT," + BEST_SCORE +
            " TEXT," + STEPS + " INTEGER," + COLUMN_WORK + " TEXT NOT NULL);";

    //Constructor
    public DatabaseHelperClass(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(" DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);

    }

    //First row which is save from delete
    public void createFirstRecord(MemoryGameModelClass memoryGameModelClass){
        ContentValues cv = new ContentValues();
        //What first row has:
        cv.put(DatabaseHelperClass.BEST_SCORE,"0");
        cv.put(DatabaseHelperClass.COLUMN_WORK, "-");

        sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.insert(DatabaseHelperClass.TABLE_NAME, null , cv);
    }

    //Add a new row
    public void addNewRecord(MemoryGameModelClass memoryGameModelClass){
        ContentValues cv = new ContentValues();
        //What a row has:
        cv.put(DatabaseHelperClass.STEPS, memoryGameModelClass.getStep());
        cv.put(DatabaseHelperClass.COLUMN_WORK, "x");

        sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.insert(DatabaseHelperClass.TABLE_NAME, null , cv);
    }

    //Change the game high score
    public void updateScore (MemoryGameModelClass memoryGameModelClass){
        ContentValues cv = new ContentValues();

        cv.put(DatabaseHelperClass.BEST_SCORE, memoryGameModelClass.getBestScore());

        sqLiteDatabase = this.getWritableDatabase();

        //The high score takes place in first row
        sqLiteDatabase.update(TABLE_NAME, cv, ID + " = ?", new String[]
                {String.valueOf(1)});

    }

    //Show the best user's score
    public String showScore (){

        String sql = "SELECT * FROM name WHERE work = ?";
        Cursor cursor = sqLiteDatabase.rawQuery(sql, new String[]{"-"});

        sqLiteDatabase = this.getReadableDatabase();

        String storeScore = "";
        if (cursor.moveToFirst()){
            String bestScore = cursor.getString(1);
            storeScore= bestScore;
        }
        cursor.close();
        //String with the best score
        return storeScore;
    }


    //Get all rows from the SqlDatabase table
    public ArrayList<Integer> getStepsColumn (String column, String arrayCol) {

        sqLiteDatabase = this.getReadableDatabase();
        String sql = "SELECT * FROM name WHERE " + column + "= ?";
        Cursor cursor = sqLiteDatabase.rawQuery(sql, new String[]{arrayCol});

        ArrayList<Integer> stepsList = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                int step = cursor.getInt(2);
                stepsList.add(step);
            } while (cursor.moveToNext());

        }
        cursor.close();
        return stepsList;
    }

    //Delete all steps column
    public void deleteSteps() {
        sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.delete(TABLE_NAME, COLUMN_WORK + "=?" , new String[]{"x"});

    }

}
