package ca.troyamurphy.remindme.models;

import java.io.Serializable;

public class ChecklistItem implements Serializable {
	private static final long serialVersionUID = 1L;
	public String name;
	public Boolean checked;
	
	public ChecklistItem(String theName) {
		this.name = theName;
		this.checked = false;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Boolean getChecked() {
		return checked;
	}
	public void setChecked(Boolean checked) {
		this.checked = checked;
	}

	public void toggleChecked() {
		this.checked = !this.checked;
	}
	
}
