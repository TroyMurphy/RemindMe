package ca.troyamurphy.remindme.views;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import ca.troyamurphy.remindme.R;
import ca.troyamurphy.remindme.models.ArchiveChecklist;
import ca.troyamurphy.remindme.models.StandardChecklist;

public class StatisticsActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_statistics);
		updateStatistics();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.statistics, menu);
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
	private void updateStatistics() {
		TextView todochecked = (TextView) findViewById(R.id.ToDoCheckedCount);
		TextView todounchecked = (TextView) findViewById(R.id.ToDoUncheckedCount);
		TextView archivecount = (TextView) findViewById(R.id.ArchivedCount);
		TextView archivechecked = (TextView) findViewById(R.id.ArchivedCheckedCount);
		TextView archiveunchecked = (TextView) findViewById(R.id.ArchivedUncheckedCount);
		
		int val_todochecked = StandardChecklist.getInstance(this).getChecked();
		int val_todounchecked = StandardChecklist.getInstance(this).getUnchecked();
		int val_archivecount = ArchiveChecklist.getInstance(this).getCount();
		int val_archivechecked = ArchiveChecklist.getInstance(this).getChecked();
		int val_archiveunchecked = ArchiveChecklist.getInstance(this).getUnchecked();
		
		todochecked.setText(""+val_todochecked);
		todounchecked.setText(""+val_todounchecked);
		archivecount.setText(""+val_archivecount);
		archivechecked.setText(""+val_archivechecked);
		todochecked.setText(""+val_archiveunchecked );		
	}
	public boolean switchToStandardList(MenuItem menuItem) {
		startActivity(new Intent(this, StandardChecklistActivity.class));
		return true;
	}
}
