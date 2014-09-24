package ca.troyamurphy.remindme.models;

import android.graphics.Paint;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import ca.troyamurphy.remindme.R;
import ca.troyamurphy.remindme.views.StandardChecklistActivity;

public class StandardArrayAdapter extends ArrayAdapter<ChecklistItem> {

	private StandardChecklistActivity standardChecklistActivity;
	private SparseBooleanArray mSelectedItemsIds;
	
	public StandardArrayAdapter(StandardChecklistActivity standardChecklistActivity) {
		//will be passed the singleton
		super(standardChecklistActivity, R.layout.activity_standard_item, StandardChecklist.getInstance(standardChecklistActivity.getApplicationContext()).getStandardList());
		mSelectedItemsIds = new SparseBooleanArray();
		this.standardChecklistActivity = standardChecklistActivity;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		// the holder provides clearer and faster access to the name and checked attributes
		// the pointer to the object is saved as a tag so it can be pulled readily from the view
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = standardChecklistActivity.getLayoutInflater().inflate(R.layout.activity_standard_item, parent, false);
			holder.name = (TextView) convertView.findViewById(R.id.standard_item_name);
			holder.checked = (CheckBox) convertView.findViewById(R.id.standard_item_checked);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		ChecklistItem selectedChecklistItem = StandardChecklist.getInstance(getContext()).getChecklistItemAtIndex(position);
		holder.name.setText(selectedChecklistItem.getName());
		if (selectedChecklistItem.getChecked()) {
			holder.name.setPaintFlags(holder.name.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
		} else {
			holder.name.setPaintFlags(0);
		}
		holder.checked.setChecked(selectedChecklistItem.getChecked());

		return convertView;
	}
	 private class ViewHolder {
		  TextView name;
		  CheckBox checked;
	 }
	 public void toggleSelection(int position) {
		 selectView(position, !mSelectedItemsIds.get(position));
	 }
	 public void selectView(int position, boolean value) {
		 if (value) {
			 mSelectedItemsIds.put(position, value);
		 } else{
			 mSelectedItemsIds.delete(position);
		 }
		 notifyDataSetChanged();
	 }
	 public void removeSelection() {
		 mSelectedItemsIds = new SparseBooleanArray();
		 notifyDataSetChanged();
	 }
	public SparseBooleanArray getSelectedIds() {
		return mSelectedItemsIds;
	}
}
