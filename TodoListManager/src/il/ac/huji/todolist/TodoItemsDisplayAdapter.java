package il.ac.huji.todolist;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class TodoItemsDisplayAdapter extends ArrayAdapter<TodoItem> {
	public TodoItemsDisplayAdapter(
			TodoListManagerActivity todoListManagerActivity,
			ArrayList<TodoItem> entries) {
		super(todoListManagerActivity, android.R.layout.simple_list_item_1, entries);
	}

	@SuppressLint("SimpleDateFormat")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TodoItem item = getItem(position);
		LayoutInflater inflater = LayoutInflater.from(getContext());
		View view = inflater.inflate(R.layout.row, null);
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
