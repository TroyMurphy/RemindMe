package ca.troyamurphy.remindme.views;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView.MultiChoiceModeListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import ca.troyamurphy.remindme.R;
import ca.troyamurphy.remindme.models.ArchiveChecklist;
import ca.troyamurphy.remindme.models.ChecklistItem;
import ca.troyamurphy.remindme.models.StandardArrayAdapter;
import ca.troyamurphy.remindme.models.StandardChecklist;

@SuppressLint("InflateParams")
public class StandardChecklistActivity extends Activity {

	private ListView standardListView;
	private StandardArrayAdapter standardAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_standard_checklist);
		//make room for action icons		
		populateStandardListView();
		setMultiChoiceOnListView();
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
	
	private void populateStandardListView() {
		this.standardAdapter = new StandardArrayAdapter(this);
		
		this.standardListView = (ListView) findViewById(R.id.standardListView);
		this.standardListView.setAdapter(this.standardAdapter);	
		
		this.standardListView.setSelector(R.drawable.state_selector);
	}

	private void setMultiChoiceOnListView() {
		this.standardListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
		this.standardListView.setMultiChoiceModeListener(new MultiChoiceModeListener() {
			
			@Override
			public void onItemCheckedStateChanged(ActionMode mode, int position,
					long id, boolean checked) {
				final int checkedCount = standardListView.getCheckedItemCount();
				mode.setTitle(checkedCount + " Selected");
				standardAdapter.toggleSelection(position);
			}
			@Override
			public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
				// TODO Auto-generated method stub
				switch (item.getItemId()) {
				case R.id.menu_email:
					emailString(standardAdapter.getSelectedItemsAsString());
					mode.finish();
					return true;
				case R.id.menu_archive:
					//call function to archive
					standardAdapter.sendSelectedItemsToArchive();
					mode.finish();
					return true;
				case R.id.menu_delete:
					standardAdapter.deleteSelectedItems();
					mode.finish();
					return true;
				default:
					return false;
				}
			}
			@Override
			public boolean onCreateActionMode(ActionMode mode, Menu menu) {
				// TODO Auto-generated method stub
				MenuInflater inflater = mode.getMenuInflater();
				inflater.inflate(R.menu.standard_context_menu, menu);
				return true;
			}
			
			@Override
			public void onDestroyActionMode(ActionMode mode) {
				standardAdapter.removeSelection();
			}
			
			@Override
			public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
				// TODO Auto-generated method stub
				return false;
			}
			
		});
	}
	
	private void registerStandardClickCallback() {
		ListView listview = (ListView) findViewById(R.id.standardListView); 
				
		listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				//call on checklist so that it saves
				StandardChecklist.getInstance(getApplicationContext()).toggleChecklistItemAtIndex(position);
				refreshList();
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
	
	public boolean switchToArchiveList(MenuItem menuItem) {
		startActivity(new Intent(this, ArchiveChecklistActivity.class));
		return true;
	}
	public boolean emailAllItems(MenuItem menuItem) {
		String items = StandardChecklist.getInstance(this).toString();
		items += ArchiveChecklist.getInstance(this).toString();
		emailString(items);
		return true;
	}
	
	private void refreshList() {
		this.standardAdapter.notifyDataSetChanged();
	}
	
	@SuppressLint("SimpleDateFormat")
	private void emailString(String itemsString) {
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		String dateString = "ToDo Items for " + sdf.format(Calendar.getInstance().getTime());
				
		Intent emailIntent = new Intent(Intent.ACTION_SEND);
		emailIntent.setType("message/rfc822");
		emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{});
		emailIntent.putExtra(Intent.EXTRA_SUBJECT, dateString);
		emailIntent.putExtra(Intent.EXTRA_TEXT, itemsString);
		try {
		    startActivity(Intent.createChooser(emailIntent, "Send mail..."));
		} catch (android.content.ActivityNotFoundException ex) {
		    Toast.makeText(this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
		}

	}
}
