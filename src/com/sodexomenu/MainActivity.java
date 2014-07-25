package com.sodexomenu;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class MainActivity extends ListActivity {

	public final static String EXTRA_MESSAGE = "com.sodexomenu.MESSAGE";
	public static ArrayList<DiningHall> diningHallList;
	
	// used to show progress of loading menu data
	private ProgressDialog pDialog;
	
	// URL to get sodexo menu JSON
	private static String url = "http://m.uploadedit.com/b037/1406074507953.txt"; // FOR TEST PURPOSES
	
	// JSON node names
	private static final String TAG_MENU = "menu";
	private static final String TAG_STATION = "station";
	private static final String TAG_DAYOFWEEK = "dayOfWeek";
	private static final String TAG_MEAL = "meal";
	private static final String TAG_ATTRIBUTES = "attributes";
	private static final String TAG_NAME = "name";
	private static final String TAG_DININGHALL = "diningHall";
	
	private int numberthing = 0; // necessary for id in hashmap...will look at removing it in future
	
    // Hashmap for ListView
    ArrayList<HashMap<String, String>> dinHallItem;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// initialize hashmap necessary for listview
		dinHallItem = new ArrayList<HashMap<String, String>>();
		
		// Calling async task to get json
		new GetHalls().execute();
	}
	
	// open specific dining hall's menu
	/*public void openMenu(View view) {
		Intent intent = new Intent(MainActivity.this, MenuActivity.class);
		EditText editText = (EditText) findViewById(R.id.name);
		String message = editText.getText().toString();
		intent.putExtra(EXTRA_MESSAGE, message);
		startActivity(intent);
	}*/
	
	@Override
	protected void onListItemClick(ListView lv, View v, int pos, long id) {
		//super.onListItemClick(lv, v, pos, id);
		Intent intent = new Intent(MainActivity.this, MenuActivity.class);
		Log.d("id: ", ""+id);
		TextView editText = (TextView) findViewById(R.id.name);
		String message = editText.getText().toString();
		intent.putExtra(EXTRA_MESSAGE, message);
		startActivity(intent);
	}
	
	/**
	 * Async task class to get json by making HTTP call
	 */
	private class GetHalls extends AsyncTask<Void, Void, Void> {
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// Showing progress dialog
			pDialog = new ProgressDialog(MainActivity.this);
			pDialog.setMessage("Please wait...");
			pDialog.setCancelable(false);
			pDialog.show();
			
		}
		
		@Override
		protected Void doInBackground(Void... arg0) {
			// Creating service handler class instance
			ServiceHandler sh = new ServiceHandler();
			
			// Making a request to url and getting response
			String jsonStr = sh.makeServiceCall(url, ServiceHandler.GET);
			
			// create list to hold all dining halls
			diningHallList = new ArrayList<DiningHall>();
			
			Log.d("Response: ", "> " + jsonStr);
			
			if (jsonStr != null) {
				try {
					
					//grab jsonarray
					JSONArray arr = new JSONArray(jsonStr);
					
					//loop through all dininghalls
					for (int i = 0; i < arr.length(); i++) {
						
						// grab individual dining hall
						JSONObject c = arr.getJSONObject(i);
						String diningHall = c.getString(TAG_DININGHALL);
						
						// construct dining hall object
						DiningHall dh = new DiningHall(diningHall);
						
						// Menu node is JSON Object
						JSONArray menu = c.getJSONArray(TAG_MENU);
						for (int x = 0; x < menu.length(); x++) {
							JSONObject d = menu.getJSONObject(x);
						
							// create FoodItem
							FoodItem fi = new FoodItem();
						
							// grab all attributes of FoodItem
							JSONArray attributes = d.getJSONArray(TAG_ATTRIBUTES);
							for (int j = 0; j < attributes.length(); j++) {
								fi.addAttribute(attributes.getString(j));
							}
							
							// grab other properties of food item
							fi.setDayOfWeek(d.getString(TAG_DAYOFWEEK));
							fi.setMealTime(d.getString(TAG_MEAL));
							fi.setName(d.getString(TAG_NAME));
							fi.setStation(d.getString(TAG_STATION));
							
							// add FoodItem to dining hall
							dh.addFoodItem(fi);
							
						}

						// add dining hall to dining hall list
						diningHallList.add(dh);
						
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				
				// grab data and do stuff with it
				/*for (int i = 0; i < diningHallList.size(); i++) {
					
					// right now just display first dining hall meal items....
					if (i == 0) {
						for (int j = 0; j < diningHallList.get(i).getMeals().size(); j++) {
							
							FoodItem fi = diningHallList.get(i).getMeals().get(j);
							
							HashMap<String, String> hall = new HashMap<String, String>();
							hall.put("id", String.valueOf(numberthing));
							numberthing++;
							hall.put(TAG_NAME, fi.getName());
							hall.put(TAG_DAYOFWEEK, fi.getDayOfWeek());
							hall.put(TAG_MEAL, fi.getMealTime());
							hall.put(TAG_STATION, fi.getStation());
							hall.put(TAG_DININGHALL, diningHallList.get(i).getName());
							hallList.add(hall);
						}
					}
				}*/
				
				//generate list of dining halls
				generateDiningHallListView(diningHallList);
				
				
			} else {
				Log.e("ServiceHandler", "Couldn't get any data from the url");
			}
			
			return null;
		}
		
		// add dining halls into dining hall list view
		protected void generateDiningHallListView(ArrayList<DiningHall> diningHallList) {
			
			// sort list of dining halls by name
			Collections.sort(diningHallList, new Comparator<DiningHall>() {
				@Override
				public int compare(final DiningHall obj1, final DiningHall obj2) {
					return obj1.getName().compareTo(obj2.getName());
				}
			});
			
			// loop through all dining halls
			for (int i = 0; i < diningHallList.size(); i++) {
				
				// generate tmp hashmap for listview of dining halls
				HashMap<String, String> hall = new HashMap<String, String>();
				hall.put("id", String.valueOf(numberthing));
				numberthing++;
				hall.put(TAG_DININGHALL, diningHallList.get(i).getName());
				dinHallItem.add(hall);
			}
		}
		
		
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			// Dismiss the progress dialog
			if (pDialog.isShowing()) {
				pDialog.dismiss();
			}
			/**
			 * Updating parsed JSON data into ListView
			 */
			
			// code for displaying all meals in listview
			/*ListAdapter adapter = new SimpleAdapter(
			 		MainActivity.this, hallList,
			 		R.layout.list_item, new String[] { TAG_NAME, TAG_DAYOFWEEK,
			 				TAG_MEAL, TAG_STATION, TAG_DININGHALL }, new int[] { R.id.name,
			 				R.id.dayOfWeek, R.id.meal, R.id.station, R.id.diningHall });*/
			
			// display dining halls in listview
			ListAdapter adapter = new SimpleAdapter(MainActivity.this, dinHallItem, R.layout.dining_hall_list_item, 
													new String[] { TAG_DININGHALL }, new int[] { R.id.name });
			 
			setListAdapter(adapter);
		}
	}

}
