package com.sodexomenu;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;

public class MainActivity extends ListActivity {

	private ProgressDialog pDialog;
	
	// URL to get contacts JSON
	//private static String url = "http://api.androidhive.info/contacts/";
	private static String url = "http://m.uploadedit.com/b037/140599911160.txt";
	
	// JSON node names
	/*private static final String TAG_CONTACTS = "contacts";
	private static final String TAG_ID = "id";
    private static final String TAG_NAME = "name";
    private static final String TAG_EMAIL = "email";
    private static final String TAG_ADDRESS = "address";
    private static final String TAG_GENDER = "gender";
    private static final String TAG_PHONE = "phone";
    private static final String TAG_PHONE_MOBILE = "mobile";
    private static final String TAG_PHONE_HOME = "home";
    private static final String TAG_PHONE_OFFICE = "office";*/
    private static final String TAG_HALLS = "halls";
	private static final String TAG_MENU = "menu";
	private static final String TAG_STATION = "station";
	private static final String TAG_DAYOFWEEK = "dayOfWeek";
	private static final String TAG_MEAL = "meal";
	private static final String TAG_ATTRIBUTES = "attributes";
	private static final String TAG_NAME = "name";
	private static final String TAG_DININGHALL = "diningHall";
	
	private int numberthing = 0;
	
    // contacts JSONArray
    //JSONArray contacts = null;
    JSONArray halls = null;
    
    // Hashmap for ListView
    //ArrayList<HashMap<String, String>> contactList;
    ArrayList<HashMap<String, String>> hallList;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		//contactList = new ArrayList<HashMap<String, String>>();
		hallList = new ArrayList<HashMap<String, String>>();
		
		// Calling async task to get json
		//new GetContacts().execute();
		new GetHalls().execute();
	}
	
	/**
	 * Async task class to get json by making HTTP call
	 */
	//private class GetContacts extends AsyncTask<Void, Void, Void> {
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
			
			Log.d("Response: ", "> " + jsonStr);
			
			if (jsonStr != null) {
				try {
					JSONObject jsonObj = new JSONObject(jsonStr);
					
					// Getting JSON Array node
					//contacts = jsonObj.getJSONArray(TAG_CONTACTS);
					halls = jsonObj.getJSONArray(TAG_HALLS);
					
					//loop through all dininghalls
					for (int i = 0; i < halls.length(); i++) {
						JSONObject c = halls.getJSONObject(i);
						
						// Menu node is JSON Object
						JSONArray menu = c.getJSONArray(TAG_MENU);
						for (int x = 0; x < menu.length(); x++) {
							JSONObject d = menu.getJSONObject(x);
						
							String station = d.getString(TAG_STATION);
							String dayOfWeek = d.getString(TAG_DAYOFWEEK);
							String meal = d.getString(TAG_MEAL);
						
							JSONArray attributes = d.getJSONArray(TAG_ATTRIBUTES);
							for (int j = 0; j < attributes.length(); j++) {
								String attr = attributes.getString(j);
								Log.i("..........", ""+attr);
							}
						
							String name = d.getString(TAG_NAME);
							
							// tmp hashmap for single dining hall
							HashMap<String, String> hall = new HashMap<String, String>();
							
							// adding each child node to HashMap key => value
							hall.put("id", String.valueOf(numberthing));
							numberthing++;
							hall.put(TAG_NAME, name);
							hall.put(TAG_DAYOFWEEK, dayOfWeek);
							hall.put(TAG_MEAL, meal);
							
							// adding meal to hall list
							hallList.add(hall);
						}
						
						String diningHall = c.getString(TAG_DININGHALL);
						
						
						
						
					 
					
					// looping through All Contacts
					/*for (int i = 0; i < contacts.length(); i++) {
						JSONObject c = contacts.getJSONObject(i);
						
						String id = c.getString(TAG_ID);
						String name = c.getString(TAG_NAME);
						String email = c.getString(TAG_EMAIL);
						String address = c.getString(TAG_ADDRESS);
						String gender = c.getString(TAG_GENDER);
						
						// Phone node is JSON Object
						JSONObject phone = c.getJSONObject(TAG_PHONE);
						String mobile = phone.getString(TAG_PHONE_MOBILE);
						String home = phone.getString(TAG_PHONE_HOME);
						String office = phone.getString(TAG_PHONE_OFFICE);
						
						// tmp hashmap for single contact
						HashMap<String, String> contact = new HashMap<String, String>();
						
						// adding each child node to HashMap key => value
						contact.put(TAG_ID, id);
						contact.put(TAG_NAME, name);
						contact.put(TAG_EMAIL, email);
						contact.put(TAG_PHONE_MOBILE, mobile);
						
						// adding contact to contact list
						contactList.add(contact);*/
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			} else {
				Log.e("ServiceHandler", "Couldn't get any data from the url");
			}
			
			return null;
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
			/*ListAdapter adapter = new SimpleAdapter(
					MainActivity.this, contactList,
					R.layout.list_item, new String[] { TAG_NAME, TAG_EMAIL,
							TAG_PHONE_MOBILE }, new int[] { R.id.name,
							R.id.email, R.id.mobile });*/
			ListAdapter adapter = new SimpleAdapter(
			 		MainActivity.this, hallList,
			 		R.layout.list_item, new String[] { TAG_NAME, TAG_DAYOFWEEK,
			 				TAG_MEAL }, new int[] { R.id.name,
			 				R.id.email, R.id.mobile });
			 
			setListAdapter(adapter);
		}
	}

	/*@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}*/

	/**
	 * A placeholder fragment containing a simple view.
	 *//*
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			return rootView;
		}
	}*/

}
