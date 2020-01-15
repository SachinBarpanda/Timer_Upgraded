package great.sachin.timer_upgraded;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class DatabaseHelperClass extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHelper";
    private static final String ID = "ID";
    private static final String TABLE_NAME = "timer_table";
    private static final String TASK_NAME = "Task_Name";
    private static final String TOTAL_TIME = "Total_Time";
    private static final String REMAIING_TIME = "Remaining_Time";

    public DatabaseHelperClass(@Nullable Context context) {
        super(context,TABLE_NAME,null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String createTable = "CREATE TABLE "+TABLE_NAME +
                " (ID INTEGER PRIMARY KEY AUTOINCREMENT, "+TASK_NAME + " TEXT NOT NULL, "+
                TOTAL_TIME + " INTEGER NOT NULL, "+ REMAIING_TIME + " INTEGER NOT NULL)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Dropping table for now but thinking of adding the data
        db.execSQL("DROP TABLE IF EXISTS " +TABLE_NAME);
        onCreate(db);
    }

    public boolean addData(String item, long totalTime,long remainingTime) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TASK_NAME,item.toUpperCase());
        contentValues.put(REMAIING_TIME,remainingTime);
        contentValues.put(TOTAL_TIME,totalTime);
        Log.d(TAG, "addData: Adding "+item+" to "+TABLE_NAME);
        long result = database.insert(TABLE_NAME,null,contentValues);

        if(result == -1){
            return false;
        }else{
            return true;
        }
    }

    public Cursor getData(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM "+TABLE_NAME;
        Cursor data = db.rawQuery(query,null);
        return data;
    }

    public Cursor getItemID(String name){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT "+ID+" FROM "+TABLE_NAME +
                " WHERE "+TASK_NAME + " = '"+name+"'";
        Cursor data = db.rawQuery(query,null);
        return data;

    }
    /**
     * Update the timer of the database
     * */
        public void updateTimerData(String taskName, long timeRemaining, long totalTime){
            SQLiteDatabase db = this.getWritableDatabase();
            String query = "UPDATE "+ TABLE_NAME + " SET "+ TOTAL_TIME + " = '"+
                    totalTime + "' , "+ REMAIING_TIME +" = '"+
                    timeRemaining + "' WHERE "+TASK_NAME + " = '"+
                    taskName+"' ";
            db.execSQL(query);
        }


    /**
     * Find the taskName in the database
     * */

    public Cursor findTask(String taskName){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT "+ID+ " FROM "+TABLE_NAME+" WHERE "+
                TASK_NAME + " = '"+taskName+"'";
        Cursor data = db.rawQuery(query,null);
        return data;
    }

    //delete from the database
    public void deleteDataFromDatabase(int id,String name){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM "+TABLE_NAME+" WHERE "+
                ID + " = '"+ id +"'"+" AND "+ TASK_NAME +"='"+ name +"'";
        Log.d(TAG, "deleteDataFromDatabase: query : "+query);
        Log.d(TAG, "deleteDataFromDatabase: name deleting "+ name);
        db.execSQL(query);
    }
    public void deleteAllDataFromDatabase() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_NAME;
        Log.d(TAG, "deleteDataFromDatabase: query : " + query);
        db.execSQL(query);
    }
}
