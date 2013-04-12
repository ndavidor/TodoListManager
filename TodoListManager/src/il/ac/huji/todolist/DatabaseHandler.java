package il.ac.huji.todolist;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

@SuppressLint("SimpleDateFormat")
public class DatabaseHandler extends SQLiteOpenHelper 
{
	// All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;
 
    // Database Name
    private static final String DATABASE_NAME = "todo_db";
 
    // Contacts table name
    private static final String TABLE_NAME = "todo";
 
    // Contacts Table Columns names
    private static final String KEY_ID = "_id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_DUE = "due";
    
    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    
	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_NAME + "(" + 
				KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + 
				KEY_TITLE + " TEXT," +
                KEY_DUE + " INTEGER" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) 
	{
		// Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
 
        // Create tables again
        onCreate(db);
	}
	
	//CRUD operations
	// Adding new item
	boolean addItem(ITodoItem item) 
	{
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(KEY_TITLE, item.getTitle());
		if (item.getDueDate() != null)
			values.put(KEY_DUE, item.getDueDate().getTime());
		else
			values.putNull(KEY_DUE);
		
	    // Inserting Row
	    long result = db.insert(TABLE_NAME, null, values);
	    db.close();
	    return (result > -1);
	}
	 
	// Getting single item
	public ITodoItem getItem(String title) 
	{
	    SQLiteDatabase db = this.getReadableDatabase();
	    
	    Cursor cursor = db.query(TABLE_NAME, new String[] { KEY_ID,
	            KEY_TITLE, KEY_DUE }, KEY_TITLE + "=?",
	            new String[] { title }, null, null, null, null);
	    if (cursor != null)
	        cursor.moveToFirst();
	    
	    String isDateNull = cursor.getString(2);
        TodoItem item = new TodoItem(cursor.getString(1), (isDateNull != null) ? new Date(cursor.getLong(2)) : null);
	    return item;
	}
	 
	// Getting All items
	public List<ITodoItem> getAllItems() 
	{
		List<ITodoItem> allItems = new ArrayList<ITodoItem>();
	    // Select All Query
	    String selectQuery = "SELECT  * FROM " + TABLE_NAME;
	 
	    SQLiteDatabase db = this.getWritableDatabase();
	    Cursor cursor = db.rawQuery(selectQuery, null);
	    
	    // looping through all rows and adding to list
	    if (cursor.moveToFirst()) {
	        do 
	        {
	        	String isDateNull = cursor.getString(2);
	            TodoItem item = new TodoItem(cursor.getString(1), (isDateNull != null) ? new Date(cursor.getLong(2)) : null);
	            // Adding contact to list
	            allItems.add(item);
	        }
	        while (cursor.moveToNext());
	    }
	    
	    return allItems;
	}
	
	// Updating single item
	public boolean updateItem(ITodoItem item) 
	{
		SQLiteDatabase db = this.getWritableDatabase();
		 
		ContentValues values = new ContentValues();
		values.put(KEY_TITLE, item.getTitle());
		if (item.getDueDate() != null)
			values.put(KEY_DUE, item.getDueDate().getTime());
		else
			values.putNull(KEY_DUE);
	 
	    // updating row
	    return (db.update(TABLE_NAME, values, KEY_TITLE + " = ?", new String[] { item.getTitle() }) > 0);
	}
	 
	// Deleting single item
	public boolean deleteItem(ITodoItem item) 
	{
		SQLiteDatabase db = this.getWritableDatabase();
		long result = db.delete(TABLE_NAME, KEY_TITLE + " = ?", new String[] { item.getTitle() });
	    db.close();
	    return (result > 0);
	}
}
