package il.ac.huji.todolist;

import java.sql.Date;
import java.text.SimpleDateFormat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;


public class TodoListCursorAdapter extends SimpleCursorAdapter{

	Context _context;

	@SuppressWarnings("deprecation")
	public TodoListCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to) 
	{
		super(context, layout, c, from, to);
		 _context = context;
	}

	@SuppressLint("SimpleDateFormat")
	public View getView(int position, View convertView, ViewGroup parent)
	{
		LayoutInflater inflater = (LayoutInflater)_context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.row, null);

		Cursor cursor = (Cursor)getItem(position);
		cursor.moveToPosition(position);
		
		String isDateNull = cursor.getString(2);
		TodoItem item = new TodoItem(cursor.getString(1), (isDateNull != null) ? new Date(cursor.getLong(2)) : null);
		
		TextView txtItem = (TextView)view.findViewById(R.id.txtTodoTitle);
		TextView txtDate = (TextView)view.findViewById(R.id.txtTodoDueDate);
		txtItem.setText(item.getTitle());
		if (item.getDueDate() == null)
		{
			txtDate.setText("No due date");
		}
		else
		{
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			txtDate.setText(dateFormat.format(item.getDueDate()));
			Date currentDate = new Date(System.currentTimeMillis());
			if (currentDate.after(item.getDueDate()))
			{
				txtDate.setTextColor(Color.RED);
				txtItem.setTextColor(Color.RED);
			}
		}

		return view;
	}
}