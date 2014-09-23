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

public class StandardChecklist {
	//singleton design pattern uses INSTANCE as the singleton.
	//access with StandardChecklist.getInstance(context) any time the checklist is required;
	
	private static StandardChecklist INSTANCE = null;
	
	private static final String DATABASEFILE = "standardChecklist.save";
	private ArrayList<ChecklistItem> standardArray;
	
	private Context context;
	
	private StandardChecklist(Context context){
		//prepare the new instance with the existing items
		this.context = context;
		this.load();
	}

	public static StandardChecklist getInstance(Context context ) {
		// ALWAYS receives context though only used on first creation. Allows for saving and loading
		if (INSTANCE == null) {
			INSTANCE = new StandardChecklist(context);
		}
		return INSTANCE;
	}
	
	public void addChecklistItem(ChecklistItem theChecklistItem) {
		INSTANCE.standardArray.add(theChecklistItem);	
		INSTANCE.save();
	}
	public void removeChecklistItemAtIndex(Integer index) {
		INSTANCE.standardArray.remove(index);
		INSTANCE.save();
	}
	
	public ArrayList<ChecklistItem> getStandardList() {
		return INSTANCE.standardArray;
	}
	
	public ChecklistItem getChecklistItemAtIndex(int index) {
		return standardArray.get(index);
	}
	private void save() {
		try {
			FileOutputStream fileOut = context.openFileOutput(StandardChecklist.DATABASEFILE, Context.MODE_PRIVATE);
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(this.standardArray);
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
			File fh = new File(context.getFilesDir(), StandardChecklist.DATABASEFILE);
			
			if (!fh.exists()) {
				this.standardArray = new ArrayList<ChecklistItem>();
				return;
			}
			FileInputStream fileIn = context.openFileInput(StandardChecklist.DATABASEFILE);
			ObjectInputStream in = new ObjectInputStream(fileIn);
			this.standardArray = (ArrayList<ChecklistItem>) in.readObject();
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
