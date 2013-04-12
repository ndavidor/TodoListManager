package il.ac.huji.todolist;

import java.util.Date;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.ListView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import il.ac.huji.todolist.AddNewTodoItemActivity;

public class TodoListManagerActivity extends Activity 
{
	Cursor cursor;
	private TodoListCursorAdapter adapter;
	private TodoDAL  dal;
	
	ListView list;
	private int callPos = 1;
	final static String callTitle = "Call ";
	final static private String callForIntent = "tel:";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_todo_list_manager);
		
		dal = new TodoDAL(this);
		String[] from = { "title", "due" };
		int[] to = { R.id.txtTodoTitle, R.id.txtTodoDueDate };
		adapter = new TodoListCursorAdapter(this, R.layout.row, dal.getCursor(), from, to);
		
		list = (ListView)findViewById(R.id.lstTodoItems);
		list.setAdapter(adapter);
        registerForContextMenu(list);
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		getMenuInflater().inflate(R.menu.contextmenu, menu);
		AdapterContextMenuInfo info = (AdapterContextMenuInfo)menuInfo;
		
		cursor = adapter.getCursor();
		cursor.moveToPosition(info.position);
		
		String title = cursor.getString(1);
		menu.setHeaderTitle(title);
		if (title.contains(callTitle))
			menu.getItem(callPos).setTitle(title);
		else
			menu.removeItem(R.id.menuItemCall);
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo)item.getMenuInfo();
		int selectedItemIndex = info.position;
		cursor.moveToPosition(selectedItemIndex);
		TodoItem selectedItem = new TodoItem(cursor.getString(1), new Date(cursor.getLong(2)));
		
		switch (item.getItemId())
		{
			case R.id.menuItemDelete:
				dal.delete(selectedItem);
				refresh();
				break;
			case R.id.menuItemCall:
				Intent callIntent = new Intent(Intent.ACTION_DIAL);
				String tel = selectedItem.title.replace(callTitle, callForIntent);
			    callIntent.setData(Uri.parse(tel));
			    startActivity(callIntent);
				break;
		}
		return true;		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.todo_list_manager, menu);
		return true;
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	if (requestCode == 1337 && resultCode == RESULT_OK) {
    		String itemName = data.getStringExtra("title");
    		Date date = (Date)data.getSerializableExtra("dueDate");
    		dal.insert(new TodoItem(itemName, date));
    		refresh();
    	}
    }
    
    public void refresh()
    {
    	cursor = dal.getCursor();
    	adapter.changeCursor(cursor);
    	adapter.notifyDataSetChanged();
    }

	/**
     * Event Handling for Individual menu item selected
     * Identify single menu item by it's id
     * */
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
        case R.id.menuItemAdd:
        	Intent intent = new Intent(this, AddNewTodoItemActivity.class);
    		startActivityForResult(intent, 1337);
    		return true;
    		
        default:
            return super.onOptionsItemSelected(item);
        }
    }   
}
