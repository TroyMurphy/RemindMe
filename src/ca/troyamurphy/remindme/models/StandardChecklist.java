package ca.troyamurphy.remindme.models;

import java.util.ArrayList;

public class StandardChecklist {
	//singleton design pattern uses INSTANCE as the singleton.
	//create with StandardChecklist standardChecklist = StandardChecklist.getInstance();
	
	private static StandardChecklist INSTANCE = null;
	
	private static final String DATABASEFILE = "standardChecklist.save";
	private ArrayList<ChecklistItem> standardArray = new ArrayList<ChecklistItem> ();
		
	private StandardChecklist(){}

	public static StandardChecklist getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new StandardChecklist();
		}
		return INSTANCE;
	}
	
	public void addChecklistItem(ChecklistItem theChecklistItem) {
		INSTANCE.standardArray.add(theChecklistItem);
		
		//INSTANCE.save
	}
	public ArrayList<ChecklistItem> getStandardList() {
		return INSTANCE.standardArray;
	}
}
