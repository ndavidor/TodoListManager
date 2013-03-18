package il.ac.huji.todolist;

import java.util.ArrayList;
import java.util.Date;

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

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TodoItem item = getItem(position);
		LayoutInflater inflater = LayoutInflater.from(getContext());
		View view = inflater.inflate(R.layout.row, null);
		TextView txtName = (TextView)view.findViewById(R.id.txtName);
		TextView txtNekudot = (TextView)view.findViewById(
				R.id.txtNekudot);
		txtName.setText(item.item);
		txtNekudot.setText(new Date(item.date).toString());
		return view;
	}
}
