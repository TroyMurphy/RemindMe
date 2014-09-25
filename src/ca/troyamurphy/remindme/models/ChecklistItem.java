package ca.troyamurphy.remindme.models;

import java.io.Serializable;

public class ChecklistItem implements Serializable {
	private static final long serialVersionUID = 1L;
	public String name;
	public Boolean checked;
	
	public ChecklistItem(String theName, Boolean checked) {
		this.name = theName;
		this.checked = checked;
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
		this.checked ^= true;
	}
	@Override
	public String toString() {
		String ret = "";
		if (this.checked) {
			ret += "[X] ";
		} else {
			ret += "[ ] ";
		}
		return ret + this.name + "\n";
	}
}
