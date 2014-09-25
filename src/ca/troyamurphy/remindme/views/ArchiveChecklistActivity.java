package ca.troyamurphy.remindme.views;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.AbsListView.MultiChoiceModeListener;
import android.widget.ListView;
import android.widget.Toast;
import ca.troyamurphy.remindme.R;
import ca.troyamurphy.remindme.models.ArchiveArrayAdapter;

public class ArchiveChecklistActivity extends Activity {

	private ListView archiveListView;
	private ArchiveArrayAdapter archiveAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_archive_checklist);
		
		populateArchiveListView();
		setMultiChoiceOnListView();
		//registerArchiveClickCallback() not an option since archived options cannot be toggled. only viewed
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.archive_checklist, menu);
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
	
	private void populateArchiveListView() {
		this.archiveAdapter = new ArchiveArrayAdapter(this);
		
		this.archiveListView = (ListView) findViewById(R.id.archiveListView);
		this.archiveListView.setAdapter(this.archiveAdapter);	
		
		this.archiveListView.setSelector(R.drawable.state_selector);
	}

	private void setMultiChoiceOnListView() {
		this.archiveListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
		this.archiveListView.setMultiChoiceModeListener(new MultiChoiceModeListener() {
			
			@Override
			public void onItemCheckedStateChanged(ActionMode mode, int position,
					long id, boolean checked) {
				final int checkedCount = archiveListView.getCheckedItemCount();
				mode.setTitle(checkedCount + " Selected");
				archiveAdapter.toggleSelection(position);
			}
			@Override
			public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
				// TODO Auto-generated method stub
				switch (item.getItemId()) {
				case R.id.menu_email:
					emailString(archiveAdapter.getSelectedItemsAsString());
					mode.finish();
					return true;
				case R.id.menu_unarchive:
					archiveAdapter.sendSelectedItemsToStandard();
					mode.finish();
					return true;
				case R.id.menu_delete:
					ArchiveChecklistActivity.this.archiveAdapter.deleteSelectedItems();
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
				inflater.inflate(R.menu.archive_context_menu, menu);
				return true;
			}
			
			@Override
			public void onDestroyActionMode(ActionMode mode) {
				archiveAdapter.removeSelection();
			}
			
			@Override
			public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
				// TODO Auto-generated method stub
				return false;
			}
			
		});
	}
	
	public boolean switchToStandardList(MenuItem menuItem) {
		startActivity(new Intent(ArchiveChecklistActivity.this, StandardChecklistActivity.class));		
		return true;
	}
	
	private void refreshList() {
		this.archiveAdapter.notifyDataSetChanged();
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
