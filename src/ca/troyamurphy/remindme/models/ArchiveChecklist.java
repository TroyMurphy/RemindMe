package ca.troyamurphy.remindme.models;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import android.content.Context;

public class ArchiveChecklist {
	//singleton design pattern uses INSTANCE as the singleton.
	//access with ArchiveChecklist.getInstance(context) any time the checklist is required;
	
	private static ArchiveChecklist INSTANCE = null;
	
	private static final String DATABASEFILE = "archiveChecklist.save";
	private ArrayList<ChecklistItem> archiveArray;
	
	private Context context;
	
	private ArchiveChecklist(Context context){
		//prepare the new instance with the existing items
		this.context = context;
		this.load();
	}
	
	public static ArchiveChecklist getInstance(Context context ) {
		// ALWAYS receives context though only used on first creation. Allows for saving and loading
		if (INSTANCE == null) {
			INSTANCE = new ArchiveChecklist(context);
		}
		return INSTANCE;
	}
	
	public void addChecklistItem(ChecklistItem theChecklistItem) {
		INSTANCE.archiveArray.add(theChecklistItem);	
		INSTANCE.save();
	}
	public void removeChecklistItemAtIndex(Integer index) {
		INSTANCE.archiveArray.remove(index);
		INSTANCE.save();
	}
	public void removeChecklistItem(ChecklistItem checklistItem) {
		INSTANCE.archiveArray.remove(checklistItem);
		INSTANCE.save();
	}
	
	public ArrayList<ChecklistItem> getArchiveList() {
		return INSTANCE.archiveArray;
	}
	
	public ChecklistItem getChecklistItemAtIndex(int index) {
		return INSTANCE.archiveArray.get(index);
	}
	public Boolean toggleChecklistItemAtIndex(Integer index) {
		INSTANCE.archiveArray.get(index).toggleChecked();
		INSTANCE.save();
		return INSTANCE.archiveArray.get(index).getChecked();
	}
	
	private void save() {
		try {
			FileOutputStream fileOut = context.openFileOutput(ArchiveChecklist.DATABASEFILE, Context.MODE_PRIVATE);
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(INSTANCE.archiveArray);
			out.close();
			fileOut.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	private void load() {
		try {
			File fh = new File(context.getFilesDir(), ArchiveChecklist.DATABASEFILE);
			
			if (!fh.exists()) {
				this.archiveArray = new ArrayList<ChecklistItem>();
				return;
			}
			FileInputStream fileIn = context.openFileInput(ArchiveChecklist.DATABASEFILE);
			ObjectInputStream in = new ObjectInputStream(fileIn);
			this.archiveArray = (ArrayList<ChecklistItem>) in.readObject();
			in.close();
			fileIn.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassCastException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
}
