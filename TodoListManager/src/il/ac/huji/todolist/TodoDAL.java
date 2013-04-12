package il.ac.huji.todolist;

import java.util.List;

import org.json.JSONObject;

import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.PushService;

import android.content.Context;
import android.database.Cursor;

public class TodoDAL {
	private DatabaseHandler dbh;
	
	// Contacts Table Columns names
	private static final String TABLE_NAME = "todo";
    private static final String KEY_ID = "_id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_DUE = "due";
	
	public TodoDAL(Context context) 
	{ 
		//DB
		dbh = new DatabaseHandler(context);
		
		//Parse
		Parse.initialize(context, context.getResources().getString(R.string.parseApplication), 
				context.getResources().getString(R.string.clientKey));
		PushService.subscribe(context, "", TodoListManagerActivity.class);
		PushService.setDefaultPushCallback(context, TodoListManagerActivity.class);
		ParseUser.enableAutomaticUser();
	}
	
	Cursor getCursor()
	{
		return dbh.getReadableDatabase().query(TABLE_NAME, new String[] { KEY_ID, KEY_TITLE, KEY_DUE }, null, null, null, null, null);
	}
	
	public boolean insert(ITodoItem todoItem)
	{
		if(todoItem == null || todoItem.getTitle() == null)
			return false;
		
		try
		{
			if (!dbh.addItem(todoItem))
				return false;
			
			ParseObject todoObg = new ParseObject(TABLE_NAME);
			todoObg.put(KEY_TITLE, todoItem.getTitle());
			if (todoItem.getDueDate() != null)
				todoObg.put(KEY_DUE, todoItem.getDueDate().getTime());
			else
				todoObg.put(KEY_DUE, JSONObject.NULL);
			todoObg.saveInBackground();
		}
		catch (Exception e) 
		{
			return false;
		}
		
		return true;
	}
	
	public boolean update(ITodoItem todoItem) 
	{
		if(todoItem == null || todoItem.getTitle() == null)
			return false;

		try
		{
			if (!dbh.updateItem(todoItem))
				return false;
			
			final ITodoItem finalTodoItem = todoItem;
			
			ParseQuery query = new ParseQuery(TABLE_NAME);
			query.whereEqualTo(KEY_TITLE, todoItem.getTitle());
			
			List<ParseObject> test = query.find();
		    for(int x = 0; x < test.size(); x++)
		    {
		    	ParseObject parseObj = test.get(x);
		    	if (finalTodoItem.getDueDate() != null)
		    		parseObj.put(KEY_DUE, finalTodoItem.getDueDate().getTime());
		    	else
		    		parseObj.put(KEY_DUE, JSONObject.NULL);
				parseObj.saveInBackground();
		    }
		}
		catch (Exception e) 
		{
			return false;
		}
		
		return true;
	}
	
	public boolean delete(ITodoItem todoItem)
	{ 
		if(todoItem == null || todoItem.getTitle() == null)
			return false;
		
		try
		{
			if (!dbh.deleteItem(todoItem))
				return false;
			
			ParseQuery query = new ParseQuery(TABLE_NAME);
			query.whereEqualTo(KEY_TITLE, todoItem.getTitle());
			if (todoItem.getDueDate() != null)
				query.whereEqualTo(KEY_DUE, todoItem.getDueDate().getTime());
	    	else
	    		query.whereEqualTo(KEY_DUE, JSONObject.NULL);
			List<ParseObject> test = query.find();
		    for(int x = 0; x < test.size(); x++)
		    {
		        ParseObject currentObject = test.get(x);
		        currentObject.deleteInBackground();
		    }
		}
		catch (Exception e) 
		{
			return false;
		}
		
		return true;
	}
	
	public List<ITodoItem> all() 
	{
		return dbh.getAllItems();
	}
}
