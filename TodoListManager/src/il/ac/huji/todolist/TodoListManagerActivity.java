package il.ac.huji.todolist;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ListView;
import il.ac.huji.todolist.AlternatingRowsAdapter;

public class TodoListManagerActivity extends Activity 
{
	ListView list;
	AlternatingRowsAdapter arrAdapt;
	ArrayList<String> entries;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_todo_list_manager);
		
		entries = new ArrayList<String>();
		arrAdapt = new AlternatingRowsAdapter(this, android.R.layout.simple_list_item_1, entries);
		arrAdapt.setNotifyOnChange(true);
		
		list = (ListView)findViewById(R.id.lstTodoItems);  
		list.setAdapter(arrAdapt);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.todo_list_manager, menu);
		return true;
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
            // Single menu item is selected do something
            EditText tmp = (EditText) findViewById(R.id.edtNewItem);
            String strNewItem = (tmp.getText()).toString();
            if (strNewItem.length() == 0)
            	return true;
            
    		arrAdapt.add(strNewItem);
    		tmp.setText("");
            return true;
 
        case R.id.menuItemDelete:
        	int selectedRow = list.getSelectedItemPosition();
        	if (selectedRow < 0)
        		return true;
        	
            entries.remove(selectedRow);
		    TodoListManagerActivity.this.runOnUiThread(new Runnable()
		    {
		        public void run() 
		        {
		            arrAdapt.notifyDataSetChanged();
		        }
		    });
            return true;
 
        default:
            return super.onOptionsItemSelected(item);
        }
    }   
}
