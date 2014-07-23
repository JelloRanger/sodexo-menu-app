package com.sodexomenu;

import java.util.ArrayList;

public class FoodItem {

	/**
	 * name: name of meal
	 * station: station meal is served at
	 * dayOfWeek: day of the week meal is being served (e.g. monday)
	 * mealTime: time of day meal is served at (e.g. breakfast)
	 * attributes: attributes of meal (vegan, vegetarian, mindful item)
	 */
	private String name = null;
	private String station = null;
	private String dayOfWeek = null;
	private String mealTime = null;
	private ArrayList<String> attributes;

	// constructor
	public FoodItem() {
		attributes = new ArrayList<String>();
	}
	
	// add attribute
	public void addAttribute(String attr) {
		attributes.add(attr);
	}
	
	// GETTERS
	public String getName() { 
		return name;
	}
	
	public String getStation() {
		return station;
	}
	
	public String getDayOfWeek() {
		return dayOfWeek;
	}
	
	public String getMealTime() {
		return mealTime;
	}
	
	public ArrayList<String> getAttributes() {
		return attributes;
	}
	
	// SETTERS
	public void setName(String n) {
		name = n;
	}
	
	public void setStation(String s) {
		station = s;
	}
	
	public void setDayOfWeek(String dow) {
		dayOfWeek = dow;
	}
	
	public void setMealTime(String mt) {
		mealTime = mt;
	}
	
	public void setAttributes(ArrayList<String> a) {
		attributes = a;
	}
}
