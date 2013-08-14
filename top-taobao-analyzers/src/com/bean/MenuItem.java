package com.bean;

import java.util.List;

public class MenuItem {
	
	private int upperID = -1;
	
	private List<Integer> lowerList = null;
	
	private String itemName;

	public int getUpperID() {
		return upperID;
	}

	public void setUpperID(int upperID) {
		this.upperID = upperID;
	}

	public List<Integer> getLowerList() {
		return lowerList;
	}

	public void setLowerList(List<Integer> lowerList) {
		this.lowerList = lowerList;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

}
