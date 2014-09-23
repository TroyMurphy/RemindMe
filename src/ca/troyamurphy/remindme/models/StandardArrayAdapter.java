package ca.troyamurphy.remindme.models;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import ca.troyamurphy.remindme.R;
import ca.troyamurphy.remindme.views.StandardChecklistActivity;

public class StandardArrayAdapter extends ArrayAdapter<ChecklistItem> {

	private StandardChecklistActivity standardChecklistActivity;

	public StandardArrayAdapter(StandardChecklistActivity standardChecklistActivity) {
		//will be passed the singleton
		super(standardChecklistActivity, R.layout.activity_standard_item, StandardChecklist.getInstance(standardChecklistActivity.getApplicationContext()).getStandardList());
		
		this.standardChecklistActivity = standardChecklistActivity;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		View itemView = convertView;
		//if the view doesn't exist create it
		if (itemView == null) {
			itemView = standardChecklistActivity.getLayoutInflater().inflate(R.layout.activity_standard_item, parent, false);
		}

		ChecklistItem selectedChecklistItem = StandardChecklist.getInstance(getContext()).getChecklistItemAtIndex(position);
		
		TextView titleTV = (TextView) itemView.findViewById(R.id.standard_item_name);
		titleTV.setText(selectedChecklistItem.getName());
		
		CheckBox checkedCB = (CheckBox) itemView.findViewById(R.id.standard_item_checked);
		checkedCB.setChecked(selectedChecklistItem.getChecked());

		return itemView;
	}
	
}
