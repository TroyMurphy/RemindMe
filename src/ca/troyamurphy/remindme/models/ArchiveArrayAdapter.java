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
import ca.troyamurphy.remindme.views.ArchiveChecklistActivity;

public class ArchiveArrayAdapter extends ArrayAdapter<ChecklistItem> {
	LayoutInflater inflater;
	private SparseBooleanArray mSelectedItemsIds;
	
	public ArchiveArrayAdapter(ArchiveChecklistActivity archiveChecklistActivity) {
		//do not use getApplicationContext or else the listview will turn light and unreadable
		super(archiveChecklistActivity, R.layout.activity_item, ArchiveChecklist.getInstance(archiveChecklistActivity).getArchiveList());
		mSelectedItemsIds = new SparseBooleanArray();
		inflater = LayoutInflater.from(archiveChecklistActivity);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		// the holder provides clearer and faster access to the name and checked attributes
		// the pointer to the object is saved as a tag so it can be pulled readily from the view
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.activity_item, parent, false);
			holder.name = (TextView) convertView.findViewById(R.id.item_name);
			holder.checked = (CheckBox) convertView.findViewById(R.id.item_checked);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		ChecklistItem selectedChecklistItem = ArchiveChecklist.getInstance(getContext()).getChecklistItemAtIndex(position);
		holder.name.setText(selectedChecklistItem.getName());
		// if done, strike the text
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
	 public void deleteSelectedItems() {
		 for (int i = (mSelectedItemsIds.size() - 1); i>=0; i--) {
			 if (mSelectedItemsIds.valueAt(i)) {
				 ChecklistItem selectedItem = this.getItem(mSelectedItemsIds.keyAt(i));
				 remove(selectedItem);
			 }
		 }
	 }
	 public void sendSelectedItemsToStandard() {
		 for (int i = (mSelectedItemsIds.size() - 1); i>=0; i--) {
			 if (mSelectedItemsIds.valueAt(i)) {
				 ChecklistItem selectedItem = this.getItem(mSelectedItemsIds.keyAt(i));
				 StandardChecklist.getInstance(getContext()).addChecklistItem((selectedItem));
			 }
		 }
		 deleteSelectedItems();
	 }
	 public SparseBooleanArray getSelectedIds() {
		 return mSelectedItemsIds;
	 }
	 public void remove(ChecklistItem checklistItem) {
		 ArchiveChecklist.getInstance(getContext()).removeChecklistItem(checklistItem);
		 notifyDataSetChanged();
	}
	public String getSelectedItemsAsString() {
		String itemsString = "";
		for (int i=0; i< mSelectedItemsIds.size(); i++) {
			itemsString += this.getItem(mSelectedItemsIds.keyAt(i)).toString();
		}
		return itemsString;
	}
}
