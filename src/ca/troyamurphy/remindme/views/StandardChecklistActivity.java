package ca.troyamurphy.remindme.views;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
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
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void populateStandardList() {
		// Will be replaced by add and remove buttons
		ChecklistItem theChecklistItem = new ChecklistItem("First Item");
		StandardChecklist.getInstance().addChecklistItem(theChecklistItem);
		ChecklistItem theSecondChecklistItem = new ChecklistItem("Second Item");
		StandardChecklist.getInstance().addChecklistItem(theSecondChecklistItem);
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
				ChecklistItem selectedChecklistItem = StandardChecklist.getInstance().getChecklistItemAtIndex(position);
				selectedChecklistItem.toggleChecked();
			}
		
		});
	}
}
