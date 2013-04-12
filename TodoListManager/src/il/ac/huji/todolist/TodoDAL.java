package il.ac.huji.todolist;

import java.util.List;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.PushService;
import com.parse.ParseException;

import android.content.Context;
import android.database.Cursor;

public class TodoDAL {
	private DatabaseHandler dbh;
	
	public TodoDAL(Context context) 
	{ 
		//DB
		dbh = new DatabaseHandler(context);
		
		//Parse
		Parse.initialize(context /*this*/, "fkzwf4i69gpztGkRz5VrbAyCiiNK9LoEYgAgNbwc", "XEx8gRNX6k86JVNegZMTc06qXSRLjfzUpRHIFEbX");
		PushService.subscribe(context, "", TodoListManagerActivity.class);
		PushService.setDefaultPushCallback(context, TodoListManagerActivity.class);
		ParseUser.enableAutomaticUser();
	}
	
	Cursor getCursor()
	{
		return dbh.getReadableDatabase().query("todo", new String[] { "_id", "title", "due" }, null, null, null, null, null);
	}
	
	public boolean insert(ITodoItem todoItem)
	{
		if(todoItem == null || todoItem.getTitle() == null)
			return false;
		
		try
		{
			if (!dbh.addItem(todoItem))
				return false;
			
			ParseObject todoObg = new ParseObject("todo");
			todoObg.put("title", todoItem.getTitle());
			todoObg.put("due", todoItem.getDueDate().getTime());
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
			
			ParseQuery query = new ParseQuery("todo");
			query.whereEqualTo("title", todoItem.getTitle());
			query.findInBackground(new FindCallback() {
				public void done(List<ParseObject> todoList, ParseException e) {
					if (e == null) {
						for(int i=0; i< todoList.size(); i++)
						{
							ParseObject parseObj = todoList.get(i);
							parseObj.put("due", finalTodoItem.getDueDate().getTime());
							parseObj.saveInBackground();
						}
					} 

				}
			});

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
			
			ParseQuery query = new ParseQuery("todo");
			query.whereEqualTo("title", todoItem.getTitle());
			query.whereEqualTo("due", todoItem.getDueDate().getTime());
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
