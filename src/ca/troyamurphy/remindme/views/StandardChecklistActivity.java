package ca.troyamurphy.remindme.views;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import ca.troyamurphy.remindme.R;
import ca.troyamurphy.remindme.models.ChecklistItem;
import ca.troyamurphy.remindme.models.StandardArrayAdapter;
import ca.troyamurphy.remindme.models.StandardChecklist;

public class StandardChecklistActivity extends Activity {

	private StandardArrayAdapter standardAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_standard_checklist);
		
		populateStandardList();
		
		populateStandardListView();
		registerStandardClickCallback();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.standard_checklist, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		//int id = item.getItemId();
		//if (id == R.id.action_settings) {
		//	return true;
		//}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		this.refreshList();
	}
	
	private void populateStandardList() {
		// Will be replaced by add and remove buttons
		// Can be used to force creation of new counter on Startup.
	}
	
	private void populateStandardListView() {
		this.standardAdapter = new StandardArrayAdapter(this);
		
		ListView standardList = (ListView) findViewById(R.id.standardListView);
		standardList.setAdapter(this.standardAdapter);
	}
	
	private void registerStandardClickCallback() {
		ListView listview = (ListView) findViewById(R.id.standardListView); 
				
		listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				//call on checklist so that it saves
				StandardChecklist.getInstance(getApplicationContext()).toggleChecklistItemAtIndex(position);
				
				ChecklistItem selectedChecklistItem = StandardChecklist.getInstance(getApplicationContext()).getChecklistItemAtIndex(position);
				
				CheckBox checkbox = (CheckBox) findViewById(R.id.standard_item_checked);
				checkbox.setChecked(selectedChecklistItem.getChecked());
				
				TextView titleTV = (TextView) findViewById(R.id.standard_item_name);
				if (selectedChecklistItem.getChecked()) {
					titleTV.setPaintFlags(titleTV.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
				} else {
					titleTV.setPaintFlags(0);
				}
			}
		});
	}
	public boolean addChecklistItemAction(MenuItem menuItem) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		
		LayoutInflater inflater = this.getLayoutInflater();
		
		builder.setView(inflater.inflate(R.layout.action_new_checklist_item, null))
			.setNegativeButton("Cancel", null)
			.setPositiveButton("Add Item", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int id) {
					EditText editText = (EditText) ((AlertDialog) dialog).findViewById(R.id.addChecklistItemEditText);
					String checklistItemString = editText.getText().toString();
					
					if (checklistItemString.length() <= 0) {
						Toast toast = Toast.makeText(getApplicationContext(), "Task cannot be empty", Toast.LENGTH_SHORT);
						toast.show();
					} else {
						ChecklistItem newChecklistItem = new ChecklistItem(checklistItemString, false); 
						StandardChecklist.getInstance(getApplicationContext()).addChecklistItem(newChecklistItem);
						
						refreshList();
					}
				}
			});
		AlertDialog dialog = builder.create();
		dialog.show();
		return true;
	}
	private void refreshList() {
		this.standardAdapter.notifyDataSetChanged();
	}
}
