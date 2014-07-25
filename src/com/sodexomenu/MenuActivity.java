package com.sodexomenu;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;

public class MenuActivity extends ListActivity {

	// tag names for food item attributes
	private static final String TAG_STATION = "station";
	private static final String TAG_DAYOFWEEK = "dayOfWeek";
	private static final String TAG_MEAL = "meal";
	private static final String TAG_ATTRIBUTES = "attributes";
	private static final String TAG_NAME = "name";
	
	// Hashmap for ListView
    public ArrayList<HashMap<String, String>> menuItem;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu);

		/*if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}*/
		
		menuItem = new ArrayList<HashMap<String, String>>();
		
		// grab the intent (dining hall name)
		Intent intent = getIntent();
		String dinHall = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
		
		Log.d("Dining Hall Selected: ", "> " + dinHall);
		
		generateMenuItems(dinHall);
	}
	
	// generate menu items for dining hall selected
	private void generateMenuItems(String dinHall) {
		
		// grab list of dining hall objects
		ArrayList<DiningHall> dh = MainActivity.diningHallList;
		
		// grab correct dining hall
		DiningHall hall = null;
		for (int i = 0; i < dh.size(); i++) {
			Log.d("rofl", dh.get(i).getName() + " " + dinHall);
			if (dh.get(i).getName().equals(dinHall)) {
				hall = dh.get(i);
				Log.d("lmao: ", "yay");
				break;
			}
		}
		
		// loop through meals of dining hall
		for (int i = 0; i < hall.getMeals().size(); i++) {
			
			FoodItem fi = hall.getMeals().get(i);
			HashMap<String, String> menu = new HashMap<String, String>();
			menu.put("id", String.valueOf(0));
			menu.put(TAG_NAME, fi.getName());
			menu.put(TAG_DAYOFWEEK, fi.getDayOfWeek());
			menu.put(TAG_MEAL, fi.getMealTime());
			menu.put(TAG_STATION, fi.getStation());
			menuItem.add(menu);
		}
	
		ListAdapter adapter = new SimpleAdapter(MenuActivity.this, menuItem, R.layout.list_item,
												new String[] { TAG_NAME, TAG_DAYOFWEEK, TAG_MEAL,
												TAG_STATION }, new int[] { R.id.name, R.id.dayOfWeek,
												R.id.meal, R.id.station});
		
		setListAdapter(adapter);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_menu, container,
					false);
			return rootView;
		}
	}

}
