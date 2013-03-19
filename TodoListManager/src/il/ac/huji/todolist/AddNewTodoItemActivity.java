package il.ac.huji.todolist;

import java.util.Calendar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.EditText;

public class AddNewTodoItemActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addnewtodoitem_activity);
		setTitle("Add New Item");
		
		findViewById(R.id.btnCancel).setOnClickListener(new OnClickListener() {
			public void onClick(View v){
				setResult(RESULT_CANCELED);
				finish();
			}
		});
		
		findViewById(R.id.btnOK).setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				EditText activity = (EditText)findViewById(R.id.edtNewItem);
				DatePicker dp = (DatePicker)findViewById(R.id.datePicker);
				Calendar calendar = Calendar.getInstance();
				calendar.set(dp.getYear(), dp.getMonth(), dp.getDayOfMonth());
				String itemName = activity.getText().toString();
				if (itemName == null || "".equals(itemName)) 
				{
					setResult(RESULT_CANCELED);
					finish();
				}
				else 
				{
					Intent resultIntent = new Intent();
					resultIntent.putExtra("title", itemName);
					resultIntent.putExtra("dueDate", calendar.getTime());
					setResult(RESULT_OK, resultIntent);
					finish();
				}
			}
		});
	}
	
}
